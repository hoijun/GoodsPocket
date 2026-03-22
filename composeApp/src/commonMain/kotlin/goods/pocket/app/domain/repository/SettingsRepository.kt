package goods.pocket.app.domain.repository

import goods.pocket.app.domain.model.AppPreference
import goods.pocket.app.domain.model.StorageLocation

interface SettingsRepository {
    fun getStorageLocations(): List<StorageLocation>
    fun saveStorageLocation(location: StorageLocation)
    fun deleteStorageLocation(id: String)
    fun getAppPreferences(): AppPreference
    fun updateAppPreferences(preferences: AppPreference)
}
