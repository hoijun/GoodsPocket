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
import goods.pocket.app.domain.model.EventType
import goods.pocket.app.domain.model.TransactionType
import goods.pocket.app.presentation.i18n.localizedLabel
import goods.pocket.app.presentation.i18n.tr
import goods.pocket.app.presentation.state.QuickAddTarget
import goodspocket.composeapp.generated.resources.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickAddSheet(
    target: QuickAddTarget,
    onTargetChange: (QuickAddTarget) -> Unit,
    onDismiss: () -> Unit,
    onSubmitItem: (String, String) -> Unit,
    onSubmitPreorder: (String, String, String) -> Unit,
    onSubmitTransaction: (String, TransactionType, String) -> Unit,
    onSubmitEvent: (String, String, EventType) -> Unit,
) {
    var itemName by remember { mutableStateOf("") }
    var itemCategory by remember { mutableStateOf("") }
    var preorderName by remember { mutableStateOf("") }
    var preorderStore by remember { mutableStateOf("") }
    var preorderReleaseDate by remember { mutableStateOf("2026-03-31") }
    var transactionAmount by remember { mutableStateOf("") }
    var transactionDate by remember { mutableStateOf("2026-03-15") }
    var transactionType by remember { mutableStateOf(TransactionType.PURCHASE) }
    var eventTitle by remember { mutableStateOf("") }
    var eventDate by remember { mutableStateOf("2026-03-25") }
    var eventType by remember { mutableStateOf(EventType.RELEASE) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = tr(Res.string.quick_add_title),
                style = MaterialTheme.typography.titleLarge,
            )

            TargetSelector(
                selected = target,
                onTargetChange = onTargetChange,
            )

            when (target) {
                QuickAddTarget.ITEM -> {
                    OutlinedTextField(
                        value = itemName,
                        onValueChange = { itemName = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(tr(Res.string.field_item_name)) },
                    )
                    OutlinedTextField(
                        value = itemCategory,
                        onValueChange = { itemCategory = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(tr(Res.string.field_category)) },
                    )
                    SubmitButton(
                        enabled = itemName.isNotBlank() && itemCategory.isNotBlank(),
                        onClick = { onSubmitItem(itemName, itemCategory) },
                    )
                }

                QuickAddTarget.PREORDER -> {
                    OutlinedTextField(
                        value = preorderName,
                        onValueChange = { preorderName = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(tr(Res.string.field_preorder_name)) },
                    )
                    OutlinedTextField(
                        value = preorderStore,
                        onValueChange = { preorderStore = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(tr(Res.string.field_store)) },
                    )
                    OutlinedTextField(
                        value = preorderReleaseDate,
                        onValueChange = { preorderReleaseDate = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(tr(Res.string.field_release_date)) },
                    )
                    SubmitButton(
                        enabled = preorderName.isNotBlank() && preorderStore.isNotBlank() && preorderReleaseDate.isNotBlank(),
                        onClick = { onSubmitPreorder(preorderName, preorderStore, preorderReleaseDate) },
                    )
                }

                QuickAddTarget.TRANSACTION -> {
                    OutlinedTextField(
                        value = transactionAmount,
                        onValueChange = { transactionAmount = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(tr(Res.string.field_amount)) },
                    )
                    OutlinedTextField(
                        value = transactionDate,
                        onValueChange = { transactionDate = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(tr(Res.string.field_date)) },
                    )
                    TransactionTypeSelector(
                        selected = transactionType,
                        onSelect = { transactionType = it },
                    )
                    SubmitButton(
                        enabled = transactionAmount.isNotBlank() && transactionDate.isNotBlank(),
                        onClick = { onSubmitTransaction(transactionAmount, transactionType, transactionDate) },
                    )
                }

                QuickAddTarget.EVENT -> {
                    OutlinedTextField(
                        value = eventTitle,
                        onValueChange = { eventTitle = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(tr(Res.string.field_event_title)) },
                    )
                    OutlinedTextField(
                        value = eventDate,
                        onValueChange = { eventDate = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(tr(Res.string.field_target_date)) },
                    )
                    EventTypeSelector(
                        selected = eventType,
                        onSelect = { eventType = it },
                    )
                    SubmitButton(
                        enabled = eventTitle.isNotBlank() && eventDate.isNotBlank(),
                        onClick = { onSubmitEvent(eventTitle, eventDate, eventType) },
                    )
                }
            }
        }
    }
}

@Composable
private fun TargetSelector(
    selected: QuickAddTarget,
    onTargetChange: (QuickAddTarget) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        QuickAddTarget.entries.forEach { target ->
            FilterChip(
                selected = target == selected,
                onClick = { onTargetChange(target) },
                label = { Text(target.localizedLabel()) },
            )
        }
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
                label = { Text(type.localizedLabel()) },
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
                label = { Text(type.localizedLabel()) },
            )
        }
    }
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
