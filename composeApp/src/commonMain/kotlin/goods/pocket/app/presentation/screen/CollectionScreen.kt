package goods.pocket.app.presentation.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
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
import goods.pocket.app.domain.model.CollectionEntry
import goods.pocket.app.domain.model.CollectionEntryStatus
import goods.pocket.app.presentation.designsystem.GoodsPocketSectionCard
import goods.pocket.app.presentation.designsystem.GoodsPocketSectionHeader
import goods.pocket.app.presentation.designsystem.GoodsPocketTonalBadge
import goods.pocket.app.presentation.designsystem.goodsPocketOutlinedFieldColors
import goods.pocket.app.presentation.designsystem.goodsPocketPrimaryScrollContentPadding
import goods.pocket.app.presentation.designsystem.goodsPocketScreenModifier
import goods.pocket.app.presentation.designsystem.pixelShadow
import goods.pocket.app.presentation.i18n.formatCurrency
import goods.pocket.app.presentation.i18n.localizedLabel
import goods.pocket.app.presentation.i18n.tr
import goods.pocket.app.presentation.state.CollectionSegment
import goodspocket.composeapp.generated.resources.Res
import goodspocket.composeapp.generated.resources.collection_empty_search
import goodspocket.composeapp.generated.resources.collection_metric_monthly_spend
import goodspocket.composeapp.generated.resources.collection_metric_reserved_count
import goodspocket.composeapp.generated.resources.collection_search_label
import goodspocket.composeapp.generated.resources.collection_search_placeholder
import goodspocket.composeapp.generated.resources.collection_series_category
import goodspocket.composeapp.generated.resources.collection_status_section
import goodspocket.composeapp.generated.resources.collection_status_section_subtitle
import goodspocket.composeapp.generated.resources.common_not_set
import goodspocket.composeapp.generated.resources.common_unknown
import goodspocket.composeapp.generated.resources.preorders_next_release
import goodspocket.composeapp.generated.resources.preorders_store_release

@Composable
fun CollectionScreen(
    entries: List<CollectionEntry>,
    selectedSegment: CollectionSegment,
    query: String,
    onSegmentChange: (CollectionSegment) -> Unit,
    onQueryChange: (String) -> Unit,
    onEntryClick: (String) -> Unit,
) {
    val queryFilteredEntries = entries.filter { entry ->
        query.isBlank() ||
            entry.name.contains(query, ignoreCase = true) ||
            entry.seriesName?.contains(query, ignoreCase = true) == true
    }
    val visibleEntries = queryFilteredEntries.filter { entry ->
        when (selectedSegment) {
            CollectionSegment.OWNED -> entry.status != CollectionEntryStatus.RESERVED
            CollectionSegment.RESERVED -> entry.status == CollectionEntryStatus.RESERVED
            CollectionSegment.ALL -> true
        }
    }
    val ownedEntries = entries.filter { it.status != CollectionEntryStatus.RESERVED }
    val reservedEntries = entries.filter { it.status == CollectionEntryStatus.RESERVED }
    val totalSpend = ownedEntries.sumOf { it.purchasePrice ?: 0L }
    val secondarySummaryLabel = if (selectedSegment == CollectionSegment.RESERVED) {
        tr(Res.string.preorders_next_release)
    } else {
        tr(Res.string.collection_metric_reserved_count)
    }
    val secondarySummaryValue = if (selectedSegment == CollectionSegment.RESERVED) {
        reservedEntries.mapNotNull(CollectionEntry::releaseDate).minOrNull() ?: tr(Res.string.common_not_set)
    } else {
        reservedEntries.size.toString()
    }

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
                    CollectionSegment.entries.forEach { segment ->
                        CollectionSegmentCard(
                            label = segment.localizedLabel(),
                            value = entries.countVisibleFor(segment).toString(),
                            selected = selectedSegment == segment,
                            onClick = { onSegmentChange(segment) },
                        )
                    }
                }
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    CollectionSummaryCard(
                        label = tr(Res.string.collection_metric_monthly_spend),
                        value = formatCurrency(totalSpend),
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                    )
                    CollectionSummaryCard(
                        label = secondarySummaryLabel,
                        value = secondarySummaryValue,
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
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
        if (visibleEntries.isEmpty()) {
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
        items(visibleEntries, key = CollectionEntry::id) { entry ->
            GoodsPocketSectionCard(
                onClick = { onEntryClick(entry.id) },
                containerColor = MaterialTheme.colorScheme.surface,
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    val imgShape = MaterialTheme.shapes.small
                    Surface(
                        modifier = Modifier
                            .size(48.dp)
                            .pixelShadow(MaterialTheme.colorScheme.outlineVariant, shape = imgShape),
                        color = MaterialTheme.colorScheme.surfaceContainerLow,
                        shape = imgShape,
                        border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.outlineVariant),
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = entry.name.take(1),
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary,
                            )
                        }
                    }
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp)
                            .padding(vertical = 6.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        Text(
                            text = entry.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                        )
                        Text(
                            text = tr(
                                Res.string.collection_series_category,
                                entry.seriesName ?: tr(Res.string.common_unknown),
                                entry.category,
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
                                text = entry.status.localizedLabel(),
                                containerColor = collectionEntryStatusContainerColor(entry.status),
                                contentColor = collectionEntryStatusContentColor(entry.status),
                            )
                            Text(
                                text = entry.subtitle(),
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
private fun CollectionSegmentCard(
    label: String,
    value: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val shape = MaterialTheme.shapes.small
    Surface(
        modifier = Modifier
            .widthIn(min = 136.dp)
            .pixelShadow(MaterialTheme.colorScheme.outlineVariant, shape = shape)
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
        shape = shape,
        border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.outlineVariant),
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
private fun CollectionSummaryCard(
    label: String,
    value: String,
    containerColor: Color,
    contentColor: Color,
) {
    val shape = MaterialTheme.shapes.small
    Surface(
        modifier = Modifier
            .widthIn(min = 136.dp)
            .pixelShadow(MaterialTheme.colorScheme.outlineVariant, shape = shape),
        color = containerColor,
        contentColor = contentColor,
        shape = shape,
        border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.outlineVariant),
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
                color = contentColor.copy(alpha = 0.84f),
            )
        }
    }
}

@Composable
private fun collectionEntryStatusContainerColor(status: CollectionEntryStatus): Color {
    return when (status) {
        CollectionEntryStatus.RESERVED -> MaterialTheme.colorScheme.primaryContainer
        CollectionEntryStatus.OWNED -> MaterialTheme.colorScheme.secondaryContainer
        CollectionEntryStatus.PLANNED_CLEANUP -> MaterialTheme.colorScheme.tertiaryContainer
    }
}

@Composable
private fun collectionEntryStatusContentColor(status: CollectionEntryStatus): Color {
    return when (status) {
        CollectionEntryStatus.RESERVED -> MaterialTheme.colorScheme.onPrimaryContainer
        CollectionEntryStatus.OWNED -> MaterialTheme.colorScheme.onSecondaryContainer
        CollectionEntryStatus.PLANNED_CLEANUP -> MaterialTheme.colorScheme.onTertiaryContainer
    }
}

@Composable
private fun CollectionEntry.subtitle(): String {
    return if (status == CollectionEntryStatus.RESERVED) {
        tr(
            Res.string.preorders_store_release,
            reservationStore ?: tr(Res.string.common_unknown),
            releaseDate ?: tr(Res.string.common_not_set),
        )
    } else {
        purchaseStore ?: storageLocationId ?: tr(Res.string.common_unknown)
    }
}

private fun List<CollectionEntry>.countVisibleFor(segment: CollectionSegment): Int {
    return count { entry ->
        when (segment) {
            CollectionSegment.OWNED -> entry.status != CollectionEntryStatus.RESERVED
            CollectionSegment.RESERVED -> entry.status == CollectionEntryStatus.RESERVED
            CollectionSegment.ALL -> true
        }
    }
}
