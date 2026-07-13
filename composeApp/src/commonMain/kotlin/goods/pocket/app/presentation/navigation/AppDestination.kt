package goods.pocket.app.presentation.navigation

sealed class AppDestination(
    val route: String,
) {
    data object Home : AppDestination("home")
    data object Collection : AppDestination("collection/list")
    data object My : AppDestination("my")
    data object Events : AppDestination("event/list")
    data object Settings : AppDestination("settings")

    companion object {
        val primaryDestinations: List<AppDestination>
            get() = listOf(
                Home,
                Collection,
                Events,
                My,
            )
    }
}
