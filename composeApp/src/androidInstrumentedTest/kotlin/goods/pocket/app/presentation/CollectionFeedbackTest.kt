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
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.unit.Density
import androidx.test.ext.junit.runners.AndroidJUnit4
import goods.pocket.app.domain.collection.CollectionEntry
import goods.pocket.app.domain.collection.CollectionRepository
import goods.pocket.app.domain.collection.MarkPreorderReceivedUseCase
import goods.pocket.app.domain.collection.ReservationResult
import goods.pocket.app.domain.service.AppClock
import goods.pocket.app.domain.service.IdGenerator
import goods.pocket.app.presentation.collection.CollectionRoute
import goods.pocket.app.presentation.collection.CollectionStateHolder
import goods.pocket.app.presentation.designsystem.GoodsPocketTheme
import goods.pocket.app.presentation.i18n.ProvideLocalizedResources
import java.util.Locale
import kotlin.test.assertTrue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CollectionFeedbackTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private val repository = FeedbackRepository()
    private val originalLocale = Locale.getDefault()

    @After
    fun closeFixture() {
        scope.cancel()
        repository.updates.close()
        Locale.setDefault(originalLocale)
    }

    @Test
    fun initialFailureIsBelowSearchAndDoesNotPretendCollectionIsEmpty() {
        showCollection()
        repository.updates.trySend(Result.failure(IllegalStateException("Fixture failure")))
        compose.waitUntil(5_000) {
            compose.onAllNodes(errorMessage).fetchSemanticsNodes().isNotEmpty()
        }
        val search = compose.onNode(hasSetTextAction()).fetchSemanticsNode().boundsInRoot
        val feedback = compose.onNode(errorMessage).fetchSemanticsNode().boundsInRoot
        assertTrue(
            feedback.top >= search.bottom,
            "Read error must be below search: $feedback / $search",
        )
        compose.onNodeWithText("현재 조건과 일치하는 굿즈가 없어요.").assertDoesNotExist()
        compose.onNodeWithText("총 0개").assertDoesNotExist()
    }

    @Test
    fun retryReplacesFailureWithLoadingThenShowsSuccessfulEmptyResult() {
        showCollection()
        repository.updates.trySend(Result.failure(IllegalStateException("Fixture failure")))
        compose.waitUntil(5_000) {
            compose.onAllNodes(errorMessage).fetchSemanticsNodes().isNotEmpty()
        }
        compose.onNodeWithText("다시 시도").performClick()
        compose.onNode(errorMessage).assertDoesNotExist()
        compose.onNodeWithText("현재 조건과 일치하는 굿즈가 없어요.").assertDoesNotExist()
        repository.updates.trySend(Result.success(emptyList()))
        compose.waitUntil(5_000) {
            compose.onAllNodes(hasText("현재 조건과 일치하는 굿즈가 없어요."))
                .fetchSemanticsNodes().isNotEmpty()
        }
        compose.onNodeWithText("현재 조건과 일치하는 굿즈가 없어요.").assertExists()
    }

    @Test
    fun enlargedEnglishFailureKeepsRetryReachable() {
        showCollection(language = "en", fontScale = 2f)
        repository.updates.trySend(Result.failure(IllegalStateException("Fixture failure")))
        compose.waitUntil(5_000) {
            compose.onAllNodes(hasText("Couldn’t load your collection"))
                .fetchSemanticsNodes().isNotEmpty()
        }
        compose.onNodeWithText("Retry").performScrollTo().assertIsDisplayed().performClick()
        compose.onNodeWithText("Couldn’t load your collection").assertDoesNotExist()
    }

    @Test
    fun searchWithKeyboardKeepsRetryReachableAndRetainsQuery() {
        showCollection()
        repository.updates.trySend(Result.failure(IllegalStateException("Fixture failure")))
        compose.waitUntil(5_000) {
            compose.onAllNodes(errorMessage).fetchSemanticsNodes().isNotEmpty()
        }
        compose.onNode(hasSetTextAction()).performTextInput("fixture query")
        compose.onNodeWithText("다시 시도").performScrollTo().assertIsDisplayed().performClick()
        compose.onNode(hasSetTextAction() and hasText("fixture query")).assertExists()
    }

    private fun showCollection(language: String = "ko", fontScale: Float = 1f) {
        val clock = object : AppClock {
            override fun currentDate(): String = "2026-10-04"
            override fun currentTimestamp(): String = "2026-10-04T03:00:00Z"
        }
        val holder = CollectionStateHolder(
            repository,
            MarkPreorderReceivedUseCase(repository, clock),
            clock,
            object : IdGenerator {
                override fun generate(prefix: String): String = error("Read-only fixture")
            },
            scope,
        )
        compose.setContent {
            GoodsPocketTheme {
                ProvideLocalizedResources(language, "KRW", "yyyy-MM-dd") {
                    val density = LocalDensity.current
                    CompositionLocalProvider(
                        LocalDensity provides Density(density.density, fontScale),
                    ) {
                        Scaffold(contentWindowInsets = WindowInsets.safeDrawing) { padding ->
                            Box(Modifier.padding(padding).consumeWindowInsets(padding)) {
                                CollectionRoute(holder) {}
                            }
                        }
                    }
                }
            }
        }
    }

    private val errorMessage = hasText("작업을 완료하지 못했어요. 다시 시도해 주세요.") or
        hasText("컬렉션을 불러오지 못했어요")
}

private class FeedbackRepository : CollectionRepository {
    val updates = Channel<Result<List<CollectionEntry>>>(Channel.UNLIMITED)
    override fun observeEntries(): Flow<List<CollectionEntry>> =
        updates.receiveAsFlow().map { it.getOrThrow() }
    override fun observeAllEntries(): Flow<List<CollectionEntry>> = observeEntries()
    override suspend fun getEntries(): List<CollectionEntry> = emptyList()
    override suspend fun getAllEntries(): List<CollectionEntry> = emptyList()
    override suspend fun getEntry(id: String): CollectionEntry? = null
    override suspend fun saveEntry(entry: CollectionEntry): Unit = error("Read-only fixture")
    override suspend fun deleteEntry(id: String): Unit = error("Read-only fixture")
    override suspend fun cancelReservation(id: String, canceledAt: String): ReservationResult =
        error("Read-only fixture")
    override suspend fun receiveReservation(
        id: String,
        receivedAt: String,
        receivedDate: String,
    ): ReservationResult = error("Read-only fixture")
}
