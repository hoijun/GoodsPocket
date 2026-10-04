package goods.pocket.app.presentation.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun HomeRoute(holder: HomeStateHolder, onAction: (HomeAction) -> Unit) {
    val state by holder.state.collectAsState()
    HomeScreen(state = state, onAction = onAction, onRetry = holder::retryLoad)
}
