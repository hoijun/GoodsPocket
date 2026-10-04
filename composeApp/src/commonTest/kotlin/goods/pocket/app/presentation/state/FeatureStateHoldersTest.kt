@file:OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)

package goods.pocket.app.presentation.state

import goods.pocket.app.data.InMemoryCollectionRepository
import goods.pocket.app.domain.collection.CollectionEntry
import goods.pocket.app.domain.collection.CollectionEntryStatus
import goods.pocket.app.domain.collection.CollectionRepository
import goods.pocket.app.domain.collection.MarkPreorderReceivedUseCase
import goods.pocket.app.domain.dashboard.GetDashboardSummaryUseCase
import goods.pocket.app.domain.dashboard.GetRecentActivitiesUseCase
import goods.pocket.app.domain.event.Event
import goods.pocket.app.domain.event.EventRepository
import goods.pocket.app.domain.service.AppClock
import goods.pocket.app.domain.service.IdGenerator
import goods.pocket.app.domain.settings.AppPreference
import goods.pocket.app.domain.settings.SettingsRepository
import goods.pocket.app.presentation.PresentationSessionFactory
import goods.pocket.app.presentation.collection.CollectionStateHolder
import goods.pocket.app.presentation.component.CollectionDraft
import goods.pocket.app.presentation.component.EventDraft
import goods.pocket.app.presentation.events.EventsStateHolder
import goods.pocket.app.presentation.settings.SettingsStateHolder
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest

class FeatureStateHoldersTest {
    @Test
    fun receiptRefreshFailureBlocksStaleEditingAndRetriesOnlyRead() = runTest {
        val reserved = entry().copy(
            status = CollectionEntryStatus.RESERVED,
            releaseDate = "2026-10-08",
            reservationStore = "Store",
        )
        val backing = InMemoryCollectionRepository(listOf(reserved))
        var receipts = 0
        var readFails = true
        val repository = object : CollectionRepository by backing {
            override fun observeEntries(): Flow<List<CollectionEntry>> =
                MutableStateFlow(listOf(reserved))
            override suspend fun receiveReservation(
                id: String,
                receivedAt: String,
                receivedDate: String,
            ): goods.pocket.app.domain.collection.ReservationResult {
                receipts++
                return backing.receiveReservation(id, receivedAt, receivedDate)
            }
            override suspend fun getEntry(id: String): CollectionEntry? {
                if (readFails && receipts > 0) error("refresh unavailable")
                return backing.getEntry(id)
            }
        }
        val holder =
            CollectionStateHolder(
                repository,
                MarkPreorderReceivedUseCase(repository, clock),
                clock,
                ids(),
                backgroundScope,
            )
        runCurrent()
        holder.markReceived(reserved.id) {}
        runCurrent()
        assertFalse(holder.state.value.canEdit(reserved.id))
        holder.save(
            CollectionDraft(
                "Wrong stale edit",
                "goods",
                CollectionEntryStatus.RESERVED,
                reservationStore = "Store",
                releaseDate = "2026-10-08",
            ),
            reserved.id,
        ) {
        }
        runCurrent()
        assertEquals(CollectionEntryStatus.OWNED, backing.getEntry(reserved.id)?.status)
        readFails = false
        holder.retryReceiptRefresh()
        runCurrent()
        assertEquals(1, receipts)
        assertTrue(holder.state.value.canEdit(reserved.id))
        assertEquals(CollectionEntryStatus.OWNED, holder.state.value.entry(reserved.id)?.status)
        assertNotNull(holder.state.value.entry(reserved.id)?.reservation?.receivedAt)
        holder.save(
            CollectionDraft("Edited after receipt", "goods", CollectionEntryStatus.OWNED),
            reserved.id,
        ) {
        }
        runCurrent()
        assertEquals(CollectionEntryStatus.OWNED, backing.getEntry(reserved.id)?.status)
        assertNotNull(backing.getEntry(reserved.id)?.reservation?.receivedAt)
    }

    @Test
    fun laterEventEmissionWithSameTimestampOverridesPendingSavedDetail() = runTest {
        val events = FakeEvents()
        events.publishWrites = false
        val original =
            Event(
                "event-one",
                "Original",
                goods.pocket.app.domain.event.EventType.RELEASE,
                "2026-10-08",
                relatedEntryId = "entry-one",
                createdAt = clock.currentTimestamp(),
                updatedAt = clock.currentTimestamp(),
            )
        events.events.value = listOf(original)
        val holder = EventsStateHolder(events, clock, ids(), backgroundScope)
        runCurrent()
        holder.save(EventDraft("Saved", "2026-10-08"), original.id) {}
        runCurrent()
        val saved = checkNotNull(holder.state.value.event(original.id))
        events.events.value = listOf(saved.copy(relatedEntryId = null))
        runCurrent()
        assertEquals(null, holder.state.value.event(original.id)?.relatedEntryId)
    }

    private val clock = object : AppClock {
        override fun currentDate(): String = "2026-10-04"
        override fun currentTimestamp(): String = "2026-10-04T03:00:00Z"
    }
    private fun ids(): IdGenerator = object : IdGenerator {
        private var number = 0
        override fun generate(prefix: String): String = "$prefix-${++number}"
    }

    @Test
    fun failedCreateRetainsIdentityAndDraftUntilRetrySucceeds() = runTest {
        val backing = InMemoryCollectionRepository()
        val attempted = mutableListOf<CollectionEntry>()
        var fails = true
        val repository = object : CollectionRepository by backing {
            override suspend fun saveEntry(entry: CollectionEntry) {
                attempted += entry
                if (fails) error("offline")
                backing.saveEntry(entry)
            }
        }
        val holder =
            CollectionStateHolder(
                repository,
                MarkPreorderReceivedUseCase(repository, clock),
                clock,
                ids(),
                backgroundScope,
            )
        runCurrent()
        holder.beginCreate()
        var navigations = 0
        holder.save(CollectionDraft(name = "Figure", category = "goods")) { navigations++ }
        runCurrent()
        assertEquals(0, navigations)
        assertTrue(holder.state.value.command.hasFailure)
        fails = false
        holder.command.retry()
        runCurrent()
        assertEquals(attempted.first(), attempted.last())
        assertEquals(1, backing.getEntries().size)
        assertEquals(1, navigations)
    }

    @Test
    fun successfulSaveProvidesDetailBeforeObserverEmitsAndNeverReplaysWriteForReadFailure() =
        runTest {
            val backing = InMemoryCollectionRepository()
            var writes = 0
            val repository = object : CollectionRepository by backing {
                override fun observeEntries(): Flow<List<CollectionEntry>> = flow {
                    emit(emptyList())
                    error("read unavailable")
                }
                override suspend fun saveEntry(entry: CollectionEntry) {
                    writes++
                    backing.saveEntry(entry)
                }
            }
            val holder =
                CollectionStateHolder(
                    repository,
                    MarkPreorderReceivedUseCase(repository, clock),
                    clock,
                    ids(),
                    backgroundScope,
                )
            runCurrent()
            var savedId = ""
            holder.save(CollectionDraft(name = "Figure", category = "goods")) { savedId = it }
            runCurrent()
            assertTrue(holder.state.value.hasLoadFailure)
            assertEquals("Figure", holder.state.value.entry(savedId)?.name)
            holder.retryLoad()
            holder.command.retry()
            runCurrent()
            assertEquals(1, writes)
        }

    @Test
    fun staleReservationDoesNotNavigateAsSuccessfulReceipt() = runTest {
        val repository = InMemoryCollectionRepository()
        val holder =
            CollectionStateHolder(
                repository,
                MarkPreorderReceivedUseCase(repository, clock),
                clock,
                ids(),
                backgroundScope,
            )
        var navigations = 0
        holder.markReceived("missing") { navigations++ }
        runCurrent()
        assertEquals(0, navigations)
        assertFalse(holder.state.value.command.hasFailure)
    }

    @Test
    fun failedPreferencesReadCannotOverwriteStoredValuesWithDefaults() = runTest {
        val repository = FakeSettings()
        repository.failRead = true
        val holder = SettingsStateHolder(repository, backgroundScope)
        runCurrent()
        holder.updateLanguage("en")
        runCurrent()
        assertTrue(holder.state.value.hasLoadFailure)
        assertEquals(0, repository.writes)
        repository.failRead = false
        holder.retryLoad()
        runCurrent()
        holder.updateLanguage("en")
        runCurrent()
        assertEquals("USD", repository.preferences.value.currencyCode)
        assertEquals("en", repository.preferences.value.languageCode)
    }

    @Test
    fun eventWritesDoNotRestartCollectionObservationAndClosingSessionStopsUpdates() = runTest {
        val backing = InMemoryCollectionRepository()
        var collections = 0
        val repository = object : CollectionRepository by backing {
            override fun observeEntries(): Flow<List<CollectionEntry>> {
                collections++
                return backing.observeEntries()
            }
        }
        val events = FakeEvents()
        val session = PresentationSessionFactory(
            repository,
            events,
            FakeSettings(),
            clock,
            ids(),
            MarkPreorderReceivedUseCase(repository, clock),
            GetDashboardSummaryUseCase(repository),
            GetRecentActivitiesUseCase(repository),
        ).create(backgroundScope)
        runCurrent()
        val before = collections
        session.events.save(EventDraft("Release", "2026-10-08")) {}
        runCurrent()
        assertEquals(before, collections)
        assertEquals(1, session.events.state.value.events.size)
        assertEquals(1, session.home.state.value.upcomingEvents.size)
        session.close()
        backing.saveEntry(entry())
        runCurrent()
        assertTrue(session.collection.state.value.entries.isEmpty())
    }

    @Test
    fun eventSaveRetainsSuccessfulDetailWhileObservationIsDelayed() = runTest {
        val events = FakeEvents()
        events.publishWrites = false
        val holder = EventsStateHolder(events, clock, ids(), backgroundScope)
        runCurrent()
        var id = ""
        holder.save(EventDraft("Release", "2026-10-08")) { id = it }
        runCurrent()
        assertNotNull(holder.state.value.event(id))
        assertTrue(holder.state.value.events.isEmpty())
    }

    private fun entry() = CollectionEntry(
        "one",
        "Figure",
        "goods",
        CollectionEntryStatus.OWNED,
        createdAt = clock.currentTimestamp(),
        updatedAt = clock.currentTimestamp(),
    )
}

private class FakeEvents : EventRepository {
    val events = MutableStateFlow<List<Event>>(emptyList())
    var publishWrites = true
    override fun observeEvents(): Flow<List<Event>> = events
    override suspend fun getEvents(): List<Event> = events.value
    override suspend fun saveEvent(event: Event) {
        if (publishWrites) events.value = events.value.filterNot { it.id == event.id } + event
    }
    override suspend fun deleteEvent(id: String) {
        events.value =
            events.value.filterNot { it.id == id }
    }
}

private class FakeSettings : SettingsRepository {
    val preferences = MutableStateFlow(AppPreference(currencyCode = "USD"))
    var failRead = false
    var writes = 0
    override fun observePreferences(): Flow<AppPreference> = if (failRead) {
        flow {
            error("read unavailable")
        }
    } else {
        preferences
    }
    override suspend fun getAppPreferences(): AppPreference = preferences.value
    override suspend fun updateAppPreferences(preferences: AppPreference) {
        writes++
        this.preferences.value =
            preferences
    }
}
