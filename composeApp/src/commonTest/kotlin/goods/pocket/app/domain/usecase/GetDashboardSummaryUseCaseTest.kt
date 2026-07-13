package goods.pocket.app.domain.usecase

import goods.pocket.app.data.InMemoryGoodsPocketRepository
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
}

private object MarchClock : AppClock {
    override fun currentDate(): String = "2026-03-15"
}
