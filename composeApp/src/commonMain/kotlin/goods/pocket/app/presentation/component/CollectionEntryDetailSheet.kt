package goods.pocket.app.presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import goods.pocket.app.domain.model.CollectionEntry
import goods.pocket.app.domain.model.CollectionEntryStatus
import goods.pocket.app.presentation.i18n.formatCurrency
import goods.pocket.app.presentation.i18n.formatDate
import goods.pocket.app.presentation.i18n.localizedCategory
import goods.pocket.app.presentation.i18n.localizedLabel
import goods.pocket.app.presentation.i18n.tr
import goodspocket.composeapp.generated.resources.Res
import goodspocket.composeapp.generated.resources.action_close
import goodspocket.composeapp.generated.resources.action_delete
import goodspocket.composeapp.generated.resources.action_edit
import goodspocket.composeapp.generated.resources.action_mark_received
import goodspocket.composeapp.generated.resources.collection_series_category
import goodspocket.composeapp.generated.resources.common_not_set
import goodspocket.composeapp.generated.resources.common_unknown
import goodspocket.composeapp.generated.resources.detail_collection_purchase_date
import goodspocket.composeapp.generated.resources.detail_collection_purchase_price
import goodspocket.composeapp.generated.resources.detail_collection_purchase_store
import goodspocket.composeapp.generated.resources.detail_collection_storage_location
import goodspocket.composeapp.generated.resources.detail_expected_release_date
import goodspocket.composeapp.generated.resources.detail_related_link
import goodspocket.composeapp.generated.resources.detail_reservation_amount
import goodspocket.composeapp.generated.resources.detail_reservation_store
import goodspocket.composeapp.generated.resources.field_note

private val MediaPlaceholder = Color(0xFFD7D2CC)
private val DetailInk = Color(0xFF202838)
private val DetailMutedInk = Color(0xFF8A8F9B)
private val DetailDivider = Color(0xFFEAE6E2)
private val DetailDanger = Color(0xFFFF5A52)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectionEntryDetailSheet(
    entry: CollectionEntry,
    onDismiss: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onMarkReceived: (() -> Unit)? = null,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = null,
        shape = RoundedCornerShape(
            topStart = CollectionDetailReferenceMetrics.SheetCornerRadius,
            topEnd = CollectionDetailReferenceMetrics.SheetCornerRadius,
        ),
        containerColor = Color.White,
        contentColor = DetailInk,
        scrimColor = Color.Black.copy(alpha = 0.32f),
        tonalElevation = 0.dp,
        contentWindowInsets = { WindowInsets(0, 0, 0, 0) },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(CollectionDetailReferenceMetrics.SheetHeight)
                .navigationBarsPadding(),
        ) {
            CollectionDetailHeader(onDismiss = onDismiss)
            Box(modifier = Modifier.weight(1f)) {
                CollectionDetailContent(
                    entry = entry,
                )
            }
            CollectionDetailFooter(
                entry = entry,
                onEdit = onEdit,
                onDelete = onDelete,
                onMarkReceived = onMarkReceived,
            )
        }
    }
}

@Composable
private fun CollectionDetailHeader(
    onDismiss: () -> Unit,
) {
    val closeDescription = tr(Res.string.action_close)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(CollectionDetailReferenceMetrics.HeaderHeight),
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 11.dp)
                .width(36.dp)
                .height(4.dp)
                .clip(CircleShape)
                .background(Color(0xFFC8C9CC)),
        )
        IconButton(
            onClick = onDismiss,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 8.dp)
                .size(40.dp)
                .semantics { contentDescription = closeDescription },
        ) {
            Canvas(modifier = Modifier.size(CollectionDetailReferenceMetrics.CloseIconSize)) {
                val strokeWidth = 1.25.dp.toPx()
                drawLine(
                    color = DetailMutedInk,
                    start = androidx.compose.ui.geometry.Offset(2.dp.toPx(), 2.dp.toPx()),
                    end = androidx.compose.ui.geometry.Offset(size.width - 2.dp.toPx(), size.height - 2.dp.toPx()),
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Round,
                )
                drawLine(
                    color = DetailMutedInk,
                    start = androidx.compose.ui.geometry.Offset(size.width - 2.dp.toPx(), 2.dp.toPx()),
                    end = androidx.compose.ui.geometry.Offset(2.dp.toPx(), size.height - 2.dp.toPx()),
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Round,
                )
            }
        }
    }
}

@Composable
private fun CollectionDetailContent(
    entry: CollectionEntry,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = CollectionDetailReferenceMetrics.HorizontalPadding),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(CollectionDetailReferenceMetrics.MediaHeight)
                .clip(RoundedCornerShape(CollectionDetailReferenceMetrics.MediaCornerRadius))
                .background(MediaPlaceholder),
        )
        Spacer(modifier = Modifier.height(CollectionDetailReferenceMetrics.MediaContentGap))
        CollectionStatusBadge(status = entry.status)
        Spacer(modifier = Modifier.height(CollectionDetailReferenceMetrics.BadgeTitleGap))
        Text(
            text = entry.name,
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = CollectionDetailReferenceMetrics.TitleFontSize,
                lineHeight = 22.sp,
                fontWeight = FontWeight.Bold,
            ),
            color = DetailInk,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(modifier = Modifier.height(CollectionDetailReferenceMetrics.TitleSubtitleGap))
        Text(
            text = collectionDetailSubtitle(entry),
            style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 10.5.sp,
                lineHeight = 16.sp,
            ),
            color = DetailMutedInk,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(modifier = Modifier.height(CollectionDetailReferenceMetrics.TitleMetadataGap))
        HorizontalDivider(color = DetailDivider)
        CollectionMetadata(entry = entry)
        HorizontalDivider(color = DetailDivider)
        CollectionNote(note = entry.note ?: tr(Res.string.common_not_set))
    }
}

@Composable
private fun CollectionDetailFooter(
    entry: CollectionEntry,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onMarkReceived: (() -> Unit)?,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = CollectionDetailReferenceMetrics.HorizontalPadding)
            .padding(top = CollectionDetailReferenceMetrics.ActionTopGap, bottom = 8.dp),
    ) {
        if (entry.status == CollectionEntryStatus.RESERVED && onMarkReceived != null) {
            Button(
                onClick = onMarkReceived,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(CollectionDetailReferenceMetrics.ActionHeight),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White,
                ),
                elevation = ButtonDefaults.buttonElevation(0.dp, 0.dp, 0.dp, 0.dp, 0.dp),
            ) {
                Text(tr(Res.string.action_mark_received))
            }
            Spacer(modifier = Modifier.height(6.dp))
        }
        CollectionDetailActions(
            onEdit = onEdit,
            onDelete = onDelete,
        )
    }
}

@Composable
private fun collectionDetailSubtitle(entry: CollectionEntry): String {
    val series = entry.seriesName ?: tr(Res.string.common_unknown)
    val character = entry.characterName
        ?.takeUnless { characterName -> entry.name.contains(characterName, ignoreCase = true) }
    val category = entry.localizedCategory()
    return if (character == null) {
        tr(Res.string.collection_series_category, series, category)
    } else {
        "$series · $character · $category"
    }
}

@Composable
private fun CollectionStatusBadge(
    status: CollectionEntryStatus,
) {
    val containerColor = when (status) {
        CollectionEntryStatus.OWNED -> MaterialTheme.colorScheme.secondaryContainer
        CollectionEntryStatus.RESERVED -> MaterialTheme.colorScheme.primaryContainer
        CollectionEntryStatus.PLANNED_CLEANUP -> MaterialTheme.colorScheme.tertiaryContainer
    }
    val contentColor = when (status) {
        CollectionEntryStatus.OWNED -> MaterialTheme.colorScheme.secondary
        CollectionEntryStatus.RESERVED -> MaterialTheme.colorScheme.primary
        CollectionEntryStatus.PLANNED_CLEANUP -> MaterialTheme.colorScheme.tertiary
    }
    Surface(
        shape = CircleShape,
        color = containerColor,
        contentColor = contentColor,
    ) {
        Text(
            text = status.localizedLabel(),
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp),
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 9.sp,
                lineHeight = 14.sp,
                fontWeight = FontWeight.Bold,
            ),
        )
    }
}

@Composable
private fun CollectionMetadata(entry: CollectionEntry) {
    when (entry.status) {
        CollectionEntryStatus.RESERVED -> {
            CollectionMetadataRow(
                label = tr(Res.string.detail_expected_release_date),
                value = entry.releaseDate?.let { formatDate(it) } ?: tr(Res.string.common_not_set),
            )
            CollectionMetadataRow(
                label = tr(Res.string.detail_reservation_store),
                value = entry.reservationStore ?: tr(Res.string.common_unknown),
            )
            CollectionMetadataRow(
                label = tr(Res.string.detail_reservation_amount),
                value = entry.purchasePrice?.let { formatCurrency(it) } ?: tr(Res.string.common_not_set),
            )
            CollectionMetadataRow(
                label = tr(Res.string.detail_related_link),
                value = entry.relatedLink ?: tr(Res.string.common_not_set),
            )
        }

        CollectionEntryStatus.OWNED,
        CollectionEntryStatus.PLANNED_CLEANUP,
        -> {
            CollectionMetadataRow(
                label = tr(Res.string.detail_collection_purchase_date),
                value = entry.purchaseDate?.let { formatDate(it) } ?: tr(Res.string.common_not_set),
            )
            CollectionMetadataRow(
                label = tr(Res.string.detail_collection_purchase_store),
                value = entry.purchaseStore ?: tr(Res.string.common_unknown),
            )
            CollectionMetadataRow(
                label = tr(Res.string.detail_collection_purchase_price),
                value = entry.purchasePrice?.let { formatCurrency(it) } ?: tr(Res.string.common_not_set),
            )
            CollectionMetadataRow(
                label = tr(Res.string.detail_related_link),
                value = entry.relatedLink ?: tr(Res.string.common_not_set),
            )
            CollectionMetadataRow(
                label = tr(Res.string.detail_collection_storage_location),
                value = entry.storageLocationId ?: tr(Res.string.common_not_set),
            )
        }
    }
}

@Composable
private fun CollectionMetadataRow(
    label: String,
    value: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(CollectionDetailReferenceMetrics.MetadataRowHeight),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            modifier = Modifier.weight(0.48f),
            style = MaterialTheme.typography.bodySmall.copy(
                fontSize = CollectionDetailReferenceMetrics.MetadataFontSize,
                lineHeight = 15.sp,
            ),
            color = DetailInk,
            maxLines = 1,
        )
        Text(
            text = value,
            modifier = Modifier.weight(0.52f),
            style = MaterialTheme.typography.bodySmall.copy(
                fontSize = CollectionDetailReferenceMetrics.MetadataFontSize,
                lineHeight = 15.sp,
            ),
            color = DetailInk,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun CollectionNote(note: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(CollectionDetailReferenceMetrics.NoteHeight)
            .padding(top = CollectionDetailReferenceMetrics.NoteTopPadding),
        verticalArrangement = Arrangement.spacedBy(CollectionDetailReferenceMetrics.NoteTextGap),
    ) {
        Text(
            text = tr(Res.string.field_note),
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = CollectionDetailReferenceMetrics.MetadataFontSize,
                lineHeight = 14.sp,
                fontWeight = FontWeight.Bold,
            ),
            color = DetailInk,
        )
        Text(
            text = note,
            style = MaterialTheme.typography.bodySmall.copy(
                fontSize = CollectionDetailReferenceMetrics.MetadataFontSize,
                lineHeight = 15.sp,
            ),
            color = DetailInk,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun CollectionDetailActions(
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Button(
            onClick = onEdit,
            modifier = Modifier
                .weight(1f)
                .height(CollectionDetailReferenceMetrics.ActionHeight),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
            ),
            elevation = ButtonDefaults.buttonElevation(0.dp, 0.dp, 0.dp, 0.dp, 0.dp),
        ) {
            Text(
                text = tr(Res.string.action_edit),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
            )
        }
        OutlinedButton(
            onClick = onDelete,
            modifier = Modifier
                .weight(1f)
                .height(CollectionDetailReferenceMetrics.ActionHeight),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, DetailDanger),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color.White,
                contentColor = DetailDanger,
            ),
        ) {
            Text(
                text = tr(Res.string.action_delete),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}
