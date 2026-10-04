package goods.pocket.app.presentation.home

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
import goods.pocket.app.domain.collection.CollectionEntry
import goods.pocket.app.domain.collection.CollectionEntryStatus
import goods.pocket.app.domain.collection.CollectionRepository
import goods.pocket.app.domain.dashboard.GetDashboardSummaryUseCase
import goods.pocket.app.domain.dashboard.GetRecentActivitiesUseCase
import goods.pocket.app.domain.event.Event
import goods.pocket.app.domain.event.EventRepository
import kotlinx.coroutines.flow.flowOf
import goods.pocket.app.domain.collection.ReservationResult
import goods.pocket.app.domain.service.AppClock
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
class HomeFeedbackTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private val repository = HomeFeedbackRepository()
    private val originalLocale = Locale.getDefault()

    @After
    fun closeFixture() {
        scope.cancel()
        repository.updates.close()
        Locale.setDefault(originalLocale)
    }

    @Test
    fun failureIsBelowHeroAndDoesNotShowInventedZeroSummary() {
        showHome()
        repository.updates.trySend(Result.failure(IllegalStateException("Fixture failure")))
        awaitFailure()
        val hero = compose.onNodeWithText("새로운 굿즈와 추억을\n기록해보세요.")
            .fetchSemanticsNode().boundsInRoot
        val feedback = compose.onNode(errorMessage).fetchSemanticsNode().boundsInRoot
        assertTrue(feedback.top >= hero.bottom, "Error overlaps Home content: $feedback / $hero")
        compose.onNodeWithText("오늘의 수집 요약").assertDoesNotExist()
        compose.onNodeWithText("이번 달 지출 요약").assertDoesNotExist()
    }

    @Test
    fun retryKeepsUnknownContentHiddenUntilSuccessfulEmptyResult() {
        showHome()
        compose.onNodeWithText("오늘의 수집 요약").assertDoesNotExist()
        repository.updates.trySend(Result.failure(IllegalStateException("Fixture failure")))
        awaitFailure()
        compose.onNodeWithText("다시 시도").performScrollTo().performClick()
        compose.onNode(errorMessage).assertDoesNotExist()
        compose.onNodeWithText("오늘의 수집 요약").assertDoesNotExist()
        repository.updates.trySend(Result.success(emptyList()))
        compose.waitUntil(5_000) {
            compose.onAllNodes(hasText("오늘의 수집 요약")).fetchSemanticsNodes().isNotEmpty()
        }
    }

    @Test
    fun enlargedEnglishFailureKeepsRetryReachable() {
        showHome(language = "en", fontScale = 2f)
        repository.updates.trySend(Result.failure(IllegalStateException("Fixture failure")))
        compose.waitUntil(5_000) {
            compose.onAllNodes(hasText("Couldn’t load Home")).fetchSemanticsNodes().isNotEmpty()
        }
        compose.onNodeWithText("Retry").performScrollTo().assertIsDisplayed().performClick()
        compose.onNodeWithText("Couldn’t load Home").assertDoesNotExist()
    }

    @Test
    fun laterFailureAndRetryRetainPreviouslyLoadedSummary() {
        showHome()
        repository.updates.trySend(Result.success(emptyList()))
        compose.waitUntil(5_000) {
            compose.onAllNodes(hasText("오늘의 수집 요약")).fetchSemanticsNodes().isNotEmpty()
        }
        repository.updates.trySend(Result.failure(IllegalStateException("Later read failure")))
        awaitFailure()
        compose.onNodeWithText("오늘의 수집 요약").assertExists()
        compose.onNodeWithText("다시 시도").performScrollTo().performClick()
        compose.onNodeWithText("오늘의 수집 요약").assertExists()
    }

    private fun awaitFailure() {
        compose.waitUntil(5_000) {
            compose.onAllNodes(errorMessage).fetchSemanticsNodes().isNotEmpty()
        }
    }

    @Test
    fun recentGoodsShowsItsSeriesInsteadOfGenericActivityCopy() {
        showHome()
        repository.updates.trySend(
            Result.success(
                listOf(
                    CollectionEntry(
                        id = "fixture", name = "테스트 피규어", category = "goods",
                        status = CollectionEntryStatus.OWNED, seriesName = "테스트 시리즈",
                        createdAt = "2026-10-04T03:00:00Z", updatedAt = "2026-10-04T03:00:00Z",
                    ),
                ),
            ),
        )
        compose.waitUntil(5_000) {
            compose.onAllNodes(hasText("최근 추가한 굿즈")).fetchSemanticsNodes().isNotEmpty()
        }
        compose.onNodeWithText("최근 추가한 굿즈").performScrollTo()
        compose.onNodeWithText("테스트 시리즈").assertExists()
        compose.onNodeWithText("컬렉션 굿즈 추가").assertDoesNotExist()
    }

    private fun showHome(language: String = "ko", fontScale: Float = 1f) {
        val clock = object : AppClock {
            override fun currentDate(): String = "2026-10-04"
            override fun currentTimestamp(): String = "2026-10-04T03:00:00Z"
        }
        val events = object : EventRepository {
            override fun observeEvents() = flowOf(emptyList<Event>())
            override suspend fun getEvents(): List<Event> = emptyList()
            override suspend fun saveEvent(event: Event): Unit = error("Read-only fixture")
            override suspend fun deleteEvent(id: String): Unit = error("Read-only fixture")
        }
        val holder = HomeStateHolder(
            repository, events, clock, scope,
            dashboard = GetDashboardSummaryUseCase(repository),
            activities = GetRecentActivitiesUseCase(repository),
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
                                HomeRoute(holder) {}
                            }
                        }
                    }
                }
            }
        }
    }

    private val errorMessage = hasText("작업을 완료하지 못했어요. 다시 시도해 주세요.") or
        hasText("홈 정보를 불러오지 못했어요")
}

private class HomeFeedbackRepository : CollectionRepository {
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
