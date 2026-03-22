package goods.pocket.app.presentation.state

sealed interface ActiveDetail {
    data class ItemDetail(val itemId: String) : ActiveDetail
    data class PreorderDetail(val preorderId: String) : ActiveDetail
    data class TransactionDetail(val transactionId: String) : ActiveDetail
    data class EventDetail(val eventId: String) : ActiveDetail
}
