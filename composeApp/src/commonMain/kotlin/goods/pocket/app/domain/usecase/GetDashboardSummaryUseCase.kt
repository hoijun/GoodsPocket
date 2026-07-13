package goods.pocket.app.domain.usecase

import goods.pocket.app.domain.model.ActivityRecord
import goods.pocket.app.domain.model.HomeSummary
import goods.pocket.app.domain.model.Preorder
import goods.pocket.app.domain.repository.CollectionRepository
import goods.pocket.app.domain.repository.PreorderRepository

class GetDashboardSummaryUseCase(
    private val collectionRepository: CollectionRepository,
    private val preorderRepository: PreorderRepository,
) {
    suspend operator fun invoke(monthFilter: String, recentActivities: List<ActivityRecord>): HomeSummary {
        return HomeSummary(
            monthlySpend = monthlySpendFor(monthFilter),
            ownedItemCount = collectionRepository.countOwnedItems(),
            activePreorderCount = preorderRepository.countActivePreorders(),
            recentActivities = recentActivities,
        )
    }

    private suspend fun monthlySpendFor(monthFilter: String): Long {
        val preorders = preorderRepository.getPreorders()
        val preorderIds = preorders.mapTo(mutableSetOf(), Preorder::id)
        val itemSpend = collectionRepository.getItems()
            .filter { item ->
                item.purchaseDate?.startsWith(monthFilter) == true &&
                    item.linkedPreorderId !in preorderIds
            }
            .sumOf { it.purchasePrice ?: 0L }
        val preorderSpend = preorders
            .filter { it.orderDate?.startsWith(monthFilter) == true }
            .sumOf { it.totalPrice ?: 0L }
        return itemSpend + preorderSpend
    }
}
