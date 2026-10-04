package goods.pocket.app.presentation.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import goods.pocket.app.presentation.component.FeatureFeedback

@Composable
fun SettingsRoute(holder: SettingsStateHolder, onBack: () -> Unit) {
    val state by holder.state.collectAsState()
    Column {
        FeatureFeedback(state.isLoading, state.hasLoadFailure, holder::retryLoad)
        FeatureFeedback(state.command.isRunning, state.command.hasFailure, holder.command::retry)
        SettingsScreen(state.preferences, holder::updateLanguage, onBack, state.canEdit)
    }
}
