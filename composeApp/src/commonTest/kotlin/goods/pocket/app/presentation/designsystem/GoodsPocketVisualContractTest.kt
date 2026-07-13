package goods.pocket.app.presentation.designsystem

import goods.pocket.app.presentation.navigation.AppDestination
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class GoodsPocketVisualContractTest {

    @Test
    fun `design md palette tokens stay aligned with the approved light theme`() {
        assertEquals(0xFFFFFCF8L, GoodsPocketVisualTokens.Background)
        assertEquals(0xFFFFFFFFL, GoodsPocketVisualTokens.Surface)
        assertEquals(0xFFFFF5EEL, GoodsPocketVisualTokens.SurfaceLow)
        assertEquals(0xFFFFE8DCL, GoodsPocketVisualTokens.SurfaceHigh)
        assertEquals(0xFFFF7445L, GoodsPocketVisualTokens.Primary)
        assertEquals(0xFF36C781L, GoodsPocketVisualTokens.Secondary)
        assertEquals(0xFF8F6EF2L, GoodsPocketVisualTokens.Tertiary)
        assertEquals(0xFF202838L, GoodsPocketVisualTokens.Ink)
        assertEquals(0xFF8A8F9BL, GoodsPocketVisualTokens.MutedInk)
        assertEquals(0xFFEAE2DCL, GoodsPocketVisualTokens.Outline)
        assertEquals(0xFFFF7445L, GoodsPocketVisualTokens.Danger)
    }

    @Test
    fun `primary destinations keep bottom navigation visible`() {
        AppDestination.primaryDestinations.forEach { destination ->
            val chrome = goodsPocketChromeFor(destination)
            assertTrue(chrome.showBottomBar, "bottom bar should stay visible for ${destination.route}")
            assertTrue(
                chrome.showCenteredQuickAdd,
                "centered quick add should stay visible for ${destination.route}",
            )
        }
    }

    @Test
    fun `secondary destinations hide bottom navigation and centered quick add`() {
        listOf(
            AppDestination.Settings,
        ).forEach { destination ->
            val chrome = goodsPocketChromeFor(destination)
            assertFalse(chrome.showBottomBar, "bottom bar should be hidden for ${destination.route}")
            assertFalse(
                chrome.showCenteredQuickAdd,
                "centered quick add should be hidden for ${destination.route}",
            )
        }
    }
}
