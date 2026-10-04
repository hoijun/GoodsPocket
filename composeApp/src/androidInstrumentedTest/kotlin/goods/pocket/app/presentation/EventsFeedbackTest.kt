package goods.pocket.app.presentation

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.unit.Density
import androidx.test.ext.junit.runners.AndroidJUnit4
import goods.pocket.app.domain.event.Event
import goods.pocket.app.domain.event.EventRepository
import goods.pocket.app.domain.event.EventType
import goods.pocket.app.domain.service.AppClock
import goods.pocket.app.domain.service.IdGenerator
import goods.pocket.app.presentation.designsystem.GoodsPocketTheme
import goods.pocket.app.presentation.events.EventsRoute
import goods.pocket.app.presentation.events.EventsStateHolder
import goods.pocket.app.presentation.i18n.ProvideLocalizedResources
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlin.test.assertTrue
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EventsFeedbackTest {
    @get:Rule val compose = createAndroidComposeRule<ComponentActivity>()
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private val repository = EventFeedbackRepository()
    private val failure = hasText("작업을 완료하지 못했어요. 다시 시도해 주세요.") or
        hasText("이벤트를 불러오지 못했어요")

    @After fun close() { scope.cancel(); repository.updates.close() }

    @Test fun initialLoadingDoesNotClaimZeroOrEmpty() {
        showEvents()
        compose.onNodeWithText("0").assertDoesNotExist()
        compose.onNodeWithText("이 필터에 해당하는 일정이 없어요.").assertDoesNotExist()
        compose.onNodeWithText("이벤트를 불러오는 중이에요").assertExists()
    }

    @Test fun initialFailureIsBelowFiltersAndDoesNotClaimEmpty() {
        showEvents(); failRead()
        val filter = compose.onNodeWithText("오프라인 행사").fetchSemanticsNode().boundsInRoot
        val error = compose.onNode(failure).fetchSemanticsNode().boundsInRoot
        assertTrue(error.top >= filter.bottom, "Error must be in the body, below filters")
        compose.onNodeWithText("0").assertDoesNotExist()
        compose.onNodeWithText("이 필터에 해당하는 일정이 없어요.").assertDoesNotExist()
    }

    @Test fun retryShowsLoadingThenSuccessfulEmptyResult() {
        showEvents(); failRead()
        compose.onNodeWithText("다시 시도").performClick()
        compose.onNode(failure).assertDoesNotExist()
        compose.onNodeWithText("이벤트를 불러오는 중이에요").assertExists()
        repository.updates.trySend(Result.success(emptyList()))
        waitFor("이 필터에 해당하는 일정이 없어요.")
        compose.onNodeWithText("0").assertExists()
    }

    @Test fun refreshFailureAndRetryKeepLoadedEventsAndFilter() {
        showEvents()
        compose.onNodeWithText("배송").performClick()
        repository.updates.trySend(Result.success(listOf(event)))
        waitFor(event.title)
        failRead()
        compose.onNodeWithText(event.title).performScrollTo().assertIsDisplayed()
        compose.onNodeWithText("다시 시도").performScrollTo().performClick()
        compose.onNodeWithText(event.title).performScrollTo().assertIsDisplayed()
        repository.updates.trySend(Result.success(listOf(event.copy(title = "새 배송 일정"))))
        waitFor("새 배송 일정")
        compose.onNodeWithText("이벤트를 불러오는 중이에요").assertDoesNotExist()
    }

    @Test fun enlargedEnglishRetryRemainsReachable() {
        showEvents("en", 2f)
        repository.updates.trySend(Result.failure(IllegalStateException("Fixture failure")))
        waitFor("Couldn’t load your events")
        compose.onNodeWithText("Retry").performScrollTo().assertIsDisplayed().performClick()
        compose.onNodeWithText("Couldn’t load your events").assertDoesNotExist()
    }

    private fun failRead() {
        repository.updates.trySend(Result.failure(IllegalStateException("Fixture failure")))
        compose.waitUntil(5_000) { compose.onAllNodes(failure).fetchSemanticsNodes().isNotEmpty() }
    }
    private fun waitFor(text: String) {
        compose.waitUntil(5_000) { compose.onAllNodes(hasText(text)).fetchSemanticsNodes().isNotEmpty() }
    }
    private fun showEvents(language: String = "ko", fontScale: Float = 1f) {
        val clock = object : AppClock {
            override fun currentDate() = "2026-04-01"
            override fun currentTimestamp() = "2026-04-01T00:00:00Z"
        }
        val holder = EventsStateHolder(repository, clock, object : IdGenerator {
            override fun generate(prefix: String) = "$prefix-fixture"
        }, scope)
        compose.setContent {
            GoodsPocketTheme {
                ProvideLocalizedResources(language, "KRW", "yyyy-MM-dd") {
                    val density = LocalDensity.current
                    CompositionLocalProvider(LocalDensity provides Density(density.density, fontScale)) {
                        Scaffold(contentWindowInsets = WindowInsets.safeDrawing) { padding ->
                            Box(Modifier.padding(padding).consumeWindowInsets(padding)) {
                                EventsRoute(holder) {}
                            }
                        }
                    }
                }
            }
        }
    }
    private val event = Event("delivery", "배송 일정", EventType.DELIVERY, "2026-04-15",
        createdAt = "2026-04-01T00:00:00Z", updatedAt = "2026-04-01T00:00:00Z")
}

private class EventFeedbackRepository : EventRepository {
    val updates = Channel<Result<List<Event>>>(Channel.UNLIMITED)
    override fun observeEvents(): Flow<List<Event>> = updates.receiveAsFlow().map { it.getOrThrow() }
    override suspend fun getEvents(): List<Event> = emptyList()
    override suspend fun saveEvent(event: Event): Unit = error("Read-only fixture")
    override suspend fun deleteEvent(id: String): Unit = error("Read-only fixture")
}
