package goods.pocket.app.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import goods.pocket.app.domain.model.Item
import goods.pocket.app.domain.model.ItemStatus
import goods.pocket.app.presentation.designsystem.GoodsPocketMetricPill
import goods.pocket.app.presentation.designsystem.GoodsPocketSectionCard
import goods.pocket.app.presentation.designsystem.GoodsPocketTonalBadge
import goods.pocket.app.presentation.designsystem.goodsPocketOutlinedFieldColors
import goods.pocket.app.presentation.designsystem.goodsPocketPrimaryScrollContentPadding
import goods.pocket.app.presentation.designsystem.goodsPocketScreenModifier
import goods.pocket.app.presentation.i18n.formatCurrency
import goods.pocket.app.presentation.i18n.localizedLabel
import goods.pocket.app.presentation.i18n.tr
import goodspocket.composeapp.generated.resources.Res
import goodspocket.composeapp.generated.resources.collection_badge_query
import goodspocket.composeapp.generated.resources.collection_badge_total
import goodspocket.composeapp.generated.resources.collection_empty_search
import goodspocket.composeapp.generated.resources.collection_metric_monthly_spend
import goodspocket.composeapp.generated.resources.collection_metric_total_items
import goodspocket.composeapp.generated.resources.collection_metric_waiting
import goodspocket.composeapp.generated.resources.collection_quantity_badge
import goodspocket.composeapp.generated.resources.collection_search_label
import goodspocket.composeapp.generated.resources.collection_search_placeholder
import goodspocket.composeapp.generated.resources.collection_series_category
import goodspocket.composeapp.generated.resources.common_unknown

@Composable
fun CollectionScreen(
    items: List<Item>,
    query: String,
    onQueryChange: (String) -> Unit,
    onItemClick: (String) -> Unit,
) {
    val totalSpend = items.sumOf { it.purchasePrice ?: 0L }
    val waitingCount = items.count { it.status == ItemStatus.WAITING_DELIVERY }
    val highlightedCategories = items.map(Item::category).distinct().take(4)
    val summaryMetricShape = RoundedCornerShape(14.dp)
    val summaryMetricPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)

    LazyColumn(
        modifier = goodsPocketScreenModifier(),
        contentPadding = goodsPocketPrimaryScrollContentPadding(),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        item {
            OutlinedTextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(tr(Res.string.collection_search_label)) },
                placeholder = { Text(tr(Res.string.collection_search_placeholder)) },
                colors = goodsPocketOutlinedFieldColors(),
                singleLine = true,
                shape = MaterialTheme.shapes.medium,
            )
        }
        item {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                GoodsPocketTonalBadge(text = tr(Res.string.collection_badge_total, items.size))
                if (query.isNotBlank()) {
                    GoodsPocketTonalBadge(text = tr(Res.string.collection_badge_query, query))
                }
                highlightedCategories.forEach { category ->
                    GoodsPocketTonalBadge(text = category)
                }
            }
        }
        if (items.isEmpty()) {
            item {
                GoodsPocketSectionCard(containerColor = MaterialTheme.colorScheme.surfaceContainerLow) {
                    Text(
                        text = tr(Res.string.collection_empty_search),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
        items(items, key = Item::id) { item ->
            GoodsPocketSectionCard(
                onClick = { onItemClick(item.id) },
                containerColor = MaterialTheme.colorScheme.surface,
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Surface(
                        modifier = Modifier.size(48.dp),
                        color = MaterialTheme.colorScheme.surfaceContainerLow,
                        shape = RoundedCornerShape(16.dp),
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = item.name.take(1),
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
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = item.name,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                            )
                            GoodsPocketTonalBadge(
                                text = tr(Res.string.collection_quantity_badge, item.quantity),
                                containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                            )
                        }
                        Text(
                            text = tr(
                                Res.string.collection_series_category,
                                item.seriesName ?: tr(Res.string.common_unknown),
                                item.category,
                            ),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            GoodsPocketTonalBadge(
                                text = item.status.localizedLabel(),
                                containerColor = collectionStatusContainerColor(item.status),
                                contentColor = collectionStatusContentColor(item.status),
                            )
                            Text(
                                text = item.purchaseStore ?: item.storageLocationId ?: tr(Res.string.common_unknown),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }
            }
        }
        if (items.isNotEmpty()) {
            item {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    GoodsPocketMetricPill(
                        label = tr(Res.string.collection_metric_total_items),
                        value = items.size.toString(),
                        shape = summaryMetricShape,
                        contentPadding = summaryMetricPadding,
                    )
                    GoodsPocketMetricPill(
                        label = tr(Res.string.collection_metric_monthly_spend),
                        value = formatCurrency(totalSpend),
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                        shape = summaryMetricShape,
                        contentPadding = summaryMetricPadding,
                    )
                    GoodsPocketMetricPill(
                        label = tr(Res.string.collection_metric_waiting),
                        value = waitingCount.toString(),
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        shape = summaryMetricShape,
                        contentPadding = summaryMetricPadding,
                    )
                }
            }
        }
    }
}

@Composable
private fun collectionStatusContainerColor(status: ItemStatus): Color {
    return when (status) {
        ItemStatus.OWNED -> MaterialTheme.colorScheme.secondaryContainer
        ItemStatus.WAITING_DELIVERY -> MaterialTheme.colorScheme.primaryContainer
        ItemStatus.PLANNED_TRANSFER -> MaterialTheme.colorScheme.surfaceContainerHigh
        ItemStatus.LOST -> MaterialTheme.colorScheme.errorContainer
    }
}

@Composable
private fun collectionStatusContentColor(status: ItemStatus): Color {
    return when (status) {
        ItemStatus.OWNED -> MaterialTheme.colorScheme.onSecondaryContainer
        ItemStatus.WAITING_DELIVERY -> MaterialTheme.colorScheme.onPrimaryContainer
        ItemStatus.PLANNED_TRANSFER -> MaterialTheme.colorScheme.onSurfaceVariant
        ItemStatus.LOST -> MaterialTheme.colorScheme.onErrorContainer
    }
}
