package goods.pocket.app.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import goods.pocket.app.domain.model.Event
import goods.pocket.app.domain.model.EventType
import goods.pocket.app.domain.model.Item
import goods.pocket.app.domain.model.ItemStatus
import goods.pocket.app.domain.model.Preorder
import goods.pocket.app.domain.model.Transaction
import goods.pocket.app.domain.model.TransactionType
import goods.pocket.app.presentation.designsystem.GoodsPocketBottomSheetHandle
import goods.pocket.app.presentation.designsystem.GoodsPocketFilterChip
import goods.pocket.app.presentation.designsystem.goodsPocketOutlinedFieldColors
import goods.pocket.app.presentation.i18n.localizedLabel
import goods.pocket.app.presentation.i18n.tr
import goodspocket.composeapp.generated.resources.Res
import goodspocket.composeapp.generated.resources.action_save_changes
import goodspocket.composeapp.generated.resources.editor_event_title
import goodspocket.composeapp.generated.resources.editor_item_title
import goodspocket.composeapp.generated.resources.editor_preorder_title
import goodspocket.composeapp.generated.resources.editor_transaction_title
import goodspocket.composeapp.generated.resources.field_amount
import goodspocket.composeapp.generated.resources.field_category
import goodspocket.composeapp.generated.resources.field_character
import goodspocket.composeapp.generated.resources.field_date
import goodspocket.composeapp.generated.resources.field_name
import goodspocket.composeapp.generated.resources.field_release_date
import goodspocket.composeapp.generated.resources.field_series
import goodspocket.composeapp.generated.resources.field_status
import goodspocket.composeapp.generated.resources.field_store
import goodspocket.composeapp.generated.resources.field_target_date
import goodspocket.composeapp.generated.resources.field_title

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemEditorSheet(
    item: Item,
    onDismiss: () -> Unit,
    onSave: (String, String, ItemStatus, String, String, String) -> Unit,
) {
    var name by remember(item.id) { mutableStateOf(item.name) }
    var category by remember(item.id) { mutableStateOf(item.category) }
    var status by remember(item.id) { mutableStateOf(item.status) }
    var seriesName by remember(item.id) { mutableStateOf(item.seriesName.orEmpty()) }
    var characterName by remember(item.id) { mutableStateOf(item.characterName.orEmpty()) }
    var purchaseStore by remember(item.id) { mutableStateOf(item.purchaseStore.orEmpty()) }

    EditorSheetContainer(title = tr(Res.string.editor_item_title), onDismiss = onDismiss) {
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
            listOf(ItemStatus.OWNED, ItemStatus.PLANNED_CLEANUP).forEach { itemStatus ->
                GoodsPocketFilterChip(
                    selected = status == itemStatus,
                    onClick = { status = itemStatus },
                    label = itemStatus.localizedLabel(),
                )
            }
        }
        GoodsPocketEditorField(seriesName, { seriesName = it }, tr(Res.string.field_series))
        GoodsPocketEditorField(characterName, { characterName = it }, tr(Res.string.field_character))
        GoodsPocketEditorField(purchaseStore, { purchaseStore = it }, tr(Res.string.field_store))
        SaveButton(enabled = name.isNotBlank() && category.isNotBlank()) {
            onSave(name, category, status, seriesName, characterName, purchaseStore)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreorderEditorSheet(
    preorder: Preorder,
    onDismiss: () -> Unit,
    onSave: (String, String, String) -> Unit,
) {
    var name by remember(preorder.id) { mutableStateOf(preorder.name) }
    var storeName by remember(preorder.id) { mutableStateOf(preorder.storeName) }
    var releaseDate by remember(preorder.id) { mutableStateOf(preorder.releaseDate) }

    EditorSheetContainer(title = tr(Res.string.editor_preorder_title), onDismiss = onDismiss) {
        GoodsPocketEditorField(name, { name = it }, tr(Res.string.field_name))
        GoodsPocketEditorField(storeName, { storeName = it }, tr(Res.string.field_store))
        GoodsPocketEditorField(releaseDate, { releaseDate = it }, tr(Res.string.field_release_date))
        SaveButton(enabled = name.isNotBlank() && storeName.isNotBlank() && releaseDate.isNotBlank()) {
            onSave(name, storeName, releaseDate)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionEditorSheet(
    transaction: Transaction,
    onDismiss: () -> Unit,
    onSave: (String, TransactionType, String) -> Unit,
) {
    var amount by remember(transaction.id) { mutableStateOf(transaction.amount.toString()) }
    var transactionDate by remember(transaction.id) { mutableStateOf(transaction.transactionDate) }
    var transactionType by remember(transaction.id) { mutableStateOf(transaction.type) }

    EditorSheetContainer(title = tr(Res.string.editor_transaction_title), onDismiss = onDismiss) {
        GoodsPocketEditorField(amount, { amount = it }, tr(Res.string.field_amount))
        GoodsPocketEditorField(transactionDate, { transactionDate = it }, tr(Res.string.field_date))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            listOf(TransactionType.PURCHASE, TransactionType.DEPOSIT, TransactionType.BALANCE).forEach { type ->
                GoodsPocketFilterChip(
                    selected = transactionType == type,
                    onClick = { transactionType = type },
                    label = type.localizedLabel(),
                )
            }
        }
        SaveButton(enabled = amount.isNotBlank() && transactionDate.isNotBlank()) {
            onSave(amount, transactionType, transactionDate)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditorSheetContainer(
    title: String,
    onDismiss: () -> Unit,
    content: @Composable () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        dragHandle = { GoodsPocketBottomSheetHandle() },
        containerColor = MaterialTheme.colorScheme.background,
        tonalElevation = 0.dp,
    ) {
        androidx.compose.foundation.layout.Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 10.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
            )
            content()
        }
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
