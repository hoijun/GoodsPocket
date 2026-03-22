package goods.pocket.app.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import goods.pocket.app.domain.model.Event
import goods.pocket.app.domain.model.Item
import goods.pocket.app.domain.model.Preorder
import goods.pocket.app.domain.model.PreorderStatus
import goods.pocket.app.domain.model.Transaction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemDetailSheet(
    item: Item,
    linkedTransactions: List<Transaction>,
    onDismiss: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    DetailSheetContainer(
        title = item.name,
        onDismiss = onDismiss,
    ) {
        DetailLine("Category", item.category)
        DetailLine("Status", item.status.name)
        DetailLine("Series", item.seriesName ?: "Unknown")
        DetailLine("Character", item.characterName ?: "Unknown")
        DetailLine("Store", item.purchaseStore ?: "Unknown")
        DetailLine("Linked preorder", item.linkedPreorderId ?: "None")
        Text(
            text = "Linked transactions",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
        )
        if (linkedTransactions.isEmpty()) {
            Text("No linked transactions yet.")
        } else {
            linkedTransactions.forEach { transaction ->
                Text("${transaction.transactionDate} · ${transaction.type} · ${transaction.amount} KRW")
            }
        }
        SheetActionRow(
            primaryLabel = "Edit",
            onPrimary = onEdit,
            secondaryLabel = "Delete",
            onSecondary = onDelete,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreorderDetailSheet(
    preorder: Preorder,
    onDismiss: () -> Unit,
    onMarkReceived: () -> Unit,
    onEdit: () -> Unit,
    onCancel: () -> Unit,
) {
    DetailSheetContainer(
        title = preorder.name,
        onDismiss = onDismiss,
    ) {
        DetailLine("Store", preorder.storeName)
        DetailLine("Release date", preorder.releaseDate)
        DetailLine("Status", preorder.status.name)
        DetailLine("Deposit", "${preorder.depositPrice ?: 0} KRW")
        DetailLine("Remaining", "${preorder.remainingPrice ?: 0} KRW")
        DetailLine("Reservation no.", preorder.reservationNumber ?: "Not set")
        if (preorder.status != PreorderStatus.RECEIVED && preorder.status != PreorderStatus.CANCELED) {
            Button(
                onClick = onMarkReceived,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Mark As Received")
            }
        }
        SheetActionRow(
            primaryLabel = "Edit",
            onPrimary = onEdit,
            secondaryLabel = "Cancel Preorder",
            onSecondary = onCancel,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionDetailSheet(
    transaction: Transaction,
    onDismiss: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    DetailSheetContainer(
        title = "${transaction.amount} KRW",
        onDismiss = onDismiss,
    ) {
        DetailLine("Type", transaction.type.name)
        DetailLine("Date", transaction.transactionDate)
        DetailLine("Place", transaction.placeName ?: "Unknown")
        DetailLine("Related item", transaction.relatedItemId ?: "None")
        DetailLine("Related preorder", transaction.relatedPreorderId ?: "None")
        SheetActionRow(
            primaryLabel = "Edit",
            onPrimary = onEdit,
            secondaryLabel = "Delete",
            onSecondary = onDelete,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailSheet(
    event: Event,
    onDismiss: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    DetailSheetContainer(
        title = event.title,
        onDismiss = onDismiss,
    ) {
        DetailLine("Type", event.eventType.name)
        DetailLine("Target date", event.targetDate)
        DetailLine("Linked preorder", event.relatedPreorderId ?: "None")
        DetailLine("Linked item", event.relatedItemId ?: "None")
        DetailLine("Location", event.locationOrStore ?: "Unknown")
        SheetActionRow(
            primaryLabel = "Edit",
            onPrimary = onEdit,
            secondaryLabel = "Delete",
            onSecondary = onDelete,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailSheetContainer(
    title: String,
    onDismiss: () -> Unit,
    content: @Composable () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )
            content()
        }
    }
}

@Composable
private fun DetailLine(
    label: String,
    value: String,
) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Composable
private fun SheetActionRow(
    primaryLabel: String,
    onPrimary: () -> Unit,
    secondaryLabel: String,
    onSecondary: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Button(
            onClick = onPrimary,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(primaryLabel)
        }
        OutlinedButton(
            onClick = onSecondary,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(secondaryLabel)
        }
    }
}
