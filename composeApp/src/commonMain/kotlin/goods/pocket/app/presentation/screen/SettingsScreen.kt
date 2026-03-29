package goods.pocket.app.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import goods.pocket.app.domain.model.AppPreference
import goods.pocket.app.presentation.designsystem.GoodsPocketFilterChip
import goods.pocket.app.presentation.designsystem.GoodsPocketSectionCard
import goods.pocket.app.presentation.designsystem.GoodsPocketSectionHeader
import goods.pocket.app.presentation.designsystem.goodsPocketScreenModifier
import goods.pocket.app.presentation.designsystem.goodsPocketSecondaryScrollContentPadding
import goods.pocket.app.presentation.i18n.AppLanguage
import goods.pocket.app.presentation.i18n.tr
import goodspocket.composeapp.generated.resources.Res
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
) {
    LazyColumn(
        modifier = goodsPocketScreenModifier(),
        contentPadding = goodsPocketSecondaryScrollContentPadding(),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        item {
            GoodsPocketSectionCard(containerColor = MaterialTheme.colorScheme.surface) {
                GoodsPocketSectionHeader(
                    title = tr(Res.string.settings_quick_preferences),
                )
                Text(
                    text = tr(Res.string.settings_language),
                    style = MaterialTheme.typography.labelLarge,
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    GoodsPocketFilterChip(
                        selected = appPreferences.languageCode == AppLanguage.KOREAN.code,
                        onClick = { onLanguageChange(AppLanguage.KOREAN.code) },
                        label = tr(Res.string.settings_language_korean),
                    )
                    GoodsPocketFilterChip(
                        selected = appPreferences.languageCode == AppLanguage.ENGLISH.code,
                        onClick = { onLanguageChange(AppLanguage.ENGLISH.code) },
                        label = tr(Res.string.settings_language_english),
                    )
                }
            }
        }
        item {
            GoodsPocketSectionCard(containerColor = MaterialTheme.colorScheme.surface) {
                GoodsPocketSectionHeader(
                    title = tr(Res.string.settings_display_format),
                )
                Text(
                    text = tr(Res.string.settings_currency, appPreferences.currencyCode),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = tr(Res.string.settings_date_format, appPreferences.dateFormat),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
