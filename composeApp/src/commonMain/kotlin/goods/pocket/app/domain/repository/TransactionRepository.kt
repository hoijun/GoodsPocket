package goods.pocket.app.domain.repository

import goods.pocket.app.domain.model.Transaction

interface TransactionRepository {
    fun getTransactions(monthFilter: String? = null): List<Transaction>
    fun getMonthlySummary(monthFilter: String): Long
    fun saveTransaction(transaction: Transaction)
    fun deleteTransaction(id: String)
}
