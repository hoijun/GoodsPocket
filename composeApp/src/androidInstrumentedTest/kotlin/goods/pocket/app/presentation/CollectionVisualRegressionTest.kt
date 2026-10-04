package goods.pocket.app.presentation

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toPixelMap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollTo
import androidx.test.ext.junit.runners.AndroidJUnit4
import goods.pocket.app.domain.collection.CollectionEntry
import goods.pocket.app.domain.collection.CollectionEntryStatus
import goods.pocket.app.domain.collection.ReservationDetails
import goods.pocket.app.presentation.collection.CollectionGoodsCard
import goods.pocket.app.presentation.collection.CollectionScreen
import goods.pocket.app.presentation.component.CollectionEntryDetailSheet
import goods.pocket.app.presentation.designsystem.GoodsPocketTheme
import goods.pocket.app.presentation.i18n.ProvideLocalizedResources
import goods.pocket.app.presentation.state.CollectionSegment
import java.util.Locale
import kotlin.math.roundToInt
import kotlin.test.assertTrue
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CollectionVisualRegressionTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()
    private val originalLocale = Locale.getDefault()
    private var density = 1f

    @After
    fun restoreLocale() = Locale.setDefault(originalLocale)

    @Test
    fun longReservationStoreDoesNotHideReleaseDate() {
        showCollection(listOf(reserved))
        val date = compose.onNodeWithText("2026-05-15", useUnmergedTree = true)
            .fetchSemanticsNode().boundsInRoot
        val store = compose.onNodeWithText("아주 긴 예약 판매처 이름 · 발매", useUnmergedTree = true)
            .fetchSemanticsNode().boundsInRoot
        assertTrue(date.top >= store.bottom, "Release date needs its own visible line")
        assertTrue(date.height > 0)
    }

    @Test
    fun mediaBottomCornerExposesCardSurfaceInsteadOfSquareGrayFill() {
        showCollection(listOf(reserved))
        val pixels = compose.onNode(hasText(reserved.name) and hasClickAction())
            .captureToImage().toPixelMap()
        val corner = pixels[(2 * density).roundToInt(), (133 * density).roundToInt()]
        assertTrue(corner.red > 0.95f, "Thumbnail bottom corner must be rounded: $corner")
    }

    @Test
    fun twoLineReservationNameKeepsFullBadgeHeight() {
        show {
            Box(Modifier.width(113.dp)) {
                CollectionGoodsCard(
                    entry = reserved.copy(name = "니지산지 애니버서리 배지 세트"),
                    metadata = "애니메이트 · 발매", secondaryMetadata = "2026-05-15",
                    statusLabel = "예약중", onClick = {},
                )
            }
        }
        val badge = compose.onNodeWithText("예약중", useUnmergedTree = true)
            .fetchSemanticsNode().boundsInRoot
        assertTrue(badge.height / density >= 10.5f, "Badge text must retain its 11sp line height: ${badge.height / density}")
    }

    @Test
    fun emptyCollectionOffersAddingInsteadOfSearchFailureCopy() {
        showCollection(emptyList())
        compose.onNodeWithText("아직 등록한 굿즈가 없어요. + 버튼으로 첫 굿즈를 추가해 보세요.")
            .assertExists()
        compose.onNodeWithText("검색어나 상태를 바꿔 보세요.", substring = true).assertDoesNotExist()
    }

    @Test
    fun noSearchResultsSuggestsChangingConditions() {
        showCollection(listOf(reserved), query = "없는 이름")
        compose.onNodeWithText("일치하는 굿즈가 없어요. 검색어나 상태를 바꿔 보세요.")
            .assertExists()
    }

    @Test
    fun reservedDetailUsesReservationTotalInsteadOfPurchasePrice() {
        show {
            CollectionEntryDetailSheet(
                entry = reserved,
                onDismiss = {}, onEdit = {}, onDelete = {}, onMarkReceived = {},
            )
        }
        compose.onNodeWithText("65,000원").performScrollTo().assertExists()
        compose.onNodeWithText("1,234원").assertDoesNotExist()
    }

    @Test
    fun unknownReservationTotalDoesNotBorrowAnUnrelatedPurchasePrice() {
        show {
            CollectionEntryDetailSheet(
                entry = reserved.copy(reservation = ReservationDetails()),
                onDismiss = {}, onEdit = {}, onDelete = {},
            )
        }
        compose.onNodeWithText("총 예약 금액").performScrollTo()
        compose.onNodeWithText("1,234원").assertDoesNotExist()
    }

    private fun showCollection(entries: List<CollectionEntry>, query: String = "") = show {
        Scaffold(contentWindowInsets = WindowInsets.safeDrawing) { padding ->
            Box(Modifier.padding(padding).consumeWindowInsets(padding)) {
                CollectionScreen(
                    entries = entries, selectedSegment = CollectionSegment.RESERVED, query = query,
                    isLoading = false, hasLoadFailure = false, hasLoaded = true,
                    onRetry = {}, onSegmentChange = {}, onQueryChange = {}, onEntryClick = {},
                )
            }
        }
    }

    private fun show(content: @Composable () -> Unit) {
        compose.setContent {
            GoodsPocketTheme {
                ProvideLocalizedResources("ko", "KRW", "yyyy-MM-dd") {
                    density = LocalDensity.current.density
                    content()
                }
            }
        }
    }

    private val reserved = CollectionEntry(
        id = "reservation", name = "예약 굿즈", category = "reserved",
        status = CollectionEntryStatus.RESERVED, reservationStore = "아주 긴 예약 판매처 이름",
        releaseDate = "2026-05-15", purchasePrice = 1234,
        reservation = ReservationDetails(totalPrice = 65000),
        createdAt = "2026-04-01T00:00:00Z", updatedAt = "2026-04-01T00:00:00Z",
    )
}
