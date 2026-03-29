package goods.pocket.app.presentation.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import goods.pocket.app.domain.model.Item
import goods.pocket.app.domain.model.ItemStatus
import goods.pocket.app.presentation.designsystem.GoodsPocketMetricPill
import goods.pocket.app.presentation.designsystem.GoodsPocketSectionCard
import goods.pocket.app.presentation.designsystem.GoodsPocketSectionHeader
import goods.pocket.app.presentation.designsystem.GoodsPocketTonalBadge
import goods.pocket.app.presentation.designsystem.goodsPocketOutlinedFieldColors
import goods.pocket.app.presentation.designsystem.goodsPocketPrimaryScrollContentPadding
import goods.pocket.app.presentation.designsystem.goodsPocketScreenModifier
import goods.pocket.app.presentation.i18n.formatCurrency
import goods.pocket.app.presentation.i18n.localizedLabel
import goods.pocket.app.presentation.i18n.tr
import goodspocket.composeapp.generated.resources.Res
import goodspocket.composeapp.generated.resources.collection_empty_search
import goodspocket.composeapp.generated.resources.collection_metric_monthly_spend
import goodspocket.composeapp.generated.resources.collection_metric_total_items
import goodspocket.composeapp.generated.resources.collection_quantity_badge
import goodspocket.composeapp.generated.resources.collection_search_label
import goodspocket.composeapp.generated.resources.collection_search_placeholder
import goodspocket.composeapp.generated.resources.collection_series_category
import goodspocket.composeapp.generated.resources.collection_status_section
import goodspocket.composeapp.generated.resources.collection_status_section_subtitle
import goodspocket.composeapp.generated.resources.common_unknown

@Composable
fun CollectionScreen(
    items: List<Item>,
    selectedStatus: ItemStatus,
    query: String,
    onStatusChange: (ItemStatus) -> Unit,
    onQueryChange: (String) -> Unit,
    onItemClick: (String) -> Unit,
) {
    val visibleItems = items.filter { it.status == selectedStatus }
    val totalSpend = items.sumOf { it.purchasePrice ?: 0L }
    val highlightedCategories = visibleItems.map(Item::category).distinct().take(4)
    val summaryMetricShape = RoundedCornerShape(14.dp)
    val summaryMetricPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)

    LazyColumn(
        modifier = goodsPocketScreenModifier(),
        contentPadding = goodsPocketPrimaryScrollContentPadding(),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        item {
            GoodsPocketSectionCard(containerColor = MaterialTheme.colorScheme.surfaceContainerLow) {
                GoodsPocketSectionHeader(
                    title = tr(Res.string.collection_status_section),
                    subtitle = tr(Res.string.collection_status_section_subtitle),
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    listOf(ItemStatus.OWNED, ItemStatus.PLANNED_CLEANUP).forEach { status ->
                        CollectionStatusCard(
                            label = status.localizedLabel(),
                            value = items.count { it.status == status }.toString(),
                            selected = selectedStatus == status,
                            onClick = { onStatusChange(status) },
                        )
                    }
                }
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
                }
            }
        }
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
        if (highlightedCategories.isNotEmpty()) {
            item {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    highlightedCategories.forEach { category ->
                        GoodsPocketTonalBadge(text = category)
                    }
                }
            }
        }
        if (visibleItems.isEmpty()) {
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
        items(visibleItems, key = Item::id) { item ->
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
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 4.dp)
                            .padding(vertical = 2.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
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
    }
}

@Composable
private fun CollectionStatusCard(
    label: String,
    value: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Surface(
        modifier = Modifier
            .widthIn(min = 136.dp)
            .clickable(onClick = onClick),
        color = if (selected) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surface
        },
        contentColor = if (selected) {
            MaterialTheme.colorScheme.onPrimaryContainer
        } else {
            MaterialTheme.colorScheme.onSurface
        },
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = if (selected) {
                    MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
            )
        }
    }
}

@Composable
private fun collectionStatusContainerColor(status: ItemStatus): Color {
    return when (status) {
        ItemStatus.OWNED -> MaterialTheme.colorScheme.secondaryContainer
        ItemStatus.PLANNED_CLEANUP -> MaterialTheme.colorScheme.tertiaryContainer
    }
}

@Composable
private fun collectionStatusContentColor(status: ItemStatus): Color {
    return when (status) {
        ItemStatus.OWNED -> MaterialTheme.colorScheme.onSecondaryContainer
        ItemStatus.PLANNED_CLEANUP -> MaterialTheme.colorScheme.onTertiaryContainer
    }
}
