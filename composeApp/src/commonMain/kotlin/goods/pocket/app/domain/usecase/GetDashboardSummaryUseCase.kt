package goods.pocket.app.domain.usecase

import goods.pocket.app.domain.model.ActivityRecord
import goods.pocket.app.domain.model.HomeSummary
import goods.pocket.app.domain.repository.CollectionRepository
import goods.pocket.app.domain.repository.PreorderRepository

class GetDashboardSummaryUseCase(
    private val collectionRepository: CollectionRepository,
    private val preorderRepository: PreorderRepository,
) {
    operator fun invoke(monthFilter: String, recentActivities: List<ActivityRecord>): HomeSummary {
        return HomeSummary(
            monthlySpend = monthlySpendFor(monthFilter),
            ownedItemCount = collectionRepository.countOwnedItems(),
            activePreorderCount = preorderRepository.countActivePreorders(),
            recentActivities = recentActivities,
        )
    }

    private fun monthlySpendFor(monthFilter: String): Long {
        val itemSpend = collectionRepository.getItems()
            .filter { it.purchaseDate?.startsWith(monthFilter) == true }
            .sumOf { it.purchasePrice ?: 0L }
        val preorderSpend = preorderRepository.getPreorders()
            .filter { it.orderDate?.startsWith(monthFilter) == true }
            .sumOf { it.totalPrice ?: 0L }
        return itemSpend + preorderSpend
    }
}
