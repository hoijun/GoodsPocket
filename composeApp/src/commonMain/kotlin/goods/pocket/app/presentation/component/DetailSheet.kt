package goods.pocket.app.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import goods.pocket.app.domain.model.CollectionEntry
import goods.pocket.app.domain.model.CollectionEntryStatus
import goods.pocket.app.domain.model.Event
import goods.pocket.app.presentation.designsystem.GoodsPocketModalBottomSheet
import goods.pocket.app.presentation.designsystem.GoodsPocketTonalBadge
import goods.pocket.app.presentation.i18n.formatCurrency
import goods.pocket.app.presentation.i18n.localizedLabel
import goods.pocket.app.presentation.i18n.tr
import goodspocket.composeapp.generated.resources.Res
import goodspocket.composeapp.generated.resources.action_delete
import goodspocket.composeapp.generated.resources.action_edit
import goodspocket.composeapp.generated.resources.action_mark_received
import goodspocket.composeapp.generated.resources.common_not_set
import goodspocket.composeapp.generated.resources.common_unknown
import goodspocket.composeapp.generated.resources.detail_category
import goodspocket.composeapp.generated.resources.detail_character
import goodspocket.composeapp.generated.resources.detail_location
import goodspocket.composeapp.generated.resources.detail_purchase_date
import goodspocket.composeapp.generated.resources.detail_purchase_price
import goodspocket.composeapp.generated.resources.detail_related_item
import goodspocket.composeapp.generated.resources.detail_related_preorder
import goodspocket.composeapp.generated.resources.detail_release_date
import goodspocket.composeapp.generated.resources.detail_series
import goodspocket.composeapp.generated.resources.detail_status
import goodspocket.composeapp.generated.resources.detail_store
import goodspocket.composeapp.generated.resources.detail_target_date
import goodspocket.composeapp.generated.resources.detail_type
import goodspocket.composeapp.generated.resources.field_note

@Composable
fun CollectionEntryDetailSheet(
    entry: CollectionEntry,
    onDismiss: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onMarkReceived: (() -> Unit)? = null,
) {
    DetailSheetContainer(
        title = entry.name,
        onDismiss = onDismiss,
    ) {
        CollectionEntryDetailHero(entry = entry)
        DetailLine(tr(Res.string.detail_category), entry.category)
        DetailLine(tr(Res.string.detail_status), entry.status.localizedLabel())
        DetailLine(tr(Res.string.detail_series), entry.seriesName ?: tr(Res.string.common_unknown))
        DetailLine(tr(Res.string.detail_character), entry.characterName ?: tr(Res.string.common_unknown))
        when (entry.status) {
            CollectionEntryStatus.RESERVED -> {
                DetailLine(
                    tr(Res.string.detail_store),
                    entry.reservationStore ?: tr(Res.string.common_unknown),
                )
                DetailLine(
                    tr(Res.string.detail_release_date),
                    entry.releaseDate ?: tr(Res.string.common_not_set),
                )
            }

            CollectionEntryStatus.OWNED,
            CollectionEntryStatus.PLANNED_CLEANUP,
            -> {
                DetailLine(
                    tr(Res.string.detail_store),
                    entry.purchaseStore ?: tr(Res.string.common_unknown),
                )
                DetailLine(
                    tr(Res.string.detail_purchase_date),
                    entry.purchaseDate ?: tr(Res.string.common_not_set),
                )
                DetailLine(
                    tr(Res.string.detail_purchase_price),
                    entry.purchasePrice?.let { formatCurrency(it) } ?: tr(Res.string.common_not_set),
                )
                DetailLine(
                    tr(Res.string.detail_location),
                    entry.storageLocationId ?: tr(Res.string.common_not_set),
                )
            }
        }
        DetailLine(
            tr(Res.string.field_note),
            entry.note ?: tr(Res.string.common_not_set),
        )
        if (entry.status == CollectionEntryStatus.RESERVED && onMarkReceived != null) {
            Button(
                onClick = onMarkReceived,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(tr(Res.string.action_mark_received))
            }
        }
        SheetActionRow(
            primaryLabel = tr(Res.string.action_edit),
            onPrimary = onEdit,
            secondaryLabel = tr(Res.string.action_delete),
            onSecondary = onDelete,
            destructive = true,
        )
    }
}

@Composable
private fun CollectionEntryDetailHero(
    entry: CollectionEntry,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Surface(
            modifier = Modifier.size(112.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surfaceContainerLow,
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = entry.name.take(1),
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
        GoodsPocketTonalBadge(
            text = entry.status.localizedLabel(),
            containerColor = collectionEntryStatusContainerColor(entry.status),
            contentColor = collectionEntryStatusContentColor(entry.status),
        )
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
fun EventDetailSheet(
    event: Event,
    onDismiss: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    DetailSheetContainer(
        title = event.title,
        onDismiss = onDismiss,
    ) {
        DetailLine(tr(Res.string.detail_type), event.eventType.localizedLabel())
        DetailLine(tr(Res.string.detail_target_date), event.targetDate)
        DetailLine(tr(Res.string.detail_related_preorder), event.relatedPreorderId ?: tr(Res.string.common_not_set))
        DetailLine(tr(Res.string.detail_related_item), event.relatedItemId ?: tr(Res.string.common_not_set))
        DetailLine(tr(Res.string.detail_location), event.locationOrStore ?: tr(Res.string.common_unknown))
        SheetActionRow(
            primaryLabel = tr(Res.string.action_edit),
            onPrimary = onEdit,
            secondaryLabel = tr(Res.string.action_delete),
            onSecondary = onDelete,
            destructive = true,
        )
    }
}

@Composable
private fun DetailSheetContainer(
    title: String,
    onDismiss: () -> Unit,
    content: @Composable () -> Unit,
) {
    GoodsPocketModalBottomSheet(
        title = title,
        onDismiss = onDismiss,
        titleFontWeight = FontWeight.SemiBold,
        contentSpacing = 12.dp,
    ) {
        content()
    }
}

@Composable
private fun DetailLine(
    label: String,
    value: String,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceContainerLow,
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}

@Composable
private fun SheetActionRow(
    primaryLabel: String,
    onPrimary: () -> Unit,
    secondaryLabel: String,
    onSecondary: () -> Unit,
    destructive: Boolean,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Button(
            onClick = onPrimary,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(primaryLabel)
        }
        TextButton(
            onClick = onSecondary,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = secondaryLabel,
                color = if (destructive) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
            )
        }
    }
}
