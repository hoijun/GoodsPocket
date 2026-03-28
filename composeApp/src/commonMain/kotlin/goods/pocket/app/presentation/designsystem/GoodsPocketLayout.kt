package goods.pocket.app.presentation.designsystem

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

fun goodsPocketScreenModifier(): Modifier {
    return Modifier.padding(start = 16.dp, top = 12.dp, end = 16.dp)
}

fun goodsPocketPrimaryScrollContentPadding(): PaddingValues {
    return PaddingValues(bottom = 28.dp)
}
