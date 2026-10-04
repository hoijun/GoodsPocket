package goods.pocket.app.presentation.state

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CommandState(
    val isRunning: Boolean = false,
    val hasFailure: Boolean = false,
    val failureVersion: Int = 0,
)

/** A feature owns its pending write; observing fresh data is never part of its retry. */
class FeatureCommand(private val scope: CoroutineScope) {
    private val _state = MutableStateFlow(CommandState())
    val state: StateFlow<CommandState> = _state.asStateFlow()
    private var pending: (suspend () -> Unit)? = null
    private var afterSuccess: (() -> Unit)? = null
    private var job: Job? = null
    private var generation = 0
    internal var lastFailure: Exception? = null
        private set

    fun run(onSuccess: () -> Unit = {}, operation: suspend () -> Unit) {
        if (_state.value.isRunning) return
        pending = operation
        afterSuccess = onSuccess
        execute()
    }

    fun retry() {
        if (!_state.value.hasFailure || _state.value.isRunning) return
        execute()
    }

    fun dismissFailure() {
        _state.value = _state.value.copy(hasFailure = false)
    }

    fun cancel() {
        generation++
        job?.cancel()
        pending = null
        afterSuccess = null
        _state.value = CommandState()
        lastFailure = null
    }

    private fun execute() {
        val operation = pending ?: return
        val completion = afterSuccess
        val currentGeneration = ++generation
        _state.value = _state.value.copy(isRunning = true, hasFailure = false)
        job = scope.launch {
            try {
                operation()
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (failure: Exception) {
                if (currentGeneration != generation) return@launch
                lastFailure = failure
                _state.value = _state.value.copy(
                    isRunning = false,
                    hasFailure = true,
                    failureVersion = _state.value.failureVersion + 1,
                )
                return@launch
            } finally {
                if (currentGeneration ==
                    generation
                ) {
                    _state.value = _state.value.copy(isRunning = false)
                }
            }
            if (currentGeneration != generation) return@launch
            pending = null
            afterSuccess = null
            lastFailure = null
            completion?.invoke()
        }
    }
}
