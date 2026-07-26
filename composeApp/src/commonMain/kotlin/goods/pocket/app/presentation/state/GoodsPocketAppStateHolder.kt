package goods.pocket.app.presentation.state

import goods.pocket.app.domain.model.Event
import goods.pocket.app.domain.model.EventType
import goods.pocket.app.domain.model.CollectionEntry
import goods.pocket.app.domain.model.CollectionEntryStatus
import goods.pocket.app.domain.repository.CollectionRepository
import goods.pocket.app.domain.repository.EventRepository
import goods.pocket.app.domain.repository.PreorderRepository
import goods.pocket.app.domain.repository.SettingsRepository
import goods.pocket.app.domain.service.AppClock
import goods.pocket.app.domain.service.IdGenerator
import goods.pocket.app.domain.usecase.MarkPreorderReceivedUseCase
import goods.pocket.app.presentation.navigation.AppDestination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GoodsPocketAppStateHolder(
    private val collectionRepository: CollectionRepository,
    private val preorderRepository: PreorderRepository,
    private val eventRepository: EventRepository,
    private val settingsRepository: SettingsRepository,
    private val contentLoader: GoodsPocketContentLoader,
    private val markPreorderReceivedUseCase: MarkPreorderReceivedUseCase,
    private val clock: AppClock,
    private val idGenerator: IdGenerator,
    private val coroutineScope: CoroutineScope,
) {
    private val _state = MutableStateFlow(GoodsPocketUiState())
    val state: StateFlow<GoodsPocketUiState> = _state.asStateFlow()
    private val mutationRunner = GoodsPocketMutationRunner(_state, coroutineScope)

    init {
        reload()
    }

    fun selectDestination(destination: AppDestination) {
        _state.update { current ->
            val closed = current.withClosedOverlays()
            closed.copy(
                currentDestination = destination,
                selectedPrimaryDestination = selectedPrimaryDestinationFor(
                    destination = destination,
                    fallback = closed.selectedPrimaryDestination,
                ),
                collectionSegment = when (destination) {
                    AppDestination.Collection -> CollectionSegment.OWNED
                    else -> closed.collectionSegment
                },
                collectionQuery = if (destination == AppDestination.Collection) "" else closed.collectionQuery,
                eventTypeFilter = if (destination == AppDestination.Events) null else closed.eventTypeFilter,
            )
        }
    }

    fun openSettings() {
        _state.update {
            it.withClosedOverlays().copy(currentDestination = AppDestination.Settings)
        }
    }

    fun closeSettings() {
        _state.update { current ->
            current.copy(currentDestination = current.selectedPrimaryDestination)
        }
    }

    fun openEventsOverview() {
        _state.update { current ->
            current.withClosedOverlays().copy(
                currentDestination = AppDestination.Events,
                selectedPrimaryDestination = AppDestination.Events,
                eventTypeFilter = null,
            )
        }
    }

    fun openEventFromHome(eventId: String) {
        _state.update { current ->
            current.withClosedOverlays().copy(
                currentDestination = AppDestination.Events,
                selectedPrimaryDestination = AppDestination.Events,
                eventTypeFilter = null,
                activeDetail = ActiveDetail.EventDetail(eventId),
            )
        }
    }

    fun openActivity(activityId: String) {
        val entry = _state.value.collectionEntries.firstOrNull { it.id == activityId } ?: return
        val segment = if (entry.status == CollectionEntryStatus.RESERVED) {
            CollectionSegment.RESERVED
        } else {
            CollectionSegment.OWNED
        }
        _state.update { current ->
            current.withClosedOverlays().copy(
                currentDestination = AppDestination.Collection,
                selectedPrimaryDestination = AppDestination.Collection,
                collectionSegment = segment,
                collectionQuery = "",
                activeDetail = ActiveDetail.CollectionEntryDetail(activityId),
            )
        }
    }

    fun updateLanguage(languageCode: String) {
        val currentPreferences = _state.value.appPreferences
        launchOperation(GoodsPocketOperation.UPDATE) {
            settingsRepository.updateAppPreferences(
                currentPreferences.copy(languageCode = languageCode),
            )
            reloadNow()
        }
    }

    fun updateCollectionQuery(query: String) {
        _state.update { it.copy(collectionQuery = query) }
    }

    fun selectCollectionSegment(segment: CollectionSegment) {
        _state.update { current ->
            current.withClosedOverlays().copy(
                currentDestination = AppDestination.Collection,
                selectedPrimaryDestination = AppDestination.Collection,
                collectionSegment = segment,
            )
        }
    }

    fun openCollectionFromHome(segment: CollectionSegment) {
        _state.update { current ->
            current.withClosedOverlays().copy(
                currentDestination = AppDestination.Collection,
                selectedPrimaryDestination = AppDestination.Collection,
                collectionSegment = segment,
                collectionQuery = "",
            )
        }
    }

    fun markCollectionEntryReceived(entryId: String) {
        val existing = _state.value.collectionEntries.firstOrNull { it.id == entryId } ?: return
        if (existing.status != CollectionEntryStatus.RESERVED) return

        launchOperation(GoodsPocketOperation.UPDATE) {
            markPreorderReceivedUseCase(entryId)
            reloadNow()
            _state.update { current ->
                current.copy(
                    currentDestination = AppDestination.Collection,
                    selectedPrimaryDestination = AppDestination.Collection,
                    collectionSegment = CollectionSegment.OWNED,
                    activeDetail = ActiveDetail.CollectionEntryDetail(entryId),
                )
            }
        }
    }

    fun updateEventTypeFilter(type: EventType?) {
        _state.update { it.copy(eventTypeFilter = type) }
    }

    fun openQuickAdd() {
        _state.update {
            it.withClosedOverlays().copy(
                isQuickAddOpen = true,
                quickAddTarget = QuickAddTarget.COLLECTION_ENTRY,
            )
        }
    }

    fun closeQuickAdd() {
        _state.update { it.copy(isQuickAddOpen = false) }
    }

    fun selectQuickAddTarget(target: QuickAddTarget) {
        _state.update { it.copy(quickAddTarget = target) }
    }

    fun submitCollectionEntry(
        name: String,
        category: String,
        status: CollectionEntryStatus,
        seriesName: String,
        characterName: String,
        purchaseStore: String,
        releaseDate: String,
        reservationStore: String,
        note: String,
    ) {
        val newId = generateId(if (status == CollectionEntryStatus.RESERVED) "pre" else "item")
        launchOperation(GoodsPocketOperation.SAVE) {
            saveCollectionEntry(
                entryId = newId,
                existingStatus = null,
                name = name,
                category = category,
                status = status,
                seriesName = seriesName,
                characterName = characterName,
                purchaseStore = purchaseStore,
                releaseDate = releaseDate,
                reservationStore = reservationStore,
                note = note,
                createdAt = clock.currentDate(),
            )
            reloadAndCloseSheet(
                destination = AppDestination.Collection,
                detail = ActiveDetail.CollectionEntryDetail(newId),
                collectionSegmentOverride = if (status == CollectionEntryStatus.RESERVED) {
                    CollectionSegment.RESERVED
                } else {
                    CollectionSegment.OWNED
                },
            )
        }
    }

    fun submitEvent(title: String, targetDate: String, eventType: EventType) {
        val newId = generateId("event")
        launchOperation(GoodsPocketOperation.SAVE) {
            eventRepository.saveEvent(
                Event(
                    id = newId,
                    title = title.trim(),
                    eventType = eventType,
                    targetDate = targetDate.trim(),
                    createdAt = clock.currentDate(),
                    updatedAt = clock.currentDate(),
                ),
            )
            reloadAndCloseSheet(
                destination = AppDestination.Events,
                detail = ActiveDetail.EventDetail(newId),
            )
        }
    }

    fun openCollectionEntryDetail(entryId: String) {
        _state.update {
            it.withClosedOverlays().copy(activeDetail = ActiveDetail.CollectionEntryDetail(entryId))
        }
    }

    fun openEventDetail(eventId: String) {
        _state.update {
            it.withClosedOverlays().copy(activeDetail = ActiveDetail.EventDetail(eventId))
        }
    }

    fun closeDetail() {
        _state.update { it.copy(activeDetail = null) }
    }

    fun openCollectionEntryEditor(entryId: String) {
        _state.update {
            it.withClosedOverlays().copy(
                activeEditor = ActiveEditor.CollectionEntryEditor(entryId),
            )
        }
    }

    fun openEventEditor(eventId: String) {
        _state.update {
            it.withClosedOverlays().copy(
                activeEditor = ActiveEditor.EventEditor(eventId),
            )
        }
    }

    fun closeEditor() {
        _state.update { it.copy(activeEditor = null) }
    }

    fun saveEditedCollectionEntry(
        entryId: String,
        name: String,
        category: String,
        status: CollectionEntryStatus,
        seriesName: String,
        characterName: String,
        purchaseStore: String,
        releaseDate: String,
        reservationStore: String,
        note: String,
    ) {
        val existing = _state.value.collectionEntries.firstOrNull { it.id == entryId } ?: return
        launchOperation(GoodsPocketOperation.UPDATE) {
            saveCollectionEntry(
                entryId = entryId,
                existingStatus = existing.status,
                name = name,
                category = category,
                status = status,
                seriesName = seriesName,
                characterName = characterName,
                purchaseStore = purchaseStore,
                releaseDate = releaseDate,
                reservationStore = reservationStore,
                note = note,
                createdAt = existing.createdAt,
            )
            reloadNow()
            _state.update { current ->
                current.copy(
                    currentDestination = AppDestination.Collection,
                    selectedPrimaryDestination = AppDestination.Collection,
                    collectionSegment = if (status == CollectionEntryStatus.RESERVED) {
                        CollectionSegment.RESERVED
                    } else {
                        CollectionSegment.OWNED
                    },
                    activeEditor = null,
                    activeDetail = ActiveDetail.CollectionEntryDetail(entryId),
                )
            }
        }
    }

    fun saveEditedEvent(
        eventId: String,
        title: String,
        targetDate: String,
        eventType: EventType,
    ) {
        val existing = _state.value.events.firstOrNull { it.id == eventId } ?: return
        launchOperation(GoodsPocketOperation.UPDATE) {
            eventRepository.saveEvent(
                existing.copy(
                    title = title.trim(),
                    targetDate = targetDate.trim(),
                    eventType = eventType,
                    updatedAt = clock.currentDate(),
                ),
            )
            reloadNow()
            _state.update {
                it.copy(
                    activeEditor = null,
                    activeDetail = ActiveDetail.EventDetail(eventId),
                )
            }
        }
    }

    fun requestDeleteCollectionEntry(entryId: String) {
        val existing = _state.value.collectionEntries.firstOrNull { it.id == entryId } ?: return
        val pendingDelete = if (existing.status == CollectionEntryStatus.RESERVED) {
            PendingDelete.PreorderCancel(entryId)
        } else {
            PendingDelete.ItemDelete(entryId)
        }
        _state.update {
            it.withClosedOverlays().copy(
                pendingDelete = pendingDelete,
            )
        }
    }

    fun requestDeleteEvent(eventId: String) {
        _state.update {
            it.withClosedOverlays().copy(
                pendingDelete = PendingDelete.EventDelete(eventId),
            )
        }
    }

    fun dismissPendingDelete() {
        _state.update { it.copy(pendingDelete = null) }
    }

    fun confirmPendingDelete() {
        when (val pending = _state.value.pendingDelete) {
            is PendingDelete.ItemDelete -> {
                launchOperation(GoodsPocketOperation.DELETE) {
                    collectionRepository.deleteEntry(pending.itemId, deletedAt = clock.currentDate())
                    reloadNow()
                    _state.update { current ->
                        current.copy(
                            pendingDelete = null,
                            currentDestination = AppDestination.Collection,
                            selectedPrimaryDestination = AppDestination.Collection,
                        )
                    }
                }
            }

            is PendingDelete.PreorderCancel -> {
                launchOperation(GoodsPocketOperation.DELETE) {
                    preorderRepository.cancelPreorder(pending.preorderId, canceledAt = clock.currentDate())
                    reloadNow()
                    _state.update { current ->
                        current.copy(
                            pendingDelete = null,
                            currentDestination = AppDestination.Collection,
                            selectedPrimaryDestination = AppDestination.Collection,
                            collectionSegment = CollectionSegment.RESERVED,
                            activeDetail = null,
                        )
                    }
                }
            }

            is PendingDelete.EventDelete -> {
                launchOperation(GoodsPocketOperation.DELETE) {
                    eventRepository.deleteEvent(pending.eventId)
                    reloadNow()
                    _state.update { current ->
                        current.copy(
                            pendingDelete = null,
                            currentDestination = AppDestination.Events,
                        )
                    }
                }
            }

            null -> Unit
        }
    }

    private suspend fun reloadAndCloseSheet(
        destination: AppDestination,
        detail: ActiveDetail,
        collectionSegmentOverride: CollectionSegment? = null,
    ) {
        reloadNow()
        _state.update { current ->
            current.copy(
                currentDestination = destination,
                selectedPrimaryDestination = selectedPrimaryDestinationFor(
                    destination = destination,
                    fallback = current.selectedPrimaryDestination,
                ),
                collectionSegment = collectionSegmentOverride ?: current.collectionSegment,
                activeDetail = detail,
                isQuickAddOpen = false,
            )
        }
    }

    private suspend fun saveCollectionEntry(
        entryId: String,
        existingStatus: CollectionEntryStatus?,
        name: String,
        category: String,
        status: CollectionEntryStatus,
        seriesName: String,
        characterName: String,
        purchaseStore: String,
        releaseDate: String,
        reservationStore: String,
        note: String,
        createdAt: String,
    ) {
        val existingEntry = _state.value.collectionEntries.firstOrNull { it.id == entryId }
        val changedAt = clock.currentDate()
        collectionRepository.saveEntry(
            CollectionEntry(
                id = entryId,
                name = name.trim(),
                category = category.trim(),
                status = status,
                seriesName = seriesName.trim().ifBlank { null },
                characterName = characterName.trim().ifBlank { null },
                quantity = existingEntry?.quantity ?: 1,
                purchasePrice = existingEntry?.purchasePrice,
                purchaseDate = if (status != CollectionEntryStatus.RESERVED) {
                    existingEntry?.purchaseDate ?: changedAt.takeIf {
                        existingStatus == CollectionEntryStatus.RESERVED
                    }
                } else {
                    null
                },
                purchaseStore = purchaseStore.trim().ifBlank {
                    existingEntry?.purchaseStore ?: existingEntry?.reservationStore.orEmpty()
                }.ifBlank { null },
                storageLocationId = existingEntry?.storageLocationId,
                releaseDate = releaseDate.trim().ifBlank { null },
                reservationStore = reservationStore.trim().ifBlank { null },
                note = note.trim().ifBlank { null },
                createdAt = createdAt,
                updatedAt = changedAt,
            ),
        )
    }

    fun retry() {
        mutationRunner.retry()
    }

    fun dismissFailure() {
        mutationRunner.dismissFailure()
    }

    private fun reload() {
        launchOperation(GoodsPocketOperation.LOAD) {
            reloadNow()
        }
    }

    private suspend fun reloadNow() {
        contentLoader.reload(
            state = _state,
            upcomingEventPreviewLimit = UPCOMING_EVENT_PREVIEW_LIMIT,
        )
    }

    private fun launchOperation(
        operation: GoodsPocketOperation,
        block: suspend () -> Unit,
    ) {
        mutationRunner.run(operation, block)
    }

    private fun generateId(prefix: String): String {
        return idGenerator.generate(prefix)
    }

    companion object {
        private const val UPCOMING_EVENT_PREVIEW_LIMIT = 3
    }
}

private fun GoodsPocketUiState.withClosedOverlays(): GoodsPocketUiState {
    return copy(
        isQuickAddOpen = false,
        activeDetail = null,
        activeEditor = null,
        pendingDelete = null,
    )
}
