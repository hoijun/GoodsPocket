package goods.pocket.app.presentation.state

import goods.pocket.app.domain.model.Event
import goods.pocket.app.domain.model.EventType
import goods.pocket.app.domain.model.Item
import goods.pocket.app.domain.model.ItemStatus
import goods.pocket.app.domain.model.Preorder
import goods.pocket.app.domain.model.PreorderStatus
import goods.pocket.app.domain.model.Transaction
import goods.pocket.app.domain.model.TransactionType
import goods.pocket.app.domain.usecase.CancelPreorderUseCase
import goods.pocket.app.domain.usecase.DeleteCollectionItemUseCase
import goods.pocket.app.domain.usecase.DeleteEventUseCase
import goods.pocket.app.domain.usecase.DeleteTransactionUseCase
import goods.pocket.app.domain.usecase.GetCollectionItemsUseCase
import goods.pocket.app.domain.usecase.GetAppPreferencesUseCase
import goods.pocket.app.domain.usecase.GetDashboardSummaryUseCase
import goods.pocket.app.domain.usecase.GetEventListUseCase
import goods.pocket.app.domain.usecase.GetItemTransactionsUseCase
import goods.pocket.app.domain.usecase.GetMonthlyTransactionsUseCase
import goods.pocket.app.domain.usecase.GetPreorderListUseCase
import goods.pocket.app.domain.usecase.GetRecentActivitiesUseCase
import goods.pocket.app.domain.usecase.GetStorageLocationsUseCase
import goods.pocket.app.domain.usecase.GetUpcomingEventsUseCase
import goods.pocket.app.domain.usecase.MarkPreorderReceivedUseCase
import goods.pocket.app.domain.usecase.SaveCollectionItemUseCase
import goods.pocket.app.domain.usecase.SaveEventUseCase
import goods.pocket.app.domain.usecase.SavePreorderUseCase
import goods.pocket.app.domain.usecase.SaveTransactionUseCase
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
    private val getItemTransactionsUseCase: GetItemTransactionsUseCase,
    private val getMonthlyTransactionsUseCase: GetMonthlyTransactionsUseCase,
    private val getPreorderListUseCase: GetPreorderListUseCase,
    private val getRecentActivitiesUseCase: GetRecentActivitiesUseCase,
    private val getStorageLocationsUseCase: GetStorageLocationsUseCase,
    private val getUpcomingEventsUseCase: GetUpcomingEventsUseCase,
    private val cancelPreorderUseCase: CancelPreorderUseCase,
    private val deleteCollectionItemUseCase: DeleteCollectionItemUseCase,
    private val deleteEventUseCase: DeleteEventUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase,
    private val markPreorderReceivedUseCase: MarkPreorderReceivedUseCase,
    private val saveCollectionItemUseCase: SaveCollectionItemUseCase,
    private val saveEventUseCase: SaveEventUseCase,
    private val savePreorderUseCase: SavePreorderUseCase,
    private val saveTransactionUseCase: SaveTransactionUseCase,
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

    fun openTransactionsOverview() {
        selectDestination(AppDestination.Transactions)
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
                        activeDetail = ActiveDetail.ItemDetail(activityId),
                    )
                }
            }

            activityId.startsWith("pre-") -> {
                _state.update { current ->
                    current.copy(
                        currentDestination = AppDestination.Preorders,
                        selectedPrimaryDestination = AppDestination.Preorders,
                        activeDetail = ActiveDetail.PreorderDetail(activityId),
                    )
                }
            }

            activityId.startsWith("tx-") -> {
                _state.update { current ->
                    current.copy(
                        currentDestination = AppDestination.Transactions,
                        selectedPrimaryDestination = selectedPrimaryDestinationFor(
                            destination = AppDestination.Transactions,
                            fallback = current.selectedPrimaryDestination,
                        ),
                        activeDetail = ActiveDetail.TransactionDetail(activityId),
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

    fun updatePreorderStatusFilter(status: PreorderStatus?) {
        _state.update { it.copy(preorderStatusFilter = status) }
        reload()
    }

    fun updateTransactionTypeFilter(type: TransactionType?) {
        _state.update { it.copy(transactionTypeFilter = type) }
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

    fun submitItem(name: String, category: String) {
        val newId = generateId("item")
        saveCollectionItemUseCase(
            Item(
                id = newId,
                name = name.trim(),
                category = category.trim(),
                status = ItemStatus.OWNED,
                createdAt = CURRENT_DATE,
                updatedAt = CURRENT_DATE,
            ),
        )
        reloadAndCloseSheet(
            destination = AppDestination.Collection,
            detail = ActiveDetail.ItemDetail(newId),
        )
    }

    fun submitPreorder(name: String, storeName: String, releaseDate: String) {
        val newId = generateId("pre")
        savePreorderUseCase(
            Preorder(
                id = newId,
                name = name.trim(),
                storeName = storeName.trim(),
                releaseDate = releaseDate.trim(),
                status = PreorderStatus.ACTIVE,
                createdAt = CURRENT_DATE,
                updatedAt = CURRENT_DATE,
            ),
        )
        reloadAndCloseSheet(
            destination = AppDestination.Preorders,
            detail = ActiveDetail.PreorderDetail(newId),
        )
    }

    fun submitTransaction(amount: String, type: TransactionType, transactionDate: String) {
        val normalizedAmount = amount.toLongOrNull() ?: return
        val newId = generateId("tx")
        saveTransactionUseCase(
            Transaction(
                id = newId,
                type = type,
                amount = normalizedAmount,
                transactionDate = transactionDate.trim(),
                createdAt = CURRENT_DATE,
            ),
        )
        reloadAndCloseSheet(
            destination = AppDestination.Transactions,
            detail = ActiveDetail.TransactionDetail(newId),
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

    fun openItemDetail(itemId: String) {
        _state.update { it.copy(activeDetail = ActiveDetail.ItemDetail(itemId)) }
    }

    fun openPreorderDetail(preorderId: String) {
        _state.update { it.copy(activeDetail = ActiveDetail.PreorderDetail(preorderId)) }
    }

    fun openTransactionDetail(transactionId: String) {
        _state.update { it.copy(activeDetail = ActiveDetail.TransactionDetail(transactionId)) }
    }

    fun openEventDetail(eventId: String) {
        _state.update { it.copy(activeDetail = ActiveDetail.EventDetail(eventId)) }
    }

    fun closeDetail() {
        _state.update { it.copy(activeDetail = null) }
    }

    fun openItemEditor(itemId: String) {
        _state.update {
            it.copy(
                activeDetail = null,
                activeEditor = ActiveEditor.ItemEditor(itemId),
            )
        }
    }

    fun openPreorderEditor(preorderId: String) {
        _state.update {
            it.copy(
                activeDetail = null,
                activeEditor = ActiveEditor.PreorderEditor(preorderId),
            )
        }
    }

    fun openTransactionEditor(transactionId: String) {
        _state.update {
            it.copy(
                activeDetail = null,
                activeEditor = ActiveEditor.TransactionEditor(transactionId),
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

    fun saveEditedItem(
        itemId: String,
        name: String,
        category: String,
        seriesName: String,
        characterName: String,
        purchaseStore: String,
    ) {
        val existing = _state.value.collectionItems.firstOrNull { it.id == itemId } ?: return
        saveCollectionItemUseCase(
            existing.copy(
                name = name.trim(),
                category = category.trim(),
                seriesName = seriesName.trim().ifBlank { null },
                characterName = characterName.trim().ifBlank { null },
                purchaseStore = purchaseStore.trim().ifBlank { null },
                updatedAt = CURRENT_DATE,
            ),
        )
        reload()
        _state.update {
            it.copy(
                activeEditor = null,
                activeDetail = ActiveDetail.ItemDetail(itemId),
            )
        }
    }

    fun saveEditedPreorder(
        preorderId: String,
        name: String,
        storeName: String,
        releaseDate: String,
    ) {
        val existing = _state.value.preorders.firstOrNull { it.id == preorderId } ?: return
        savePreorderUseCase(
            existing.copy(
                name = name.trim(),
                storeName = storeName.trim(),
                releaseDate = releaseDate.trim(),
                updatedAt = CURRENT_DATE,
            ),
        )
        reload()
        _state.update {
            it.copy(
                activeEditor = null,
                activeDetail = ActiveDetail.PreorderDetail(preorderId),
            )
        }
    }

    fun saveEditedTransaction(
        transactionId: String,
        amount: String,
        type: TransactionType,
        transactionDate: String,
    ) {
        val normalizedAmount = amount.toLongOrNull() ?: return
        val existing = _state.value.transactions.firstOrNull { it.id == transactionId } ?: return
        saveTransactionUseCase(
            existing.copy(
                amount = normalizedAmount,
                type = type,
                transactionDate = transactionDate.trim(),
            ),
        )
        reload()
        _state.update {
            it.copy(
                activeEditor = null,
                activeDetail = ActiveDetail.TransactionDetail(transactionId),
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

    fun requestDeleteItem(itemId: String) {
        val linkedCount = itemTransactions(itemId).size
        _state.update {
            it.copy(
                activeDetail = null,
                pendingDelete = PendingDelete.ItemDelete(itemId, linkedCount),
            )
        }
    }

    fun requestCancelPreorder(preorderId: String) {
        _state.update {
            it.copy(
                activeDetail = null,
                pendingDelete = PendingDelete.PreorderCancel(preorderId),
            )
        }
    }

    fun requestDeleteTransaction(transactionId: String) {
        _state.update {
            it.copy(
                activeDetail = null,
                pendingDelete = PendingDelete.TransactionDelete(transactionId),
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
                _state.update {
                    it.copy(
                        pendingDelete = null,
                        activeDetail = ActiveDetail.PreorderDetail(pending.preorderId),
                    )
                }
            }

            is PendingDelete.TransactionDelete -> {
                deleteTransactionUseCase(pending.transactionId)
                reload()
                _state.update { current ->
                    current.copy(
                        pendingDelete = null,
                        currentDestination = AppDestination.Transactions,
                        selectedPrimaryDestination = selectedPrimaryDestinationFor(
                            destination = AppDestination.Transactions,
                            fallback = current.selectedPrimaryDestination,
                        ),
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

    fun markPreorderReceived(preorderId: String) {
        val newItemId = generateId("item")
        markPreorderReceivedUseCase(
            preorderId = preorderId,
            receiveDate = CURRENT_DATE,
            newItemId = newItemId,
            transactionId = generateId("tx"),
            languageCode = _state.value.appPreferences.languageCode,
        )
        reload()
        _state.update { current ->
            current.copy(
                currentDestination = AppDestination.Collection,
                selectedPrimaryDestination = AppDestination.Collection,
                activeDetail = ActiveDetail.ItemDetail(newItemId),
            )
        }
    }

    fun itemTransactions(itemId: String): List<Transaction> = getItemTransactionsUseCase(itemId)

    private fun reloadAndCloseSheet(destination: AppDestination, detail: ActiveDetail) {
        reload()
        _state.update { current ->
            current.copy(
                currentDestination = destination,
                selectedPrimaryDestination = selectedPrimaryDestinationFor(
                    destination = destination,
                    fallback = current.selectedPrimaryDestination,
                ),
                activeDetail = detail,
                isQuickAddOpen = false,
            )
        }
    }

    private fun reload() {
        reloadState(
            state = _state,
            getCollectionItemsUseCase = getCollectionItemsUseCase,
            getAppPreferencesUseCase = getAppPreferencesUseCase,
            getDashboardSummaryUseCase = getDashboardSummaryUseCase,
            getEventListUseCase = getEventListUseCase,
            getMonthlyTransactionsUseCase = getMonthlyTransactionsUseCase,
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
