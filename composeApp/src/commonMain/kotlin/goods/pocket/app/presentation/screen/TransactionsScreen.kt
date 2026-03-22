package goods.pocket.app.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import goods.pocket.app.domain.model.Transaction
import goods.pocket.app.domain.model.TransactionType
import goods.pocket.app.presentation.i18n.formatCurrency
import goods.pocket.app.presentation.i18n.localizedLabel
import goods.pocket.app.presentation.i18n.tr
import goodspocket.composeapp.generated.resources.*

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
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text = tr(Res.string.transactions_month_summary),
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Text(
                        text = tr(Res.string.transactions_visible_total, formatCurrency(totalAmount)),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }
        }
        item {
            androidx.compose.foundation.layout.Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                FilterChip(
                    selected = selectedType == null,
                    onClick = { onTypeChange(null) },
                    label = { Text(tr(Res.string.common_all)) },
                )
                listOf(
                    TransactionType.PURCHASE,
                    TransactionType.DEPOSIT,
                    TransactionType.BALANCE,
                ).forEach { type ->
                    FilterChip(
                        selected = selectedType == type,
                        onClick = { onTypeChange(type) },
                        label = { Text(type.localizedLabel()) },
                    )
                }
            }
        }
        if (transactions.isEmpty()) {
            item {
                Text(
                    text = tr(Res.string.transactions_empty_filter),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
        items(transactions, key = Transaction::id) { transaction ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onTransactionClick(transaction.id) },
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Text(text = formatCurrency(transaction.amount), style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = tr(Res.string.transactions_row_subtitle, transaction.type.localizedLabel(), transaction.transactionDate),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Text(
                        text = transaction.placeName ?: tr(Res.string.common_no_place),
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
        }
    }
}
