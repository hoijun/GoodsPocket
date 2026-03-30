package goods.pocket.app.presentation.designsystem

import goods.pocket.app.presentation.navigation.AppDestination

object GoodsPocketVisualTokens {
    const val Background = 0xFFFFF9FCL
    const val Surface = 0xFFFFFFFFL
    const val SurfaceLow = 0xFFF8F2F7L
    const val SurfaceHigh = 0xFFEDE6EEL
    const val SurfaceTint = 0xFFFBEFF5L
    const val Primary = 0xFFF26CA7L
    const val PrimaryDim = 0xFFD95C91L
    const val PrimaryContainer = 0xFFFFE1EDL
    const val Secondary = 0xFF87DCCBL
    const val SecondaryContainer = 0xFFE3F7F1L
    const val Tertiary = 0xFFFFD46FL
    const val TertiaryContainer = 0xFFFFF1C6L
    const val Ink = 0xFF2B2530L
    const val MutedInk = 0xFF665D6DL
    const val Outline = 0xFFE2D5E3L
    const val Success = 0xFF73C7A5L
    const val Warning = 0xFFF3B85AL
    const val Danger = 0xFFE07A94L
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

        AppDestination.Events,
        AppDestination.Settings,
        -> GoodsPocketChrome(
            showBottomBar = false,
            showFab = false,
            showBackButton = true,
        )
    }
}
