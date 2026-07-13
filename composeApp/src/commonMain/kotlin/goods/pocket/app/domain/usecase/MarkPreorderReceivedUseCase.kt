package goods.pocket.app.domain.usecase

import goods.pocket.app.domain.model.Item
import goods.pocket.app.domain.model.ItemStatus
import goods.pocket.app.domain.model.GOODS_COLLECTION_CATEGORY_CODE
import goods.pocket.app.domain.repository.CollectionRepository
import goods.pocket.app.domain.service.AppClock

class MarkPreorderReceivedUseCase(
    private val collectionRepository: CollectionRepository,
    private val clock: AppClock,
) {
    suspend operator fun invoke(preorderId: String): MarkPreorderReceivedResult {
        val entry = collectionRepository.getEntry(preorderId)
            ?: return MarkPreorderReceivedResult.NOT_FOUND
        if (entry.status != goods.pocket.app.domain.model.CollectionEntryStatus.RESERVED) {
            return MarkPreorderReceivedResult.NOT_RECEIVABLE
        }

        val receivedAt = clock.currentDate()
        val receivedItem = Item(
            id = entry.id,
            name = entry.name,
            category = RECEIVED_ITEM_CATEGORY_CODE,
            status = ItemStatus.OWNED,
            seriesName = entry.seriesName,
            characterName = entry.characterName,
            quantity = entry.quantity,
            purchasePrice = entry.purchasePrice,
            purchaseDate = receivedAt,
            purchaseStore = entry.reservationStore ?: entry.purchaseStore,
            storageLocationId = entry.storageLocationId,
            linkedPreorderId = entry.id,
            note = entry.note,
            createdAt = entry.createdAt,
            updatedAt = receivedAt,
        )
        collectionRepository.receiveReservedEntry(
            preorderId = preorderId,
            receivedItem = receivedItem,
            receivedAt = receivedAt,
        )
        return MarkPreorderReceivedResult.RECEIVED
    }
}

enum class MarkPreorderReceivedResult {
    RECEIVED,
    NOT_FOUND,
    NOT_RECEIVABLE,
}

const val RECEIVED_ITEM_CATEGORY_CODE = GOODS_COLLECTION_CATEGORY_CODE
