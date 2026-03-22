package goods.pocket.app.domain.usecase

import goods.pocket.app.domain.model.ActivityRecord
import goods.pocket.app.domain.model.HomeSummary
import goods.pocket.app.domain.repository.CollectionRepository
import goods.pocket.app.domain.repository.PreorderRepository
import goods.pocket.app.domain.repository.TransactionRepository

class GetDashboardSummaryUseCase(
    private val collectionRepository: CollectionRepository,
    private val preorderRepository: PreorderRepository,
    private val transactionRepository: TransactionRepository,
) {
    operator fun invoke(monthFilter: String, recentActivities: List<ActivityRecord>): HomeSummary {
        return HomeSummary(
            monthlySpend = transactionRepository.getMonthlySummary(monthFilter),
            ownedItemCount = collectionRepository.countOwnedItems(),
            activePreorderCount = preorderRepository.countActivePreorders(),
            recentActivities = recentActivities,
        )
    }
}
