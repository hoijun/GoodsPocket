package goods.pocket.app.domain.usecase

import goods.pocket.app.domain.model.Transaction
import goods.pocket.app.domain.repository.TransactionRepository

class SaveTransactionUseCase(
    private val transactionRepository: TransactionRepository,
) {
    operator fun invoke(transaction: Transaction) {
        transactionRepository.saveTransaction(transaction)
    }
}
