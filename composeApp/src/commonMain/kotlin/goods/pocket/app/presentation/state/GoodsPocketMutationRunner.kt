package goods.pocket.app.presentation.state

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

internal class GoodsPocketMutationRunner(
    private val state: MutableStateFlow<GoodsPocketUiState>,
    private val coroutineScope: CoroutineScope,
) {
    private val operationMutex = Mutex()
    private var retryOperation: GoodsPocketOperation = GoodsPocketOperation.LOAD
    private var retryBlock: (suspend () -> Unit)? = null

    fun run(
        operation: GoodsPocketOperation,
        block: suspend () -> Unit,
    ) {
        coroutineScope.launch {
            operationMutex.withLock {
                state.update { it.copy(isLoading = true, failure = null) }
                try {
                    block()
                    retryBlock = null
                    state.update { it.copy(isLoading = false, failure = null) }
                } catch (error: CancellationException) {
                    throw error
                } catch (_: Throwable) {
                    retryOperation = operation
                    retryBlock = block
                    state.update {
                        it.copy(
                            isLoading = false,
                            failure = GoodsPocketFailure(operation),
                        )
                    }
                }
            }
        }
    }

    fun retry() {
        val block = retryBlock ?: return
        run(retryOperation, block)
    }

    fun dismissFailure() {
        retryBlock = null
        state.update { it.copy(failure = null) }
    }
}
