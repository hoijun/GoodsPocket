package goods.pocket.app.domain.usecase

import goods.pocket.app.domain.repository.CollectionRepository

class DeleteCollectionItemUseCase(
    private val collectionRepository: CollectionRepository,
) {
    operator fun invoke(itemId: String) {
        collectionRepository.deleteItem(itemId)
    }
}
