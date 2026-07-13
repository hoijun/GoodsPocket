package goods.pocket.app.domain.repository

import goods.pocket.app.domain.model.AppPreference

interface SettingsRepository {
    suspend fun getAppPreferences(): AppPreference
    suspend fun updateAppPreferences(preferences: AppPreference)
}
