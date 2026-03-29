package goods.pocket.app.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import goods.pocket.app.domain.model.Event
import goods.pocket.app.domain.model.Item
import goods.pocket.app.domain.model.ItemStatus
import goods.pocket.app.domain.model.Preorder
import goods.pocket.app.domain.model.PreorderStatus
import goods.pocket.app.domain.model.Transaction
import goods.pocket.app.presentation.designsystem.GoodsPocketTonalBadge
import goods.pocket.app.presentation.i18n.formatCurrency
import goods.pocket.app.presentation.i18n.localizedLabel
import goods.pocket.app.presentation.i18n.tr
import goodspocket.composeapp.generated.resources.Res
import goodspocket.composeapp.generated.resources.action_cancel_preorder
import goodspocket.composeapp.generated.resources.action_delete
import goodspocket.composeapp.generated.resources.action_edit
import goodspocket.composeapp.generated.resources.action_mark_received
import goodspocket.composeapp.generated.resources.common_none
import goodspocket.composeapp.generated.resources.common_not_set
import goodspocket.composeapp.generated.resources.common_unknown
import goodspocket.composeapp.generated.resources.detail_category
import goodspocket.composeapp.generated.resources.detail_character
import goodspocket.composeapp.generated.resources.detail_date
import goodspocket.composeapp.generated.resources.detail_deposit
import goodspocket.composeapp.generated.resources.detail_linked_preorder
import goodspocket.composeapp.generated.resources.detail_linked_transactions
import goodspocket.composeapp.generated.resources.detail_location
import goodspocket.composeapp.generated.resources.detail_no_linked_transactions
import goodspocket.composeapp.generated.resources.detail_place
import goodspocket.composeapp.generated.resources.detail_related_item
import goodspocket.composeapp.generated.resources.detail_related_preorder
import goodspocket.composeapp.generated.resources.detail_release_date
import goodspocket.composeapp.generated.resources.detail_remaining
import goodspocket.composeapp.generated.resources.detail_reservation_number
import goodspocket.composeapp.generated.resources.detail_series
import goodspocket.composeapp.generated.resources.detail_status
import goodspocket.composeapp.generated.resources.detail_store
import goodspocket.composeapp.generated.resources.detail_target_date
import goodspocket.composeapp.generated.resources.detail_type

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
        ItemDetailHero(item = item)
        DetailLine(tr(Res.string.detail_category), item.category)
        DetailLine(tr(Res.string.detail_status), item.status.localizedLabel())
        DetailLine(tr(Res.string.detail_series), item.seriesName ?: tr(Res.string.common_unknown))
        DetailLine(tr(Res.string.detail_character), item.characterName ?: tr(Res.string.common_unknown))
        DetailLine(tr(Res.string.detail_store), item.purchaseStore ?: tr(Res.string.common_unknown))
        DetailLine(tr(Res.string.detail_linked_preorder), item.linkedPreorderId ?: tr(Res.string.common_none))
        Text(
            text = tr(Res.string.detail_linked_transactions),
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
        )
        if (linkedTransactions.isEmpty()) {
            Text(
                text = tr(Res.string.detail_no_linked_transactions),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        } else {
            linkedTransactions.forEach { transaction ->
                LinkedTransactionRow(transaction)
            }
        }
        SheetActionRow(
            primaryLabel = tr(Res.string.action_edit),
            onPrimary = onEdit,
            secondaryLabel = tr(Res.string.action_delete),
            onSecondary = onDelete,
            destructive = true,
        )
    }
}

@Composable
private fun ItemDetailHero(
    item: Item,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Surface(
            modifier = Modifier.size(112.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surfaceContainerLow,
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = item.name.take(1),
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
        GoodsPocketTonalBadge(
            text = item.status.localizedLabel(),
            containerColor = itemStatusContainerColor(item.status),
            contentColor = itemStatusContentColor(item.status),
        )
    }
}

@Composable
private fun itemStatusContainerColor(status: ItemStatus): androidx.compose.ui.graphics.Color {
    return when (status) {
        ItemStatus.OWNED -> MaterialTheme.colorScheme.secondaryContainer
        ItemStatus.PLANNED_CLEANUP -> MaterialTheme.colorScheme.tertiaryContainer
    }
}

@Composable
private fun itemStatusContentColor(status: ItemStatus): androidx.compose.ui.graphics.Color {
    return when (status) {
        ItemStatus.OWNED -> MaterialTheme.colorScheme.onSecondaryContainer
        ItemStatus.PLANNED_CLEANUP -> MaterialTheme.colorScheme.onTertiaryContainer
    }
}

@Composable
private fun LinkedTransactionRow(
    transaction: Transaction,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.surfaceContainerLow,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = transaction.type.localizedLabel(),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = transaction.transactionDate,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Text(
                text = formatCurrency(transaction.amount),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold,
            )
        }
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
        DetailLine(tr(Res.string.detail_store), preorder.storeName)
        DetailLine(tr(Res.string.detail_release_date), preorder.releaseDate)
        DetailLine(tr(Res.string.detail_status), preorder.status.localizedLabel())
        DetailLine(tr(Res.string.detail_deposit), formatCurrency(preorder.depositPrice ?: 0))
        DetailLine(tr(Res.string.detail_remaining), formatCurrency(preorder.remainingPrice ?: 0))
        DetailLine(tr(Res.string.detail_reservation_number), preorder.reservationNumber ?: tr(Res.string.common_not_set))
        if (preorder.status != PreorderStatus.RECEIVED && preorder.status != PreorderStatus.CANCELED) {
            Button(
                onClick = onMarkReceived,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(tr(Res.string.action_mark_received))
            }
        }
        SheetActionRow(
            primaryLabel = tr(Res.string.action_edit),
            onPrimary = onEdit,
            secondaryLabel = tr(Res.string.action_cancel_preorder),
            onSecondary = onCancel,
            destructive = true,
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
        title = formatCurrency(transaction.amount),
        onDismiss = onDismiss,
    ) {
        DetailLine(tr(Res.string.detail_type), transaction.type.localizedLabel())
        DetailLine(tr(Res.string.detail_date), transaction.transactionDate)
        DetailLine(tr(Res.string.detail_place), transaction.placeName ?: tr(Res.string.common_unknown))
        DetailLine(tr(Res.string.detail_related_item), transaction.relatedItemId ?: tr(Res.string.common_none))
        DetailLine(tr(Res.string.detail_related_preorder), transaction.relatedPreorderId ?: tr(Res.string.common_none))
        SheetActionRow(
            primaryLabel = tr(Res.string.action_edit),
            onPrimary = onEdit,
            secondaryLabel = tr(Res.string.action_delete),
            onSecondary = onDelete,
            destructive = true,
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
        DetailLine(tr(Res.string.detail_type), event.eventType.localizedLabel())
        DetailLine(tr(Res.string.detail_target_date), event.targetDate)
        DetailLine(tr(Res.string.detail_related_preorder), event.relatedPreorderId ?: tr(Res.string.common_none))
        DetailLine(tr(Res.string.detail_related_item), event.relatedItemId ?: tr(Res.string.common_none))
        DetailLine(tr(Res.string.detail_location), event.locationOrStore ?: tr(Res.string.common_unknown))
        SheetActionRow(
            primaryLabel = tr(Res.string.action_edit),
            onPrimary = onEdit,
            secondaryLabel = tr(Res.string.action_delete),
            onSecondary = onDelete,
            destructive = true,
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
        containerColor = MaterialTheme.colorScheme.background,
        tonalElevation = 0.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 10.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
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
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceContainerLow,
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}

@Composable
private fun SheetActionRow(
    primaryLabel: String,
    onPrimary: () -> Unit,
    secondaryLabel: String,
    onSecondary: () -> Unit,
    destructive: Boolean,
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
        TextButton(
            onClick = onSecondary,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = secondaryLabel,
                color = if (destructive) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
            )
        }
    }
}
