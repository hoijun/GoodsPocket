package goods.pocket.app.presentation.state

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ObservedContent<T>(
    val value: T,
    val isLoading: Boolean = true,
    val hasFailure: Boolean = false,
    val hasLoaded: Boolean = false,
    val version: Long = 0,
)

/** Restarts only a failed read stream and preserves the last successful content. */
class FeatureObservation<T>(
    private val scope: CoroutineScope,
    initial: T,
    private val source: () -> Flow<T>,
) {
    private val _state = MutableStateFlow(ObservedContent(initial))
    val state: StateFlow<ObservedContent<T>> = _state.asStateFlow()
    private var job: Job? = null
    internal var lastFailure: Exception? = null
        private set

    init {
        retry()
    }

    fun retry() {
        job?.cancel()
        _state.value = _state.value.copy(isLoading = true, hasFailure = false)
        job = scope.launch {
            try {
                source().collect { value ->
                    lastFailure = null
                    _state.value =
                        ObservedContent(
                            value,
                            isLoading = false,
                            hasLoaded = true,
                            version =
                            _state.value.version + 1,
                        )
                }
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (failure: Exception) {
                lastFailure = failure
                _state.value = _state.value.copy(isLoading = false, hasFailure = true)
            }
        }
    }
}
