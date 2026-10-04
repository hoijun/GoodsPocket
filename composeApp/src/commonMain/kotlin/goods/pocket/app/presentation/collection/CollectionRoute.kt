package goods.pocket.app.presentation.collection

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import goods.pocket.app.presentation.component.FeatureFeedback

@Composable
fun CollectionRoute(holder: CollectionStateHolder, onEntryClick: (String) -> Unit) {
    val state by holder.state.collectAsState()
    Box {
        CollectionScreen(
            state.entries,
            state.segment,
            state.query,
            holder::selectSegment,
            holder::updateQuery,
            onEntryClick,
        )
        FeatureFeedback(state.isLoading, state.hasLoadFailure, holder::retryLoad)
    }
}
