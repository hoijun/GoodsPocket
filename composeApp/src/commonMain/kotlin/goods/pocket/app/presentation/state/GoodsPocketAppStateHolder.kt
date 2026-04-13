package goods.pocket.app.presentation.state

import goods.pocket.app.domain.model.Event
import goods.pocket.app.domain.model.EventType
import goods.pocket.app.domain.model.Item
import goods.pocket.app.domain.model.ItemStatus
import goods.pocket.app.domain.model.Preorder
import goods.pocket.app.domain.model.PreorderStatus
import goods.pocket.app.domain.model.CollectionEntryStatus
import goods.pocket.app.domain.usecase.CancelPreorderUseCase
import goods.pocket.app.domain.usecase.DeleteCollectionItemUseCase
import goods.pocket.app.domain.usecase.DeleteEventUseCase
import goods.pocket.app.domain.usecase.GetCollectionItemsUseCase
import goods.pocket.app.domain.usecase.GetAppPreferencesUseCase
import goods.pocket.app.domain.usecase.GetDashboardSummaryUseCase
import goods.pocket.app.domain.usecase.GetEventListUseCase
import goods.pocket.app.domain.usecase.GetPreorderListUseCase
import goods.pocket.app.domain.usecase.GetRecentActivitiesUseCase
import goods.pocket.app.domain.usecase.GetStorageLocationsUseCase
import goods.pocket.app.domain.usecase.GetUpcomingEventsUseCase
import goods.pocket.app.domain.usecase.MarkPreorderReceivedUseCase
import goods.pocket.app.domain.usecase.SaveCollectionItemUseCase
import goods.pocket.app.domain.usecase.SaveEventUseCase
import goods.pocket.app.domain.usecase.SavePreorderUseCase
import goods.pocket.app.domain.usecase.UpdateAppPreferencesUseCase
import goods.pocket.app.presentation.navigation.AppDestination
import kotlin.random.Random
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GoodsPocketAppStateHolder(
    private val getCollectionItemsUseCase: GetCollectionItemsUseCase,
    private val getAppPreferencesUseCase: GetAppPreferencesUseCase,
    private val getDashboardSummaryUseCase: GetDashboardSummaryUseCase,
    private val getEventListUseCase: GetEventListUseCase,
    private val getPreorderListUseCase: GetPreorderListUseCase,
    private val getRecentActivitiesUseCase: GetRecentActivitiesUseCase,
    private val getStorageLocationsUseCase: GetStorageLocationsUseCase,
    private val getUpcomingEventsUseCase: GetUpcomingEventsUseCase,
    private val cancelPreorderUseCase: CancelPreorderUseCase,
    private val deleteCollectionItemUseCase: DeleteCollectionItemUseCase,
    private val deleteEventUseCase: DeleteEventUseCase,
    private val markPreorderReceivedUseCase: MarkPreorderReceivedUseCase,
    private val saveCollectionItemUseCase: SaveCollectionItemUseCase,
    private val saveEventUseCase: SaveEventUseCase,
    private val savePreorderUseCase: SavePreorderUseCase,
    private val updateAppPreferencesUseCase: UpdateAppPreferencesUseCase,
) {
    private val _state = MutableStateFlow(GoodsPocketUiState())
    val state: StateFlow<GoodsPocketUiState> = _state.asStateFlow()

    init {
        reload()
    }

    fun selectDestination(destination: AppDestination) {
        _state.update { current ->
            current.copy(
                currentDestination = destination,
                selectedPrimaryDestination = selectedPrimaryDestinationFor(
                    destination = destination,
                    fallback = current.selectedPrimaryDestination,
                ),
                collectionSegment = when (destination) {
                    AppDestination.Collection -> CollectionSegment.OWNED
                    else -> current.collectionSegment
                },
            )
        }
    }

    fun openSettings() {
        _state.update { it.copy(currentDestination = AppDestination.Settings) }
    }

    fun closeSettings() {
        _state.update { current ->
            current.copy(currentDestination = current.selectedPrimaryDestination)
        }
    }

    fun openEventsOverview() {
        selectDestination(AppDestination.Events)
    }

    fun openEventFromHome(eventId: String) {
        _state.update { current ->
            current.copy(
                currentDestination = AppDestination.Events,
                activeDetail = ActiveDetail.EventDetail(eventId),
            )
        }
    }

    fun openActivity(activityId: String) {
        when {
            activityId.startsWith("item-") -> {
                _state.update { current ->
                    current.copy(
                        currentDestination = AppDestination.Collection,
                        selectedPrimaryDestination = AppDestination.Collection,
                        collectionSegment = CollectionSegment.OWNED,
                        activeDetail = ActiveDetail.CollectionEntryDetail(activityId),
                    )
                }
            }

            activityId.startsWith("pre-") -> {
                _state.update { current ->
                    current.copy(
                        currentDestination = AppDestination.Collection,
                        selectedPrimaryDestination = AppDestination.Collection,
                        collectionSegment = CollectionSegment.RESERVED,
                        activeDetail = ActiveDetail.CollectionEntryDetail(activityId),
                    )
                }
            }
        }
    }

    fun updateLanguage(languageCode: String) {
        val currentPreferences = _state.value.appPreferences
        updateAppPreferencesUseCase(
            currentPreferences.copy(languageCode = languageCode),
        )
        reload()
    }

    fun updateCollectionQuery(query: String) {
        _state.update { it.copy(collectionQuery = query) }
        reload()
    }

    fun updateCollectionStatusFilter(status: ItemStatus) {
        _state.update { it.copy(collectionStatusFilter = status) }
    }

    fun selectCollectionSegment(segment: CollectionSegment) {
        _state.update {
            it.copy(
                currentDestination = AppDestination.Collection,
                selectedPrimaryDestination = AppDestination.Collection,
                collectionSegment = segment,
            )
        }
    }

    fun markCollectionEntryReceived(entryId: String) {
        val existing = _state.value.collectionEntries.firstOrNull { it.id == entryId } ?: return
        if (existing.status != CollectionEntryStatus.RESERVED) return

        saveCollectionItemUseCase(
            Item(
                id = existing.id,
                name = existing.name,
                category = existing.category,
                status = ItemStatus.OWNED,
                seriesName = existing.seriesName,
                characterName = existing.characterName,
                quantity = existing.quantity,
                purchasePrice = existing.purchasePrice,
                purchaseDate = CURRENT_DATE,
                purchaseStore = existing.reservationStore,
                storageLocationId = existing.storageLocationId,
                linkedPreorderId = existing.id,
                note = existing.note,
                createdAt = existing.createdAt,
                updatedAt = CURRENT_DATE,
            ),
        )
        cancelPreorderUseCase(entryId)
        reload()
        _state.update { current ->
            current.copy(
                currentDestination = AppDestination.Collection,
                selectedPrimaryDestination = AppDestination.Collection,
                collectionStatusFilter = ItemStatus.OWNED,
                collectionSegment = CollectionSegment.OWNED,
                activeDetail = ActiveDetail.CollectionEntryDetail(entryId),
            )
        }
    }

    fun updatePreorderStatusFilter(status: PreorderStatus?) {
        _state.update { it.copy(preorderStatusFilter = status) }
        reload()
    }

    fun updateEventTypeFilter(type: EventType?) {
        _state.update { it.copy(eventTypeFilter = type) }
        reload()
    }

    fun openQuickAdd() {
        _state.update { it.copy(isQuickAddOpen = true) }
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
            createdAt = CURRENT_DATE,
        )
        reloadAndCloseSheet(
            destination = AppDestination.Collection,
            detail = ActiveDetail.CollectionEntryDetail(newId),
            collectionStatusOverride = status.toCollectionItemStatusOrNull(),
            collectionSegmentOverride = if (status == CollectionEntryStatus.RESERVED) {
                CollectionSegment.RESERVED
            } else {
                CollectionSegment.OWNED
            },
        )
    }

    fun submitEvent(title: String, targetDate: String, eventType: EventType) {
        val newId = generateId("event")
        saveEventUseCase(
            Event(
                id = newId,
                title = title.trim(),
                eventType = eventType,
                targetDate = targetDate.trim(),
                createdAt = CURRENT_DATE,
                updatedAt = CURRENT_DATE,
            ),
        )
        reloadAndCloseSheet(
            destination = AppDestination.Events,
            detail = ActiveDetail.EventDetail(newId),
        )
    }

    fun openCollectionEntryDetail(entryId: String) {
        _state.update { it.copy(activeDetail = ActiveDetail.CollectionEntryDetail(entryId)) }
    }

    fun openEventDetail(eventId: String) {
        _state.update { it.copy(activeDetail = ActiveDetail.EventDetail(eventId)) }
    }

    fun closeDetail() {
        _state.update { it.copy(activeDetail = null) }
    }

    fun openCollectionEntryEditor(entryId: String) {
        _state.update {
            it.copy(
                activeDetail = null,
                activeEditor = ActiveEditor.CollectionEntryEditor(entryId),
            )
        }
    }

    fun openEventEditor(eventId: String) {
        _state.update {
            it.copy(
                activeDetail = null,
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
        reload()
        _state.update { current ->
            current.copy(
                currentDestination = AppDestination.Collection,
                selectedPrimaryDestination = AppDestination.Collection,
                collectionSegment = if (status == CollectionEntryStatus.RESERVED) {
                    CollectionSegment.RESERVED
                } else {
                    CollectionSegment.OWNED
                },
                collectionStatusFilter = status.toCollectionItemStatusOrNull() ?: current.collectionStatusFilter,
                activeEditor = null,
                activeDetail = ActiveDetail.CollectionEntryDetail(entryId),
            )
        }
    }

    fun saveEditedEvent(
        eventId: String,
        title: String,
        targetDate: String,
        eventType: EventType,
    ) {
        val existing = _state.value.events.firstOrNull { it.id == eventId } ?: return
        saveEventUseCase(
            existing.copy(
                title = title.trim(),
                targetDate = targetDate.trim(),
                eventType = eventType,
                updatedAt = CURRENT_DATE,
            ),
        )
        reload()
        _state.update {
            it.copy(
                activeEditor = null,
                activeDetail = ActiveDetail.EventDetail(eventId),
            )
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
            it.copy(
                activeDetail = null,
                pendingDelete = pendingDelete,
            )
        }
    }

    fun requestDeleteEvent(eventId: String) {
        _state.update {
            it.copy(
                activeDetail = null,
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
                deleteCollectionItemUseCase(pending.itemId)
                reload()
                _state.update { current ->
                    current.copy(
                        pendingDelete = null,
                        currentDestination = AppDestination.Collection,
                        selectedPrimaryDestination = AppDestination.Collection,
                    )
                }
            }

            is PendingDelete.PreorderCancel -> {
                cancelPreorderUseCase(pending.preorderId)
                reload()
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

            is PendingDelete.EventDelete -> {
                deleteEventUseCase(pending.eventId)
                reload()
                _state.update { current ->
                    current.copy(
                        pendingDelete = null,
                        currentDestination = AppDestination.Events,
                    )
                }
            }

            null -> Unit
        }
    }

    private fun reloadAndCloseSheet(
        destination: AppDestination,
        detail: ActiveDetail,
        collectionStatusOverride: ItemStatus? = null,
        collectionSegmentOverride: CollectionSegment? = null,
    ) {
        reload()
        _state.update { current ->
            current.copy(
                currentDestination = destination,
                selectedPrimaryDestination = selectedPrimaryDestinationFor(
                    destination = destination,
                    fallback = current.selectedPrimaryDestination,
                ),
                collectionStatusFilter = collectionStatusOverride ?: current.collectionStatusFilter,
                collectionSegment = collectionSegmentOverride ?: current.collectionSegment,
                activeDetail = detail,
                isQuickAddOpen = false,
            )
        }
    }

    private fun saveCollectionEntry(
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
        when (status) {
            CollectionEntryStatus.RESERVED -> {
                savePreorderUseCase(
                    Preorder(
                        id = entryId,
                        name = name.trim(),
                        storeName = reservationStore.trim(),
                        releaseDate = releaseDate.trim(),
                        status = PreorderStatus.ACTIVE,
                        seriesName = seriesName.trim().ifBlank { null },
                        characterName = characterName.trim().ifBlank { null },
                        note = note.trim().ifBlank { null },
                        createdAt = createdAt,
                        updatedAt = CURRENT_DATE,
                    ),
                )
                if (existingStatus != null && existingStatus != CollectionEntryStatus.RESERVED) {
                    deleteCollectionItemUseCase(entryId)
                }
            }

            CollectionEntryStatus.OWNED,
            CollectionEntryStatus.PLANNED_CLEANUP,
            -> {
                saveCollectionItemUseCase(
                    Item(
                        id = entryId,
                        name = name.trim(),
                        category = category.trim(),
                        status = status.toCollectionItemStatus(),
                        seriesName = seriesName.trim().ifBlank { null },
                        characterName = characterName.trim().ifBlank { null },
                        quantity = existingEntry?.quantity ?: 1,
                        purchasePrice = existingEntry?.purchasePrice,
                        purchaseStore = purchaseStore.trim().ifBlank { null },
                        purchaseDate = if (existingStatus == CollectionEntryStatus.RESERVED) {
                            CURRENT_DATE
                        } else {
                            existingEntry?.purchaseDate
                        },
                        storageLocationId = existingEntry?.storageLocationId,
                        linkedPreorderId = if (existingStatus == CollectionEntryStatus.RESERVED) {
                            entryId
                        } else {
                            null
                        },
                        note = note.trim().ifBlank { null },
                        createdAt = createdAt,
                        updatedAt = CURRENT_DATE,
                    ),
                )
                if (existingStatus == CollectionEntryStatus.RESERVED) {
                    cancelPreorderUseCase(entryId)
                }
            }
        }
    }

    private fun reload() {
        reloadState(
            state = _state,
            getCollectionItemsUseCase = getCollectionItemsUseCase,
            getAppPreferencesUseCase = getAppPreferencesUseCase,
            getDashboardSummaryUseCase = getDashboardSummaryUseCase,
            getEventListUseCase = getEventListUseCase,
            getPreorderListUseCase = getPreorderListUseCase,
            getRecentActivitiesUseCase = getRecentActivitiesUseCase,
            getStorageLocationsUseCase = getStorageLocationsUseCase,
            getUpcomingEventsUseCase = getUpcomingEventsUseCase,
            currentMonth = CURRENT_MONTH,
            upcomingEventPreviewLimit = UPCOMING_EVENT_PREVIEW_LIMIT,
        )
    }

    private fun generateId(prefix: String): String {
        return "$prefix-${Random.nextInt(100_000, 999_999)}"
    }

    companion object {
        const val CURRENT_DATE = "2026-03-15"
        const val CURRENT_MONTH = "2026-03"
        private const val UPCOMING_EVENT_PREVIEW_LIMIT = 3
    }
}

private fun CollectionEntryStatus.toCollectionItemStatus(): ItemStatus {
    return when (this) {
        CollectionEntryStatus.RESERVED -> error("Reserved entries cannot be mapped to ItemStatus.")
        CollectionEntryStatus.OWNED -> ItemStatus.OWNED
        CollectionEntryStatus.PLANNED_CLEANUP -> ItemStatus.PLANNED_CLEANUP
    }
}

private fun CollectionEntryStatus.toCollectionItemStatusOrNull(): ItemStatus? {
    return when (this) {
        CollectionEntryStatus.RESERVED -> null
        CollectionEntryStatus.OWNED -> ItemStatus.OWNED
        CollectionEntryStatus.PLANNED_CLEANUP -> ItemStatus.PLANNED_CLEANUP
    }
}
