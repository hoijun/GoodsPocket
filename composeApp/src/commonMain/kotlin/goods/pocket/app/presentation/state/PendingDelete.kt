package goods.pocket.app.presentation.state

sealed interface PendingDelete {
    data class ItemDelete(val itemId: String) : PendingDelete
    data class PreorderCancel(val preorderId: String) : PendingDelete
    data class EventDelete(val eventId: String) : PendingDelete
}
