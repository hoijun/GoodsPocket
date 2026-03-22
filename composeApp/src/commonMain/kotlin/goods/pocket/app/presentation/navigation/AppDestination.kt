package goods.pocket.app.presentation.navigation

sealed class AppDestination(
    val route: String,
    val label: String,
) {
    data object Home : AppDestination("home", "Home")
    data object Collection : AppDestination("collection/list", "Collection")
    data object Preorders : AppDestination("preorder/list", "Preorders")
    data object Transactions : AppDestination("transaction/list", "Transactions")
    data object Events : AppDestination("event/list", "Events")
    data object Settings : AppDestination("settings", "Settings")

    companion object {
        val primaryDestinations: List<AppDestination>
            get() = listOf(
                Home,
                Collection,
                Preorders,
                Transactions,
            )

        val allDestinations: List<AppDestination>
            get() = primaryDestinations + listOf(
                Events,
                Settings,
            )
    }
}
