package goods.pocket.app.data

import android.content.Context
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import goods.pocket.app.data.local.GoodsPocketStore
import goods.pocket.app.data.repository.SqlCollectionRepository
import goods.pocket.app.data.repository.SqlEventRepository
import goods.pocket.app.db.GoodsPocketDatabase
import goods.pocket.app.domain.collection.CollectionEntry
import goods.pocket.app.domain.collection.CollectionEntryStatus
import goods.pocket.app.domain.collection.MarkPreorderReceivedUseCase
import goods.pocket.app.domain.event.Event
import goods.pocket.app.domain.event.EventType
import goods.pocket.app.domain.repository.RepositoryFailure
import goods.pocket.app.domain.repository.RepositoryOperation
import goods.pocket.app.domain.service.AppClock
import goods.pocket.app.domain.service.IdGenerator
import goods.pocket.app.presentation.collection.CollectionStateHolder
import goods.pocket.app.presentation.component.CollectionDraft
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AndroidStorageRecoveryTest {
    @Test
    fun failedSaveRetriesOriginalDraftAndIdentityThenSurvivesReopening() = runBlocking {
        val fixture = DeviceDatabase("instrumentation-save-recovery.db")
        val featureJob = SupervisorJob()
        val scope = CoroutineScope(featureJob + Dispatchers.Default)
        val clock = FixedClock()
        val ids = CountingIds()
        val repository = fixture.collections
        val holder = CollectionStateHolder(
            repository,
            MarkPreorderReceivedUseCase(repository, clock),
            clock,
            ids,
            scope,
        )
        val draft = CollectionDraft(
            name = "Original draft",
            category = "goods",
            seriesName = "Series",
            characterName = "Character",
            purchaseStore = "Shop",
            note = "Keep this note after disk failure",
        )
        val savedId = CompletableDeferred<String>()
        try {
            repository.getEntries()
            fixture.driver.execute(
                null,
                "CREATE TRIGGER deny_insert BEFORE INSERT ON collection_entry " +
                    "BEGIN SELECT RAISE(ABORT, 'injected storage failure'); END",
                0,
            )
            holder.beginCreate()
            holder.save(draft) { savedId.complete(it) }
            withTimeout(10_000) { holder.command.state.first { it.hasFailure } }

            assertFalse(savedId.isCompleted)
            assertEquals(1, ids.calls)
            assertTrue(repository.getEntries().isEmpty())

            fixture.driver.execute(null, "DROP TRIGGER deny_insert", 0)
            holder.command.retry()
            assertEquals("entry-stable-id", withTimeout(10_000) { savedId.await() })
            val saved = assertNotNull(repository.getEntry("entry-stable-id"))
            assertEquals(draft.name, saved.name)
            assertEquals(draft.category, saved.category)
            assertEquals(draft.seriesName, saved.seriesName)
            assertEquals(draft.characterName, saved.characterName)
            assertEquals(draft.purchaseStore, saved.purchaseStore)
            assertEquals(draft.note, saved.note)
            assertEquals(1, ids.calls)
            assertEquals(1, repository.getEntries().size)
            assertFalse(holder.command.state.value.hasFailure)

            featureJob.cancelAndJoin()
            fixture.reopen()
            assertEquals(saved, fixture.collections.getEntry(saved.id))
        } finally {
            featureJob.cancelAndJoin()
            fixture.close()
        }
    }

    @Test
    fun failedDeleteRollsBackEventUnlinkAndSuccessfulRetryPersistsAfterReopen() = runBlocking {
        val fixture = DeviceDatabase("instrumentation-delete-recovery.db")
        try {
            val entry = ownedEntry()
            val event = linkedEvent()
            fixture.collections.saveEntry(entry)
            fixture.events.saveEvent(event)
            fixture.driver.execute(
                null,
                "CREATE TRIGGER deny_delete BEFORE DELETE ON collection_entry " +
                    "BEGIN SELECT RAISE(ABORT, 'injected storage failure'); END",
                0,
            )

            val failure = assertFailsWith<RepositoryFailure> {
                fixture.collections.deleteEntry(entry.id)
            }
            assertEquals(RepositoryOperation.WRITE, failure.operation)
            assertNotNull(failure.cause)
            assertEquals(entry, fixture.collections.getEntry(entry.id))
            assertEquals(event, fixture.events.getEvents().single())

            fixture.driver.execute(null, "DROP TRIGGER deny_delete", 0)
            fixture.collections.deleteEntry(entry.id)
            fixture.reopen()
            assertNull(fixture.collections.getEntry(entry.id))
            assertEquals(event.copy(relatedEntryId = null), fixture.events.getEvents().single())
        } finally {
            fixture.close()
        }
    }

    @Test
    fun androidSQLiteRejectsMissingCollectionRelationship() = runBlocking {
        val fixture = DeviceDatabase("instrumentation-foreign-key.db")
        try {
            val failure = assertFailsWith<RepositoryFailure> {
                fixture.events.saveEvent(linkedEvent())
            }
            assertEquals(RepositoryOperation.WRITE, failure.operation)
            assertNotNull(failure.cause)
            assertTrue(fixture.events.getEvents().isEmpty())
        } finally {
            fixture.close()
        }
    }
}

private class DeviceDatabase(private val name: String) {
    private val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
    var driver: AndroidSqliteDriver
        private set
    var collections: SqlCollectionRepository
        private set
    var events: SqlEventRepository
        private set

    init {
        context.deleteDatabase(name)
        driver = createDriver()
        val store = GoodsPocketStore({ driver }, Dispatchers.IO)
        collections = SqlCollectionRepository(store)
        events = SqlEventRepository(store)
    }

    fun reopen() {
        driver.close()
        driver = createDriver()
        val store = GoodsPocketStore({ driver }, Dispatchers.IO)
        collections = SqlCollectionRepository(store)
        events = SqlEventRepository(store)
    }

    fun close() {
        driver.close()
        context.deleteDatabase(name)
    }

    private fun createDriver(): AndroidSqliteDriver = AndroidSqliteDriver(
        schema = GoodsPocketDatabase.Schema,
        context = context,
        name = name,
        callback = object : AndroidSqliteDriver.Callback(GoodsPocketDatabase.Schema) {
            override fun onConfigure(db: SupportSQLiteDatabase) {
                db.setForeignKeyConstraintsEnabled(true)
            }
        },
    )
}

private class FixedClock : AppClock {
    override fun currentDate(): String = "2026-10-04"

    override fun currentTimestamp(): String = "2026-10-04T03:00:00Z"
}

private class CountingIds : IdGenerator {
    var calls: Int = 0
        private set

    override fun generate(prefix: String): String {
        calls++
        return "$prefix-stable-id"
    }
}

private fun ownedEntry(): CollectionEntry = CollectionEntry(
    id = "entry-stable-id",
    name = "Goods",
    category = "goods",
    status = CollectionEntryStatus.OWNED,
    purchasePrice = 12000,
    purchaseDate = "2026-10-04",
    relatedLink = "https://example.com/goods",
    createdAt = "2026-10-04T03:00:00Z",
    updatedAt = "2026-10-04T03:00:00Z",
)

private fun linkedEvent(): Event = Event(
    id = "event-stable-id",
    title = "Release",
    eventType = EventType.RELEASE,
    targetDate = "2026-10-20",
    relatedEntryId = "entry-stable-id",
    locationOrStore = "Shop",
    memo = "Keep this event",
    createdAt = "2026-10-04T03:00:00Z",
    updatedAt = "2026-10-04T03:00:00Z",
)
