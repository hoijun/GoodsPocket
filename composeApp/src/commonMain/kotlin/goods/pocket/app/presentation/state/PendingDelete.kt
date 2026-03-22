package goods.pocket.app.presentation.state

sealed interface PendingDelete {
    data class ItemDelete(val itemId: String, val linkedTransactionCount: Int) : PendingDelete
    data class PreorderCancel(val preorderId: String) : PendingDelete
    data class TransactionDelete(val transactionId: String) : PendingDelete
    data class EventDelete(val eventId: String) : PendingDelete
}
