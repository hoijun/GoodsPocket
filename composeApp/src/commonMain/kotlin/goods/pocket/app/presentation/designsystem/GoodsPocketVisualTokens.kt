package goods.pocket.app.presentation.designsystem

import goods.pocket.app.presentation.navigation.AppDestination

object GoodsPocketVisualTokens {
    const val Background = 0xFFFFFCF8L
    const val Surface = 0xFFFFFFFFL
    const val SurfaceLow = 0xFFFFF5EEL
    const val SurfaceHigh = 0xFFFFE8DCL
    const val SurfaceTint = 0xFFFFF1E7L
    const val Primary = 0xFFFF7445L
    const val PrimaryDim = 0xFFE65F35L
    const val PrimaryContainer = 0xFFFFE1D5L
    const val Secondary = 0xFF36C781L
    const val SecondaryContainer = 0xFFDDF8E9L
    const val Tertiary = 0xFF8F6EF2L
    const val TertiaryContainer = 0xFFEDE8FFL
    const val Ink = 0xFF202838L
    const val MutedInk = 0xFF8A8F9BL
    const val Outline = 0xFFEAE2DCL
    const val Success = 0xFF36C781L
    const val Warning = 0xFFFFB13BL
    const val Danger = 0xFFFF7445L
}

data class GoodsPocketChrome(
    val showBottomBar: Boolean,
    val showCenteredQuickAdd: Boolean,
    val showBackButton: Boolean,
)

fun goodsPocketChromeFor(destination: AppDestination): GoodsPocketChrome {
    return when (destination) {
        AppDestination.Home,
        AppDestination.Collection,
        AppDestination.Events,
        AppDestination.My,
        -> GoodsPocketChrome(
            showBottomBar = true,
            showCenteredQuickAdd = true,
            showBackButton = false,
        )

        AppDestination.Settings,
        -> GoodsPocketChrome(
            showBottomBar = false,
            showCenteredQuickAdd = false,
            showBackButton = true,
        )
    }
}
