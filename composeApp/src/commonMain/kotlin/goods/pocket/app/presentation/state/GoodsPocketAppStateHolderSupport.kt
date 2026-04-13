package goods.pocket.app.presentation.state

import goods.pocket.app.domain.model.AppPreference
import goods.pocket.app.domain.model.CollectionEntry
import goods.pocket.app.domain.model.CollectionEntryStatus
import goods.pocket.app.domain.model.HomeSummary
import goods.pocket.app.domain.model.Item
import goods.pocket.app.domain.model.Preorder
import goods.pocket.app.domain.model.PreorderStatus
import goods.pocket.app.domain.usecase.GetAppPreferencesUseCase
import goods.pocket.app.domain.usecase.GetCollectionItemsUseCase
import goods.pocket.app.domain.usecase.GetDashboardSummaryUseCase
import goods.pocket.app.domain.usecase.GetEventListUseCase
import goods.pocket.app.domain.usecase.GetPreorderListUseCase
import goods.pocket.app.domain.usecase.GetRecentActivitiesUseCase
import goods.pocket.app.domain.usecase.GetStorageLocationsUseCase
import goods.pocket.app.domain.usecase.GetUpcomingEventsUseCase
import goods.pocket.app.i18n.localizedLocalProfileLabel
import goods.pocket.app.i18n.localizedSyncNotConnectedLabel
import goods.pocket.app.presentation.navigation.AppDestination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

internal fun reloadState(
    state: MutableStateFlow<GoodsPocketUiState>,
    getCollectionItemsUseCase: GetCollectionItemsUseCase,
    getAppPreferencesUseCase: GetAppPreferencesUseCase,
    getDashboardSummaryUseCase: GetDashboardSummaryUseCase,
    getEventListUseCase: GetEventListUseCase,
    getPreorderListUseCase: GetPreorderListUseCase,
    getRecentActivitiesUseCase: GetRecentActivitiesUseCase,
    getStorageLocationsUseCase: GetStorageLocationsUseCase,
    getUpcomingEventsUseCase: GetUpcomingEventsUseCase,
    currentMonth: String,
    upcomingEventPreviewLimit: Int,
) {
    val appPreferences = getAppPreferencesUseCase()
    val recentActivities = getRecentActivitiesUseCase(
        limit = 5,
        languageCode = appPreferences.languageCode,
    )
    val homeSummary = getDashboardSummaryUseCase(
        monthFilter = currentMonth,
        recentActivities = recentActivities,
    )
    val allUpcomingEvents = getUpcomingEventsUseCase(limit = Int.MAX_VALUE)
    state.update { current ->
        val collectionItems = getCollectionItemsUseCase(current.collectionQuery)
        val preorders = getPreorderListUseCase(current.preorderStatusFilter)
        current.copy(
            myPage = homeSummary.toMyPageUiModel(
                upcomingEventCount = allUpcomingEvents.size,
                appPreferences = appPreferences,
            ),
            homeSummary = homeSummary,
            upcomingEvents = allUpcomingEvents.take(upcomingEventPreviewLimit),
            collectionEntries = buildCollectionEntries(
                items = collectionItems,
                preorders = preorders,
            ),
            collectionItems = collectionItems,
            preorders = preorders,
            events = getEventListUseCase(current.eventTypeFilter),
            storageLocations = getStorageLocationsUseCase(),
            appPreferences = appPreferences,
            currentDestination = current.currentDestination,
            selectedPrimaryDestination = selectedPrimaryDestinationFor(
                destination = current.currentDestination,
                fallback = current.selectedPrimaryDestination,
            ),
            collectionQuery = current.collectionQuery,
            collectionSegment = current.collectionSegment,
            collectionStatusFilter = current.collectionStatusFilter,
            preorderStatusFilter = current.preorderStatusFilter,
            eventTypeFilter = current.eventTypeFilter,
            isQuickAddOpen = current.isQuickAddOpen,
            quickAddTarget = current.quickAddTarget,
            activeDetail = current.activeDetail,
            activeEditor = current.activeEditor,
            pendingDelete = current.pendingDelete,
        )
    }
}

internal fun selectedPrimaryDestinationFor(
    destination: AppDestination,
    fallback: AppDestination,
): AppDestination {
    return AppDestination.primaryDestinations.firstOrNull { it.route == destination.route } ?: fallback
}

private fun HomeSummary.toMyPageUiModel(
    upcomingEventCount: Int,
    appPreferences: AppPreference,
): MyPageUiModel {
    return MyPageUiModel(
        displayName = localizedLocalProfileLabel(appPreferences.languageCode),
        syncStatusLabel = localizedSyncNotConnectedLabel(appPreferences.languageCode),
        notificationsEnabled = appPreferences.languageCode.isNotBlank(),
        ownedItemCount = ownedItemCount,
        activePreorderCount = activePreorderCount,
        monthlySpend = monthlySpend,
        upcomingEventCount = upcomingEventCount,
    )
}

private fun buildCollectionEntries(
    items: List<Item>,
    preorders: List<Preorder>,
): List<CollectionEntry> {
    return (items.map(Item::toCollectionEntry) + preorders.mapNotNull(Preorder::toCollectionEntryOrNull))
        .sortedByDescending(CollectionEntry::updatedAt)
}

private fun Item.toCollectionEntry(): CollectionEntry {
    return CollectionEntry(
        id = id,
        name = name,
        category = category,
        status = when (status) {
            goods.pocket.app.domain.model.ItemStatus.OWNED -> CollectionEntryStatus.OWNED
            goods.pocket.app.domain.model.ItemStatus.PLANNED_CLEANUP -> CollectionEntryStatus.PLANNED_CLEANUP
        },
        seriesName = seriesName,
        characterName = characterName,
        quantity = quantity,
        purchasePrice = purchasePrice,
        purchaseDate = purchaseDate,
        purchaseStore = purchaseStore,
        storageLocationId = storageLocationId,
        releaseDate = null,
        reservationStore = null,
        note = note,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}

private fun Preorder.toCollectionEntryOrNull(): CollectionEntry? {
    if (status == PreorderStatus.CANCELED) return null

    return CollectionEntry(
        id = id,
        name = name,
        category = "예약 굿즈",
        status = when (status) {
            PreorderStatus.ACTIVE,
            PreorderStatus.PAYMENT_PENDING,
            -> CollectionEntryStatus.RESERVED
            PreorderStatus.RECEIVED -> CollectionEntryStatus.OWNED
            PreorderStatus.CANCELED -> return null
        },
        seriesName = seriesName,
        characterName = characterName,
        quantity = 1,
        purchasePrice = totalPrice,
        purchaseDate = orderDate,
        purchaseStore = storeName,
        storageLocationId = null,
        releaseDate = releaseDate,
        reservationStore = storeName,
        note = note,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}
