package goods.pocket.app.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import goods.pocket.app.domain.model.Preorder
import goods.pocket.app.domain.model.Transaction
import goods.pocket.app.domain.model.TransactionType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemEditorSheet(
    item: Item,
    onDismiss: () -> Unit,
    onSave: (String, String, String, String, String) -> Unit,
) {
    var name by remember(item.id) { mutableStateOf(item.name) }
    var category by remember(item.id) { mutableStateOf(item.category) }
    var seriesName by remember(item.id) { mutableStateOf(item.seriesName.orEmpty()) }
    var characterName by remember(item.id) { mutableStateOf(item.characterName.orEmpty()) }
    var purchaseStore by remember(item.id) { mutableStateOf(item.purchaseStore.orEmpty()) }

    EditorSheetContainer(title = "Edit Item", onDismiss = onDismiss) {
        OutlinedTextField(value = name, onValueChange = { name = it }, modifier = Modifier.fillMaxWidth(), label = { Text("Name") })
        OutlinedTextField(value = category, onValueChange = { category = it }, modifier = Modifier.fillMaxWidth(), label = { Text("Category") })
        OutlinedTextField(value = seriesName, onValueChange = { seriesName = it }, modifier = Modifier.fillMaxWidth(), label = { Text("Series") })
        OutlinedTextField(value = characterName, onValueChange = { characterName = it }, modifier = Modifier.fillMaxWidth(), label = { Text("Character") })
        OutlinedTextField(value = purchaseStore, onValueChange = { purchaseStore = it }, modifier = Modifier.fillMaxWidth(), label = { Text("Store") })
        SaveButton(enabled = name.isNotBlank() && category.isNotBlank()) {
            onSave(name, category, seriesName, characterName, purchaseStore)
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

    EditorSheetContainer(title = "Edit Preorder", onDismiss = onDismiss) {
        OutlinedTextField(value = name, onValueChange = { name = it }, modifier = Modifier.fillMaxWidth(), label = { Text("Name") })
        OutlinedTextField(value = storeName, onValueChange = { storeName = it }, modifier = Modifier.fillMaxWidth(), label = { Text("Store") })
        OutlinedTextField(value = releaseDate, onValueChange = { releaseDate = it }, modifier = Modifier.fillMaxWidth(), label = { Text("Release date") })
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

    EditorSheetContainer(title = "Edit Transaction", onDismiss = onDismiss) {
        OutlinedTextField(value = amount, onValueChange = { amount = it }, modifier = Modifier.fillMaxWidth(), label = { Text("Amount") })
        OutlinedTextField(value = transactionDate, onValueChange = { transactionDate = it }, modifier = Modifier.fillMaxWidth(), label = { Text("Date") })
        TransactionTypeSelector(selected = transactionType, onSelect = { transactionType = it })
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

    EditorSheetContainer(title = "Edit Event", onDismiss = onDismiss) {
        OutlinedTextField(value = title, onValueChange = { title = it }, modifier = Modifier.fillMaxWidth(), label = { Text("Title") })
        OutlinedTextField(value = targetDate, onValueChange = { targetDate = it }, modifier = Modifier.fillMaxWidth(), label = { Text("Target date") })
        EventTypeSelector(selected = eventType, onSelect = { eventType = it })
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
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(text = title, style = MaterialTheme.typography.titleLarge)
            content()
        }
    }
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
        Text("Save Changes")
    }
}

@Composable
private fun TransactionTypeSelector(
    selected: TransactionType,
    onSelect: (TransactionType) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        listOf(TransactionType.PURCHASE, TransactionType.DEPOSIT, TransactionType.BALANCE).forEach { type ->
            FilterChip(
                selected = selected == type,
                onClick = { onSelect(type) },
                label = { Text(type.name) },
            )
        }
    }
}

@Composable
private fun EventTypeSelector(
    selected: EventType,
    onSelect: (EventType) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        listOf(EventType.RELEASE, EventType.PAYMENT_DUE, EventType.DELIVERY).forEach { type ->
            FilterChip(
                selected = selected == type,
                onClick = { onSelect(type) },
                label = { Text(type.name) },
            )
        }
    }
}
