package goods.pocket.app.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import goods.pocket.app.domain.model.AppPreference
import goods.pocket.app.presentation.designsystem.GoodsPocketVisualTokens
import goods.pocket.app.presentation.i18n.AppLanguage
import goods.pocket.app.presentation.i18n.tr
import goodspocket.composeapp.generated.resources.Res
import goodspocket.composeapp.generated.resources.action_back
import goodspocket.composeapp.generated.resources.nav_settings
import goodspocket.composeapp.generated.resources.settings_currency
import goodspocket.composeapp.generated.resources.settings_date_format
import goodspocket.composeapp.generated.resources.settings_display_format
import goodspocket.composeapp.generated.resources.settings_language
import goodspocket.composeapp.generated.resources.settings_language_english
import goodspocket.composeapp.generated.resources.settings_language_korean
import goodspocket.composeapp.generated.resources.settings_quick_preferences

@Composable
fun SettingsScreen(
    appPreferences: AppPreference,
    onLanguageChange: (String) -> Unit,
    onBack: () -> Unit,
) {
    val koreanCode = AppLanguage.KOREAN.code
    val englishCode = AppLanguage.ENGLISH.code

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(GoodsPocketVisualTokens.Background)),
        contentPadding = PaddingValues(bottom = SettingsReferenceMetrics.BottomContentPadding),
    ) {
        item {
            SettingsPageHeader(
                title = tr(Res.string.nav_settings),
                backLabel = tr(Res.string.action_back),
                onBack = onBack,
            )
        }
        item {
            SettingsSectionHeader(
                title = tr(Res.string.settings_quick_preferences),
                topSpacing = SettingsReferenceMetrics.HeaderToFirstSectionSpacing,
            )
        }
        item {
            SettingsLanguageCard(
                label = tr(Res.string.settings_language),
                koreanLabel = tr(Res.string.settings_language_korean),
                englishLabel = tr(Res.string.settings_language_english),
                selectedLanguageCode = appPreferences.languageCode,
                koreanCode = koreanCode,
                englishCode = englishCode,
                onLanguageChange = onLanguageChange,
            )
        }
        item {
            SettingsSectionHeader(
                title = tr(Res.string.settings_display_format),
                topSpacing = SettingsReferenceMetrics.LanguageCardToFormatTitleSpacing,
            )
        }
        item {
            SettingsFormatCard(
                currency = tr(Res.string.settings_currency, appPreferences.currencyCode),
                dateFormat = tr(Res.string.settings_date_format, appPreferences.dateFormat),
            )
        }
    }
}
