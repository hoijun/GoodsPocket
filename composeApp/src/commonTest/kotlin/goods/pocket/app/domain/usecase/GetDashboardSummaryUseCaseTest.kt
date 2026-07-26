package goods.pocket.app.domain.usecase

import goods.pocket.app.data.InMemoryGoodsPocketRepository
import goods.pocket.app.domain.model.Item
import goods.pocket.app.domain.model.ItemStatus
import goods.pocket.app.domain.model.Preorder
import goods.pocket.app.domain.model.PreorderStatus
import goods.pocket.app.domain.repository.CollectionRepository
import goods.pocket.app.domain.repository.PreorderRepository
import goods.pocket.app.domain.service.AppClock
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetDashboardSummaryUseCaseTest {

    private val repository = InMemoryGoodsPocketRepository()

    @Test
    fun returnsMarchDashboardSummary() = runTest {
        val recentActivities = GetRecentActivitiesUseCase(repository, repository).invoke(limit = 5)
        val summary = GetDashboardSummaryUseCase(repository, repository).invoke(
            monthFilter = "2026-03",
            recentActivities = recentActivities,
        )

        assertEquals(106000, summary.monthlySpend)
        assertEquals(42_000, summary.previousMonthSpend)
        assertEquals(
            listOf(74_000L, 32_000L, 0L, 0L, 0L, 0L, 0L, 0L, 0L),
            summary.spendingBuckets,
        )
        assertEquals(2, summary.ownedItemCount)
        assertEquals(2, summary.activePreorderCount)
        assertEquals(4, summary.recentActivities.size)
    }

    @Test
    fun `received preorder is not counted again as a linked item`() = runTest {
        val repository = InMemoryGoodsPocketRepository()
        MarkPreorderReceivedUseCase(repository, MarchClock)("pre-2")

        val summary = GetDashboardSummaryUseCase(repository, repository)(
            monthFilter = "2026-03",
            recentActivities = emptyList(),
        )

        assertEquals(106_000, summary.monthlySpend)
    }

    @Test
    fun `previous month spending crosses the year boundary`() = runTest {
        val repository = DashboardRepository(
            items = listOf(
                item(id = "december", price = 10_000, date = "2025-12-30"),
                item(id = "january", price = 20_000, date = "2026-01-03"),
            ),
        )

        val summary = GetDashboardSummaryUseCase(repository, repository)(
            monthFilter = "2026-01",
            recentActivities = emptyList(),
        )

        assertEquals(20_000, summary.monthlySpend)
        assertEquals(10_000, summary.previousMonthSpend)
        assertEquals(
            listOf(20_000L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L),
            summary.spendingBuckets,
        )
    }

    @Test
    fun `empty spending month returns zero totals and nine empty buckets`() = runTest {
        val summary = GetDashboardSummaryUseCase(repository, repository)(
            monthFilter = "2027-08",
            recentActivities = emptyList(),
        )

        assertEquals(0, summary.monthlySpend)
        assertEquals(0, summary.previousMonthSpend)
        assertEquals(List(9) { 0L }, summary.spendingBuckets)
    }
}

private class DashboardRepository(
    private val items: List<Item>,
    private val preorders: List<Preorder> = emptyList(),
    private val delegate: InMemoryGoodsPocketRepository = InMemoryGoodsPocketRepository(),
) : CollectionRepository by delegate, PreorderRepository by delegate {
    override suspend fun getItems(filter: String?): List<Item> = items

    override suspend fun countOwnedItems(): Int = items.count { it.status == ItemStatus.OWNED }

    override suspend fun getPreorders(status: PreorderStatus?): List<Preorder> {
        return if (status == null) preorders else preorders.filter { it.status == status }
    }

    override suspend fun countActivePreorders(): Int {
        return preorders.count { it.status == PreorderStatus.ACTIVE || it.status == PreorderStatus.PAYMENT_PENDING }
    }
}

private fun item(
    id: String,
    price: Long,
    date: String,
): Item {
    return Item(
        id = id,
        name = id,
        category = "goods",
        status = ItemStatus.OWNED,
        purchasePrice = price,
        purchaseDate = date,
        createdAt = date,
        updatedAt = date,
    )
}

private object MarchClock : AppClock {
    override fun currentDate(): String = "2026-03-15"
}
