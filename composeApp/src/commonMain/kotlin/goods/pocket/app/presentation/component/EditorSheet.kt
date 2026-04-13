package goods.pocket.app.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import goods.pocket.app.domain.model.CollectionEntry
import goods.pocket.app.domain.model.CollectionEntryStatus
import goods.pocket.app.domain.model.Event
import goods.pocket.app.domain.model.EventType
import goods.pocket.app.presentation.designsystem.GoodsPocketFilterChip
import goods.pocket.app.presentation.designsystem.GoodsPocketModalBottomSheet
import goods.pocket.app.presentation.designsystem.goodsPocketOutlinedFieldColors
import goods.pocket.app.presentation.i18n.localizedLabel
import goods.pocket.app.presentation.i18n.tr
import goodspocket.composeapp.generated.resources.Res
import goodspocket.composeapp.generated.resources.action_save_changes
import goodspocket.composeapp.generated.resources.editor_collection_entry_title
import goodspocket.composeapp.generated.resources.editor_event_title
import goodspocket.composeapp.generated.resources.field_category
import goodspocket.composeapp.generated.resources.field_character
import goodspocket.composeapp.generated.resources.field_name
import goodspocket.composeapp.generated.resources.field_note
import goodspocket.composeapp.generated.resources.field_release_date
import goodspocket.composeapp.generated.resources.field_series
import goodspocket.composeapp.generated.resources.field_status
import goodspocket.composeapp.generated.resources.field_store
import goodspocket.composeapp.generated.resources.field_target_date
import goodspocket.composeapp.generated.resources.field_title

@Composable
fun CollectionEntryEditorSheet(
    entry: CollectionEntry,
    onDismiss: () -> Unit,
    onSave: (
        name: String,
        category: String,
        status: CollectionEntryStatus,
        seriesName: String,
        characterName: String,
        purchaseStore: String,
        releaseDate: String,
        reservationStore: String,
        note: String,
    ) -> Unit,
) {
    var name by remember(entry.id) { mutableStateOf(entry.name) }
    var category by remember(entry.id) { mutableStateOf(entry.category) }
    var status by remember(entry.id) { mutableStateOf(entry.status) }
    var seriesName by remember(entry.id) { mutableStateOf(entry.seriesName.orEmpty()) }
    var characterName by remember(entry.id) { mutableStateOf(entry.characterName.orEmpty()) }
    var purchaseStore by remember(entry.id) { mutableStateOf(entry.purchaseStore.orEmpty()) }
    var releaseDate by remember(entry.id) { mutableStateOf(entry.releaseDate.orEmpty()) }
    var reservationStore by remember(entry.id) { mutableStateOf(entry.reservationStore.orEmpty()) }
    var note by remember(entry.id) { mutableStateOf(entry.note.orEmpty()) }

    EditorSheetContainer(
        title = tr(Res.string.editor_collection_entry_title),
        onDismiss = onDismiss,
    ) {
        GoodsPocketEditorField(name, { name = it }, tr(Res.string.field_name))
        GoodsPocketEditorField(category, { category = it }, tr(Res.string.field_category))
        Text(
            text = tr(Res.string.field_status),
            style = MaterialTheme.typography.labelLarge,
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            CollectionEntryStatus.entries.forEach { entryStatus ->
                GoodsPocketFilterChip(
                    selected = status == entryStatus,
                    onClick = { status = entryStatus },
                    label = entryStatus.localizedLabel(),
                )
            }
        }
        GoodsPocketEditorField(seriesName, { seriesName = it }, tr(Res.string.field_series))
        GoodsPocketEditorField(characterName, { characterName = it }, tr(Res.string.field_character))
        GoodsPocketEditorField(
            value = if (status == CollectionEntryStatus.RESERVED) reservationStore else purchaseStore,
            onValueChange = {
                if (status == CollectionEntryStatus.RESERVED) {
                    reservationStore = it
                } else {
                    purchaseStore = it
                }
            },
            label = tr(Res.string.field_store),
        )
        if (status == CollectionEntryStatus.RESERVED) {
            GoodsPocketEditorField(releaseDate, { releaseDate = it }, tr(Res.string.field_release_date))
        }
        GoodsPocketEditorField(note, { note = it }, tr(Res.string.field_note))
        SaveButton(
            enabled = name.isNotBlank() &&
                category.isNotBlank() &&
                if (status == CollectionEntryStatus.RESERVED) {
                    reservationStore.isNotBlank() && releaseDate.isNotBlank()
                } else {
                    true
                },
        ) {
            onSave(
                name,
                category,
                status,
                seriesName,
                characterName,
                purchaseStore,
                releaseDate,
                reservationStore,
                note,
            )
        }
    }
}

@Composable
fun EventEditorSheet(
    event: Event,
    onDismiss: () -> Unit,
    onSave: (String, String, EventType) -> Unit,
) {
    var title by remember(event.id) { mutableStateOf(event.title) }
    var targetDate by remember(event.id) { mutableStateOf(event.targetDate) }
    var eventType by remember(event.id) { mutableStateOf(event.eventType) }

    EditorSheetContainer(title = tr(Res.string.editor_event_title), onDismiss = onDismiss) {
        GoodsPocketEditorField(title, { title = it }, tr(Res.string.field_title))
        GoodsPocketEditorField(targetDate, { targetDate = it }, tr(Res.string.field_target_date))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            listOf(EventType.RELEASE, EventType.PAYMENT_DUE, EventType.DELIVERY).forEach { type ->
                GoodsPocketFilterChip(
                    selected = eventType == type,
                    onClick = { eventType = type },
                    label = type.localizedLabel(),
                )
            }
        }
        SaveButton(enabled = title.isNotBlank() && targetDate.isNotBlank()) {
            onSave(title, targetDate, eventType)
        }
    }
}

@Composable
private fun EditorSheetContainer(
    title: String,
    onDismiss: () -> Unit,
    content: @Composable () -> Unit,
) {
    GoodsPocketModalBottomSheet(
        title = title,
        onDismiss = onDismiss,
        contentSpacing = 14.dp,
    ) {
        content()
    }
}

@Composable
private fun GoodsPocketEditorField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label) },
        colors = goodsPocketOutlinedFieldColors(),
        shape = MaterialTheme.shapes.small,
        singleLine = true,
    )
}

@Composable
private fun SaveButton(
    enabled: Boolean,
    onClick: () -> Unit,
) {
    Button(
        enabled = enabled,
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(tr(Res.string.action_save_changes))
    }
}
