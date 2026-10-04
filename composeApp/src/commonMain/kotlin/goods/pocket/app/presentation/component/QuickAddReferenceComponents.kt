package goods.pocket.app.presentation.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import goods.pocket.app.presentation.i18n.tr
import goodspocket.composeapp.generated.resources.Res
import goodspocket.composeapp.generated.resources.action_close
import goodspocket.composeapp.generated.resources.action_save
import goodspocket.composeapp.generated.resources.quick_add_title

private val QuickAddInk = Color(0xFF202838)
private val QuickAddMuted = Color(0xFF8A8F9B)
private val QuickAddOrange = Color(0xFFFF7445)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun QuickAddReferenceSheet(
    canSubmit: Boolean,
    onDismiss: () -> Unit,
    onSubmit: () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    val density = LocalDensity.current
    val windowHeight = LocalWindowInfo.current.containerSize.height
    val keyboardHeight = WindowInsets.ime.getBottom(density)
    val statusBarHeight = WindowInsets.statusBars.getTop(density)
    val availableHeight = with(density) { (windowHeight - keyboardHeight - statusBarHeight).toDp() }
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        dragHandle = null,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor = Color(0xFFFFFCF8),
        contentColor = QuickAddInk,
        scrimColor = Color.Black.copy(alpha = 0.48f),
        tonalElevation = 0.dp,
        contentWindowInsets = { WindowInsets(0, 0, 0, 0) },
    ) {
        Column(
            Modifier.fillMaxWidth().height(
                availableHeight.coerceAtMost(648.dp),
            ).navigationBarsPadding(),
        ) {
            Box(Modifier.fillMaxWidth().height(72.dp)) {
                Box(
                    Modifier.align(Alignment.TopCenter).padding(top = 11.dp)
                        .width(
                            43.dp,
                        ).height(5.dp).clip(RoundedCornerShape(3.dp)).background(Color(0xFFC8C9CC)),
                )
                Text(
                    tr(Res.string.quick_add_title),
                    Modifier.align(Alignment.CenterStart).padding(start = 19.dp, top = 24.dp),
                    fontSize = 22.sp,
                    lineHeight = 27.sp,
                    fontWeight = FontWeight.Bold,
                )
                val closeLabel = tr(Res.string.action_close)
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.CenterEnd)
                        .padding(end = 5.dp, top = 10.dp).semantics {
                            contentDescription = closeLabel
                        },
                ) {
                    Canvas(Modifier.size(22.dp)) {
                        drawLine(
                            QuickAddMuted,
                            Offset(3.dp.toPx(), 3.dp.toPx()),
                            Offset(
                                size.width - 3.dp.toPx(),
                                size.height - 3.dp.toPx(),
                            ),
                            1.75.dp.toPx(),
                            StrokeCap.Round,
                        )
                        drawLine(
                            QuickAddMuted,
                            Offset(size.width - 3.dp.toPx(), 3.dp.toPx()),
                            Offset(
                                3.dp.toPx(),
                                size.height - 3.dp.toPx(),
                            ),
                            1.75.dp.toPx(),
                            StrokeCap.Round,
                        )
                    }
                }
            }
            Column(
                modifier = Modifier.weight(
                    1f,
                ).verticalScroll(rememberScrollState()).padding(horizontal = 19.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                content = content,
            )
            Button(
                onClick = onSubmit,
                enabled = canSubmit,
                modifier = Modifier.fillMaxWidth().padding(
                    horizontal = 19.dp,
                    vertical = 17.dp,
                ).height(46.dp),
                shape = RoundedCornerShape(9.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = QuickAddOrange,
                    disabledContainerColor = Color(0xFFFFCDB8),
                    disabledContentColor = Color.White,
                ),
            ) {
                Text(tr(Res.string.action_save), fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
internal fun QuickAddSegments(
    labels: List<String>,
    selectedIndex: Int,
    onSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    outlined: Boolean = false,
) {
    Row(
        modifier.fillMaxWidth().heightIn(
            min = if (outlined) 39.dp else 41.dp,
        ).clip(RoundedCornerShape(10.dp))
            .background(if (outlined) Color.Transparent else Color(0xFFF3F0EE))
            .then(
                if (outlined) {
                    Modifier.border(
                        1.dp,
                        Color(0xFFD7D6D8),
                        RoundedCornerShape(8.dp),
                    )
                } else {
                    Modifier
                },
            )
            .padding(3.dp).drawBehind {
                if (outlined) {
                    for (boundary in 1 until labels.size) {
                        if (selectedIndex != boundary - 1 && selectedIndex != boundary) {
                            val x = size.width * boundary / labels.size
                            val halfHeight = 11.dp.toPx()
                            drawLine(
                                color = Color(0xFFE2E1E3),
                                start = Offset(x, size.height / 2 - halfHeight),
                                end = Offset(x, size.height / 2 + halfHeight),
                                strokeWidth = 1.dp.toPx(),
                            )
                        }
                    }
                }
            }.selectableGroup(),
    ) {
        labels.forEachIndexed { index, label ->
            val selected = selectedIndex == index
            Box(
                Modifier.weight(1f).clip(RoundedCornerShape(8.dp))
                    .background(if (selected) QuickAddOrange else Color.Transparent)
                    .selectable(selected = selected, role = Role.Tab, onClick = {
                        onSelected(index)
                    })
                    .padding(horizontal = 4.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    label,
                    fontSize = 14.sp,
                    lineHeight = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = when {
                        selected -> Color.White
                        outlined -> QuickAddMuted
                        else -> QuickAddInk
                    },
                )
            }
        }
    }
}

@Composable
internal fun QuickAddField(
    value: String,
    label: String,
    onValueChange: (String) -> Unit,
    multiline: Boolean = false,
    minHeight: Dp = if (multiline) 64.dp else 42.dp,
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth().heightIn(min = minHeight)
            .border(1.dp, Color(0xFFD7D6D8), RoundedCornerShape(8.dp))
            .semantics { contentDescription = label }.padding(horizontal = 13.dp, vertical = 10.dp),
        singleLine = !multiline,
        textStyle = TextStyle(
            color = QuickAddInk,
            fontSize = 15.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.sp,
        ),
        cursorBrush = SolidColor(QuickAddOrange),
        decorationBox = { inner ->
            Box {
                if (value.isEmpty()) {
                    Text(
                        label,
                        Modifier.clearAndSetSemantics {},
                        color = QuickAddMuted,
                        fontSize = 15.sp,
                        lineHeight = 20.sp,
                        fontWeight = FontWeight.Medium,
                    )
                }
                inner()
            }
        },
    )
}
