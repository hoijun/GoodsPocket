package goods.pocket.app.presentation.designsystem

import goods.pocket.app.presentation.navigation.AppDestination
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class GoodsPocketVisualContractTest {

    @Test
    fun `design md palette tokens stay aligned with the approved light theme`() {
        assertEquals(0xFFFFF9FCL, GoodsPocketVisualTokens.Background)
        assertEquals(0xFFFFFFFFL, GoodsPocketVisualTokens.Surface)
        assertEquals(0xFFF8F2F7L, GoodsPocketVisualTokens.SurfaceLow)
        assertEquals(0xFFEDE6EEL, GoodsPocketVisualTokens.SurfaceHigh)
        assertEquals(0xFFF26CA7L, GoodsPocketVisualTokens.Primary)
        assertEquals(0xFF87DCCBL, GoodsPocketVisualTokens.Secondary)
        assertEquals(0xFFFFD46FL, GoodsPocketVisualTokens.Tertiary)
        assertEquals(0xFF2B2530L, GoodsPocketVisualTokens.Ink)
        assertEquals(0xFF665D6DL, GoodsPocketVisualTokens.MutedInk)
        assertEquals(0xFFE2D5E3L, GoodsPocketVisualTokens.Outline)
        assertEquals(0xFFE07A94L, GoodsPocketVisualTokens.Danger)
    }

    @Test
    fun `primary destinations keep bottom navigation visible`() {
        AppDestination.primaryDestinations.forEach { destination ->
            val chrome = goodsPocketChromeFor(destination)
            assertTrue(chrome.showBottomBar, "bottom bar should stay visible for ${destination.route}")
        }
    }

    @Test
    fun `secondary destinations hide bottom navigation and floating action button`() {
        listOf(
            AppDestination.Transactions,
            AppDestination.Events,
            AppDestination.Settings,
        ).forEach { destination ->
            val chrome = goodsPocketChromeFor(destination)
            assertFalse(chrome.showBottomBar, "bottom bar should be hidden for ${destination.route}")
            assertFalse(chrome.showFab, "fab should be hidden for ${destination.route}")
        }
    }

    @Test
    fun `my keeps the bottom bar but does not show the add button`() {
        val chrome = goodsPocketChromeFor(AppDestination.My)

        assertTrue(chrome.showBottomBar)
        assertFalse(chrome.showFab)
    }
}
