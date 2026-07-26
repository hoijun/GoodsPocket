package goods.pocket.app.presentation.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import goods.pocket.app.domain.model.Event
import goods.pocket.app.domain.model.EventType
import goods.pocket.app.presentation.designsystem.GoodsPocketVisualTokens
import goods.pocket.app.presentation.i18n.formatDate
import goods.pocket.app.presentation.i18n.localizedLabel

@Composable
internal fun EventsHeader(
    pageTitle: String,
    monthLabel: String,
    overviewTitle: String,
    count: String,
    filterLabels: List<String>,
    selectedFilterIndex: Int,
    onFilterSelected: (Int) -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(EventsReferenceMetrics.PageTitleTopPadding))
        Text(
            text = pageTitle,
            modifier = Modifier.fillMaxWidth(),
            color = EventsColors.Ink,
            fontSize = EventsReferenceMetrics.PageTitleFontSize,
            lineHeight = EventsReferenceMetrics.PageTitleLineHeight,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(EventsReferenceMetrics.TitleToOverviewSpacing))
        EventsOverview(
            monthLabel = monthLabel,
            overviewTitle = overviewTitle,
            count = count,
        )
        Spacer(modifier = Modifier.height(EventsReferenceMetrics.OverviewToFilterSpacing))
        EventsFilterRow(
            labels = filterLabels,
            selectedIndex = selectedFilterIndex,
            onSelected = onFilterSelected,
        )
        Spacer(modifier = Modifier.height(EventsReferenceMetrics.FilterToFeaturedSpacing))
    }
}

@Composable
private fun EventsOverview(
    monthLabel: String,
    overviewTitle: String,
    count: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = EventsReferenceMetrics.ScreenHorizontalPadding),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom,
    ) {
        Column {
            Text(
                text = monthLabel,
                modifier = Modifier.height(EventsReferenceMetrics.MonthLineHeight.value.dp),
                color = EventsColors.Muted,
                fontSize = EventsReferenceMetrics.MonthFontSize,
                lineHeight = EventsReferenceMetrics.MonthLineHeight,
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(modifier = Modifier.height(EventsReferenceMetrics.OverviewTextSpacing))
            Text(
                text = overviewTitle,
                color = EventsColors.Ink,
                fontSize = EventsReferenceMetrics.OverviewTitleFontSize,
                lineHeight = EventsReferenceMetrics.OverviewTitleLineHeight,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
            )
        }
        Surface(
            modifier = Modifier
                .width(EventsReferenceMetrics.CountPillWidth)
                .height(EventsReferenceMetrics.CountPillHeight)
                .offset(y = EventsReferenceMetrics.CountPillOffsetY),
            shape = RoundedCornerShape(10.dp),
            color = EventsColors.CountContainer,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .offset(y = EventsReferenceMetrics.CountTextOffsetY),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = count,
                    color = EventsColors.Ink,
                    fontSize = EventsReferenceMetrics.CountFontSize,
                    lineHeight = EventsReferenceMetrics.CountLineHeight,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

@Composable
private fun EventsFilterRow(
    labels: List<String>,
    selectedIndex: Int,
    onSelected: (Int) -> Unit,
) {
    val weights = listOf(60f, 59f, 74f, 59f, 85f)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = EventsReferenceMetrics.ScreenHorizontalPadding,
                end = EventsReferenceMetrics.FilterEndPadding,
            ),
        horizontalArrangement = Arrangement.spacedBy(EventsReferenceMetrics.FilterSpacing),
    ) {
        labels.forEachIndexed { index, label ->
            val selected = index == selectedIndex
            Surface(
                onClick = { onSelected(index) },
                modifier = Modifier
                    .weight(weights.getOrElse(index) { 60f })
                    .height(EventsReferenceMetrics.FilterHeight),
                shape = RoundedCornerShape(16.dp),
                color = if (selected) EventsColors.Primary else EventsColors.Card,
                contentColor = if (selected) Color.White else EventsColors.Muted,
                border = if (selected) null else BorderStroke(1.dp, EventsColors.Outline),
                shadowElevation = EventsReferenceMetrics.CardShadowElevation,
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = label,
                        fontSize = EventsReferenceMetrics.FilterFontSize,
                        lineHeight = EventsReferenceMetrics.FilterLineHeight,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                    )
                }
            }
        }
    }
}

@Composable
internal fun EventsFeaturedCard(
    event: Event,
    date: String,
    typeLabel: String,
    location: String,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = EventsReferenceMetrics.ScreenHorizontalPadding)
            .height(EventsReferenceMetrics.FeaturedCardHeight),
        shape = RoundedCornerShape(EventsReferenceMetrics.FeaturedCardRadius),
        color = EventsColors.Card,
        contentColor = EventsColors.Ink,
        border = BorderStroke(1.dp, EventsColors.Outline),
        shadowElevation = EventsReferenceMetrics.CardShadowElevation,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = EventsReferenceMetrics.FeaturedCardPadding)
                .padding(top = EventsReferenceMetrics.FeaturedTopPadding),
        ) {
            EventsTypeBadge(
                eventType = event.eventType,
                label = typeLabel,
                featured = true,
            )
            Spacer(modifier = Modifier.height(EventsReferenceMetrics.FeaturedBadgeToDateSpacing))
            Text(
                text = date,
                color = EventsColors.Primary,
                fontSize = EventsReferenceMetrics.FeaturedDateFontSize,
                lineHeight = EventsReferenceMetrics.FeaturedDateLineHeight,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(EventsReferenceMetrics.FeaturedDateToTitleSpacing))
            Text(
                text = event.title,
                color = EventsColors.Ink,
                fontSize = EventsReferenceMetrics.FeaturedTitleFontSize,
                lineHeight = EventsReferenceMetrics.FeaturedTitleLineHeight,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(EventsReferenceMetrics.FeaturedTitleToLocationSpacing))
            Text(
                text = location,
                color = EventsColors.Muted,
                fontSize = EventsReferenceMetrics.FeaturedLocationFontSize,
                lineHeight = EventsReferenceMetrics.FeaturedLocationLineHeight,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
internal fun EventsEmptyCard(message: String) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = EventsReferenceMetrics.ScreenHorizontalPadding)
            .height(EventsReferenceMetrics.FeaturedCardHeight),
        shape = RoundedCornerShape(EventsReferenceMetrics.FeaturedCardRadius),
        color = EventsColors.Card,
        border = BorderStroke(1.dp, EventsColors.Outline),
        shadowElevation = EventsReferenceMetrics.CardShadowElevation,
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = message,
                color = EventsColors.Muted,
                fontSize = EventsReferenceMetrics.FeaturedLocationFontSize,
                lineHeight = EventsReferenceMetrics.FeaturedLocationLineHeight,
            )
        }
    }
}

@Composable
internal fun EventsTimelineSection(
    title: String,
    events: List<Event>,
    missingLocation: String,
    onEventClick: (String) -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(EventsReferenceMetrics.FeaturedToTimelineTitleSpacing))
        Text(
            text = title,
            modifier = Modifier.padding(horizontal = EventsReferenceMetrics.ScreenHorizontalPadding),
            color = EventsColors.Ink,
            fontSize = EventsReferenceMetrics.SectionTitleFontSize,
            lineHeight = EventsReferenceMetrics.SectionTitleLineHeight,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(EventsReferenceMetrics.TimelineTitleToCardSpacing))
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = EventsReferenceMetrics.ScreenHorizontalPadding)
                .height(EventsReferenceMetrics.TimelineRowHeight * events.size),
            shape = RoundedCornerShape(EventsReferenceMetrics.TimelineCardRadius),
            color = EventsColors.Card,
            border = BorderStroke(1.dp, EventsColors.Outline),
            shadowElevation = EventsReferenceMetrics.CardShadowElevation,
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                events.forEachIndexed { index, event ->
                    EventsTimelineRow(
                        event = event,
                        typeLabel = event.eventType.localizedLabel(),
                        location = event.locationOrStore ?: missingLocation,
                        date = formatDate(event.targetDate),
                        showDivider = index < events.lastIndex,
                        contentOffsetY = if (index == 0) 2.dp else 0.dp,
                        onClick = { onEventClick(event.id) },
                    )
                }
            }
        }
    }
}

@Composable
private fun EventsTimelineRow(
    event: Event,
    typeLabel: String,
    location: String,
    date: String,
    showDivider: Boolean,
    contentOffsetY: Dp,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(EventsReferenceMetrics.TimelineRowHeight),
        color = Color.Transparent,
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = EventsReferenceMetrics.TimelineHorizontalPadding),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier.width(66.dp),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    EventsTypeBadge(
                        eventType = event.eventType,
                        label = typeLabel,
                        featured = false,
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .offset(y = contentOffsetY),
                ) {
                    Text(
                        text = event.title,
                        color = EventsColors.Ink,
                        fontSize = EventsReferenceMetrics.TimelineTitleFontSize,
                        lineHeight = EventsReferenceMetrics.TimelineTitleLineHeight,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = location,
                        color = EventsColors.Muted,
                        fontSize = EventsReferenceMetrics.TimelineMetadataFontSize,
                        lineHeight = EventsReferenceMetrics.TimelineMetadataLineHeight,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = date,
                    modifier = Modifier.width(75.dp),
                    color = EventsColors.Muted,
                    fontSize = EventsReferenceMetrics.TimelineDateFontSize,
                    lineHeight = EventsReferenceMetrics.TimelineDateLineHeight,
                    textAlign = TextAlign.End,
                    maxLines = 1,
                )
            }
            if (showDivider) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(horizontal = EventsReferenceMetrics.TimelineHorizontalPadding)
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(EventsColors.Outline),
                )
            }
        }
    }
}

@Composable
private fun EventsTypeBadge(
    eventType: EventType,
    label: String,
    featured: Boolean,
) {
    val colors = when (eventType) {
        EventType.PAYMENT_DUE -> EventsBadgeColors(EventsColors.PaymentContainer, EventsColors.Primary)
        EventType.RELEASE -> EventsBadgeColors(EventsColors.EventContainer, EventsColors.Event)
        EventType.DELIVERY -> EventsBadgeColors(EventsColors.SuccessContainer, EventsColors.Success)
        EventType.OFFLINE_EVENT -> EventsBadgeColors(EventsColors.EventContainer, EventsColors.Event)
    }
    Surface(
        modifier = Modifier.height(
            if (featured) EventsReferenceMetrics.FeaturedBadgeHeight else 23.dp,
        ),
        shape = RoundedCornerShape(8.dp),
        color = colors.container,
        contentColor = colors.content,
    ) {
        Box(
            modifier = Modifier.padding(horizontal = if (featured) 10.dp else 7.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = label,
                fontSize = if (featured) {
                    EventsReferenceMetrics.FeaturedBadgeFontSize
                } else {
                    EventsReferenceMetrics.TimelineBadgeFontSize
                },
                lineHeight = if (featured) {
                    EventsReferenceMetrics.FeaturedBadgeLineHeight
                } else {
                    EventsReferenceMetrics.TimelineBadgeLineHeight
                },
                fontWeight = FontWeight.Bold,
                maxLines = 1,
            )
        }
    }
}

private object EventsColors {
    val Primary = Color(GoodsPocketVisualTokens.Primary)
    val Success = Color(GoodsPocketVisualTokens.Secondary)
    val Event = Color(GoodsPocketVisualTokens.Tertiary)
    val Ink = Color(GoodsPocketVisualTokens.Ink)
    val Muted = Color(GoodsPocketVisualTokens.MutedInk)
    val Card = Color(0xFFFEFBF8)
    val Outline = Color(0xFFEFEDEC)
    val CountContainer = Color(0xFFF3F0ED)
    val PaymentContainer = Color(0xFFFFECE3)
    val EventContainer = Color(0xFFEDE8FF)
    val SuccessContainer = Color(0xFFE5F8ED)
}

private data class EventsBadgeColors(
    val container: Color,
    val content: Color,
)
