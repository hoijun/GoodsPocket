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
import goods.pocket.app.domain.model.Preorder
import goods.pocket.app.domain.model.PreorderStatus
import goods.pocket.app.presentation.designsystem.GoodsPocketFilterChip
import goods.pocket.app.presentation.designsystem.GoodsPocketSectionCard
import goods.pocket.app.presentation.designsystem.GoodsPocketTonalBadge
import goods.pocket.app.presentation.i18n.formatCurrency
import goods.pocket.app.presentation.i18n.localizedLabel
import goods.pocket.app.presentation.i18n.tr
import goodspocket.composeapp.generated.resources.Res
import goodspocket.composeapp.generated.resources.common_all
import goodspocket.composeapp.generated.resources.preorders_empty_filter
import goodspocket.composeapp.generated.resources.preorders_status_filter
import goodspocket.composeapp.generated.resources.preorders_status_remaining
import goodspocket.composeapp.generated.resources.preorders_store_release

@Composable
fun PreordersScreen(
    preorders: List<Preorder>,
    selectedStatus: PreorderStatus?,
    onStatusChange: (PreorderStatus?) -> Unit,
    onPreorderClick: (String) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        item {
            GoodsPocketSectionCard(
                containerColor = MaterialTheme.colorScheme.surface,
            ) {
                Text(
                    text = tr(Res.string.preorders_status_filter),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    GoodsPocketFilterChip(
                        selected = selectedStatus == null,
                        onClick = { onStatusChange(null) },
                        label = tr(Res.string.common_all),
                    )
                    listOf(
                        PreorderStatus.ACTIVE,
                        PreorderStatus.PAYMENT_PENDING,
                        PreorderStatus.RECEIVED,
                        PreorderStatus.CANCELED,
                    ).forEach { status ->
                        GoodsPocketFilterChip(
                            selected = selectedStatus == status,
                            onClick = { onStatusChange(status) },
                            label = status.localizedLabel(),
                        )
                    }
                }
            }
        }
        if (preorders.isEmpty()) {
            item {
                GoodsPocketSectionCard(containerColor = MaterialTheme.colorScheme.surfaceContainerLow) {
                    Text(
                        text = tr(Res.string.preorders_empty_filter),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
        items(preorders, key = Preorder::id) { preorder ->
            GoodsPocketSectionCard(
                onClick = { onPreorderClick(preorder.id) },
                containerColor = MaterialTheme.colorScheme.surface,
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = preorder.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                    GoodsPocketTonalBadge(
                        text = preorder.status.localizedLabel(),
                        containerColor = if (preorder.status == PreorderStatus.PAYMENT_PENDING) {
                            MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.32f)
                        } else {
                            MaterialTheme.colorScheme.surfaceContainerHigh
                        },
                        contentColor = if (preorder.status == PreorderStatus.PAYMENT_PENDING) {
                            MaterialTheme.colorScheme.error
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        },
                    )
                }
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
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
