package goods.pocket.app.presentation.collection

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import goods.pocket.app.domain.collection.CollectionEntry
import goods.pocket.app.domain.collection.CollectionEntryStatus
import goods.pocket.app.presentation.designsystem.GoodsPocketVisualTokens

@Composable
internal fun CollectionHeader(
    title: String,
    segments: List<String>,
    selectedSegmentIndex: Int,
    query: String,
    searchPlaceholder: String,
    resultCount: String,
    onSegmentChange: (Int) -> Unit,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(GoodsPocketVisualTokens.BACKGROUND))
            .padding(horizontal = CollectionReferenceMetrics.ScreenHorizontalPadding),
    ) {
        Spacer(modifier = Modifier.height(CollectionReferenceMetrics.TitleTopPadding))
        Text(
            text = title,
            modifier = Modifier.fillMaxWidth(),
            color = Color(GoodsPocketVisualTokens.INK),
            fontSize = CollectionReferenceMetrics.TitleFontSize,
            lineHeight = CollectionReferenceMetrics.TitleLineHeight,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(CollectionReferenceMetrics.TitleToSegmentSpacing))
        CollectionSegmentedControl(
            options = segments,
            selectedIndex = selectedSegmentIndex,
            onSelectedIndexChange = onSegmentChange,
        )
        Spacer(modifier = Modifier.height(CollectionReferenceMetrics.SegmentToSearchSpacing))
        CollectionSearchField(
            value = query,
            onValueChange = onQueryChange,
            placeholder = searchPlaceholder,
        )
        Spacer(modifier = Modifier.height(CollectionReferenceMetrics.SearchToCountSpacing))
        Text(
            text = resultCount,
            color = Color(GoodsPocketVisualTokens.INK),
            fontSize = CollectionReferenceMetrics.ResultCountFontSize,
            lineHeight = CollectionReferenceMetrics.ResultCountLineHeight,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Composable
private fun CollectionSegmentedControl(
    options: List<String>,
    selectedIndex: Int,
    onSelectedIndexChange: (Int) -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(CollectionReferenceMetrics.SegmentHeight),
        shape = RoundedCornerShape(CollectionReferenceMetrics.ControlRadius),
        color = CollectionColors.Card,
        border = BorderStroke(1.dp, CollectionColors.Outline),
        shadowElevation = 0.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = CollectionReferenceMetrics.SegmentInnerPadding),
        ) {
            options.forEachIndexed { index, option ->
                val selected = selectedIndex == index
                val interactionSource = remember { MutableInteractionSource() }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .selectable(
                            selected = selected,
                            onClick = { onSelectedIndexChange(index) },
                            role = Role.Tab,
                            interactionSource = interactionSource,
                            indication = null,
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    if (selected) {
                        Surface(
                            modifier = Modifier
                                .matchParentSize()
                                .padding(
                                    horizontal =
                                    CollectionReferenceMetrics.SelectionPaintHorizontalInset,
                                    vertical =
                                    CollectionReferenceMetrics.SelectionPaintVerticalInset,
                                ),
                            shape = RoundedCornerShape(
                                CollectionReferenceMetrics.SelectedSegmentRadius,
                            ),
                            color = Color(GoodsPocketVisualTokens.PRIMARY),
                        ) {}
                    }
                    Text(
                        text = option,
                        color = if (selected) Color.White else Color(GoodsPocketVisualTokens.INK),
                        fontSize = CollectionReferenceMetrics.SegmentFontSize,
                        lineHeight = CollectionReferenceMetrics.SegmentLineHeight,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                    )
                }
            }
        }
    }
}

@Composable
private fun CollectionSearchField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(CollectionReferenceMetrics.SearchHeight),
        shape = RoundedCornerShape(CollectionReferenceMetrics.ControlRadius),
        color = CollectionColors.Card,
        border = BorderStroke(1.dp, CollectionColors.Outline),
        shadowElevation = 0.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CollectionSearchIcon()
            Spacer(modifier = Modifier.width(9.dp))
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.weight(1f),
                singleLine = true,
                textStyle = TextStyle(
                    color = Color(GoodsPocketVisualTokens.INK),
                    fontSize = CollectionReferenceMetrics.SearchFontSize,
                    lineHeight = CollectionReferenceMetrics.SearchLineHeight,
                    fontWeight = FontWeight.Normal,
                ),
                cursorBrush = SolidColor(Color(GoodsPocketVisualTokens.PRIMARY)),
                decorationBox = { innerTextField ->
                    Box(contentAlignment = Alignment.CenterStart) {
                        if (value.isEmpty()) {
                            Text(
                                text = placeholder,
                                color = Color(GoodsPocketVisualTokens.MUTED_INK),
                                fontSize = CollectionReferenceMetrics.SearchFontSize,
                                lineHeight = CollectionReferenceMetrics.SearchLineHeight,
                            )
                        }
                        innerTextField()
                    }
                },
            )
        }
    }
}

@Composable
private fun CollectionSearchIcon() {
    Canvas(modifier = Modifier.size(17.dp)) {
        val color = Color(GoodsPocketVisualTokens.MUTED_INK)
        drawCircle(
            color = color,
            radius = size.minDimension * 0.31f,
            center = Offset(size.width * 0.42f, size.height * 0.40f),
            style = Stroke(width = 2.1f),
        )
        drawLine(
            color = color,
            start = Offset(size.width * 0.64f, size.height * 0.63f),
            end = Offset(size.width * 0.88f, size.height * 0.87f),
            strokeWidth = 2.1f,
        )
    }
}

@Composable
internal fun CollectionGoodsCard(
    entry: CollectionEntry,
    metadata: String,
    statusLabel: String,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(CollectionReferenceMetrics.GoodsCardHeight),
        shape = RoundedCornerShape(CollectionReferenceMetrics.GoodsCardRadius),
        color = CollectionColors.Card,
        contentColor = Color(GoodsPocketVisualTokens.INK),
        border = BorderStroke(1.dp, CollectionColors.Outline),
        shadowElevation = CollectionReferenceMetrics.CardShadowElevation,
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(CollectionReferenceMetrics.GoodsMediaHeight)
                    .clip(
                        RoundedCornerShape(
                            topStart = CollectionReferenceMetrics.GoodsCardRadius,
                            topEnd = CollectionReferenceMetrics.GoodsCardRadius,
                        ),
                    )
                    .background(CollectionColors.MediaPlaceholder),
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp, vertical = 7.dp)
                    .padding(top = 3.dp),
            ) {
                Text(
                    text = entry.name,
                    color = Color(GoodsPocketVisualTokens.INK),
                    fontSize = CollectionReferenceMetrics.GoodsNameFontSize,
                    lineHeight = CollectionReferenceMetrics.GoodsNameLineHeight,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = metadata,
                    color = Color(GoodsPocketVisualTokens.MUTED_INK),
                    fontSize = CollectionReferenceMetrics.GoodsMetadataFontSize,
                    lineHeight = CollectionReferenceMetrics.GoodsMetadataLineHeight,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.weight(1f))
                CollectionStatusBadge(
                    text = statusLabel,
                    status = entry.status,
                )
            }
        }
    }
}

@Composable
private fun CollectionStatusBadge(text: String, status: CollectionEntryStatus) {
    val colors = when (status) {
        CollectionEntryStatus.OWNED -> CollectionStatusColors(
            container = CollectionColors.OwnedContainer,
            content = Color(GoodsPocketVisualTokens.SECONDARY),
        )
        CollectionEntryStatus.RESERVED -> CollectionStatusColors(
            container = CollectionColors.ReservedContainer,
            content = Color(GoodsPocketVisualTokens.PRIMARY),
        )
        CollectionEntryStatus.PLANNED_CLEANUP -> CollectionStatusColors(
            container = CollectionColors.PlannedContainer,
            content = Color(GoodsPocketVisualTokens.TERTIARY),
        )
    }
    Surface(
        shape = RoundedCornerShape(50),
        color = colors.container,
        contentColor = colors.content,
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
            fontSize = CollectionReferenceMetrics.BadgeFontSize,
            lineHeight = CollectionReferenceMetrics.BadgeLineHeight,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
        )
    }
}

@Composable
internal fun CollectionSummaryBand(
    ownedLabel: String,
    ownedValue: String,
    reservedLabel: String,
    reservedValue: String,
    amountLabel: String,
    amountValue: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(CollectionReferenceMetrics.SummaryHeight),
        shape = RoundedCornerShape(CollectionReferenceMetrics.SummaryRadius),
        color = CollectionColors.Card,
        contentColor = Color(GoodsPocketVisualTokens.INK),
        border = BorderStroke(1.dp, CollectionColors.Outline),
        shadowElevation = CollectionReferenceMetrics.CardShadowElevation,
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CollectionSummaryMetric(
                label = ownedLabel,
                value = ownedValue,
                valueColor = Color(GoodsPocketVisualTokens.SECONDARY),
                modifier = Modifier.weight(1.05f),
            )
            CollectionSummaryDivider()
            CollectionSummaryMetric(
                label = reservedLabel,
                value = reservedValue,
                valueColor = Color(GoodsPocketVisualTokens.WARNING),
                modifier = Modifier.weight(1f),
            )
            CollectionSummaryDivider()
            CollectionSummaryMetric(
                label = amountLabel,
                value = amountValue,
                valueColor = Color(GoodsPocketVisualTokens.INK),
                modifier = Modifier.weight(1.5f),
            )
        }
    }
}

@Composable
private fun CollectionSummaryMetric(
    label: String,
    value: String,
    valueColor: Color,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = label,
            color = Color(GoodsPocketVisualTokens.MUTED_INK),
            fontSize = CollectionReferenceMetrics.SummaryLabelFontSize,
            lineHeight = CollectionReferenceMetrics.SummaryLabelLineHeight,
            maxLines = 1,
        )
        Text(
            text = value,
            color = valueColor,
            fontSize = CollectionReferenceMetrics.SummaryValueFontSize,
            lineHeight = CollectionReferenceMetrics.SummaryValueLineHeight,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun CollectionSummaryDivider() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(38.dp)
            .background(CollectionColors.Outline),
    )
}

private object CollectionColors {
    val Card = Color(0xFFFEFBF8)
    val Outline = Color(0xFFEFEDEC)
    val MediaPlaceholder = Color(0xFFD7D2CC)
    val OwnedContainer = Color(0xFFE5F8ED)
    val ReservedContainer = Color(0xFFFFECE3)
    val PlannedContainer = Color(0xFFEDE8FF)
}

private data class CollectionStatusColors(val container: Color, val content: Color)
