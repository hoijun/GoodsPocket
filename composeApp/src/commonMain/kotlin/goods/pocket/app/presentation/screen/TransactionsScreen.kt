package goods.pocket.app.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import goods.pocket.app.domain.model.Transaction
import goods.pocket.app.domain.model.TransactionType
import goods.pocket.app.presentation.designsystem.GoodsPocketFilterChip
import goods.pocket.app.presentation.designsystem.GoodsPocketSectionCard
import goods.pocket.app.presentation.designsystem.GoodsPocketTonalBadge
import goods.pocket.app.presentation.designsystem.goodsPocketScreenModifier
import goods.pocket.app.presentation.i18n.formatCurrency
import goods.pocket.app.presentation.i18n.localizedLabel
import goods.pocket.app.presentation.i18n.tr
import goodspocket.composeapp.generated.resources.Res
import goodspocket.composeapp.generated.resources.common_all
import goodspocket.composeapp.generated.resources.common_no_place
import goodspocket.composeapp.generated.resources.transactions_empty_filter
import goodspocket.composeapp.generated.resources.transactions_incoming_total
import goodspocket.composeapp.generated.resources.transactions_month_summary
import goodspocket.composeapp.generated.resources.transactions_outgoing_total
import goodspocket.composeapp.generated.resources.transactions_visible_entries
import goodspocket.composeapp.generated.resources.transactions_visible_total

@Composable
fun TransactionsScreen(
    transactions: List<Transaction>,
    selectedType: TransactionType?,
    onTypeChange: (TransactionType?) -> Unit,
    onTransactionClick: (String) -> Unit,
) {
    val overview = buildTransactionJournalOverview(
        transactions = transactions,
        selectedType = selectedType,
    )

    LazyColumn(
        modifier = goodsPocketScreenModifier(),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        item {
            GoodsPocketSectionCard(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                contentColor = MaterialTheme.colorScheme.onSurface,
            ) {
                Text(
                    text = overview.headlineMonth.ifBlank { tr(Res.string.transactions_month_summary) },
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = tr(Res.string.transactions_visible_total, formatCurrency(overview.visibleTotalAmount)),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
        item {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                TransactionMiniMetricCard(
                    label = tr(Res.string.transactions_outgoing_total),
                    value = formatCurrency(overview.outgoingTotal),
                )
                TransactionMiniMetricCard(
                    label = tr(Res.string.transactions_incoming_total),
                    value = formatCurrency(overview.incomingTotal),
                )
                TransactionMiniMetricCard(
                    label = tr(Res.string.transactions_visible_entries),
                    value = transactions.visibleBy(selectedType).size.toString(),
                )
            }
        }
        item {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                GoodsPocketFilterChip(
                    selected = selectedType == null,
                    onClick = { onTypeChange(null) },
                    label = tr(Res.string.common_all),
                )
                listOf(
                    TransactionType.PURCHASE,
                    TransactionType.DEPOSIT,
                    TransactionType.BALANCE,
                    TransactionType.SHIPPING,
                    TransactionType.REFUND,
                    TransactionType.TRANSFER_INCOME,
                ).forEach { type ->
                    GoodsPocketFilterChip(
                        selected = selectedType == type,
                        onClick = { onTypeChange(type) },
                        label = type.localizedLabel(),
                    )
                }
            }
        }
        if (overview.dayGroups.isEmpty()) {
            item {
                GoodsPocketSectionCard(containerColor = MaterialTheme.colorScheme.surfaceContainerLow) {
                    Text(
                        text = tr(Res.string.transactions_empty_filter),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
        overview.dayGroups.forEach { dayGroup ->
            item(key = dayGroup.date) {
                Text(
                    text = dayGroup.date,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            items(dayGroup.transactions, key = Transaction::id) { transaction ->
                TransactionLedgerRow(
                    transaction = transaction,
                    onClick = { onTransactionClick(transaction.id) },
                )
            }
        }
    }
}

@Composable
private fun TransactionMiniMetricCard(
    label: String,
    value: String,
) {
    GoodsPocketSectionCard(
        modifier = Modifier.widthIn(min = 148.dp),
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Surface(
            color = MaterialTheme.colorScheme.surfaceContainerLow,
            shape = MaterialTheme.shapes.medium,
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 14.dp),
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

@Composable
private fun TransactionLedgerRow(
    transaction: Transaction,
    onClick: () -> Unit,
) {
    GoodsPocketSectionCard(
        onClick = onClick,
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                GoodsPocketTonalBadge(
                    text = transaction.type.localizedLabel(),
                    containerColor = if (transaction.type == TransactionType.REFUND || transaction.type == TransactionType.TRANSFER_INCOME) {
                        MaterialTheme.colorScheme.secondaryContainer
                    } else {
                        MaterialTheme.colorScheme.surfaceContainerLow
                    },
                    contentColor = if (transaction.type == TransactionType.REFUND || transaction.type == TransactionType.TRANSFER_INCOME) {
                        MaterialTheme.colorScheme.onSecondaryContainer
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                )
                Text(
                    text = transaction.placeName ?: tr(Res.string.common_no_place),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = transaction.paymentMethod ?: transaction.createdAt,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = formatCurrency(transaction.amount),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = transaction.transactionDate,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
