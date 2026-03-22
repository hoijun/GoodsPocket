package goods.pocket.app.presentation.designsystem

import goods.pocket.app.presentation.navigation.AppDestination

object GoodsPocketVisualTokens {
    const val Background = 0xFFF6FAFBL
    const val Surface = 0xFFFFFFFFL
    const val SurfaceLow = 0xFFEFF5F6L
    const val SurfaceHigh = 0xFFE1EAECL
    const val SurfaceTint = 0xFFDAE4E7L
    const val Primary = 0xFF1E6876L
    const val PrimaryDim = 0xFF085B69L
    const val PrimaryContainer = 0xFFA5E7F8L
    const val Ink = 0xFF2A3437L
    const val MutedInk = 0xFF576163L
    const val Outline = 0xFFAAB4B7L
    const val Danger = 0xFFA83836L
}

data class GoodsPocketChrome(
    val showBottomBar: Boolean,
    val showFab: Boolean,
    val showBackButton: Boolean,
)

fun goodsPocketChromeFor(destination: AppDestination): GoodsPocketChrome {
    return when (destination) {
        AppDestination.Home,
        AppDestination.Collection,
        AppDestination.Preorders,
        -> GoodsPocketChrome(
            showBottomBar = true,
            showFab = true,
            showBackButton = false,
        )

        AppDestination.My -> GoodsPocketChrome(
            showBottomBar = true,
            showFab = false,
            showBackButton = false,
        )

        AppDestination.Transactions,
        AppDestination.Events,
        AppDestination.Settings,
        -> GoodsPocketChrome(
            showBottomBar = false,
            showFab = false,
            showBackButton = true,
        )
    }
}
