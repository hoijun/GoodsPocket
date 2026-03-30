package goods.pocket.app.presentation.designsystem

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoodsPocketModalBottomSheet(
    title: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    titleFontWeight: FontWeight? = null,
    contentSpacing: Dp = 14.dp,
    content: @Composable ColumnScope.() -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current
    val dismissThresholdPx = with(density) { 72.dp.toPx() }
    val dismissVelocityPx = with(density) { 1_100.dp.toPx() }
    var sheetDragOffsetPx by remember { mutableFloatStateOf(0f) }
    var sheetHeightPx by remember { mutableIntStateOf(0) }
    var settleJob by remember { mutableStateOf<Job?>(null) }

    fun closeSheet() {
        settleJob?.cancel()
        scope.launch {
            sheetState.hide()
            onDismiss()
        }
    }

    val headerDragState = rememberDraggableState { dragDelta ->
        settleJob?.cancel()
        val maxOffset = sheetHeightPx.takeIf { it > 0 }?.toFloat() ?: Float.MAX_VALUE
        sheetDragOffsetPx = (sheetDragOffsetPx + dragDelta).coerceIn(0f, maxOffset)
    }

    fun settleSheet(velocity: Float) {
        settleJob?.cancel()
        settleJob = scope.launch {
            val shouldDismiss = sheetDragOffsetPx > dismissThresholdPx || velocity > dismissVelocityPx
            if (shouldDismiss && sheetHeightPx > 0) {
                animate(
                    initialValue = sheetDragOffsetPx,
                    targetValue = sheetHeightPx.toFloat(),
                    animationSpec = tween(durationMillis = 180, easing = FastOutLinearInEasing),
                ) { value, _ ->
                    sheetDragOffsetPx = value
                }
                sheetState.hide()
                sheetDragOffsetPx = 0f
                onDismiss()
            } else {
                animate(
                    initialValue = sheetDragOffsetPx,
                    targetValue = 0f,
                    animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                ) { value, _ ->
                    sheetDragOffsetPx = value
                }
            }
        }
    }

    ModalBottomSheet(
        onDismissRequest = ::closeSheet,
        modifier = modifier,
        sheetState = sheetState,
        sheetGesturesEnabled = false,
        dragHandle = null,
        containerColor = Color.Transparent,
        tonalElevation = 0.dp,
        properties = ModalBottomSheetProperties(
            shouldDismissOnBackPress = false,
            shouldDismissOnClickOutside = false,
        ),
    ) {
        BoxWithConstraints(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset { IntOffset(x = 0, y = sheetDragOffsetPx.roundToInt()) }
                    .onSizeChanged { sheetHeightPx = it.height }
                    .heightIn(max = maxHeight - 88.dp),
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                color = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onSurface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    GoodsPocketBottomSheetHeader(
                        title = title,
                        onDismiss = ::closeSheet,
                        modifier = Modifier.draggable(
                            orientation = Orientation.Vertical,
                            state = headerDragState,
                            onDragStopped = { velocity -> settleSheet(velocity) },
                        ),
                        fontWeight = titleFontWeight,
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 18.dp, vertical = 10.dp),
                        verticalArrangement = Arrangement.spacedBy(contentSpacing),
                        content = content,
                    )
                }
            }
        }
    }
}
