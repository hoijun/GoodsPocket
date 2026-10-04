package goods.pocket.app.presentation.home

import goods.pocket.app.domain.dashboard.HomeSummary
import goods.pocket.app.domain.event.Event

data class HomeUiState(
    val summary: HomeSummary = HomeSummary(0, 0, List(9) { 0L }, 0, 0, emptyList()),
    val upcomingEvents: List<Event> = emptyList(),
    val currentDate: String = "",
    val isLoading: Boolean = true,
    val hasLoadFailure: Boolean = false,
)
