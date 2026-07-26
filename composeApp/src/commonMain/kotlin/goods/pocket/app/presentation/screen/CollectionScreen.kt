package goods.pocket.app.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import goods.pocket.app.domain.model.CollectionEntry
import goods.pocket.app.domain.model.CollectionEntryStatus
import goods.pocket.app.presentation.designsystem.GoodsPocketVisualTokens
import goods.pocket.app.presentation.i18n.formatCurrency
import goods.pocket.app.presentation.i18n.formatDate
import goods.pocket.app.presentation.i18n.localizedCategory
import goods.pocket.app.presentation.i18n.localizedLabel
import goods.pocket.app.presentation.i18n.tr
import goods.pocket.app.presentation.state.CollectionSegment
import goodspocket.composeapp.generated.resources.Res
import goodspocket.composeapp.generated.resources.collection_empty_search
import goodspocket.composeapp.generated.resources.collection_metric_monthly_spend
import goodspocket.composeapp.generated.resources.collection_result_count
import goodspocket.composeapp.generated.resources.collection_search_placeholder
import goodspocket.composeapp.generated.resources.collection_segment_owned
import goodspocket.composeapp.generated.resources.collection_segment_reserved
import goodspocket.composeapp.generated.resources.collection_series_category
import goodspocket.composeapp.generated.resources.common_not_set
import goodspocket.composeapp.generated.resources.common_unknown
import goodspocket.composeapp.generated.resources.nav_collection
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
    val visibleEntries = remember(entries, query, selectedSegment) {
        visibleCollectionEntries(entries, query, selectedSegment)
    }
    val summary = remember(entries) { collectionSummary(entries) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(GoodsPocketVisualTokens.Background)),
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = CollectionReferenceMetrics.ScreenHorizontalPadding)
                .padding(top = CollectionReferenceMetrics.GridTop),
            contentPadding = PaddingValues(bottom = CollectionReferenceMetrics.GridBottomClearance),
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(
                CollectionReferenceMetrics.GridSpacing,
            ),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(
                CollectionReferenceMetrics.GridSpacing,
            ),
        ) {
            if (visibleEntries.isEmpty()) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(CollectionReferenceMetrics.EmptyStateHeight),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = tr(Res.string.collection_empty_search),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
            items(
                items = visibleEntries,
                key = CollectionEntry::id,
            ) { entry ->
                val metadata = entry.collectionCardMetadata()
                CollectionGoodsCard(
                    entry = entry,
                    metadata = when (metadata) {
                        is CollectionCardMetadata.Catalog -> tr(
                            Res.string.collection_series_category,
                            metadata.seriesName ?: tr(Res.string.common_unknown),
                            entry.localizedCategory(),
                        )
                        is CollectionCardMetadata.Reservation -> tr(
                            Res.string.preorders_store_release,
                            metadata.store ?: tr(Res.string.common_unknown),
                            metadata.releaseDate?.let { formatDate(it) }
                                ?: tr(Res.string.common_not_set),
                        )
                    },
                    statusLabel = entry.status.localizedLabel(),
                    onClick = { onEntryClick(entry.id) },
                )
            }
        }

        CollectionHeader(
            title = tr(Res.string.nav_collection),
            segments = CollectionSegment.entries.map { it.localizedLabel() },
            selectedSegmentIndex = CollectionSegment.entries.indexOf(selectedSegment),
            query = query,
            searchPlaceholder = tr(Res.string.collection_search_placeholder),
            resultCount = tr(Res.string.collection_result_count, visibleEntries.size),
            onSegmentChange = { index -> onSegmentChange(CollectionSegment.entries[index]) },
            onQueryChange = onQueryChange,
            modifier = Modifier.align(Alignment.TopCenter),
        )

        CollectionSummaryBand(
            ownedLabel = tr(Res.string.collection_segment_owned),
            ownedValue = summary.ownedCount.toString(),
            reservedLabel = tr(Res.string.collection_segment_reserved),
            reservedValue = summary.reservedCount.toString(),
            amountLabel = tr(Res.string.collection_metric_monthly_spend),
            amountValue = formatCurrency(summary.totalPurchaseAmount),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(
                    horizontal = CollectionReferenceMetrics.ScreenHorizontalPadding,
                    vertical = CollectionReferenceMetrics.SummaryBottomPadding,
                ),
        )
    }
}

internal sealed interface CollectionCardMetadata {
    data class Catalog(
        val seriesName: String?,
        val category: String,
    ) : CollectionCardMetadata

    data class Reservation(
        val store: String?,
        val releaseDate: String?,
    ) : CollectionCardMetadata
}

internal fun CollectionEntry.collectionCardMetadata(): CollectionCardMetadata {
    return if (status == CollectionEntryStatus.RESERVED) {
        CollectionCardMetadata.Reservation(
            store = reservationStore,
            releaseDate = releaseDate,
        )
    } else {
        CollectionCardMetadata.Catalog(
            seriesName = seriesName,
            category = category,
        )
    }
}

internal data class CollectionSummary(
    val ownedCount: Int,
    val reservedCount: Int,
    val totalPurchaseAmount: Long,
)

internal fun collectionSummary(entries: List<CollectionEntry>): CollectionSummary {
    val ownedEntries = entries.filter { it.status != CollectionEntryStatus.RESERVED }
    return CollectionSummary(
        ownedCount = ownedEntries.size,
        reservedCount = entries.count { it.status == CollectionEntryStatus.RESERVED },
        totalPurchaseAmount = ownedEntries.sumOf { it.purchasePrice ?: 0L },
    )
}

internal fun visibleCollectionEntries(
    entries: List<CollectionEntry>,
    query: String,
    selectedSegment: CollectionSegment,
): List<CollectionEntry> {
    return entries.filter { entry ->
        val matchesQuery = query.isBlank() ||
            entry.name.contains(query, ignoreCase = true) ||
            entry.seriesName?.contains(query, ignoreCase = true) == true ||
            entry.characterName?.contains(query, ignoreCase = true) == true
        val matchesSegment = when (selectedSegment) {
            CollectionSegment.OWNED -> entry.status != CollectionEntryStatus.RESERVED
            CollectionSegment.RESERVED -> entry.status == CollectionEntryStatus.RESERVED
            CollectionSegment.ALL -> true
        }
        matchesQuery && matchesSegment
    }
}
