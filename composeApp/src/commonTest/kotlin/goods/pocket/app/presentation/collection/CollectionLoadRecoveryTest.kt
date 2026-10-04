@file:OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)

package goods.pocket.app.presentation.collection

import goods.pocket.app.data.InMemoryCollectionRepository
import goods.pocket.app.domain.collection.CollectionEntry
import goods.pocket.app.domain.collection.CollectionEntryStatus
import goods.pocket.app.domain.collection.CollectionRepository
import goods.pocket.app.domain.collection.MarkPreorderReceivedUseCase
import goods.pocket.app.domain.service.AppClock
import goods.pocket.app.domain.service.IdGenerator
import goods.pocket.app.presentation.state.CollectionSegment
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest

class CollectionLoadRecoveryTest {
    @Test
    fun initialReadFailureRetriesWithoutLosingSearchOrSelectedSegment() = runTest {
        val entry = reservedEntry()
        val backing = InMemoryCollectionRepository(listOf(entry))
        val releaseRetry = CompletableDeferred<Unit>()
        var subscriptions = 0
        var writes = 0
        val repository = object : CollectionRepository by backing {
            override fun observeEntries(): Flow<List<CollectionEntry>> = flow {
                subscriptions++
                if (subscriptions == 1) error("Fixture read failure")
                releaseRetry.await()
                emit(listOf(entry))
            }

            override suspend fun saveEntry(entry: CollectionEntry) {
                writes++
                backing.saveEntry(entry)
            }
        }
        val holder = CollectionStateHolder(
            repository,
            MarkPreorderReceivedUseCase(repository, clock),
            clock,
            readOnlyIds,
            backgroundScope,
        )
        holder.updateQuery("Fixture")
        holder.selectSegment(CollectionSegment.RESERVED)
        runCurrent()
        assertTrue(holder.state.value.hasLoadFailure)
        assertFalse(holder.state.value.isLoading)
        assertFalse(holder.state.value.hasLoaded)

        holder.retryLoad()
        runCurrent()
        assertTrue(holder.state.value.isLoading)
        assertFalse(holder.state.value.hasLoadFailure)
        assertFalse(holder.state.value.hasLoaded)
        assertEquals("Fixture", holder.state.value.query)
        assertEquals(CollectionSegment.RESERVED, holder.state.value.segment)

        releaseRetry.complete(Unit)
        runCurrent()
        assertEquals(listOf(entry), holder.state.value.entries)
        assertTrue(holder.state.value.hasLoaded)
        assertFalse(holder.state.value.isLoading)
        assertFalse(holder.state.value.hasLoadFailure)
        assertEquals("Fixture", holder.state.value.query)
        assertEquals(CollectionSegment.RESERVED, holder.state.value.segment)
        assertEquals(2, subscriptions)
        assertEquals(0, writes)
    }

    @Test
    fun refreshFailureAndRetryKeepLastLoadedEntriesUntilRecovery() = runTest {
        val original = reservedEntry()
        val updates = Channel<Result<List<CollectionEntry>>>(Channel.UNLIMITED)
        val repository = object : CollectionRepository by InMemoryCollectionRepository() {
            override fun observeEntries(): Flow<List<CollectionEntry>> =
                updates.receiveAsFlow().map { it.getOrThrow() }
        }
        val holder = CollectionStateHolder(
            repository,
            MarkPreorderReceivedUseCase(repository, clock),
            clock,
            readOnlyIds,
            backgroundScope,
        )
        updates.send(Result.success(listOf(original)))
        runCurrent()
        updates.send(Result.failure(IllegalStateException("Fixture observation failure")))
        runCurrent()
        assertEquals(listOf(original), holder.state.value.entries)
        assertTrue(holder.state.value.hasLoadFailure)
        assertTrue(holder.state.value.hasLoaded)

        holder.retryLoad()
        runCurrent()
        assertEquals(listOf(original), holder.state.value.entries)
        assertTrue(holder.state.value.isLoading)
        assertFalse(holder.state.value.hasLoadFailure)

        val refreshed = original.copy(name = "Recovered fixture")
        updates.send(Result.success(listOf(refreshed)))
        runCurrent()
        assertEquals(listOf(refreshed), holder.state.value.entries)
        assertFalse(holder.state.value.isLoading)
        assertFalse(holder.state.value.hasLoadFailure)
        updates.close()
    }

    private fun reservedEntry() = CollectionEntry(
        id = "load-recovery-fixture",
        name = "Fixture reservation",
        category = "goods",
        status = CollectionEntryStatus.RESERVED,
        createdAt = clock.currentTimestamp(),
        updatedAt = clock.currentTimestamp(),
    )

    private val clock = object : AppClock {
        override fun currentDate(): String = "2026-10-04"
        override fun currentTimestamp(): String = "2026-10-04T03:00:00Z"
    }

    private val readOnlyIds = object : IdGenerator {
        override fun generate(prefix: String): String = error("Read recovery must not create IDs")
    }
}
