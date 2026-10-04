package goods.pocket.app.presentation.events

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import goods.pocket.app.presentation.component.FeatureFeedback

@Composable
fun EventsRoute(holder: EventsStateHolder, onEventClick: (String) -> Unit) {
    val state by holder.state.collectAsState()
    Box {
        EventsScreen(
            state.events,
            state.selectedType,
            state.currentDate,
            holder::updateType,
            onEventClick,
        )
        FeatureFeedback(state.isLoading, state.hasLoadFailure, holder::retryLoad)
    }
}
