package goods.pocket.app.presentation.i18n

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import java.util.Locale

@Composable
actual fun ApplyAppLanguage(languageCode: String): String = remember(languageCode) {
    applyResourceLocale(languageCode)
}

internal fun applyResourceLocale(languageCode: String): String {
    // Compose Resources reads the default locale before rendering the keyed content.
    Locale.setDefault(Locale.forLanguageTag(languageCode))
    return languageCode
}
