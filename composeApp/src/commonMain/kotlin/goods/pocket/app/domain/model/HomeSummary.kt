package goods.pocket.app.domain.model

data class HomeSummary(
    val monthlySpend: Long,
    val ownedItemCount: Int,
    val activePreorderCount: Int,
    val recentActivities: List<ActivityRecord>,
)
