package goods.pocket.app.presentation.my

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import goods.pocket.app.presentation.component.FeatureFeedback

@Composable
fun MyRoute(holder: MyStateHolder, onOpenSettings: () -> Unit) {
    val state by holder.state.collectAsState()
    Box {
        MyScreen(state.profile, onOpenSettings)
        FeatureFeedback(state.isLoading, state.hasLoadFailure, holder::retryLoad)
    }
}
