package goods.pocket.app.presentation.designsystem

import goods.pocket.app.presentation.navigation.AppDestination
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class GoodsPocketVisualContractTest {

    @Test
    fun `stitch palette tokens stay aligned with the approved light theme`() {
        assertEquals(0xFFF6FAFBL, GoodsPocketVisualTokens.Background)
        assertEquals(0xFFFFFFFFL, GoodsPocketVisualTokens.Surface)
        assertEquals(0xFFEFF5F6L, GoodsPocketVisualTokens.SurfaceLow)
        assertEquals(0xFF1E6876L, GoodsPocketVisualTokens.Primary)
        assertEquals(0xFF2A3437L, GoodsPocketVisualTokens.Ink)
        assertEquals(0xFFA83836L, GoodsPocketVisualTokens.Danger)
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
