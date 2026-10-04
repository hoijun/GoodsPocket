package goods.pocket.app.presentation.my

import goods.pocket.app.presentation.state.MyPageUiModel

data class MyUiState(
    val profile: MyPageUiModel = MyPageUiModel(),
    val isLoading: Boolean = true,
    val hasLoadFailure: Boolean = false,
)
