package goods.pocket.app.presentation.my

internal enum class MyHubAction {
    SYNC_BACKUP,
    NOTIFICATIONS,
    SETTINGS,
}

internal data class MyHubQuickLinkModel(val action: MyHubAction, val badgeCount: Int? = null)

internal fun buildMyHubQuickLinks(): List<MyHubQuickLinkModel> = listOf(
    MyHubQuickLinkModel(MyHubAction.SYNC_BACKUP),
    MyHubQuickLinkModel(MyHubAction.NOTIFICATIONS),
    MyHubQuickLinkModel(MyHubAction.SETTINGS),
)
