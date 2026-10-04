package goods.pocket.app.presentation

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.toPixelMap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import goods.pocket.app.domain.event.Event
import goods.pocket.app.domain.event.EventType
import goods.pocket.app.presentation.component.EventDetailSheet
import goods.pocket.app.presentation.designsystem.GoodsPocketImageLockedBottomBar
import goods.pocket.app.presentation.designsystem.GoodsPocketTheme
import goods.pocket.app.presentation.i18n.ProvideLocalizedResources
import goods.pocket.app.presentation.navigation.AppDestination
import kotlin.math.roundToInt
import kotlin.test.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EventsVisualRegressionTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()
    private var density = 1f

    @Test
    fun selectedCalendarHasSolidOrangeBody() {
        show {
            GoodsPocketImageLockedBottomBar(AppDestination.Events, {}, {})
        }
        val pixels = compose.onNode(hasText("이벤트") and hasClickAction())
            .captureToImage().toPixelMap()
        val body = pixels[pixels.width / 2, (26 * density).roundToInt()]
        assertTrue(body.red > 0.9f && body.green < 0.6f && body.blue < 0.4f,
            "Selected calendar interior must be orange: $body")
    }

    @Test
    fun detailCloseGlyphIsCompactAndKeepsItsTouchTarget() {
        var dismissed = false
        show {
            EventDetailSheet(
                event = Event("event", "결제 마감", EventType.PAYMENT_DUE, "2026-04-01",
                    createdAt = "2026-04-01T00:00:00Z", updatedAt = "2026-04-01T00:00:00Z"),
                onDismiss = { dismissed = true }, onEdit = {}, onDelete = {},
            )
        }
        val close = compose.onNodeWithContentDescription("닫기")
        val bounds = close.fetchSemanticsNode().boundsInRoot
        assertTrue(bounds.width / density >= 39.5f, "IconButton visual container must not shrink: ${bounds.width / density}")
        val pixels = close.captureToImage().toPixelMap()
        val inkXs = mutableListOf<Int>()
        for (y in 0 until pixels.height) for (x in 0 until pixels.width) {
            val color = pixels[x, y]
            if (color.red < 0.75f && color.green < 0.75f && color.blue < 0.8f) inkXs.add(x)
        }
        val inkWidth = (inkXs.max() - inkXs.min() + 1) / density
        assertTrue(inkWidth in 14f..17f, "Reference close glyph spans about 15dp: $inkWidth")
        close.performClick()
        compose.runOnIdle { assertTrue(dismissed) }
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
}
