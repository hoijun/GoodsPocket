package goods.pocket.app.domain.usecase

import goods.pocket.app.data.InMemoryGoodsPocketRepository
import kotlin.test.Test
import kotlin.test.assertEquals

class GetDashboardSummaryUseCaseTest {

    private val repository = InMemoryGoodsPocketRepository()

    @Test
    fun returnsMarchDashboardSummary() {
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
}
