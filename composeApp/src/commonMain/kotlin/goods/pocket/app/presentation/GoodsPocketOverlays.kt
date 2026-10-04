package goods.pocket.app.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import goods.pocket.app.domain.collection.CollectionEntryStatus
import goods.pocket.app.presentation.component.CollectionDraft
import goods.pocket.app.presentation.component.CollectionEntryDetailSheet
import goods.pocket.app.presentation.component.CollectionEntryEditorSheet
import goods.pocket.app.presentation.component.DeleteConfirmationDialog
import goods.pocket.app.presentation.component.EventDetailSheet
import goods.pocket.app.presentation.component.EventDraft
import goods.pocket.app.presentation.component.EventEditorSheet
import goods.pocket.app.presentation.component.QuickAddSheet
import goods.pocket.app.presentation.i18n.tr
import goods.pocket.app.presentation.navigation.AppDestination
import goods.pocket.app.presentation.state.ActiveDetail
import goods.pocket.app.presentation.state.ActiveEditor
import goods.pocket.app.presentation.state.PendingDelete
import goods.pocket.app.presentation.state.QuickAddTarget
import goodspocket.composeapp.generated.resources.Res
import goodspocket.composeapp.generated.resources.action_cancel_preorder
import goodspocket.composeapp.generated.resources.action_delete
import goodspocket.composeapp.generated.resources.dialog_cancel_preorder_body
import goodspocket.composeapp.generated.resources.dialog_cancel_preorder_title
import goodspocket.composeapp.generated.resources.dialog_delete_event_body
import goodspocket.composeapp.generated.resources.dialog_delete_event_title
import goodspocket.composeapp.generated.resources.dialog_delete_item_body
import goodspocket.composeapp.generated.resources.dialog_delete_item_title

@Composable
internal fun GoodsPocketOverlays(session: PresentationSession) {
    val shell by session.shell.state.collectAsState()
    val collection by session.collection.state.collectAsState()
    val events by session.events.state.collectAsState()
    fun showEntry(id: String) {
        session.shell.navigate(AppDestination.Collection)
        session.shell.showDetail(ActiveDetail.CollectionEntryDetail(id))
    }
    fun showEvent(id: String) {
        session.shell.navigate(AppDestination.Events)
        session.shell.showDetail(ActiveDetail.EventDetail(id))
    }
    if (shell.isQuickAddOpen) {
        val command = if (shell.quickAddTarget ==
            QuickAddTarget.COLLECTION_ENTRY
        ) {
            session.collection.command
        } else {
            session.events.command
        }
        val commandState by command.state.collectAsState()
        QuickAddSheet(
            target = shell.quickAddTarget,
            commandState = commandState,
            onRetry = command::retry,
            onTargetChange = {
                if (!commandState.isRunning) {
                    command.cancel()
                    session.shell.selectQuickAddTarget(it)
                }
            },
            onDismiss = {
                if (!commandState.isRunning) {
                    command.cancel()
                    session.shell.closeQuickAdd()
                }
            },
            onSubmitCollectionEntry = {
                    name,
                    category,
                    status,
                    series,
                    character,
                    store,
                    release,
                    reservationStore,
                    note,
                ->
                session.collection.save(
                    CollectionDraft(
                        name,
                        category,
                        status,
                        series,
                        character,
                        store,
                        release,
                        reservationStore,
                        note,
                    ),
                    onSuccess = ::showEntry,
                )
            },
            onSubmitEvent = { title, date, type ->
                session.events.save(EventDraft(title, date, type), onSuccess = ::showEvent)
            },
        )
    }
    when (val detail = shell.detail) {
        is ActiveDetail.CollectionEntryDetail -> collection.entry(detail.entryId)?.let { entry ->
            CollectionEntryDetailSheet(
                entry = entry,
                commandState = if (collection.receiptPendingId ==
                    entry.id
                ) {
                    collection.receiptRefresh
                } else {
                    collection.command
                },
                actionsEnabled = collection.canEdit(entry.id),
                onRetry = {
                    if (collection.receiptPendingId ==
                        entry.id
                    ) {
                        session.collection.retryReceiptRefresh()
                    } else {
                        session.collection.command.retry()
                    }
                },
                onDismiss = {
                    if (!collection.command.isRunning) {
                        session.collection.command.cancel()
                        session.shell.closeDetail()
                    }
                },
                onEdit = {
                    session.collection.command.cancel()
                    session.shell.showEditor(ActiveEditor.CollectionEntryEditor(entry.id))
                },
                onDelete = {
                    session.collection.command.cancel()
                    session.shell.requestDelete(
                        if (entry.status ==
                            CollectionEntryStatus.RESERVED
                        ) {
                            PendingDelete.PreorderCancel(entry.id)
                        } else {
                            PendingDelete.ItemDelete(entry.id)
                        },
                    )
                },
                onMarkReceived = if (entry.status == CollectionEntryStatus.RESERVED) {
                    { session.collection.markReceived(entry.id) { showEntry(entry.id) } }
                } else {
                    null
                },
            )
        }
        is ActiveDetail.EventDetail -> events.event(detail.eventId)?.let { event ->
            EventDetailSheet(
                event = event,
                entries = collection.entries,
                onDismiss = session.shell::closeDetail,
                onEdit = { session.shell.showEditor(ActiveEditor.EventEditor(event.id)) },
                onDelete = { session.shell.requestDelete(PendingDelete.EventDelete(event.id)) },
            )
        }
        null -> Unit
    }
    when (val editor = shell.editor) {
        is ActiveEditor.CollectionEntryEditor -> collection.entry(editor.entryId)?.let { entry ->
            CollectionEntryEditorSheet(
                entry = entry,
                commandState = collection.command,
                onRetry = session.collection.command::retry,
                onDismiss = {
                    if (!collection.command.isRunning) {
                        session.collection.command.cancel()
                        session.shell.closeEditor()
                    }
                },
                onSave = {
                        name,
                        category,
                        status,
                        series,
                        character,
                        store,
                        release,
                        reservationStore,
                        note,
                    ->
                    session.collection.save(
                        CollectionDraft(
                            name,
                            category,
                            status,
                            series,
                            character,
                            store,
                            release,
                            reservationStore,
                            note,
                        ),
                        entry.id,
                        ::showEntry,
                    )
                },
            )
        }
        is ActiveEditor.EventEditor -> events.event(editor.eventId)?.let { event ->
            EventEditorSheet(
                event = event,
                commandState = events.command,
                onRetry = session.events.command::retry,
                onDismiss = {
                    if (!events.command.isRunning) {
                        session.events.command.cancel()
                        session.shell.closeEditor()
                    }
                },
                onSave = { title, date, type ->
                    session.events.save(EventDraft(title, date, type), event.id, ::showEvent)
                },
            )
        }
        null -> Unit
    }
    shell.pendingDelete?.let { pending ->
        val command = if (pending is PendingDelete.EventDelete) {
            session.events.command
        } else {
            session.collection.command
        }
        val commandState by command.state.collectAsState()
        DeleteConfirmationDialog(
            title = tr(
                when (pending) {
                    is PendingDelete.ItemDelete -> Res.string.dialog_delete_item_title
                    is PendingDelete.PreorderCancel -> Res.string.dialog_cancel_preorder_title
                    is PendingDelete.EventDelete -> Res.string.dialog_delete_event_title
                },
            ),
            body = tr(
                when (pending) {
                    is PendingDelete.ItemDelete -> Res.string.dialog_delete_item_body
                    is PendingDelete.PreorderCancel -> Res.string.dialog_cancel_preorder_body
                    is PendingDelete.EventDelete -> Res.string.dialog_delete_event_body
                },
            ),
            confirmLabel = tr(
                if (pending is PendingDelete.PreorderCancel) {
                    Res.string.action_cancel_preorder
                } else {
                    Res.string.action_delete
                },
            ),
            commandState = commandState,
            onRetry = command::retry,
            onDismiss = {
                if (!commandState.isRunning) {
                    command.cancel()
                    session.shell.dismissDelete()
                }
            },
            onConfirm = {
                when (pending) {
                    is PendingDelete.ItemDelete -> session.collection.delete(
                        pending.itemId,
                        false,
                    ) {
                        session.shell.navigate(AppDestination.Collection)
                    }
                    is PendingDelete.PreorderCancel -> session.collection.delete(
                        pending.preorderId,
                        true,
                    ) {
                        session.shell.navigate(AppDestination.Collection)
                    }
                    is PendingDelete.EventDelete -> session.events.delete(pending.eventId) {
                        session.shell.navigate(AppDestination.Events)
                    }
                }
            },
        )
    }
}
