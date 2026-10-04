package goods.pocket.app.presentation.navigation

import goods.pocket.app.presentation.state.ActiveDetail
import goods.pocket.app.presentation.state.ActiveEditor
import goods.pocket.app.presentation.state.PendingDelete
import goods.pocket.app.presentation.state.QuickAddTarget
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class ShellUiState(
    val destination: AppDestination = AppDestination.Home,
    val primaryDestination: AppDestination = AppDestination.Home,
    val isQuickAddOpen: Boolean = false,
    val quickAddTarget: QuickAddTarget = QuickAddTarget.COLLECTION_ENTRY,
    val detail: ActiveDetail? = null,
    val editor: ActiveEditor? = null,
    val pendingDelete: PendingDelete? = null,
)

class ShellStateHolder {
    private val _state = MutableStateFlow(ShellUiState())
    val state = _state.asStateFlow()

    fun navigate(destination: AppDestination) {
        _state.update {
            it.closed().copy(
                destination = destination,
                primaryDestination = if (destination ==
                    AppDestination.Settings
                ) {
                    it.primaryDestination
                } else {
                    destination
                },
            )
        }
    }

    fun back() {
        val current = _state.value
        when {
            current.pendingDelete != null -> dismissDelete()
            current.editor != null -> closeEditor()
            current.detail != null -> closeDetail()
            current.isQuickAddOpen -> closeQuickAdd()
            current.destination == AppDestination.Settings -> navigate(current.primaryDestination)
        }
    }

    fun openQuickAdd() {
        _state.update { it.closed().copy(isQuickAddOpen = true) }
    }

    fun closeQuickAdd() {
        _state.update { it.copy(isQuickAddOpen = false) }
    }

    fun selectQuickAddTarget(target: QuickAddTarget) {
        _state.update { it.copy(quickAddTarget = target) }
    }

    fun showDetail(detail: ActiveDetail) {
        _state.update { it.closed().copy(detail = detail) }
    }

    fun closeDetail() {
        _state.update { it.copy(detail = null) }
    }

    fun showEditor(editor: ActiveEditor) {
        _state.update { it.closed().copy(editor = editor) }
    }

    fun closeEditor() {
        _state.update { current ->
            current.closed().copy(
                detail = when (val editor = current.editor) {
                    is ActiveEditor.CollectionEntryEditor -> ActiveDetail.CollectionEntryDetail(
                        editor.entryId,
                    )
                    is ActiveEditor.EventEditor -> ActiveDetail.EventDetail(editor.eventId)
                    null -> null
                },
            )
        }
    }

    fun requestDelete(pending: PendingDelete) {
        _state.update { it.closed().copy(pendingDelete = pending) }
    }

    fun dismissDelete() {
        _state.update { current ->
            current.closed().copy(
                detail = when (val pending = current.pendingDelete) {
                    is PendingDelete.ItemDelete -> ActiveDetail.CollectionEntryDetail(
                        pending.itemId,
                    )
                    is PendingDelete.PreorderCancel -> ActiveDetail.CollectionEntryDetail(
                        pending.preorderId,
                    )
                    is PendingDelete.EventDelete -> ActiveDetail.EventDetail(pending.eventId)
                    null -> null
                },
            )
        }
    }

    private fun ShellUiState.closed() = copy(
        isQuickAddOpen = false,
        quickAddTarget = QuickAddTarget.COLLECTION_ENTRY,
        detail = null,
        editor = null,
        pendingDelete = null,
    )
}
