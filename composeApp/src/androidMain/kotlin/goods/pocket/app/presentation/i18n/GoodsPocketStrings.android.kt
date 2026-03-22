package goods.pocket.app.presentation.i18n

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

@Composable
actual fun ApplyAppLanguage(languageCode: String): String {
    SideEffect {
        val localeList = LocaleListCompat.forLanguageTags(languageCode)
        if (AppCompatDelegate.getApplicationLocales() != localeList) {
            AppCompatDelegate.setApplicationLocales(localeList)
        }
    }
    return languageCode
}
