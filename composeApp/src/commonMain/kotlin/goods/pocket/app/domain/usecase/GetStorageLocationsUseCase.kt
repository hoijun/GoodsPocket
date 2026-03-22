package goods.pocket.app.domain.usecase

import goods.pocket.app.domain.model.StorageLocation
import goods.pocket.app.domain.repository.SettingsRepository

class GetStorageLocationsUseCase(
    private val settingsRepository: SettingsRepository,
) {
    operator fun invoke(): List<StorageLocation> = settingsRepository.getStorageLocations()
}
