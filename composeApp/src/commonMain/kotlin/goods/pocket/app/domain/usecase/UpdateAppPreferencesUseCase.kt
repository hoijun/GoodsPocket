package goods.pocket.app.domain.usecase

import goods.pocket.app.domain.model.AppPreference
import goods.pocket.app.domain.repository.SettingsRepository

class UpdateAppPreferencesUseCase(
    private val settingsRepository: SettingsRepository,
) {
    operator fun invoke(preferences: AppPreference) {
        settingsRepository.updateAppPreferences(preferences)
    }
}
