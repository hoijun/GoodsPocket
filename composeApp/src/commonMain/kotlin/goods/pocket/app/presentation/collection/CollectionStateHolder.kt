package goods.pocket.app.presentation.collection

import goods.pocket.app.domain.collection.CollectionEntry
import goods.pocket.app.domain.collection.CollectionEntryStatus
import goods.pocket.app.domain.collection.CollectionRepository
import goods.pocket.app.domain.collection.MarkPreorderReceivedUseCase
import goods.pocket.app.domain.collection.ReservationDetails
import goods.pocket.app.domain.collection.ReservationResult
import goods.pocket.app.domain.service.AppClock
import goods.pocket.app.domain.service.IdGenerator
import goods.pocket.app.presentation.component.CollectionDraft
import goods.pocket.app.presentation.state.CollectionSegment
import goods.pocket.app.presentation.state.CommandState
import goods.pocket.app.presentation.state.FeatureCommand
import goods.pocket.app.presentation.state.FeatureObservation
import goods.pocket.app.presentation.state.PendingSavedContent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CollectionStateHolder(
    private val repository: CollectionRepository,
    private val receive: MarkPreorderReceivedUseCase,
    private val clock: AppClock,
    private val ids: IdGenerator,
    scope: CoroutineScope,
) {
    private val content =
        FeatureObservation(scope, emptyList<CollectionEntry>(), repository::observeEntries)
    private val query = MutableStateFlow("")
    private val segment = MutableStateFlow(CollectionSegment.OWNED)
    private val savedEntry = MutableStateFlow<PendingSavedContent<CollectionEntry>?>(null)
    private val receiptPendingId = MutableStateFlow<String?>(null)
    private val receiptRefresh = FeatureCommand(scope)
    val command: FeatureCommand = FeatureCommand(scope)
    private val operations =
        combine(command.state, receiptRefresh.state, receiptPendingId) { write, read, id ->
            CollectionOperations(write, read, id)
        }
    val state: kotlinx.coroutines.flow.StateFlow<CollectionUiState> = combine(
        content.state,
        query,
        segment,
        operations,
        savedEntry,
    ) {
            data,
            query,
            segment,
            operations,
            saved,
        ->
        CollectionUiState(
            data.value,
            query,
            segment,
            data.isLoading,
            data.hasFailure,
            operations.write,
            saved?.value,
            operations.receiptRead,
            operations.receiptId,
            hasLoaded = data.hasLoaded,
        )
    }.stateIn(scope, SharingStarted.Eagerly, CollectionUiState())
    private var newEntryId: String? = null

    init {
        scope.launch {
            combine(content.state, savedEntry) { observed, saved ->
                saved?.takeIf { pending ->
                    observed.version > pending.observationVersion ||
                        observed.value.any { it == pending.value }
                }
            }.collect { acknowledged ->
                if (acknowledged != null &&
                    savedEntry.value == acknowledged
                ) {
                    savedEntry.value = null
                }
            }
        }
    }

    fun updateQuery(value: String) {
        query.value = value
    }

    fun selectSegment(value: CollectionSegment) {
        segment.value = value
    }

    fun resetFilters(value: CollectionSegment = CollectionSegment.OWNED) {
        query.value = ""
        segment.value = value
    }

    fun beginCreate() {
        newEntryId = ids.generate("entry")
        command.cancel()
    }

    fun retryLoad() = content.retry()

    fun retryReceiptRefresh() = receiptRefresh.retry()

    fun save(draft: CollectionDraft, entryId: String? = null, onSuccess: (String) -> Unit) {
        if (!draft.canSubmit || command.state.value.isRunning) return
        if (entryId != null && receiptPendingId.value == entryId) return
        val existing = entryId?.let { id -> state.value.entry(id) }
        if (entryId != null && existing == null) return
        val id = entryId ?: newEntryId ?: ids.generate("entry").also { newEntryId = it }
        val now = clock.currentTimestamp()
        val entry = (
            existing ?: CollectionEntry(
                id = id,
                name = draft.name.trim(),
                category = draft.category.trim(),
                status = draft.status,
                createdAt = now,
                updatedAt = now,
            )
            ).copy(
            name = draft.name.trim(),
            category = draft.category.trim(),
            status = draft.status,
            seriesName = draft.seriesName.trim().ifBlank { null },
            characterName = draft.characterName.trim().ifBlank { null },
            purchaseStore = draft.purchaseStore.trim().ifBlank { null },
            releaseDate = draft.releaseDate.trim().ifBlank { null },
            reservationStore = draft.reservationStore.trim().ifBlank { null },
            reservation =
            existing?.reservation
                ?: ReservationDetails(orderDate = clock.currentDate()).takeIf {
                    draft.isReserved
                },
            note = draft.note.trim().ifBlank { null },
            updatedAt = now,
        )
        command.run(onSuccess = {
            savedEntry.value = PendingSavedContent(entry, content.state.value.version)
            if (entryId == null) newEntryId = null
            segment.value = if (entry.status == CollectionEntryStatus.RESERVED) {
                CollectionSegment.RESERVED
            } else {
                CollectionSegment.OWNED
            }
            onSuccess(id)
        }) { repository.saveEntry(entry) }
    }

    fun markReceived(id: String, onSuccess: () -> Unit) {
        if (receiptPendingId.value == id) return
        var received = false
        command.run(onSuccess = {
            if (received) {
                receiptPendingId.value = id
                receiptRefresh.run(onSuccess = {
                    receiptPendingId.value = null
                    segment.value = CollectionSegment.OWNED
                }) {
                    val refreshed = checkNotNull(repository.getEntry(id))
                    savedEntry.value = PendingSavedContent(refreshed, content.state.value.version)
                }
                onSuccess()
            }
        }) {
            received = when (receive(id)) {
                ReservationResult.UPDATED, ReservationResult.ALREADY_RECEIVED -> true
                ReservationResult.NOT_FOUND, ReservationResult.NOT_RESERVED -> false
            }
        }
    }

    fun delete(id: String, reserved: Boolean, onSuccess: () -> Unit) {
        if (receiptPendingId.value == id) return
        val timestamp = clock.currentTimestamp()
        command.run(onSuccess = {
            if (savedEntry.value?.value?.id == id) savedEntry.value = null
            onSuccess()
        }) {
            if (reserved) {
                repository.cancelReservation(
                    id,
                    timestamp,
                )
            } else {
                repository.deleteEntry(id)
            }
        }
    }
}

private data class CollectionOperations(
    val write: CommandState,
    val receiptRead: CommandState,
    val receiptId: String?,
)
