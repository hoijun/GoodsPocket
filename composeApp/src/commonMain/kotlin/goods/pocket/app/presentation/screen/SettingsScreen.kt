package goods.pocket.app.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import goods.pocket.app.domain.model.AppPreference
import goods.pocket.app.domain.model.StorageLocation
import goods.pocket.app.presentation.i18n.AppLanguage
import goods.pocket.app.presentation.i18n.localizedLabel
import goods.pocket.app.presentation.i18n.tr
import goods.pocket.app.presentation.navigation.AppDestination
import goodspocket.composeapp.generated.resources.*

@Composable
fun SettingsScreen(
    storageLocations: List<StorageLocation>,
    appPreferences: AppPreference,
    onStartTabChange: (String) -> Unit,
    onLanguageChange: (String) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Text(
                        text = tr(Res.string.settings_app_preferences),
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Text(
                        text = tr(Res.string.settings_currency, appPreferences.currencyCode),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Text(
                        text = tr(Res.string.settings_date_format, appPreferences.dateFormat),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Text(
                        text = tr(Res.string.settings_language),
                        style = MaterialTheme.typography.titleSmall,
                    )
                    androidx.compose.foundation.layout.FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        FilterChip(
                            selected = appPreferences.languageCode == AppLanguage.KOREAN.code,
                            onClick = { onLanguageChange(AppLanguage.KOREAN.code) },
                            label = {
                                Text(tr(Res.string.settings_language_korean))
                            },
                        )
                        FilterChip(
                            selected = appPreferences.languageCode == AppLanguage.ENGLISH.code,
                            onClick = { onLanguageChange(AppLanguage.ENGLISH.code) },
                            label = {
                                Text(tr(Res.string.settings_language_english))
                            },
                        )
                    }
                    Text(
                        text = tr(Res.string.settings_default_start_tab),
                        style = MaterialTheme.typography.titleSmall,
                    )
                    androidx.compose.foundation.layout.FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        AppDestination.primaryDestinations.forEach { destination ->
                            FilterChip(
                                selected = appPreferences.startTabRoute == destination.route,
                                onClick = { onStartTabChange(destination.route) },
                                label = { Text(destination.localizedLabel()) },
                            )
                        }
                    }
                }
            }
        }
        item {
            Text(
                text = tr(Res.string.settings_storage_presets),
                style = MaterialTheme.typography.titleMedium,
            )
        }
        items(storageLocations, key = StorageLocation::id) { location ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Text(text = location.name, style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = location.memo ?: tr(Res.string.settings_storage_preset_fallback),
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
        }
    }
}
