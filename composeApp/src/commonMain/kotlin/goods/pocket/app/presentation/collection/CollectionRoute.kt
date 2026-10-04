package goods.pocket.app.presentation.collection

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun CollectionRoute(holder: CollectionStateHolder, onEntryClick: (String) -> Unit) {
    val state by holder.state.collectAsState()
    CollectionScreen(
        entries = state.entries,
        selectedSegment = state.segment,
        query = state.query,
        isLoading = state.isLoading,
        hasLoadFailure = state.hasLoadFailure,
        hasLoaded = state.hasLoaded,
        onRetry = holder::retryLoad,
        onSegmentChange = holder::selectSegment,
        onQueryChange = holder::updateQuery,
        onEntryClick = onEntryClick,
    )
}
