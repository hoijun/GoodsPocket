@file:OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)

package goods.pocket.app.presentation.state

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest

class FeatureCommandTest {
    @Test
    fun canceledCommandCannotBeRetriedForADifferentEditor() = runTest {
        val command = FeatureCommand(backgroundScope)
        var writes = 0
        command.run {
            writes++
            error("disk unavailable")
        }
        runCurrent()
        command.cancel()
        command.retry()
        runCurrent()
        assertEquals(1, writes)
        assertFalse(command.state.value.hasFailure)
    }

    @Test
    fun canceledJobsFinallyCannotUnlockANewerPendingCommand() = runTest {
        val command = FeatureCommand(backgroundScope)
        val firstGate = CompletableDeferred<Unit>()
        val secondGate = CompletableDeferred<Unit>()
        command.run { firstGate.await() }
        runCurrent()
        command.cancel()
        command.run { secondGate.await() }
        runCurrent()
        assertTrue(command.state.value.isRunning)
        secondGate.complete(Unit)
        runCurrent()
        assertFalse(command.state.value.isRunning)
    }

    @Test
    fun duplicateSubmissionIsIgnoredUntilTheWriteFinishes() = runTest {
        val command = FeatureCommand(backgroundScope)
        val gate = CompletableDeferred<Unit>()
        var writes = 0
        command.run {
            writes++
            gate.await()
        }
        command.run { writes++ }
        runCurrent()
        assertEquals(1, writes)
        assertTrue(command.state.value.isRunning)
        gate.complete(Unit)
        runCurrent()
        assertFalse(command.state.value.isRunning)
    }

    @Test
    fun failureRetriesTheSamePayloadAndSuccessfulWriteCannotBeRetried() = runTest {
        val command = FeatureCommand(backgroundScope)
        val ids = mutableListOf<String>()
        var fail = true
        val stableId = "entry-one"
        command.run {
            ids += stableId
            if (fail) error("storage unavailable")
        }
        runCurrent()
        assertTrue(command.state.value.hasFailure)
        fail = false
        command.retry()
        runCurrent()
        command.retry()
        runCurrent()
        assertEquals(listOf(stableId, stableId), ids)
        assertFalse(command.state.value.hasFailure)
    }

    @Test
    fun closingFeatureCancelsPendingWriteWithoutPresentingFailure() = runTest {
        val command = FeatureCommand(backgroundScope)
        val gate = CompletableDeferred<Unit>()
        var completed = false
        command.run {
            gate.await()
            completed = true
        }
        runCurrent()
        command.cancel()
        gate.complete(Unit)
        runCurrent()
        assertFalse(completed)
        assertFalse(command.state.value.hasFailure)
    }
}
