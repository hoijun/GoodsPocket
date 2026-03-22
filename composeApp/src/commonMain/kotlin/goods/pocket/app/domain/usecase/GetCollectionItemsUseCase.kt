package goods.pocket.app.domain.usecase

import goods.pocket.app.domain.model.Item
import goods.pocket.app.domain.repository.CollectionRepository

class GetCollectionItemsUseCase(
    private val collectionRepository: CollectionRepository,
) {
    operator fun invoke(filter: String? = null): List<Item> = collectionRepository.getItems(filter)
}
