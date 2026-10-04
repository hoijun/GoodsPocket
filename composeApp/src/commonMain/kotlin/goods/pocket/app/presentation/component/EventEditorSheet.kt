package goods.pocket.app.presentation.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import goods.pocket.app.domain.event.Event
import goods.pocket.app.domain.event.EventType
import goods.pocket.app.presentation.i18n.localizedLabel
import goods.pocket.app.presentation.i18n.tr
import goods.pocket.app.presentation.state.CommandState
import goodspocket.composeapp.generated.resources.Res
import goodspocket.composeapp.generated.resources.action_close
import goodspocket.composeapp.generated.resources.editor_event_date
import goodspocket.composeapp.generated.resources.editor_event_heading
import goodspocket.composeapp.generated.resources.editor_event_offline
import goodspocket.composeapp.generated.resources.editor_owned_save
import goodspocket.composeapp.generated.resources.field_title
import goodspocket.composeapp.generated.resources.quick_add_event_type

private val EventEditorOrange = Color(0xFFFF7445)
private val EventEditorMuted = Color(0xFF8A8F9B)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventEditorSheet(
    event: Event,
    commandState: CommandState = CommandState(),
    onRetry: () -> Unit = {},
    onDismiss: () -> Unit,
    onSave: (String, String, EventType) -> Unit,
) {
    var draft by rememberSaveable(event.id, stateSaver = EventDraftSaver) {
        mutableStateOf(EventDraft(event.title, event.targetDate, event.eventType))
    }
    val density = LocalDensity.current
    val windowHeight = LocalWindowInfo.current.containerSize.height
    val keyboardHeight = WindowInsets.ime.getBottom(density)
    val statusHeight = WindowInsets.statusBars.getTop(density)
    val availableHeight = with(density) { (windowHeight - keyboardHeight - statusHeight).toDp() }
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        dragHandle = null,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor = Color(0xFFFFFCF8),
        contentColor = Color(0xFF202838),
        scrimColor = Color.Black.copy(alpha = 0.48f),
        tonalElevation = 0.dp,
        contentWindowInsets = { WindowInsets(0, 0, 0, 0) },
    ) {
        Column(
            Modifier.fillMaxWidth().height(
                availableHeight.coerceAtMost(438.dp),
            ).navigationBarsPadding(),
        ) {
            Box(Modifier.fillMaxWidth().heightIn(min = 79.dp)) {
                Box(
                    Modifier.align(
                        Alignment.TopCenter,
                    ).padding(top = 11.dp).width(43.dp).height(5.dp)
                        .clip(RoundedCornerShape(3.dp)).background(Color(0xFFC8C9CC)),
                )
                Text(
                    tr(Res.string.editor_event_heading),
                    Modifier.align(
                        Alignment.TopStart,
                    ).padding(start = 20.dp, end = 58.dp, top = 35.dp),
                    fontSize = 22.sp,
                    lineHeight = 27.sp,
                    fontWeight = FontWeight.Bold,
                )
                val closeLabel = tr(Res.string.action_close)
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.TopEnd)
                        .padding(end = 5.dp, top = 20.dp).semantics {
                            contentDescription = closeLabel
                        },
                ) {
                    Canvas(Modifier.size(20.dp)) {
                        drawLine(
                            EventEditorMuted,
                            Offset(3.dp.toPx(), 3.dp.toPx()),
                            Offset(size.width - 3.dp.toPx(), size.height - 3.dp.toPx()),
                            2.dp.toPx(),
                            StrokeCap.Round,
                        )
                        drawLine(
                            EventEditorMuted,
                            Offset(size.width - 3.dp.toPx(), 3.dp.toPx()),
                            Offset(3.dp.toPx(), size.height - 3.dp.toPx()),
                            2.dp.toPx(),
                            StrokeCap.Round,
                        )
                    }
                }
            }
            Column(
                Modifier.weight(
                    1f,
                ).verticalScroll(rememberScrollState()).padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(15.dp),
            ) {
                FeatureFeedback(commandState.isRunning, commandState.hasFailure, onRetry)
                EditorField(
                    draft.title,
                    tr(Res.string.field_title),
                    {
                        draft =
                            draft.copy(title = it)
                    },
                    outlineColor = Color(0xFFDFDFE5),
                )
                EditorField(
                    draft.targetDate,
                    tr(Res.string.editor_event_date),
                    {
                        draft =
                            draft.copy(targetDate = it)
                    },
                    outlineColor = Color(0xFFDFDFE5),
                )
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        tr(Res.string.quick_add_event_type),
                        color = EventEditorMuted,
                        fontSize = 12.sp,
                        lineHeight = 16.sp,
                        fontWeight = FontWeight.Medium,
                    )
                    Row(
                        Modifier.fillMaxWidth().border(
                            1.dp,
                            Color(0xFFE4E2E4),
                            RoundedCornerShape(8.dp),
                        )
                            .padding(2.dp).selectableGroup(),
                    ) {
                        EventType.entries.forEachIndexed { index, type ->
                            val selected = draft.eventType == type
                            Box(
                                Modifier.weight(1f).clip(RoundedCornerShape(6.dp))
                                    .background(
                                        if (selected) EventEditorOrange else Color.Transparent,
                                    )
                                    .selectable(selected, role = Role.Tab, onClick = {
                                        draft =
                                            draft.copy(eventType = type)
                                    })
                                    .padding(horizontal = 2.dp, vertical = 7.dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(
                                    if (type ==
                                        EventType.OFFLINE_EVENT
                                    ) {
                                        tr(Res.string.editor_event_offline)
                                    } else {
                                        type.localizedLabel()
                                    },
                                    fontSize = 12.sp,
                                    lineHeight = 16.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (selected) Color.White else EventEditorMuted,
                                )
                            }
                            val next = EventType.entries.getOrNull(index + 1)
                            if (next != null) {
                                Box(
                                    Modifier.align(
                                        Alignment.CenterVertically,
                                    ).width(1.dp).height(16.dp)
                                        .background(
                                            if (!selected &&
                                                draft.eventType != next
                                            ) {
                                                Color(0xFFE4E2E4)
                                            } else {
                                                Color.Transparent
                                            },
                                        ),
                                )
                            }
                        }
                    }
                }
            }
            Button(
                onClick = { onSave(draft.title, draft.targetDate, draft.eventType) },
                enabled = draft.canSubmit && !commandState.isRunning,
                modifier = Modifier.fillMaxWidth().padding(
                    start = 20.dp,
                    end = 20.dp,
                    top = 8.dp,
                    bottom = 23.dp,
                ).heightIn(min = 44.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = EventEditorOrange,
                    disabledContainerColor = Color(0xFFFFCDB8),
                ),
            ) {
                Text(
                    tr(Res.string.editor_owned_save),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}
