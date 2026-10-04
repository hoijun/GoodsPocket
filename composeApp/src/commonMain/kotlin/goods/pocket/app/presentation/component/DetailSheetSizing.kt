package goods.pocket.app.presentation.component

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp

@Composable
internal fun boundedDetailSheetHeight(referenceHeight: Dp): Dp {
    val density = LocalDensity.current
    val windowHeight = LocalWindowInfo.current.containerSize.height
    val safeTop = WindowInsets.safeDrawing.getTop(density)
    val keyboardHeight = WindowInsets.ime.getBottom(density)
    val availableHeight = with(density) {
        (windowHeight - safeTop - keyboardHeight).coerceAtLeast(0).toDp()
    }
    return referenceHeight.coerceAtMost(availableHeight)
}
