package goods.pocket.app.presentation.designsystem

import goods.pocket.app.presentation.navigation.AppDestination

object GoodsPocketVisualTokens {
    const val BACKGROUND = 0xFFFFFCF8L
    const val SURFACE = 0xFFFFFFFFL
    const val SURFACE_LOW = 0xFFFFF5EEL
    const val SURFACE_HIGH = 0xFFFFE8DCL
    const val SURFACE_TINT = 0xFFFFF1E7L
    const val PRIMARY = 0xFFFF7445L
    const val PRIMARY_DIM = 0xFFE65F35L
    const val PRIMARY_CONTAINER = 0xFFFFE1D5L
    const val SECONDARY = 0xFF36C781L
    const val SECONDARY_CONTAINER = 0xFFDDF8E9L
    const val TERTIARY = 0xFF8F6EF2L
    const val TERTIARY_CONTAINER = 0xFFEDE8FFL
    const val INK = 0xFF202838L
    const val MUTED_INK = 0xFF8A8F9BL
    const val OUTLINE = 0xFFEAE2DCL
    const val SUCCESS = 0xFF36C781L
    const val WARNING = 0xFFFFB13BL
    const val DANGER = 0xFFFF7445L
}

data class GoodsPocketChrome(
    val showBottomBar: Boolean,
    val showCenteredQuickAdd: Boolean,
    val showBackButton: Boolean,
)

fun goodsPocketChromeFor(destination: AppDestination): GoodsPocketChrome = when (destination) {
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
