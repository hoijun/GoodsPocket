package goods.pocket.app.presentation.i18n

import java.util.Locale
import kotlin.test.Test
import kotlin.test.assertEquals

class ResourceLocaleTest {
    @Test
    fun storedLanguageOverridesEnglishSystemLocaleBeforeResourceLookup() {
        val original = Locale.getDefault()
        try {
            Locale.setDefault(Locale.US)
            assertEquals("ko", applyResourceLocale("ko"))
            assertEquals("ko", Locale.getDefault().language)
            assertEquals("en", applyResourceLocale("en"))
            assertEquals("en", Locale.getDefault().language)
            applyResourceLocale("ko")
            assertEquals("ko", Locale.getDefault().language)
        } finally {
            Locale.setDefault(original)
        }
    }
}
