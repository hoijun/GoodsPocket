package goods.pocket.app.presentation.state

sealed interface ActiveEditor {
    data class CollectionEntryEditor(val entryId: String) : ActiveEditor
    data class EventEditor(val eventId: String) : ActiveEditor
}
