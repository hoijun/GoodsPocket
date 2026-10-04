package goods.pocket.app.presentation.settings

import goods.pocket.app.domain.settings.AppPreference
import goods.pocket.app.presentation.state.CommandState

data class SettingsUiState(
    val preferences: AppPreference = AppPreference(),
    val isLoading: Boolean = true,
    val hasLoadFailure: Boolean = false,
    val command: CommandState = CommandState(),
    val canEdit: Boolean = false,
)
