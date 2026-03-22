package goods.pocket.app.domain.model

data class Transaction(
    val id: String,
    val type: TransactionType,
    val amount: Long,
    val transactionDate: String,
    val relatedItemId: String? = null,
    val relatedPreorderId: String? = null,
    val paymentMethod: String? = null,
    val placeName: String? = null,
    val note: String? = null,
    val createdAt: String,
)

enum class TransactionType {
    DEPOSIT,
    BALANCE,
    PURCHASE,
    SHIPPING,
    REFUND,
    TRANSFER_INCOME,
}
