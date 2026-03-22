package goods.pocket.app.domain.usecase

import goods.pocket.app.domain.model.Transaction
import goods.pocket.app.domain.repository.TransactionRepository

class GetMonthlyTransactionsUseCase(
    private val transactionRepository: TransactionRepository,
) {
    operator fun invoke(monthFilter: String): List<Transaction> = transactionRepository.getTransactions(monthFilter)
}
