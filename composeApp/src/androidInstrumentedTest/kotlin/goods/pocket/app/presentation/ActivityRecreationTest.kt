package goods.pocket.app.presentation

import android.content.Context
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isDialog
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextReplacement
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import goods.pocket.app.MainActivity
import goods.pocket.app.data.local.GoodsPocketStore
import goods.pocket.app.data.repository.SqlCollectionRepository
import goods.pocket.app.data.repository.SqlEventRepository
import goods.pocket.app.data.repository.SqlSettingsRepository
import goods.pocket.app.db.GoodsPocketDatabase
import goods.pocket.app.domain.collection.CollectionEntry
import goods.pocket.app.domain.collection.CollectionEntryStatus
import goods.pocket.app.domain.event.Event
import goods.pocket.app.domain.event.EventType
import goods.pocket.app.domain.settings.AppPreference
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNotSame
import kotlin.test.assertSame
import kotlin.test.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ActivityRecreationTest {
    @get:Rule
    val compose = createEmptyComposeRule()

    private lateinit var driver: AndroidSqliteDriver
    private lateinit var entries: SqlCollectionRepository
    private lateinit var events: SqlEventRepository
    private lateinit var settings: SqlSettingsRepository
    private lateinit var originalPreferences: AppPreference
    private var scenario: ActivityScenario<MainActivity>? = null

    @Before
    fun prepareDedicatedEmulatorData() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        driver = createDriver(context)
        val store = GoodsPocketStore({ driver }, Dispatchers.IO)
        entries = SqlCollectionRepository(store)
        events = SqlEventRepository(store)
        settings = SqlSettingsRepository(store)
        originalPreferences = settings.getAppPreferences()
        settings.updateAppPreferences(originalPreferences.copy(languageCode = "en"))
        removeTestEntries()
    }

    @After
    fun removeOnlyTestEntriesAndRestorePreferences() {
        scenario?.close()
        runBlocking {
            removeTestEntries()
            settings.updateAppPreferences(originalPreferences)
        }
        driver.close()
    }

    @Test
    fun collectionRouteAndSearchSurviveActivityRecreation() {
        launch()
        compose.onNode(hasText("Collection") and hasClickAction()).performClick()
        compose.onNode(hasSetTextAction()).performTextInput("retained search")

        checkNotNull(scenario).recreate()

        compose.onNode(hasSetTextAction()).assertTextEquals("retained search")
        compose.onNode(hasText("Collection") and hasClickAction()).assertExists()
    }

    @Test
    fun quickAddDraftSurvivesRecreationAndSavingCreatesOnlyOneRecord() {
        launch()
        compose.onNodeWithContentDescription("Quick Add").performClick()
        compose.onNodeWithContentDescription("Item name").performTextInput(QUICK_NAME)
        compose.onNodeWithContentDescription("Category").performTextInput("goods")
        compose.onNodeWithContentDescription("Series").performScrollTo()
            .performTextInput("Retained series")

        checkNotNull(scenario).recreate()

        compose.onNodeWithContentDescription("Item name").assertTextEquals(QUICK_NAME)
        compose.onNodeWithContentDescription("Category").assertTextEquals("goods")
        compose.onNodeWithContentDescription("Series").assertTextEquals("Retained series")
        compose.onNodeWithText("Save").performClick()
        compose.waitUntil(10_000) {
            runBlocking { entries.getAllEntries().count { it.name == QUICK_NAME } == 1 }
        }
        val saved = runBlocking { entries.getAllEntries().single { it.name == QUICK_NAME } }
        assertEquals("Retained series", saved.seriesName)

        checkNotNull(scenario).recreate()
        compose.waitForIdle()

        val afterRecreation = runBlocking {
            entries.getAllEntries().filter { it.name == QUICK_NAME }
        }
        assertEquals(listOf(saved), afterRecreation)
        compose.onNodeWithContentDescription("Item name").assertDoesNotExist()
    }

    @Test
    fun editorKeepsUnsavedChangesAndOriginalIdentityAfterRecreation() {
        runBlocking { entries.saveEntry(editEntry()) }
        launch()
        compose.onNode(hasText("Collection") and hasClickAction()).performClick()
        compose.waitUntil(10_000) {
            compose.onAllNodes(hasText(EDIT_NAME)).fetchSemanticsNodes().isNotEmpty()
        }
        compose.onNodeWithText(EDIT_NAME).performClick()
        compose.onNodeWithText("Edit").performClick()
        compose.onNodeWithContentDescription("Item name").performTextReplacement(EDITED_NAME)
        compose.onNodeWithContentDescription("Note").performScrollTo()
            .performTextReplacement("Unsaved editor note")

        checkNotNull(scenario).recreate()

        compose.onNodeWithContentDescription("Item name").assertTextEquals(EDITED_NAME)
        compose.onNodeWithContentDescription("Note").assertTextEquals("Unsaved editor note")
        compose.onNodeWithText("Save Changes").performClick()
        compose.waitUntil(10_000) {
            runBlocking { entries.getEntry(EDIT_ID)?.name == EDITED_NAME }
        }
        val saved = assertNotNull(runBlocking { entries.getEntry(EDIT_ID) })
        assertEquals("Unsaved editor note", saved.note)
        assertEquals(19000L, saved.purchasePrice)
        assertEquals("https://example.com/recreation", saved.relatedLink)
        assertEquals(
            1,
            runBlocking {
                entries.getAllEntries().count { it.name == EDIT_NAME || it.name == EDITED_NAME }
            },
        )
    }

    @Test
    fun eventQuickAddDraftSurvivesRecreationAndWritesOnce() {
        launch()
        compose.onNodeWithContentDescription("Quick Add").performClick()
        compose.onNode(hasText("Events") and hasAnyAncestor(isDialog())).performClick()
        compose.onNodeWithContentDescription("Event name").performTextInput(EVENT_QUICK_NAME)
        compose.onNodeWithContentDescription("Target date").performTextInput("2030-04-05")
        compose.onNodeWithText("Delivery").performClick()

        checkNotNull(scenario).recreate()

        compose.onNodeWithContentDescription("Event name").assertTextEquals(EVENT_QUICK_NAME)
        compose.onNodeWithContentDescription("Target date").assertTextEquals("2030-04-05")
        compose.onNodeWithText("Save").performClick()
        compose.waitUntil(10_000) {
            runBlocking { events.getEvents().count { it.title == EVENT_QUICK_NAME } == 1 }
        }
        val saved = runBlocking { events.getEvents().single { it.title == EVENT_QUICK_NAME } }
        assertEquals(EventType.DELIVERY, saved.eventType)
        checkNotNull(scenario).recreate()
        compose.waitForIdle()
        assertEquals(
            listOf(saved),
            runBlocking {
                events.getEvents().filter { it.title == EVENT_QUICK_NAME }
            },
        )
    }

    @Test
    fun eventEditorRetainsDraftAndHiddenFieldsAcrossRecreation() {
        runBlocking {
            events.saveEvent(
                Event(
                    id = EVENT_EDIT_ID,
                    title = EVENT_EDIT_NAME,
                    eventType = EventType.RELEASE,
                    targetDate = "2026-10-20",
                    locationOrStore = "Retained store",
                    memo = "Retained memo",
                    createdAt = "2026-10-04T03:00:00Z",
                    updatedAt = "2026-10-04T03:00:00Z",
                ),
            )
        }
        launch()
        compose.onNode(hasText("Events") and hasClickAction()).performClick()
        compose.waitUntil(10_000) {
            compose.onAllNodes(hasText(EVENT_EDIT_NAME)).fetchSemanticsNodes().isNotEmpty()
        }
        compose.onAllNodes(hasText(EVENT_EDIT_NAME))[0].performClick()
        compose.onNodeWithText("Edit").performClick()
        compose.onNodeWithContentDescription("Title").performTextReplacement(EVENT_EDITED_NAME)
        compose.onNodeWithContentDescription("Scheduled Date").performTextReplacement("2030-05-06")

        checkNotNull(scenario).recreate()

        compose.onNodeWithContentDescription("Title").assertTextEquals(EVENT_EDITED_NAME)
        compose.onNodeWithContentDescription("Scheduled Date").assertTextEquals("2030-05-06")
        compose.onNodeWithText("Save Changes").performClick()
        compose.waitUntil(10_000) {
            runBlocking { events.getEvents().any { it.title == EVENT_EDITED_NAME } }
        }
        val saved = runBlocking { events.getEvents().single { it.id == EVENT_EDIT_ID } }
        assertEquals(EVENT_EDITED_NAME, saved.title)
        assertEquals("2030-05-06", saved.targetDate)
        assertEquals("Retained store", saved.locationOrStore)
        assertEquals("Retained memo", saved.memo)
    }

    @Test
    fun sessionOwnerSurvivesRecreationAndCancelsOnlyAfterFinalClose() {
        launch()
        val original = sessionOwner()
        val job = assertNotNull(original.viewModelScope.coroutineContext[Job])

        checkNotNull(scenario).recreate()
        compose.waitForIdle()

        assertSame(original, sessionOwner())
        assertTrue(job.isActive)
        checkNotNull(scenario).close()
        scenario = null
        assertTrue(job.isCancelled)
        launch()
        assertNotSame(original, sessionOwner())
    }

    @Test
    fun finishingActivityDiscardsUnsavedOverlayForANewSession() {
        launch()
        compose.onNodeWithContentDescription("Quick Add").performClick()
        compose.onNodeWithContentDescription("Item name").performTextInput(QUICK_NAME)
        checkNotNull(scenario).close()
        scenario = null

        launch()

        compose.onNodeWithText("GoodsPocket").assertExists()
        compose.onNodeWithContentDescription("Item name").assertDoesNotExist()
        compose.onNodeWithContentDescription("Quick Add").performClick()
        compose.onNodeWithContentDescription("Item name").assertTextEquals("")
        assertEquals(0, runBlocking { entries.getAllEntries().count { it.name == QUICK_NAME } })
    }

    private fun launch() {
        scenario = ActivityScenario.launch(MainActivity::class.java)
        compose.waitUntil(10_000) {
            compose.onAllNodes(hasContentDescription("Quick Add"))
                .fetchSemanticsNodes().isNotEmpty()
        }
    }

    private suspend fun removeTestEntries() {
        events.getEvents().filter {
            it.id == EVENT_EDIT_ID ||
                it.title == EVENT_QUICK_NAME ||
                it.title == EVENT_EDIT_NAME ||
                it.title == EVENT_EDITED_NAME
        }.forEach { events.deleteEvent(it.id) }
        entries.getAllEntries().filter {
            it.id == EDIT_ID ||
                it.name == QUICK_NAME ||
                it.name == EDIT_NAME ||
                it.name == EDITED_NAME
        }.forEach { entries.deleteEntry(it.id) }
    }

    private fun sessionOwner(): PresentationSessionViewModel {
        var owner: PresentationSessionViewModel? = null
        checkNotNull(scenario).onActivity { activity ->
            owner = ViewModelProvider(activity)[PresentationSessionViewModel::class.java]
        }
        return checkNotNull(owner)
    }
}

private fun createDriver(context: Context): AndroidSqliteDriver = AndroidSqliteDriver(
    schema = GoodsPocketDatabase.Schema,
    context = context,
    name = "GoodsPocket-v2.db",
    callback = object : AndroidSqliteDriver.Callback(GoodsPocketDatabase.Schema) {
        override fun onConfigure(db: SupportSQLiteDatabase) {
            db.setForeignKeyConstraintsEnabled(true)
        }
    },
)

private fun editEntry(): CollectionEntry = CollectionEntry(
    id = EDIT_ID,
    name = EDIT_NAME,
    category = "goods",
    status = CollectionEntryStatus.OWNED,
    purchasePrice = 19000,
    relatedLink = "https://example.com/recreation",
    note = "Original editor note",
    createdAt = "2026-10-04T03:00:00Z",
    updatedAt = "2026-10-04T03:00:00Z",
)

private const val QUICK_NAME = "instrumentation-recreation-quick"
private const val EDIT_NAME = "instrumentation-recreation-edit"
private const val EDITED_NAME = "instrumentation-recreation-edited"
private const val EDIT_ID = "instrumentation-recreation-edit-id"
private const val EVENT_QUICK_NAME = "instrumentation-recreation-event-quick"
private const val EVENT_EDIT_NAME = "instrumentation-recreation-event-edit"
private const val EVENT_EDITED_NAME = "instrumentation-recreation-event-edited"
private const val EVENT_EDIT_ID = "instrumentation-recreation-event-edit-id"
