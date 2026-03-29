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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import goods.pocket.app.domain.model.EventType
import goods.pocket.app.domain.model.TransactionType
import goods.pocket.app.presentation.designsystem.GoodsPocketBottomSheetHeader
import goods.pocket.app.presentation.designsystem.GoodsPocketFilterChip
import goods.pocket.app.presentation.designsystem.goodsPocketOutlinedFieldColors
import goods.pocket.app.presentation.i18n.localizedLabel
import goods.pocket.app.presentation.i18n.tr
import goods.pocket.app.presentation.state.QuickAddTarget
import goodspocket.composeapp.generated.resources.Res
import goodspocket.composeapp.generated.resources.action_save
import goodspocket.composeapp.generated.resources.field_amount
import goodspocket.composeapp.generated.resources.field_category
import goodspocket.composeapp.generated.resources.field_date
import goodspocket.composeapp.generated.resources.field_event_title
import goodspocket.composeapp.generated.resources.field_item_name
import goodspocket.composeapp.generated.resources.field_preorder_name
import goodspocket.composeapp.generated.resources.field_release_date
import goodspocket.composeapp.generated.resources.field_store
import goodspocket.composeapp.generated.resources.field_target_date
import goodspocket.composeapp.generated.resources.quick_add_title

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
    var preorderReleaseDate by remember { mutableStateOf("") }
    var transactionAmount by remember { mutableStateOf("") }
    var transactionDate by remember { mutableStateOf("") }
    var transactionType by remember { mutableStateOf(TransactionType.PURCHASE) }
    var eventTitle by remember { mutableStateOf("") }
    var eventDate by remember { mutableStateOf("") }
    var eventType by remember { mutableStateOf(EventType.RELEASE) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        dragHandle = null,
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
            GoodsPocketBottomSheetHeader(
                title = tr(Res.string.quick_add_title),
                fontWeight = FontWeight.SemiBold,
            )

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
                QuickAddTarget.ITEM -> {
                    GoodsPocketInputField(
                        value = itemName,
                        onValueChange = { itemName = it },
                        label = tr(Res.string.field_item_name),
                    )
                    GoodsPocketInputField(
                        value = itemCategory,
                        onValueChange = { itemCategory = it },
                        label = tr(Res.string.field_category),
                    )
                    SubmitButton(
                        enabled = itemName.isNotBlank() && itemCategory.isNotBlank(),
                        onClick = { onSubmitItem(itemName, itemCategory) },
                    )
                }

                QuickAddTarget.PREORDER -> {
                    GoodsPocketInputField(
                        value = preorderName,
                        onValueChange = { preorderName = it },
                        label = tr(Res.string.field_preorder_name),
                    )
                    GoodsPocketInputField(
                        value = preorderStore,
                        onValueChange = { preorderStore = it },
                        label = tr(Res.string.field_store),
                    )
                    GoodsPocketInputField(
                        value = preorderReleaseDate,
                        onValueChange = { preorderReleaseDate = it },
                        label = tr(Res.string.field_release_date),
                    )
                    SubmitButton(
                        enabled = preorderName.isNotBlank() && preorderStore.isNotBlank() && preorderReleaseDate.isNotBlank(),
                        onClick = { onSubmitPreorder(preorderName, preorderStore, preorderReleaseDate) },
                    )
                }

                QuickAddTarget.TRANSACTION -> {
                    GoodsPocketInputField(
                        value = transactionAmount,
                        onValueChange = { transactionAmount = it },
                        label = tr(Res.string.field_amount),
                    )
                    GoodsPocketInputField(
                        value = transactionDate,
                        onValueChange = { transactionDate = it },
                        label = tr(Res.string.field_date),
                    )
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
                    SubmitButton(
                        enabled = transactionAmount.isNotBlank() && transactionDate.isNotBlank(),
                        onClick = { onSubmitTransaction(transactionAmount, transactionType, transactionDate) },
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
