package goods.pocket.app.domain.settings

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun observePreferences(): Flow<AppPreference>
    suspend fun getAppPreferences(): AppPreference
    suspend fun updateAppPreferences(preferences: AppPreference)
}
