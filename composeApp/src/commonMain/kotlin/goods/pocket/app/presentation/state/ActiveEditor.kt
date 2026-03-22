package goods.pocket.app.presentation.state

sealed interface ActiveEditor {
    data class ItemEditor(val itemId: String) : ActiveEditor
    data class PreorderEditor(val preorderId: String) : ActiveEditor
    data class TransactionEditor(val transactionId: String) : ActiveEditor
    data class EventEditor(val eventId: String) : ActiveEditor
}
