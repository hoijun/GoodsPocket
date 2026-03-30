package goods.pocket.app.presentation.state

import goods.pocket.app.domain.model.AppPreference
import goods.pocket.app.domain.model.HomeSummary
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
        current.copy(
            myPage = homeSummary.toMyPageUiModel(
                upcomingEventCount = allUpcomingEvents.size,
                appPreferences = appPreferences,
            ),
            homeSummary = homeSummary,
            upcomingEvents = allUpcomingEvents.take(upcomingEventPreviewLimit),
            collectionItems = getCollectionItemsUseCase(current.collectionQuery),
            preorders = getPreorderListUseCase(current.preorderStatusFilter),
            events = getEventListUseCase(current.eventTypeFilter),
            storageLocations = getStorageLocationsUseCase(),
            appPreferences = appPreferences,
            currentDestination = current.currentDestination,
            selectedPrimaryDestination = selectedPrimaryDestinationFor(
                destination = current.currentDestination,
                fallback = current.selectedPrimaryDestination,
            ),
            collectionQuery = current.collectionQuery,
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
