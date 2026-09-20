package goods.pocket.app.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.Canvas
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import goods.pocket.app.domain.model.CollectionEntry
import goods.pocket.app.domain.model.CollectionEntryStatus
import goods.pocket.app.presentation.i18n.localizedLabel
import goods.pocket.app.presentation.i18n.tr
import goodspocket.composeapp.generated.resources.Res
import goodspocket.composeapp.generated.resources.action_close
import goodspocket.composeapp.generated.resources.editor_owned_save
import goodspocket.composeapp.generated.resources.editor_owned_title
import goodspocket.composeapp.generated.resources.field_category
import goodspocket.composeapp.generated.resources.field_character
import goodspocket.composeapp.generated.resources.field_note
import goodspocket.composeapp.generated.resources.field_series
import goodspocket.composeapp.generated.resources.field_status
import goodspocket.composeapp.generated.resources.quick_add_item_name
import goodspocket.composeapp.generated.resources.quick_add_owned
import goodspocket.composeapp.generated.resources.quick_add_store
import goodspocket.composeapp.generated.resources.quick_add_reservation_store
import goodspocket.composeapp.generated.resources.quick_add_release_date
import goodspocket.composeapp.generated.resources.quick_add_reserved

private val EditorInk = Color(0xFF202838)
private val EditorMuted = Color(0xFF8A8F9B)
private val EditorOrange = Color(0xFFFF7445)
private val EditorOutline = Color(0xFFD7D6D8)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectionEntryEditorSheet(
    entry: CollectionEntry,
    onDismiss: () -> Unit,
    onSave: (String, String, CollectionEntryStatus, String, String, String, String, String, String) -> Unit,
) {
    var draft by remember(entry.id) { mutableStateOf(collectionEditorDraft(entry)) }
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
        contentColor = EditorInk,
        scrimColor = Color.Black.copy(alpha = 0.48f),
        tonalElevation = 0.dp,
        contentWindowInsets = { WindowInsets(0, 0, 0, 0) },
    ) {
        Column(Modifier.fillMaxWidth().height(availableHeight.coerceAtMost(648.dp)).navigationBarsPadding()) {
            Box(Modifier.fillMaxWidth().height(72.dp)) {
                Box(Modifier.align(Alignment.TopCenter).padding(top = 11.dp)
                    .width(43.dp).height(5.dp).clip(RoundedCornerShape(3.dp)).background(Color(0xFFC8C9CC)))
                Text(tr(Res.string.editor_owned_title),
                    Modifier.align(Alignment.CenterStart).padding(start = 20.dp, top = 24.dp),
                    fontSize = 22.sp, lineHeight = 27.sp, fontWeight = FontWeight.Bold)
                val closeLabel = tr(Res.string.action_close)
                IconButton(onClick = onDismiss, modifier = Modifier.align(Alignment.CenterEnd)
                    .padding(end = 5.dp, top = 10.dp).semantics { contentDescription = closeLabel }) {
                    Canvas(Modifier.size(20.dp)) {
                        drawLine(EditorMuted, Offset(3.dp.toPx(), 3.dp.toPx()),
                            Offset(size.width - 3.dp.toPx(), size.height - 3.dp.toPx()), 1.75.dp.toPx(), StrokeCap.Round)
                        drawLine(EditorMuted, Offset(size.width - 3.dp.toPx(), 3.dp.toPx()),
                            Offset(3.dp.toPx(), size.height - 3.dp.toPx()), 1.75.dp.toPx(), StrokeCap.Round)
                    }
                }
            }
            Column(
                modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(start = 20.dp, end = 20.dp, top = 1.dp),
                verticalArrangement = Arrangement.spacedBy(13.dp),
            ) {
                EditorField(draft.name, tr(Res.string.quick_add_item_name), { draft = draft.copy(name = it) })
                EditorField(draft.category, tr(Res.string.field_category), { draft = draft.copy(category = it) })
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    EditorLabel(tr(Res.string.field_status))
                    Row(Modifier.fillMaxWidth().border(1.dp, EditorOutline, RoundedCornerShape(8.dp))
                        .padding(2.dp).selectableGroup()) {
                        editableCollectionStatuses(entry.status).forEach { status ->
                            val selected = draft.status == status
                            Box(Modifier.weight(1f).clip(RoundedCornerShape(6.dp))
                                .background(if (selected) EditorOrange else Color.Transparent)
                                .selectable(selected, role = Role.Tab, onClick = { draft = draft.copy(status = status) })
                                .padding(horizontal = 4.dp, vertical = 6.dp), contentAlignment = Alignment.Center) {
                                Text(when (status) {
                                    CollectionEntryStatus.OWNED -> tr(Res.string.quick_add_owned)
                                    CollectionEntryStatus.RESERVED -> tr(Res.string.quick_add_reserved)
                                    CollectionEntryStatus.PLANNED_CLEANUP -> status.localizedLabel()
                                },
                                    fontSize = 14.sp, lineHeight = 18.sp, fontWeight = FontWeight.SemiBold,
                                    color = if (selected) Color.White else EditorMuted)
                            }
                        }
                    }
                }
                EditorField(draft.seriesName, tr(Res.string.field_series), { draft = draft.copy(seriesName = it) })
                EditorField(draft.characterName, tr(Res.string.field_character), { draft = draft.copy(characterName = it) })
                if (draft.isReserved) {
                    EditorField(draft.reservationStore, tr(Res.string.quick_add_reservation_store),
                        { draft = draft.copy(reservationStore = it) })
                    EditorField(draft.releaseDate, tr(Res.string.quick_add_release_date),
                        { draft = draft.copy(releaseDate = it) })
                } else {
                    EditorField(draft.purchaseStore, tr(Res.string.quick_add_store), { draft = draft.copy(purchaseStore = it) })
                }
                EditorField(draft.note, tr(Res.string.field_note), { draft = draft.copy(note = it) }, multiline = true)
            }
            Button(
                onClick = { onSave(draft.name, draft.category, draft.status, draft.seriesName, draft.characterName,
                    draft.purchaseStore, draft.releaseDate, draft.reservationStore, draft.note) },
                enabled = draft.canSubmit,
                modifier = Modifier.fillMaxWidth().padding(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 13.dp).height(44.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = EditorOrange, disabledContainerColor = Color(0xFFFFCDB8)),
            ) {
                Text(tr(Res.string.editor_owned_save), fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun EditorLabel(label: String) {
    Text(label, modifier = Modifier.heightIn(min = 16.dp).clearAndSetSemantics {}, color = EditorMuted,
        fontSize = 12.sp, lineHeight = 15.sp, fontWeight = FontWeight.Medium)
}

@Composable
private fun EditorField(value: String, label: String, onValueChange: (String) -> Unit, multiline: Boolean = false) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        EditorLabel(label)
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth().heightIn(min = if (multiline) 54.dp else 34.dp)
                .border(1.dp, EditorOutline, RoundedCornerShape(8.dp))
                .semantics { contentDescription = label }.padding(horizontal = 12.dp, vertical = 8.dp),
            singleLine = !multiline,
            textStyle = TextStyle(color = EditorInk, fontSize = 14.sp, lineHeight = 18.sp,
                letterSpacing = 0.sp, fontWeight = FontWeight.Medium),
            cursorBrush = SolidColor(EditorOrange),
            decorationBox = { inner ->
                Box {
                    if (value.isEmpty()) Text(label, Modifier.clearAndSetSemantics {}, color = EditorMuted.copy(alpha = 0.7f),
                        fontSize = 14.sp, lineHeight = 18.sp)
                    inner()
                }
            },
        )
    }
}
