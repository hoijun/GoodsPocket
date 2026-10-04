package goods.pocket.app.presentation.my

import goods.pocket.app.domain.collection.CollectionRepository
import goods.pocket.app.domain.dashboard.GetDashboardSummaryUseCase
import goods.pocket.app.domain.event.EventRepository
import goods.pocket.app.domain.service.AppClock
import goods.pocket.app.presentation.state.FeatureObservation
import goods.pocket.app.presentation.state.MyPageUiModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class MyStateHolder(
    collectionRepository: CollectionRepository,
    eventRepository: EventRepository,
    clock: AppClock,
    scope: CoroutineScope,
    date: kotlinx.coroutines.flow.StateFlow<String> = kotlinx.coroutines.flow.MutableStateFlow(
        clock.currentDate(),
    ),
    private val dashboard: GetDashboardSummaryUseCase,
) {
    private val content = FeatureObservation(scope, MyPageUiModel()) {
        combine(collectionRepository.observeAllEntries(), eventRepository.observeEvents(), date) {
                entries,
                events,
                today,
            ->
            val summary = dashboard.calculate(entries, today.take(7))
            MyPageUiModel(
                ownedItemCount = summary.ownedItemCount,
                activePreorderCount = summary.activePreorderCount,
                monthlySpend = summary.monthlySpend,
                upcomingEventCount = events.count { it.targetDate >= today },
            )
        }
    }
    val state: kotlinx.coroutines.flow.StateFlow<MyUiState> = content.state.map {
        MyUiState(it.value, it.isLoading, it.hasFailure)
    }
        .stateIn(scope, SharingStarted.Eagerly, MyUiState())

    fun retryLoad() = content.retry()
}
