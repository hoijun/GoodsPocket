package goods.pocket.app.domain

import goods.pocket.app.domain.collection.CollectionEntry
import goods.pocket.app.domain.collection.CollectionEntryStatus
import goods.pocket.app.domain.collection.ReservationDetails
import goods.pocket.app.domain.dashboard.GetDashboardSummaryUseCase
import kotlin.test.Test
import kotlin.test.assertEquals

class DashboardContractTest {
    @Test
    fun `planned cleanup is not counted as owned`() {
        val entry = CollectionEntry(
            id = "cleanup",
            name = "Goods",
            category = "goods",
            status = CollectionEntryStatus.PLANNED_CLEANUP,
            createdAt = "2026-10-01T00:00:00Z",
            updatedAt = "2026-10-01T00:00:00Z",
        )
        assertEquals(
            0,
            GetDashboardSummaryUseCase.calculate(listOf(entry), "2026-10").ownedItemCount,
        )
    }

    @Test
    fun `receipt retains reservation spending month and excludes duplicate purchase amount`() {
        val reservation = CollectionEntry(
            id = "one", name = "Goods", category = "goods", status = CollectionEntryStatus.OWNED,
            purchasePrice = 20000, purchaseDate = "2026-10-04", createdAt = "2026-09-30T00:00:00Z",
            updatedAt = "2026-10-04T00:00:00Z",
            reservation = ReservationDetails(
                orderDate = "2026-09-30",
                totalPrice = 20000,
                receivedAt = "2026-10-04T00:00:00Z",
            ),
        )
        val result = GetDashboardSummaryUseCase.calculate(listOf(reservation), "2026-10")
        assertEquals(0L, result.monthlySpend)
        assertEquals(20000L, result.previousMonthSpend)
        assertEquals(1, result.ownedItemCount)
    }

    @Test
    fun `canceled reservation remains in historical totals but not active counts`() {
        val canceled = CollectionEntry(
            id = "one",
            name = "Goods",
            category = "goods",
            status = CollectionEntryStatus.RESERVED,
            createdAt = "2025-12-31T00:00:00Z",
            updatedAt = "2026-01-01T00:00:00Z",
            canceledAt = "2026-01-01T00:00:00Z",
            reservation = ReservationDetails(orderDate = "2025-12-31", totalPrice = 10000),
        )
        val result = GetDashboardSummaryUseCase.calculate(listOf(canceled), "2026-01")
        assertEquals(10000L, result.previousMonthSpend)
        assertEquals(0, result.activePreorderCount)
        val december = GetDashboardSummaryUseCase.calculate(listOf(canceled), "2025-12")
        assertEquals(10000L, december.spendingBuckets.last())
    }
}
