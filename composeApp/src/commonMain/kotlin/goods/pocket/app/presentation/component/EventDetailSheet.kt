package goods.pocket.app.presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import goods.pocket.app.domain.collection.CollectionEntry
import goods.pocket.app.domain.event.Event
import goods.pocket.app.domain.event.EventType
import goods.pocket.app.presentation.i18n.formatDate
import goods.pocket.app.presentation.i18n.localizedLabel
import goods.pocket.app.presentation.i18n.tr
import goodspocket.composeapp.generated.resources.Res
import goodspocket.composeapp.generated.resources.action_close
import goodspocket.composeapp.generated.resources.action_delete
import goodspocket.composeapp.generated.resources.action_edit
import goodspocket.composeapp.generated.resources.detail_location
import goodspocket.composeapp.generated.resources.detail_related_item
import goodspocket.composeapp.generated.resources.detail_related_preorder
import goodspocket.composeapp.generated.resources.detail_target_date
import goodspocket.composeapp.generated.resources.field_note

private val EventDetailInk = Color(0xFF202838)
private val EventDetailMuted = Color(0xFF8A8F9B)
private val EventDetailOrange = Color(0xFFFF7445)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailSheet(
    event: Event,
    onDismiss: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    entries: List<CollectionEntry> = emptyList(),
) {
    val fields = remember(event, entries) { eventDetailFields(event, entries) }
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        dragHandle = null,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor = Color.White,
        contentColor = EventDetailInk,
        scrimColor = Color.Black.copy(alpha = 0.48f),
        tonalElevation = 0.dp,
        contentWindowInsets = { WindowInsets(0, 0, 0, 0) },
    ) {
        Column(Modifier.fillMaxWidth().height(428.dp).navigationBarsPadding()) {
            EventDetailHandle(onDismiss)
            Column(
                Modifier.weight(
                    1f,
                ).verticalScroll(rememberScrollState()).padding(horizontal = 22.dp),
            ) {
                Text(
                    event.title,
                    fontSize = 22.sp,
                    lineHeight = 28.sp,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(Modifier.height(10.dp))
                val accent = when (event.eventType) {
                    EventType.PAYMENT_DUE -> EventDetailOrange
                    EventType.DELIVERY -> Color(0xFF36C781)
                    EventType.RELEASE, EventType.OFFLINE_EVENT -> Color(0xFF8F6EF2)
                }
                Text(
                    event.eventType.localizedLabel(),
                    Modifier.clip(RoundedCornerShape(10.dp)).background(accent.copy(alpha = 0.12f))
                        .padding(horizontal = 14.dp, vertical = 6.dp),
                    color = accent,
                    fontSize = 13.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                )
                Spacer(Modifier.height(12.dp))
                fields.forEachIndexed { index, field ->
                    EventDetailMetadata(field, isFirst = index == 0)
                    if (index < fields.lastIndex) HorizontalDivider(color = Color(0xFFEFEDEC))
                }
            }
            Row(
                Modifier.fillMaxWidth().padding(horizontal = 22.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Button(
                    onClick = onEdit,
                    modifier = Modifier.weight(1f).height(46.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = EventDetailOrange),
                ) {
                    Text(
                        tr(Res.string.action_edit),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
                OutlinedButton(
                    onClick = onDelete,
                    modifier = Modifier.weight(1f).height(46.dp),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, EventDetailOrange),
                ) {
                    Text(
                        tr(Res.string.action_delete),
                        color = EventDetailOrange,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
        }
    }
}

@Composable
private fun EventDetailHandle(onDismiss: () -> Unit) {
    val closeLabel = tr(Res.string.action_close)
    Box(Modifier.fillMaxWidth().height(54.dp)) {
        Box(
            Modifier.align(Alignment.TopCenter).padding(top = 11.dp).width(40.dp).height(5.dp)
                .clip(RoundedCornerShape(3.dp)).background(Color(0xFFC8C9CC)),
        )
        IconButton(
            onClick = onDismiss,
            modifier = Modifier.align(Alignment.TopEnd).padding(end = 8.dp, top = 6.dp)
                .semantics { contentDescription = closeLabel },
        ) {
            Canvas(Modifier.size(20.dp)) {
                drawLine(
                    EventDetailMuted,
                    Offset.Zero,
                    Offset(size.width, size.height),
                    1.5.dp.toPx(),
                    StrokeCap.Round,
                )
                drawLine(
                    EventDetailMuted,
                    Offset(size.width, 0f),
                    Offset(0f, size.height),
                    1.5.dp.toPx(),
                    StrokeCap.Round,
                )
            }
        }
    }
}

@Composable
private fun EventDetailMetadata(field: EventDetailField, isFirst: Boolean) {
    val label = when (field.kind) {
        EventDetailFieldKind.TARGET_DATE -> Res.string.detail_target_date
        EventDetailFieldKind.RELATED_PREORDER -> Res.string.detail_related_preorder
        EventDetailFieldKind.RELATED_ITEM -> Res.string.detail_related_item
        EventDetailFieldKind.LOCATION -> Res.string.detail_location
        EventDetailFieldKind.MEMO -> Res.string.field_note
    }
    Row(
        Modifier.fillMaxWidth().heightIn(
            min = if (isFirst) 42.dp else 46.dp,
        ).padding(vertical = 9.dp),
        verticalAlignment = Alignment.Top,
    ) {
        Text(
            tr(label),
            Modifier.width(115.dp),
            color = EventDetailMuted,
            fontSize = 12.sp,
            lineHeight = 20.sp,
            fontWeight = FontWeight.SemiBold,
        )
        Text(
            if (field.kind ==
                EventDetailFieldKind.TARGET_DATE
            ) {
                formatDate(field.value)
            } else {
                field.value
            },
            Modifier.weight(1f),
            color = EventDetailInk,
            fontSize = 14.sp,
            lineHeight = 20.sp,
        )
    }
}
