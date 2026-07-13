package goods.pocket.app.presentation.state

import goods.pocket.app.domain.model.AppPreference
import goods.pocket.app.domain.model.HomeSummary
import goods.pocket.app.domain.repository.CollectionRepository
import goods.pocket.app.domain.repository.EventRepository
import goods.pocket.app.domain.repository.SettingsRepository
import goods.pocket.app.domain.service.AppClock
import goods.pocket.app.domain.usecase.GetDashboardSummaryUseCase
import goods.pocket.app.domain.usecase.GetRecentActivitiesUseCase
import goods.pocket.app.i18n.localizedLocalProfileLabel
import goods.pocket.app.i18n.localizedSyncNotConnectedLabel
import goods.pocket.app.presentation.navigation.AppDestination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class GoodsPocketContentLoader(
    private val collectionRepository: CollectionRepository,
    private val eventRepository: EventRepository,
    private val settingsRepository: SettingsRepository,
    private val getDashboardSummaryUseCase: GetDashboardSummaryUseCase,
    private val getRecentActivitiesUseCase: GetRecentActivitiesUseCase,
    private val clock: AppClock,
) {
    suspend fun reload(
        state: MutableStateFlow<GoodsPocketUiState>,
        upcomingEventPreviewLimit: Int,
    ) {
        val appPreferences = settingsRepository.getAppPreferences()
        val recentActivities = getRecentActivitiesUseCase(
            limit = RECENT_ACTIVITY_LIMIT,
            languageCode = appPreferences.languageCode,
        )
        val homeSummary = getDashboardSummaryUseCase(
            monthFilter = clock.currentMonth(),
            recentActivities = recentActivities,
        )
        val allUpcomingEvents = eventRepository.getUpcomingEvents(limit = Int.MAX_VALUE)
        val collectionEntries = collectionRepository.getEntries()
        val events = eventRepository.getEvents()
        state.update { current ->
            current.copy(
                myPage = homeSummary.toMyPageUiModel(
                    upcomingEventCount = allUpcomingEvents.size,
                    appPreferences = appPreferences,
                ),
                homeSummary = homeSummary,
                upcomingEvents = allUpcomingEvents.take(upcomingEventPreviewLimit),
                collectionEntries = collectionEntries,
                events = events,
                appPreferences = appPreferences,
                selectedPrimaryDestination = selectedPrimaryDestinationFor(
                    destination = current.currentDestination,
                    fallback = current.selectedPrimaryDestination,
                ),
            )
        }
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
        ownedItemCount = ownedItemCount,
        activePreorderCount = activePreorderCount,
        monthlySpend = monthlySpend,
        upcomingEventCount = upcomingEventCount,
    )
}

private const val RECENT_ACTIVITY_LIMIT = 5
