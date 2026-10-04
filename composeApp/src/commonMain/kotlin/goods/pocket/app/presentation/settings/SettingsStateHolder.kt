package goods.pocket.app.presentation.settings

import goods.pocket.app.domain.settings.AppPreference
import goods.pocket.app.domain.settings.SettingsRepository
import goods.pocket.app.presentation.state.FeatureCommand
import goods.pocket.app.presentation.state.FeatureObservation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class SettingsStateHolder(private val repository: SettingsRepository, scope: CoroutineScope) {
    private val content = FeatureObservation(scope, AppPreference(), repository::observePreferences)
    val command: FeatureCommand = FeatureCommand(scope)
    val state: kotlinx.coroutines.flow.StateFlow<SettingsUiState> = combine(
        content.state,
        command.state,
    ) {
            data,
            command,
        ->
        SettingsUiState(
            data.value,
            data.isLoading,
            data.hasFailure,
            command,
            data.hasLoaded && !command.isRunning,
        )
    }.stateIn(scope, SharingStarted.Eagerly, SettingsUiState())

    fun updateLanguage(languageCode: String) {
        if (!content.state.value.hasLoaded) return
        if (languageCode !in setOf("ko", "en")) return
        val preferences = content.state.value.value.copy(languageCode = languageCode)
        command.run { repository.updateAppPreferences(preferences) }
    }

    fun retryLoad() = content.retry()
}
