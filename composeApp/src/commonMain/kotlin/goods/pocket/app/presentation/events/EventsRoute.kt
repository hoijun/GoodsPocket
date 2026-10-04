package goods.pocket.app.presentation.events

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun EventsRoute(holder: EventsStateHolder, onEventClick: (String) -> Unit) {
    val state by holder.state.collectAsState()
    EventsScreen(
        events = state.events,
        selectedType = state.selectedType,
        currentDate = state.currentDate,
        isLoading = state.isLoading,
        hasLoadFailure = state.hasLoadFailure,
        hasLoaded = state.hasLoaded,
        onRetry = holder::retryLoad,
        onTypeChange = holder::updateType,
        onEventClick = onEventClick,
    )
}
