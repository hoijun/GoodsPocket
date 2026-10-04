package goods.pocket.app.presentation.home

import goods.pocket.app.domain.collection.CollectionRepository
import goods.pocket.app.domain.dashboard.GetDashboardSummaryUseCase
import goods.pocket.app.domain.dashboard.GetRecentActivitiesUseCase
import goods.pocket.app.domain.event.EventRepository
import goods.pocket.app.domain.service.AppClock
import goods.pocket.app.presentation.state.FeatureObservation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HomeStateHolder(
    collectionRepository: CollectionRepository,
    eventRepository: EventRepository,
    clock: AppClock,
    scope: CoroutineScope,
    date: kotlinx.coroutines.flow.StateFlow<String> = kotlinx.coroutines.flow.MutableStateFlow(
        clock.currentDate(),
    ),
    private val dashboard: GetDashboardSummaryUseCase,
    private val activities: GetRecentActivitiesUseCase,
) {
    private val content =
        FeatureObservation(scope, HomeUiState(currentDate = clock.currentDate())) {
            combine(
                collectionRepository.observeAllEntries(),
                eventRepository.observeEvents(),
                date,
            ) {
                    entries,
                    events,
                    today,
                ->
                HomeUiState(
                    summary = dashboard.calculate(
                        entries,
                        today.take(7),
                        activities.calculate(entries, 5),
                    ),
                    upcomingEvents = events.filter { it.targetDate >= today }
                        .sortedBy { it.targetDate }.take(3),
                    currentDate = today,
                    isLoading = false,
                )
            }
        }
    val state: kotlinx.coroutines.flow.StateFlow<HomeUiState> = content.state.map { data ->
        data.value.copy(isLoading = data.isLoading, hasLoadFailure = data.hasFailure)
    }.stateIn(scope, SharingStarted.Eagerly, HomeUiState(currentDate = clock.currentDate()))

    fun retryLoad() = content.retry()
}
