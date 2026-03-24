package goods.pocket.app.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import goods.pocket.app.domain.model.Preorder
import goods.pocket.app.domain.model.PreorderStatus
import goods.pocket.app.presentation.designsystem.GoodsPocketFilterChip
import goods.pocket.app.presentation.designsystem.GoodsPocketSectionCard
import goods.pocket.app.presentation.designsystem.GoodsPocketTonalBadge
import goods.pocket.app.presentation.designsystem.goodsPocketScreenModifier
import goods.pocket.app.presentation.i18n.formatCurrency
import goods.pocket.app.presentation.i18n.localizedLabel
import goods.pocket.app.presentation.i18n.tr
import goodspocket.composeapp.generated.resources.Res
import goodspocket.composeapp.generated.resources.common_all
import goodspocket.composeapp.generated.resources.common_not_set
import goodspocket.composeapp.generated.resources.preorders_arriving
import goodspocket.composeapp.generated.resources.preorders_empty_filter
import goodspocket.composeapp.generated.resources.preorders_next_release
import goodspocket.composeapp.generated.resources.preorders_pending_payment_short
import goodspocket.composeapp.generated.resources.preorders_status_filter
import goodspocket.composeapp.generated.resources.preorders_tracker_summary
import goodspocket.composeapp.generated.resources.preorders_tracking_items
import goodspocket.composeapp.generated.resources.preorders_visible_remaining

@Composable
fun PreordersScreen(
    preorders: List<Preorder>,
    selectedStatus: PreorderStatus?,
    onStatusChange: (PreorderStatus?) -> Unit,
    onPreorderClick: (String) -> Unit,
) {
    val visiblePreorders = preorders.visibleBy(selectedStatus)
    val overview = buildPreorderJournalOverview(
        preorders = preorders,
        selectedStatus = selectedStatus,
    )

    LazyColumn(
        modifier = goodsPocketScreenModifier(),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        item {
            PreorderSummaryCard(overview = overview)
        }
        item {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                PreorderMiniMetricCard(
                    label = tr(Res.string.preorders_visible_remaining),
                    value = formatCurrency(overview.visibleRemainingTotal),
                )
                PreorderMiniMetricCard(
                    label = tr(Res.string.preorders_next_release),
                    value = overview.nextReleaseDate ?: tr(Res.string.common_not_set),
                )
                PreorderMiniMetricCard(
                    label = tr(Res.string.preorders_pending_payment_short),
                    value = overview.pendingPaymentCount.toString(),
                )
            }
        }
        item {
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
        if (visiblePreorders.isEmpty()) {
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
        items(visiblePreorders, key = Preorder::id) { preorder ->
            PreorderJournalRow(
                preorder = preorder,
                onClick = { onPreorderClick(preorder.id) },
            )
        }
    }
}

@Composable
private fun PreorderSummaryCard(
    overview: PreorderJournalOverview,
) {
    GoodsPocketSectionCard(
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = tr(Res.string.preorders_status_filter).uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = tr(Res.string.preorders_tracker_summary),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                PreorderCounter(
                    label = tr(Res.string.preorders_tracking_items),
                    value = overview.visibleCount.toString(),
                )
                PreorderCounter(
                    label = tr(Res.string.preorders_arriving),
                    value = overview.arrivingCount.toString(),
                )
            }
        }
    }
}

@Composable
private fun PreorderCounter(
    label: String,
    value: String,
) {
    Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Composable
private fun PreorderMiniMetricCard(
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
            shape = RoundedCornerShape(18.dp),
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
private fun PreorderJournalRow(
    preorder: Preorder,
    onClick: () -> Unit,
) {
    GoodsPocketSectionCard(
        onClick = onClick,
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(
                modifier = Modifier.size(44.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surfaceContainerLow,
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = preorder.name.take(1),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top,
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(3.dp),
                    ) {
                        Text(
                            text = preorder.storeName,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Text(
                            text = preorder.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                    Text(
                        text = preorder.releaseDate,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = preorder.remainingPrice?.let { formatCurrency(it) }
                            ?: preorder.totalPrice?.let { formatCurrency(it) }
                            ?: tr(Res.string.common_not_set),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    GoodsPocketTonalBadge(
                        text = preorder.status.localizedLabel(),
                        containerColor = preorderStatusContainerColor(preorder.status),
                        contentColor = preorderStatusContentColor(preorder.status),
                    )
                }
            }
        }
    }
}

@Composable
private fun preorderStatusContainerColor(status: PreorderStatus): Color {
    return when (status) {
        PreorderStatus.ACTIVE -> MaterialTheme.colorScheme.primaryContainer
        PreorderStatus.PAYMENT_PENDING -> MaterialTheme.colorScheme.tertiaryContainer
        PreorderStatus.RECEIVED -> MaterialTheme.colorScheme.secondaryContainer
        PreorderStatus.CANCELED -> MaterialTheme.colorScheme.errorContainer
    }
}

@Composable
private fun preorderStatusContentColor(status: PreorderStatus): Color {
    return when (status) {
        PreorderStatus.ACTIVE -> MaterialTheme.colorScheme.onPrimaryContainer
        PreorderStatus.PAYMENT_PENDING -> MaterialTheme.colorScheme.onTertiaryContainer
        PreorderStatus.RECEIVED -> MaterialTheme.colorScheme.onSecondaryContainer
        PreorderStatus.CANCELED -> MaterialTheme.colorScheme.onErrorContainer
    }
}
