package goods.pocket.app.domain.usecase

import goods.pocket.app.domain.model.Item
import goods.pocket.app.domain.model.ItemStatus
import goods.pocket.app.domain.model.PreorderStatus
import goods.pocket.app.domain.repository.CollectionRepository
import goods.pocket.app.domain.repository.PreorderRepository
import goods.pocket.app.i18n.DEFAULT_LANGUAGE_CODE
import goods.pocket.app.i18n.localizedReceivedItemCategory

class MarkPreorderReceivedUseCase(
    private val preorderRepository: PreorderRepository,
    private val collectionRepository: CollectionRepository,
) {
    operator fun invoke(
        preorderId: String,
        receiveDate: String,
        newItemId: String,
        languageCode: String = DEFAULT_LANGUAGE_CODE,
    ) {
        val preorder = preorderRepository.getPreorder(preorderId) ?: return
        if (preorder.status == PreorderStatus.RECEIVED || preorder.status == PreorderStatus.CANCELED) return

        preorderRepository.markAsReceived(preorderId, receiveDate)

        collectionRepository.saveItem(
            Item(
                id = newItemId,
                name = preorder.name,
                category = localizedReceivedItemCategory(languageCode),
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
    }
}
