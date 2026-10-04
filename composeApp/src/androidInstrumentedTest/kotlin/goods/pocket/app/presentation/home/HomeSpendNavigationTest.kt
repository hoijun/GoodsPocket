package goods.pocket.app.presentation.home

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.toPixelMap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.Density
import androidx.test.ext.junit.runners.AndroidJUnit4
import goods.pocket.app.domain.dashboard.HomeSummary
import goods.pocket.app.presentation.designsystem.GoodsPocketCenteredQuickAddButton
import goods.pocket.app.presentation.designsystem.GoodsPocketImageLockedBottomBar
import goods.pocket.app.presentation.designsystem.GoodsPocketTheme
import goods.pocket.app.presentation.i18n.ProvideLocalizedResources
import goods.pocket.app.presentation.navigation.AppDestination
import java.util.Locale
import kotlin.math.abs
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalTestApi::class)
@RunWith(AndroidJUnit4::class)
class HomeSpendNavigationTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()
    private val originalLocale = Locale.getDefault()
    private var density = 1f
    private var clicks = 0

    @After
    fun restoreLocale() = Locale.setDefault(originalLocale)

    @Test
    fun spendingTextHasReferenceHierarchyInsteadOfOnePixelGaps() {
        showContent()
        val title = compose.onNodeWithText("이번 달 지출 요약").fetchSemanticsNode().boundsInRoot
        val month = compose.onNodeWithText("2024년 5월").fetchSemanticsNode().boundsInRoot
        val amount = compose.onNodeWithText("243,600원").fetchSemanticsNode().boundsInRoot
        val change = compose.onNodeWithText("지난 달 대비").fetchSemanticsNode().boundsInRoot
        assertTrue(abs((month.top - title.bottom) / density - 9f) <= 1f)
        assertTrue(abs((amount.top - month.bottom) / density - 4f) <= 1f)
        assertTrue(abs((change.top - amount.bottom) / density - 4f) <= 1f)
    }

    @Test
    fun quickAddHasReferenceSizePlusAndKeepsClickAction() {
        showContent()
        assertPlusSizeAndClick()
    }

    @Test
    fun quickAddGlyphDoesNotGrowWithSystemText() {
        showContent(fontScale = 2f)
        assertPlusSizeAndClick()
    }

    @Test
    fun quickAddIsCenteredWithinNavigationControls() {
        assertNavigationCenter(fontScale = 1f)
    }

    @Test
    fun quickAddStaysCenteredWhenNavigationLabelsGrow() {
        assertNavigationCenter(fontScale = 2f)
    }

    private fun assertNavigationCenter(fontScale: Float) {
        showContent(fontScale = fontScale, navigationOnly = true)
        val tab = compose.onNode(hasText("홈") and hasClickAction())
            .fetchSemanticsNode().boundsInRoot
        val quickAdd = compose.onNodeWithContentDescription("빠른 추가")
            .fetchSemanticsNode().boundsInRoot
        assertTrue(
            abs(quickAdd.center.y - tab.center.y) / density <= 1f,
            "Quick add must share the tabs' vertical center: $quickAdd / $tab",
        )
        compose.onNodeWithContentDescription("빠른 추가").performClick()
        compose.runOnIdle { assertEquals(1, clicks) }
    }

    private fun assertPlusSizeAndClick() {
        val button = compose.onNodeWithContentDescription("빠른 추가")
        val pixels = button.captureToImage().toPixelMap()
        val points = buildList {
            for (y in pixels.height / 4 until pixels.height * 3 / 4) {
                for (x in pixels.width / 4 until pixels.width * 3 / 4) {
                    val color = pixels[x, y]
                    if (color.red > 0.9f && color.green > 0.9f && color.blue > 0.9f) add(x to y)
                }
            }
        }
        assertTrue(points.isNotEmpty())
        val width = (points.maxOf { it.first } - points.minOf { it.first } + 1) / density
        val height = (points.maxOf { it.second } - points.minOf { it.second } + 1) / density
        assertTrue(width in 14f..17f && height in 14f..17f, "Plus is $width x $height dp")
        button.performClick()
        compose.runOnIdle { assertEquals(1, clicks) }
    }

    private fun showContent(fontScale: Float = 1f, navigationOnly: Boolean = false) {
        compose.setContent {
            GoodsPocketTheme {
                ProvideLocalizedResources("ko", "KRW", "yyyy-MM-dd") {
                    density = LocalDensity.current.density
                    CompositionLocalProvider(LocalDensity provides Density(density, fontScale)) {
                        if (navigationOnly) {
                            GoodsPocketImageLockedBottomBar(
                                selectedPrimaryDestination = AppDestination.Home,
                                onSelectDestination = {},
                                onQuickAdd = { clicks++ },
                            )
                        } else {
                            Column {
                                HomeMonthlySpendCard(
                                    HomeSummary(243600, 217500, List(9) { 1L }, 0, 0, emptyList()),
                                    "2024-05-18",
                                )
                                GoodsPocketCenteredQuickAddButton(onClick = { clicks++ })
                            }
                        }
                    }
                }
            }
        }
    }
}
