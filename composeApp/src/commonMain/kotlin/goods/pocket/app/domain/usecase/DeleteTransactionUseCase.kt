package goods.pocket.app.domain.usecase

import goods.pocket.app.domain.repository.TransactionRepository

class DeleteTransactionUseCase(
    private val transactionRepository: TransactionRepository,
) {
    operator fun invoke(transactionId: String) {
        transactionRepository.deleteTransaction(transactionId)
    }
}
