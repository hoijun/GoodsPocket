package goods.pocket.app.presentation.designsystem

import goods.pocket.app.presentation.navigation.AppDestination
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class GoodsPocketVisualContractTest {

    @Test
    fun `design md palette tokens stay aligned with the approved light theme`() {
        assertEquals(0xFFFFFCF8L, GoodsPocketVisualTokens.BACKGROUND)
        assertEquals(0xFFFFFFFFL, GoodsPocketVisualTokens.SURFACE)
        assertEquals(0xFFFFF5EEL, GoodsPocketVisualTokens.SURFACE_LOW)
        assertEquals(0xFFFFE8DCL, GoodsPocketVisualTokens.SURFACE_HIGH)
        assertEquals(0xFFFF7445L, GoodsPocketVisualTokens.PRIMARY)
        assertEquals(0xFF36C781L, GoodsPocketVisualTokens.SECONDARY)
        assertEquals(0xFF8F6EF2L, GoodsPocketVisualTokens.TERTIARY)
        assertEquals(0xFF202838L, GoodsPocketVisualTokens.INK)
        assertEquals(0xFF8A8F9BL, GoodsPocketVisualTokens.MUTED_INK)
        assertEquals(0xFFEAE2DCL, GoodsPocketVisualTokens.OUTLINE)
        assertEquals(0xFFFF7445L, GoodsPocketVisualTokens.DANGER)
    }

    @Test
    fun `primary destinations keep bottom navigation visible`() {
        AppDestination.primaryDestinations.forEach { destination ->
            val chrome = goodsPocketChromeFor(destination)
            assertTrue(
                chrome.showBottomBar,
                "bottom bar should stay visible for ${destination.route}",
            )
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
            assertFalse(
                chrome.showBottomBar,
                "bottom bar should be hidden for ${destination.route}",
            )
            assertFalse(
                chrome.showCenteredQuickAdd,
                "centered quick add should be hidden for ${destination.route}",
            )
        }
    }
}
