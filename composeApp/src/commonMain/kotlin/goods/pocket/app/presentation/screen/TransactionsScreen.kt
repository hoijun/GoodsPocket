package goods.pocket.app.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import goods.pocket.app.domain.model.Transaction
import goods.pocket.app.domain.model.TransactionType
import goods.pocket.app.presentation.designsystem.GoodsPocketFilterChip
import goods.pocket.app.presentation.designsystem.GoodsPocketSectionCard
import goods.pocket.app.presentation.designsystem.GoodsPocketTonalBadge
import goods.pocket.app.presentation.i18n.formatCurrency
import goods.pocket.app.presentation.i18n.localizedLabel
import goods.pocket.app.presentation.i18n.tr
import goodspocket.composeapp.generated.resources.Res
import goodspocket.composeapp.generated.resources.common_all
import goodspocket.composeapp.generated.resources.common_no_place
import goodspocket.composeapp.generated.resources.transactions_empty_filter
import goodspocket.composeapp.generated.resources.transactions_month_summary
import goodspocket.composeapp.generated.resources.transactions_row_subtitle
import goodspocket.composeapp.generated.resources.transactions_visible_total

@Composable
fun TransactionsScreen(
    transactions: List<Transaction>,
    selectedType: TransactionType?,
    onTypeChange: (TransactionType?) -> Unit,
    onTransactionClick: (String) -> Unit,
) {
    val totalAmount = transactions.sumOf { it.amount }
    LazyColumn(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        item {
            GoodsPocketSectionCard(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            ) {
                Text(
                    text = tr(Res.string.transactions_month_summary),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = tr(Res.string.transactions_visible_total, formatCurrency(totalAmount)),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
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
                ).forEach { type ->
                    GoodsPocketFilterChip(
                        selected = selectedType == type,
                        onClick = { onTypeChange(type) },
                        label = type.localizedLabel(),
                    )
                }
            }
        }
        if (transactions.isEmpty()) {
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
        items(transactions, key = Transaction::id) { transaction ->
            GoodsPocketSectionCard(
                onClick = { onTransactionClick(transaction.id) },
                containerColor = MaterialTheme.colorScheme.surface,
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = formatCurrency(transaction.amount),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                    GoodsPocketTonalBadge(text = transaction.type.localizedLabel())
                }
                Text(
                    text = tr(
                        Res.string.transactions_row_subtitle,
                        transaction.type.localizedLabel(),
                        transaction.transactionDate,
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                )
                Text(
                    text = transaction.placeName ?: tr(Res.string.common_no_place),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
