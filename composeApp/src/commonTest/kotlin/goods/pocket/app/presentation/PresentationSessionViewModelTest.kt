@file:OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)

package goods.pocket.app.presentation

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import goods.pocket.app.data.InMemoryCollectionRepository
import goods.pocket.app.domain.collection.CollectionEntry
import goods.pocket.app.domain.collection.CollectionRepository
import goods.pocket.app.domain.collection.MarkPreorderReceivedUseCase
import goods.pocket.app.domain.dashboard.GetDashboardSummaryUseCase
import goods.pocket.app.domain.dashboard.GetRecentActivitiesUseCase
import goods.pocket.app.domain.event.Event
import goods.pocket.app.domain.event.EventRepository
import goods.pocket.app.domain.event.EventType
import goods.pocket.app.domain.service.AppClock
import goods.pocket.app.domain.service.IdGenerator
import goods.pocket.app.domain.settings.AppPreference
import goods.pocket.app.domain.settings.SettingsRepository
import goods.pocket.app.presentation.component.CollectionDraft
import goods.pocket.app.presentation.navigation.AppDestination
import goods.pocket.app.presentation.state.ActiveEditor
import goods.pocket.app.presentation.state.CollectionSegment
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertSame
import kotlin.test.assertTrue
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

class PresentationSessionViewModelTest {
    @Test
    fun retainedOwnerPreservesRouteFiltersAndOneInFlightWrite() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        val store = ViewModelStore()
        val gate = CompletableDeferred<Unit>()
        val backing = InMemoryCollectionRepository()
        var writes = 0
        var generatedIds = 0
        var closes = 0
        val repository = object : CollectionRepository by backing {
            override suspend fun saveEntry(entry: CollectionEntry) {
                writes++
                gate.await()
                backing.saveEntry(entry)
            }
        }
        val factory = viewModelFactory {
            initializer {
                PresentationSessionViewModel(
                    sessionFactory(repository) { "id-${++generatedIds}" },
                ) {
                    closes++
                }
            }
        }
        try {
            val first =
                ViewModelProvider.create(store, factory)[PresentationSessionViewModel::class]
            val session = first.session
            session.shell.navigate(AppDestination.Collection)
            session.collection.updateQuery("figure")
            session.collection.selectSegment(CollectionSegment.RESERVED)
            session.events.updateType(EventType.DELIVERY)
            session.shell.showEditor(ActiveEditor.CollectionEntryEditor("existing"))
            session.collection.beginCreate()
            session.collection.save(CollectionDraft("Figure", "Goods")) {}
            runCurrent()

            val recreated =
                ViewModelProvider.create(store, factory)[PresentationSessionViewModel::class]
            assertSame(first, recreated)
            assertSame(session, recreated.session)
            assertEquals(AppDestination.Collection, recreated.session.shell.state.value.destination)
            assertEquals(
                ActiveEditor.CollectionEntryEditor("existing"),
                recreated.session.shell.state.value.editor,
            )
            assertEquals("figure", recreated.session.collection.state.value.query)
            assertEquals(
                CollectionSegment.RESERVED,
                recreated.session.collection.state.value.segment,
            )
            assertEquals(EventType.DELIVERY, recreated.session.events.state.value.selectedType)
            assertTrue(recreated.session.collection.command.state.value.isRunning)
            recreated.session.collection.save(CollectionDraft("Duplicate", "Goods")) {}
            gate.complete(Unit)
            runCurrent()
            assertEquals(1, writes)
            assertEquals(1, generatedIds)
            assertEquals(1, backing.getEntries().size)
            assertEquals(0, closes)
        } finally {
            store.clear()
            Dispatchers.resetMain()
        }
        assertEquals(1, closes)
    }

    @Test
    fun clearingFinalOwnerCancelsWorkAndClosesResourcesOnce() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        val store = ViewModelStore()
        val gate = CompletableDeferred<Unit>()
        val backing = InMemoryCollectionRepository()
        var canceled = false
        var closes = 0
        val repository = object : CollectionRepository by backing {
            override suspend fun saveEntry(entry: CollectionEntry) {
                try {
                    gate.await()
                    backing.saveEntry(entry)
                } finally {
                    canceled = true
                }
            }
        }
        val factory = viewModelFactory {
            initializer {
                PresentationSessionViewModel(sessionFactory(repository) { "id" }) { closes++ }
            }
        }
        try {
            val owner =
                ViewModelProvider.create(store, factory)[PresentationSessionViewModel::class]
            owner.session.collection.save(CollectionDraft("Figure", "Goods")) {}
            runCurrent()
            store.clear()
            runCurrent()
            assertTrue(canceled)
            assertFalse(owner.session.collection.command.state.value.isRunning)
            assertTrue(backing.getEntries().isEmpty())
            assertEquals(1, closes)
            val fresh =
                ViewModelProvider.create(store, factory)[PresentationSessionViewModel::class]
            assertEquals(AppDestination.Home, fresh.session.shell.state.value.destination)
        } finally {
            store.clear()
            Dispatchers.resetMain()
        }
        assertEquals(2, closes)
    }
}

private fun sessionFactory(
    repository: CollectionRepository,
    ids: () -> String,
): PresentationSessionFactory {
    val clock = object : AppClock {
        override fun currentDate(): String = "2026-10-08"
        override fun currentTimestamp(): String = "2026-10-08T10:00:00Z"
    }
    val events = object : EventRepository {
        private val values = MutableStateFlow<List<Event>>(emptyList())
        override fun observeEvents() = values
        override suspend fun getEvents(): List<Event> = values.value
        override suspend fun saveEvent(event: Event) {
            values.value += event
        }
        override suspend fun deleteEvent(id: String) {
            values.value = values.value.filterNot { it.id == id }
        }
    }
    val settings = object : SettingsRepository {
        private val value = MutableStateFlow(AppPreference())
        override fun observePreferences() = value
        override suspend fun getAppPreferences(): AppPreference = value.value
        override suspend fun updateAppPreferences(preferences: AppPreference) {
            value.value = preferences
        }
    }
    return PresentationSessionFactory(
        repository,
        events,
        settings,
        clock,
        object : IdGenerator {
            override fun generate(prefix: String): String = ids()
        },
        MarkPreorderReceivedUseCase(repository, clock),
        GetDashboardSummaryUseCase(repository),
        GetRecentActivitiesUseCase(repository),
    )
}
