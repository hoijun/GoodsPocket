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
import goods.pocket.app.domain.model.Preorder
import goods.pocket.app.domain.model.PreorderStatus
import goods.pocket.app.presentation.i18n.formatCurrency
import goods.pocket.app.presentation.i18n.localizedLabel
import goods.pocket.app.presentation.i18n.tr
import goodspocket.composeapp.generated.resources.*

@Composable
fun PreordersScreen(
    preorders: List<Preorder>,
    selectedStatus: PreorderStatus?,
    onStatusChange: (PreorderStatus?) -> Unit,
    onPreorderClick: (String) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = tr(Res.string.preorders_status_filter),
                    style = MaterialTheme.typography.titleSmall,
                )
                androidx.compose.foundation.layout.Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    FilterChip(
                        selected = selectedStatus == null,
                        onClick = { onStatusChange(null) },
                        label = { Text(tr(Res.string.common_all)) },
                    )
                    listOf(
                        PreorderStatus.ACTIVE,
                        PreorderStatus.PAYMENT_PENDING,
                        PreorderStatus.RECEIVED,
                        PreorderStatus.CANCELED,
                    ).forEach { status ->
                        FilterChip(
                            selected = selectedStatus == status,
                            onClick = { onStatusChange(status) },
                            label = { Text(status.localizedLabel()) },
                        )
                    }
                }
            }
        }
        if (preorders.isEmpty()) {
            item {
                Text(
                    text = tr(Res.string.preorders_empty_filter),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
        items(preorders, key = Preorder::id) { preorder ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onPreorderClick(preorder.id) },
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Text(text = preorder.name, style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = tr(
                            Res.string.preorders_store_release,
                            preorder.storeName,
                            preorder.releaseDate,
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Text(
                        text = tr(
                            Res.string.preorders_status_remaining,
                            preorder.status.localizedLabel(),
                            formatCurrency(preorder.remainingPrice ?: 0),
                        ),
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
        }
    }
}
