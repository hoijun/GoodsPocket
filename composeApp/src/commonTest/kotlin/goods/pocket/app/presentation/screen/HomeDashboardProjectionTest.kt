package goods.pocket.app.presentation.screen

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class HomeDashboardProjectionTest {
    @Test
    fun `home month is parsed from the injected current date`() {
        assertEquals(HomeMonth(year = 2026, month = 7), homeMonth("2026-07-26"))
        assertNull(homeMonth("not-a-date"))
    }

    @Test
    fun `d day label is derived from current and target dates`() {
        assertEquals("D-3", dDayLabel(today = "2026-07-26", targetDate = "2026-07-29"))
        assertEquals("D-day", dDayLabel(today = "2026-07-26", targetDate = "2026-07-26"))
        assertEquals("D+2", dDayLabel(today = "2026-07-26", targetDate = "2026-07-24"))
        assertNull(dDayLabel(today = "2026-07-26", targetDate = "not-a-date"))
    }

    @Test
    fun `spending change is omitted without a previous month basis`() {
        assertNull(spendingChangePercent(current = 10_000, previous = 0))
        assertEquals(25, spendingChangePercent(current = 12_500, previous = 10_000))
        assertEquals(-25, spendingChangePercent(current = 7_500, previous = 10_000))
    }

    @Test
    fun `nine spending buckets normalize into the fixed chart range`() {
        assertEquals(
            listOf(1f, 0.59f, 0.18f, 0.18f, 0.18f, 0.18f, 0.18f, 0.18f, 0.18f),
            spendingBarFractions(listOf(100L, 50L, 0L, 0L, 0L, 0L, 0L, 0L, 0L)),
        )
        assertEquals(List(9) { 0.18f }, spendingBarFractions(List(9) { 0L }))
    }
}
