package goods.pocket.app.domain.usecase

import goods.pocket.app.domain.model.Item
import goods.pocket.app.domain.repository.CollectionRepository

class SaveCollectionItemUseCase(
    private val collectionRepository: CollectionRepository,
) {
    operator fun invoke(item: Item) {
        collectionRepository.saveItem(item)
    }
}
