package goods.pocket.app.i18n

import kotlin.test.Test
import kotlin.test.assertEquals

class GoodsPocketCopyTest {

    @Test
    fun `currency formatting honors currency and language preferences`() {
        assertEquals("12,000원", formatCurrencyByPreference(12_000, "KRW", "ko"))
        assertEquals("12,000 KRW", formatCurrencyByPreference(12_000, "KRW", "en"))
        assertEquals("${'$'}12,000", formatCurrencyByPreference(12_000, "USD", "en"))
    }

    @Test
    fun `date formatting honors supported preference patterns`() {
        assertEquals("2026-07-13", formatDateByPreference("2026-07-13", "yyyy-MM-dd"))
        assertEquals("07/13/2026", formatDateByPreference("2026-07-13", "MM/dd/yyyy"))
        assertEquals("13/07/2026", formatDateByPreference("2026-07-13", "dd/MM/yyyy"))
        assertEquals("not-a-date", formatDateByPreference("not-a-date", "MM/dd/yyyy"))
    }
}
