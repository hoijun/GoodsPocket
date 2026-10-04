package goods.pocket.app.presentation.collection

import goods.pocket.app.domain.collection.CollectionEntry
import goods.pocket.app.presentation.state.CollectionSegment
import goods.pocket.app.presentation.state.CommandState

data class CollectionUiState(
    val entries: List<CollectionEntry> = emptyList(),
    val query: String = "",
    val segment: CollectionSegment = CollectionSegment.OWNED,
    val isLoading: Boolean = true,
    val hasLoadFailure: Boolean = false,
    val command: CommandState = CommandState(),
    val savedEntry: CollectionEntry? = null,
    val receiptRefresh: CommandState = CommandState(),
    val receiptPendingId: String? = null,
    val hasLoaded: Boolean = false,
) {
    fun canEdit(id: String): Boolean = receiptPendingId != id && !command.isRunning

    fun entry(id: String): CollectionEntry? {
        val observed = entries.firstOrNull { it.id == id }
        val saved = savedEntry?.takeIf { it.id == id } ?: return observed
        return saved
    }
}
