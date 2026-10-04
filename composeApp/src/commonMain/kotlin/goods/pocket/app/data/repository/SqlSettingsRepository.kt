package goods.pocket.app.data.repository

import goods.pocket.app.data.local.GoodsPocketStore
import goods.pocket.app.db.App_preference
import goods.pocket.app.domain.settings.AppPreference
import goods.pocket.app.domain.settings.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SqlSettingsRepository(private val store: GoodsPocketStore) : SettingsRepository {
    override fun observePreferences(): Flow<AppPreference> =
        store.observe({ it.goodsPocketDatabaseQueries.selectPreferences() }, ::toDomain)
            .map { it.firstOrNull() ?: AppPreference() }

    override suspend fun getAppPreferences(): AppPreference = store.read {
        it.goodsPocketDatabaseQueries.selectPreferences().executeAsOneOrNull()?.let(::toDomain)
            ?: AppPreference()
    }

    override suspend fun updateAppPreferences(preferences: AppPreference): Unit = store.write {
        it.goodsPocketDatabaseQueries.upsertPreferences(
            currency_code = preferences.currencyCode,
            date_format = preferences.dateFormat,
            language_code = preferences.languageCode,
        )
    }
}

private fun toDomain(row: App_preference): AppPreference =
    AppPreference(row.currency_code, row.date_format, row.language_code)
