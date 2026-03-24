package goods.pocket.app.presentation.state

data class MyPageUiModel(
    val displayName: String = "",
    val syncStatusLabel: String = "",
    val notificationsEnabled: Boolean = true,
    val ownedItemCount: Int = 0,
    val activePreorderCount: Int = 0,
    val monthlySpend: Long = 0,
    val upcomingEventCount: Int = 0,
)
