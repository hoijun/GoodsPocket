package goods.pocket.app.domain.usecase

import goods.pocket.app.domain.model.Transaction
import goods.pocket.app.domain.repository.CollectionRepository

class GetItemTransactionsUseCase(
    private val collectionRepository: CollectionRepository,
) {
    operator fun invoke(itemId: String): List<Transaction> = collectionRepository.getItemTransactions(itemId)
}
