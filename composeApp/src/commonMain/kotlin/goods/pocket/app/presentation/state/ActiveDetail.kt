package goods.pocket.app.presentation.state

sealed interface ActiveDetail {
    data class CollectionEntryDetail(val entryId: String) : ActiveDetail
    data class EventDetail(val eventId: String) : ActiveDetail
}
