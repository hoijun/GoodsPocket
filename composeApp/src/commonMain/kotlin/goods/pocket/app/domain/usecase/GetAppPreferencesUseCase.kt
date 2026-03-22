package goods.pocket.app.domain.usecase

import goods.pocket.app.domain.model.AppPreference
import goods.pocket.app.domain.repository.SettingsRepository

class GetAppPreferencesUseCase(
    private val settingsRepository: SettingsRepository,
) {
    operator fun invoke(): AppPreference = settingsRepository.getAppPreferences()
}
