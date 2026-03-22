package goods.pocket.app.presentation.i18n

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import platform.Foundation.NSUserDefaults

@Composable
actual fun ApplyAppLanguage(languageCode: String): String {
    return remember(languageCode) {
        NSUserDefaults.standardUserDefaults.setObject(
            value = listOf(languageCode),
            forKey = "AppleLanguages",
        )
        NSUserDefaults.standardUserDefaults.synchronize()
        languageCode
    }
}
