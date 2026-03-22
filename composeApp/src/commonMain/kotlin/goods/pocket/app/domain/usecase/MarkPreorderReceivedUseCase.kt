package goods.pocket.app.domain.usecase

import goods.pocket.app.domain.model.Item
import goods.pocket.app.domain.model.ItemStatus
import goods.pocket.app.domain.model.PreorderStatus
import goods.pocket.app.domain.model.Transaction
import goods.pocket.app.domain.model.TransactionType
import goods.pocket.app.domain.repository.CollectionRepository
import goods.pocket.app.domain.repository.PreorderRepository
import goods.pocket.app.domain.repository.TransactionRepository

class MarkPreorderReceivedUseCase(
    private val preorderRepository: PreorderRepository,
    private val collectionRepository: CollectionRepository,
    private val transactionRepository: TransactionRepository,
) {
    operator fun invoke(preorderId: String, receiveDate: String, newItemId: String, transactionId: String) {
        val preorder = preorderRepository.getPreorder(preorderId) ?: return
        if (preorder.status == PreorderStatus.RECEIVED || preorder.status == PreorderStatus.CANCELED) return

        preorderRepository.markAsReceived(preorderId, receiveDate)

        collectionRepository.saveItem(
            Item(
                id = newItemId,
                name = preorder.name,
                category = "Preorder Item",
                status = ItemStatus.OWNED,
                seriesName = preorder.seriesName,
                characterName = preorder.characterName,
                quantity = 1,
                purchasePrice = preorder.totalPrice,
                purchaseDate = receiveDate,
                purchaseStore = preorder.storeName,
                linkedPreorderId = preorder.id,
                note = preorder.note,
                createdAt = receiveDate,
                updatedAt = receiveDate,
            ),
        )

        val remainingPrice = preorder.remainingPrice ?: 0L
        if (remainingPrice > 0L) {
            transactionRepository.saveTransaction(
                Transaction(
                    id = transactionId,
                    type = TransactionType.BALANCE,
                    amount = remainingPrice,
                    transactionDate = receiveDate,
                    relatedItemId = newItemId,
                    relatedPreorderId = preorder.id,
                    placeName = preorder.storeName,
                    createdAt = receiveDate,
                ),
            )
        }
    }
}
