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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import goods.pocket.app.domain.model.CollectionEntryStatus
import goods.pocket.app.domain.model.EventType
import goods.pocket.app.presentation.designsystem.GoodsPocketFilterChip
import goods.pocket.app.presentation.designsystem.GoodsPocketModalBottomSheet
import goods.pocket.app.presentation.designsystem.goodsPocketOutlinedFieldColors
import goods.pocket.app.presentation.i18n.localizedLabel
import goods.pocket.app.presentation.i18n.tr
import goods.pocket.app.presentation.state.QuickAddTarget
import goodspocket.composeapp.generated.resources.Res
import goodspocket.composeapp.generated.resources.action_save
import goodspocket.composeapp.generated.resources.field_category
import goodspocket.composeapp.generated.resources.field_character
import goodspocket.composeapp.generated.resources.field_event_title
import goodspocket.composeapp.generated.resources.field_item_name
import goodspocket.composeapp.generated.resources.field_note
import goodspocket.composeapp.generated.resources.field_release_date
import goodspocket.composeapp.generated.resources.field_series
import goodspocket.composeapp.generated.resources.field_status
import goodspocket.composeapp.generated.resources.field_store
import goodspocket.composeapp.generated.resources.field_target_date
import goodspocket.composeapp.generated.resources.quick_add_title

@Composable
fun QuickAddSheet(
    target: QuickAddTarget,
    onTargetChange: (QuickAddTarget) -> Unit,
    onDismiss: () -> Unit,
    onSubmitCollectionEntry: (
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
    onSubmitEvent: (String, String, EventType) -> Unit,
) {
    var entryName by remember { mutableStateOf("") }
    var entryCategory by remember { mutableStateOf("") }
    var entryStatus by remember { mutableStateOf(CollectionEntryStatus.OWNED) }
    var entrySeries by remember { mutableStateOf("") }
    var entryCharacter by remember { mutableStateOf("") }
    var purchaseStore by remember { mutableStateOf("") }
    var releaseDate by remember { mutableStateOf("") }
    var reservationStore by remember { mutableStateOf("") }
    var entryNote by remember { mutableStateOf("") }
    var eventTitle by remember { mutableStateOf("") }
    var eventDate by remember { mutableStateOf("") }
    var eventType by remember { mutableStateOf(EventType.RELEASE) }

    GoodsPocketModalBottomSheet(
        title = tr(Res.string.quick_add_title),
        onDismiss = onDismiss,
        titleFontWeight = FontWeight.SemiBold,
        contentSpacing = 14.dp,
    ) {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            QuickAddTarget.entries.forEach { entry ->
                GoodsPocketFilterChip(
                    selected = target == entry,
                    onClick = { onTargetChange(entry) },
                    label = entry.localizedLabel(),
                )
            }
        }

        when (target) {
            QuickAddTarget.COLLECTION_ENTRY -> {
                GoodsPocketInputField(
                    value = entryName,
                    onValueChange = { entryName = it },
                    label = tr(Res.string.field_item_name),
                )
                GoodsPocketInputField(
                    value = entryCategory,
                    onValueChange = { entryCategory = it },
                    label = tr(Res.string.field_category),
                )
                Text(
                    text = tr(Res.string.field_status),
                    style = MaterialTheme.typography.labelLarge,
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    listOf(
                        CollectionEntryStatus.RESERVED,
                        CollectionEntryStatus.OWNED,
                        CollectionEntryStatus.PLANNED_CLEANUP,
                    ).forEach { status ->
                        GoodsPocketFilterChip(
                            selected = entryStatus == status,
                            onClick = { entryStatus = status },
                            label = status.localizedLabel(),
                        )
                    }
                }
                GoodsPocketInputField(
                    value = entrySeries,
                    onValueChange = { entrySeries = it },
                    label = tr(Res.string.field_series),
                )
                GoodsPocketInputField(
                    value = entryCharacter,
                    onValueChange = { entryCharacter = it },
                    label = tr(Res.string.field_character),
                )
                GoodsPocketInputField(
                    value = if (entryStatus == CollectionEntryStatus.RESERVED) {
                        reservationStore
                    } else {
                        purchaseStore
                    },
                    onValueChange = {
                        if (entryStatus == CollectionEntryStatus.RESERVED) {
                            reservationStore = it
                        } else {
                            purchaseStore = it
                        }
                    },
                    label = tr(Res.string.field_store),
                )
                if (entryStatus == CollectionEntryStatus.RESERVED) {
                    GoodsPocketInputField(
                        value = releaseDate,
                        onValueChange = { releaseDate = it },
                        label = tr(Res.string.field_release_date),
                    )
                }
                GoodsPocketInputField(
                    value = entryNote,
                    onValueChange = { entryNote = it },
                    label = tr(Res.string.field_note),
                )
                SubmitButton(
                    enabled = entryName.isNotBlank() &&
                        entryCategory.isNotBlank() &&
                        if (entryStatus == CollectionEntryStatus.RESERVED) {
                            reservationStore.isNotBlank() && releaseDate.isNotBlank()
                        } else {
                            true
                        },
                    onClick = {
                        onSubmitCollectionEntry(
                            entryName,
                            entryCategory,
                            entryStatus,
                            entrySeries,
                            entryCharacter,
                            purchaseStore,
                            releaseDate,
                            reservationStore,
                            entryNote,
                        )
                    },
                )
            }

            QuickAddTarget.EVENT -> {
                GoodsPocketInputField(
                    value = eventTitle,
                    onValueChange = { eventTitle = it },
                    label = tr(Res.string.field_event_title),
                )
                GoodsPocketInputField(
                    value = eventDate,
                    onValueChange = { eventDate = it },
                    label = tr(Res.string.field_target_date),
                )
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
                SubmitButton(
                    enabled = eventTitle.isNotBlank() && eventDate.isNotBlank(),
                    onClick = { onSubmitEvent(eventTitle, eventDate, eventType) },
                )
            }
        }
    }
}

@Composable
private fun GoodsPocketInputField(
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
private fun SubmitButton(
    enabled: Boolean,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(tr(Res.string.action_save))
    }
}
