package goods.pocket.app.domain.usecase

import goods.pocket.app.domain.model.ActivityRecord
import goods.pocket.app.domain.model.Item
import goods.pocket.app.domain.model.Preorder
import goods.pocket.app.domain.model.Transaction
import goods.pocket.app.domain.repository.CollectionRepository
import goods.pocket.app.domain.repository.PreorderRepository
import goods.pocket.app.domain.repository.TransactionRepository

class GetRecentActivitiesUseCase(
    private val collectionRepository: CollectionRepository,
    private val preorderRepository: PreorderRepository,
    private val transactionRepository: TransactionRepository,
) {
    operator fun invoke(limit: Int): List<ActivityRecord> {
        val itemActivities = collectionRepository.getItems().map(Item::toActivityRecord)
        val preorderActivities = preorderRepository.getPreorders().map(Preorder::toActivityRecord)
        val transactionActivities = transactionRepository.getTransactions().map(Transaction::toActivityRecord)
        return (itemActivities + preorderActivities + transactionActivities)
            .sortedByDescending(ActivityRecord::happenedAt)
            .take(limit)
    }
}

private fun Item.toActivityRecord(): ActivityRecord {
    return ActivityRecord(
        id = id,
        title = name,
        subtitle = "Collection item added",
        happenedAt = updatedAt,
    )
}

private fun Preorder.toActivityRecord(): ActivityRecord {
    return ActivityRecord(
        id = id,
        title = name,
        subtitle = "Preorder tracked at $storeName",
        happenedAt = updatedAt,
    )
}

private fun Transaction.toActivityRecord(): ActivityRecord {
    return ActivityRecord(
        id = id,
        title = "${amount} spent",
        subtitle = "Transaction recorded as $type",
        happenedAt = createdAt,
    )
}
