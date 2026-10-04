package goods.pocket.app.presentation.home

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import goods.pocket.app.presentation.component.FeatureFeedback

@Composable
fun HomeRoute(holder: HomeStateHolder, onAction: (HomeAction) -> Unit) {
    val state by holder.state.collectAsState()
    Box {
        HomeScreen(state.summary, state.upcomingEvents, state.currentDate, onAction)
        FeatureFeedback(state.isLoading, state.hasLoadFailure, holder::retryLoad)
    }
}
