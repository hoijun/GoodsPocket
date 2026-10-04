package goods.pocket.app.data

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import goods.pocket.app.data.local.GoodsPocketStore
import goods.pocket.app.data.repository.SqlCollectionRepository
import goods.pocket.app.data.repository.SqlEventRepository
import goods.pocket.app.data.repository.SqlSettingsRepository
import goods.pocket.app.db.GoodsPocketDatabase
import goods.pocket.app.domain.collection.CollectionEntry
import goods.pocket.app.domain.collection.CollectionEntryStatus
import goods.pocket.app.domain.collection.ReservationDetails
import goods.pocket.app.domain.collection.ReservationResult
import goods.pocket.app.domain.event.Event
import goods.pocket.app.domain.event.EventType
import goods.pocket.app.domain.repository.RepositoryFailure
import goods.pocket.app.domain.repository.RepositoryOperation
import goods.pocket.app.domain.settings.AppPreference
import java.nio.file.Files
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
class SqlRepositoryTest {
    @Test
    fun `event with a missing collection reference is rejected without partial save`() = runTest {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        GoodsPocketDatabase.Schema.create(driver)
        val events =
            SqlEventRepository(GoodsPocketStore({ driver }, StandardTestDispatcher(testScheduler)))
        val failure = assertFailsWith<RepositoryFailure> { events.saveEvent(linkedEvent()) }
        assertEquals(RepositoryOperation.WRITE, failure.operation)
        assertNotNull(failure.cause)
        assertTrue(events.getEvents().isEmpty())
        driver.close()
    }

    @Test
    fun `invalid new insert reports failure and keeps database empty`() = runTest {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        GoodsPocketDatabase.Schema.create(driver)
        val repository =
            SqlCollectionRepository(
                GoodsPocketStore({
                    driver
                }, StandardTestDispatcher(testScheduler)),
            )
        val failure =
            assertFailsWith<RepositoryFailure> {
                repository.saveEntry(reservedEntry().copy(quantity = 0))
            }
        assertEquals(RepositoryOperation.WRITE, failure.operation)
        assertNotNull(failure.cause)
        assertTrue(repository.getEntries().isEmpty())
        driver.close()
    }

    @Test
    fun `failed deletion rolls back the preceding event unlink`() = runTest {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        GoodsPocketDatabase.Schema.create(driver)
        val store = GoodsPocketStore({ driver }, StandardTestDispatcher(testScheduler))
        val collections = SqlCollectionRepository(store)
        val events = SqlEventRepository(store)
        collections.saveEntry(reservedEntry())
        events.saveEvent(linkedEvent())
        driver.execute(
            null,
            "CREATE TRIGGER deny_delete BEFORE DELETE ON collection_entry BEGIN SELECT RAISE(ABORT, 'denied'); END",
            0,
        )
        assertFailsWith<RepositoryFailure> { collections.deleteEntry("entry-1") }
        assertNotNull(collections.getEntry("entry-1"))
        assertEquals("entry-1", events.getEvents().single().relatedEntryId)
        driver.close()
    }

    @Test
    fun `editing existing entry retains its event link and hidden reservation details`() = runTest {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        GoodsPocketDatabase.Schema.create(driver)
        val store = GoodsPocketStore({ driver }, StandardTestDispatcher(testScheduler))
        val collections = SqlCollectionRepository(store)
        val events = SqlEventRepository(store)
        val entry = reservedEntry().copy(relatedLink = "https://example.com/goods", note = "memo")
        collections.saveEntry(entry)
        events.saveEvent(linkedEvent())
        collections.saveEntry(entry.copy(name = "Edited"))
        assertEquals(entry.copy(name = "Edited"), collections.getEntry(entry.id))
        assertEquals(entry.id, events.getEvents().single().relatedEntryId)
        driver.close()
    }

    @Test
    fun `receipt uses explicit local day and does not overwrite a saved purchase day`() = runTest {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        GoodsPocketDatabase.Schema.create(driver)
        val collections =
            SqlCollectionRepository(
                GoodsPocketStore({
                    driver
                }, StandardTestDispatcher(testScheduler)),
            )
        collections.saveEntry(reservedEntry())
        collections.receiveReservation("entry-1", "2026-10-03T15:05:00Z", "2026-10-04")
        assertEquals("2026-10-04", collections.getEntry("entry-1")?.purchaseDate)
        collections.saveEntry(reservedEntry().copy(id = "entry-2", purchaseDate = "2026-09-30"))
        collections.receiveReservation("entry-2", "2026-10-03T15:05:00Z", "2026-10-04")
        assertEquals("2026-09-30", collections.getEntry("entry-2")?.purchaseDate)
        driver.close()
    }

    @Test
    fun `canceled reservations disappear from active query and remain in history`() = runTest {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        GoodsPocketDatabase.Schema.create(driver)
        val collections =
            SqlCollectionRepository(
                GoodsPocketStore({
                    driver
                }, StandardTestDispatcher(testScheduler)),
            )
        collections.saveEntry(reservedEntry())
        assertEquals(
            ReservationResult.UPDATED,
            collections.cancelReservation("entry-1", "2026-10-04T00:00:00Z"),
        )
        assertTrue(collections.getEntries().isEmpty())
        assertEquals(12000L, collections.getAllEntries().single().reservation?.totalPrice)
        assertEquals(
            ReservationResult.NOT_RESERVED,
            collections.receiveReservation("entry-1", "2026-10-05T00:00:00Z", "2026-10-05"),
        )
        assertEquals(
            ReservationResult.NOT_FOUND,
            collections.receiveReservation("missing", "2026-10-05T00:00:00Z", "2026-10-05"),
        )
        driver.close()
    }

    @Test
    fun `query observation updates after writes without refreshing unrelated event data`() =
        runTest {
            val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
            GoodsPocketDatabase.Schema.create(driver)
            val store = GoodsPocketStore({ driver }, StandardTestDispatcher(testScheduler))
            val collections = SqlCollectionRepository(store)
            val events = SqlEventRepository(store)
            val emissions = mutableListOf<List<CollectionEntry>>()
            var eventEmissions = 0
            val collectionJob = backgroundScope.launch {
                collections.observeEntries().collect {
                    emissions +=
                        it
                }
            }
            val eventJob = backgroundScope.launch {
                events.observeEvents().collect { eventEmissions++ }
            }
            runCurrent()
            assertTrue(emissions.single().isEmpty())
            collections.saveEntry(reservedEntry())
            runCurrent()
            assertEquals("entry-1", emissions.last().single().id)
            assertEquals(1, eventEmissions)
            collectionJob.cancel()
            eventJob.cancel()
            driver.close()
        }

    @Test
    fun `database creation is deferred and cancellation is not wrapped`() = runTest {
        var created = false
        val dispatcher = StandardTestDispatcher(testScheduler)
        val store =
            GoodsPocketStore({
                created = true
                throw CancellationException("stop")
            }, dispatcher)
        val collections = SqlCollectionRepository(store)
        assertEquals(false, created)
        assertFailsWith<CancellationException> { collections.getEntries() }
        assertTrue(created)
    }

    @Test
    fun `all nullable details and preferences survive closing and reopening database`() = runTest {
        val file = Files.createTempFile("goodspocket-test", ".db").toFile()
        val url = "jdbc:sqlite:${file.absolutePath}"
        val dispatcher = StandardTestDispatcher(testScheduler)
        val original = reservedEntry().copy(
            seriesName = "Series", characterName = "Character", quantity = 3,
            purchasePrice = 15000, purchaseDate = "2026-09-30", purchaseStore = "Shop",
            storageLocationId = "location-1", relatedLink = "https://example.com", note = "Note",
            reservation = ReservationDetails("2026-09-30", 15000, 5000, 10000, 3000, "R1", null),
        )
        try {
            val firstDriver = JdbcSqliteDriver(url)
            try {
                GoodsPocketDatabase.Schema.create(firstDriver)
                val store = GoodsPocketStore({ firstDriver }, dispatcher)
                SqlCollectionRepository(store).saveEntry(original)
                SqlSettingsRepository(
                    store,
                ).updateAppPreferences(AppPreference(languageCode = "en"))
            } finally {
                firstDriver.close()
            }
            val reopened = JdbcSqliteDriver(url)
            try {
                val store = GoodsPocketStore({ reopened }, dispatcher)
                assertEquals(original, SqlCollectionRepository(store).getEntry(original.id))
                assertEquals("en", SqlSettingsRepository(store).getAppPreferences().languageCode)
            } finally {
                reopened.close()
            }
        } finally {
            file.delete()
        }
    }

    @Test
    fun `new database starts empty and reservation receipt preserves identity and history`() =
        runTest {
            val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
            GoodsPocketDatabase.Schema.create(driver)
            val repository =
                SqlCollectionRepository(
                    GoodsPocketStore({
                        driver
                    }, StandardTestDispatcher(testScheduler)),
                )
            assertTrue(repository.getEntries().isEmpty())
            val entry = reservedEntry()
            repository.saveEntry(entry)
            assertEquals(
                ReservationResult.UPDATED,
                repository.receiveReservation(entry.id, "2026-10-04T10:00:00Z", "2026-10-04"),
            )
            val received = repository.getEntry(entry.id)
            assertEquals(CollectionEntryStatus.OWNED, received?.status)
            assertEquals(entry.reservation?.totalPrice, received?.reservation?.totalPrice)
            assertEquals("2026-10-04T10:00:00Z", received?.reservation?.receivedAt)
            assertEquals(
                ReservationResult.ALREADY_RECEIVED,
                repository.receiveReservation(entry.id, "2026-10-05T10:00:00Z", "2026-10-05"),
            )
            driver.close()
        }

    @Test
    fun `deleting a collection entry preserves its event and clears the relationship`() = runTest {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        GoodsPocketDatabase.Schema.create(driver)
        val store = GoodsPocketStore({ driver }, StandardTestDispatcher(testScheduler))
        val collections = SqlCollectionRepository(store)
        val events = SqlEventRepository(store)
        collections.saveEntry(reservedEntry())
        events.saveEvent(
            Event(
                "event-1",
                "Release",
                EventType.RELEASE,
                "2026-10-04",
                "entry-1",
                createdAt = "2026-10-01T00:00:00Z",
                updatedAt = "2026-10-01T00:00:00Z",
            ),
        )
        collections.deleteEntry("entry-1")
        assertNull(events.getEvents().single().relatedEntryId)
        driver.close()
    }
}

private fun linkedEvent(): Event = Event(
    id = "event-1",
    title = "Release",
    eventType = EventType.RELEASE,
    targetDate = "2026-10-04",
    relatedEntryId = "entry-1",
    createdAt = "2026-10-01T00:00:00Z",
    updatedAt = "2026-10-01T00:00:00Z",
)

internal fun reservedEntry(): CollectionEntry = CollectionEntry(
    id = "entry-1",
    name = "Badge",
    category = "goods",
    status = CollectionEntryStatus.RESERVED,
    releaseDate = "2026-10-04",
    reservationStore = "Store",
    reservation = ReservationDetails(orderDate = "2026-09-30", totalPrice = 12000),
    createdAt = "2026-09-30T10:00:00Z",
    updatedAt = "2026-09-30T10:00:00Z",
)
