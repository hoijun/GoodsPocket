package goods.pocket.app.presentation.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import goods.pocket.app.domain.model.ActivityRecord
import goods.pocket.app.domain.model.Event
import goods.pocket.app.domain.model.EventType
import goods.pocket.app.domain.model.HomeSummary
import goods.pocket.app.presentation.designsystem.GoodsPocketBadgeTone
import goods.pocket.app.presentation.i18n.formatCurrency
import goods.pocket.app.presentation.i18n.localizedLabel
import goods.pocket.app.presentation.i18n.tr
import goodspocket.composeapp.generated.resources.Res
import goodspocket.composeapp.generated.resources.home_monthly_spend_basis
import goodspocket.composeapp.generated.resources.home_monthly_spend_change
import goodspocket.composeapp.generated.resources.home_monthly_spend_title
import goodspocket.composeapp.generated.resources.home_no_recent_activity
import goodspocket.composeapp.generated.resources.home_no_upcoming_events
import goodspocket.composeapp.generated.resources.home_recent_goods_title
import goodspocket.composeapp.generated.resources.home_upcoming_schedule_title
import goodspocket.composeapp.generated.resources.home_view_all

@Composable
internal fun HomeRecentGoodsCarousel(
    activities: List<ActivityRecord>,
    onEntryClick: (String) -> Unit,
    onViewAll: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        HomeSectionHeader(
            title = tr(Res.string.home_recent_goods_title),
            onViewAll = onViewAll,
        )
        if (activities.isEmpty()) {
            EmptyStateLine(text = tr(Res.string.home_no_recent_activity), onClick = onViewAll)
        } else {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(
                    items = activities.take(8).map(ActivityRecord::toRecentGoodsCardModel),
                    key = RecentGoodsCardModel::id,
                ) { model ->
                    HomeRecentGoodsCard(
                        model = model,
                        onClick = { onEntryClick(model.id) },
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeRecentGoodsCard(
    model: RecentGoodsCardModel,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        modifier = Modifier
            .width(84.dp)
            .height(140.dp),
        shape = RoundedCornerShape(8.dp),
        color = HomeCardSurface,
        contentColor = HomeInk,
        shadowElevation = HomeReferenceMetrics.RecentCardShadowElevation,
        border = BorderStroke(1.dp, HomeCardBorder),
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(HomeReferenceMetrics.RecentArtworkHeight)
                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)),
            ) {
                HomeRecentGoodsMediaPlaceholder(modifier = Modifier.fillMaxSize())
            }
            Column(
                modifier = Modifier.padding(
                    start = 8.dp,
                    top = 6.dp,
                    end = 8.dp,
                    bottom = 6.dp,
                ),
                verticalArrangement = Arrangement.spacedBy(3.dp),
            ) {
                Text(
                    text = model.subtitle,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontSize = HomeReferenceMetrics.RecentSeriesFontSize,
                        lineHeight = 11.sp,
                    ),
                    color = HomeMuted,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = model.title,
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontSize = HomeReferenceMetrics.RecentTitleFontSize,
                        lineHeight = 13.sp,
                    ),
                    color = HomeInk,
                    fontWeight = FontWeight.ExtraBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
internal fun HomeMonthlySpendCard(
    dashboardSummary: HomeSummary,
) {
    HomeWhiteCard(
        modifier = Modifier.height(102.dp),
        contentPadding = 0.dp,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 13.dp, vertical = 7.dp),
        ) {
            Column(
                modifier = Modifier.align(Alignment.TopStart),
                verticalArrangement = Arrangement.spacedBy(1.dp),
            ) {
                Text(
                    text = tr(Res.string.home_monthly_spend_title),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 13.sp,
                        lineHeight = 17.sp,
                    ),
                    color = HomeInk,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = tr(Res.string.home_monthly_spend_basis),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 10.sp,
                        lineHeight = 13.sp,
                    ),
                    color = HomeMuted,
                    fontWeight = FontWeight.Medium,
                )
                Text(
                    text = formatCurrency(dashboardSummary.monthlySpend),
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontSize = 18.sp,
                        lineHeight = 20.sp,
                    ),
                    color = HomeInk,
                    fontWeight = FontWeight.Bold,
                )
                val changeText = tr(Res.string.home_monthly_spend_change)
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = changeText.substringBeforeLast(" "),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 10.sp,
                            lineHeight = 12.sp,
                        ),
                        color = HomeMuted,
                        fontWeight = FontWeight.Medium,
                    )
                    Text(
                        text = changeText.substringAfterLast(" "),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 10.sp,
                            lineHeight = 12.sp,
                        ),
                        color = HomeOrange,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
            HomeSpendingBars(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 13.dp, bottom = 6.dp),
            )
        }
    }
}

@Composable
internal fun HomeUpcomingScheduleCard(
    events: List<Event>,
    onCardClick: () -> Unit,
    onEventClick: (String) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        HomeSectionHeader(
            title = tr(Res.string.home_upcoming_schedule_title),
            onViewAll = onCardClick,
        )
        HomeWhiteCard(
            contentPadding = 0.dp,
        ) {
            if (events.isEmpty()) {
                HomeEmptyStateContent(
                    text = tr(Res.string.home_no_upcoming_events),
                    onClick = onCardClick,
                )
            } else {
                Column(
                    modifier = Modifier.padding(horizontal = 11.dp, vertical = 7.dp),
                ) {
                    events.take(2).forEachIndexed { index, event ->
                        HomeScheduleRow(
                            row = event.toHomeScheduleRowModel(index),
                            onClick = { onEventClick(event.id) },
                        )
                        if (index == 0 && events.size > 1) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(14.dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                Spacer(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(1.dp)
                                        .background(HomeCardBorder),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeScheduleRow(
    row: HomeScheduleRowModel,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        HomeScheduleMediaPlaceholder(
            modifier = Modifier
                .size(HomeReferenceMetrics.ScheduleThumbnailSize)
                .clip(RoundedCornerShape(10.dp)),
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                HomeScheduleBadge(text = row.badge, tone = row.tone)
                Text(
                    text = row.title,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontSize = 11.sp,
                        lineHeight = 14.sp,
                    ),
                    color = HomeInk,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Text(
                text = row.dateLabel,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 10.sp,
                    lineHeight = 13.sp,
                ),
                color = HomeMuted,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = if (row.tone == GoodsPocketBadgeTone.Event) {
                MaterialTheme.colorScheme.tertiaryContainer
            } else {
                MaterialTheme.colorScheme.primaryContainer
            },
        ) {
            Text(
                text = row.dDay,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                style = MaterialTheme.typography.labelLarge.copy(
                    fontSize = 10.sp,
                    lineHeight = 13.sp,
                ),
                color = if (row.tone == GoodsPocketBadgeTone.Event) HomePurple else HomeOrange,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun HomeScheduleBadge(
    text: String,
    tone: GoodsPocketBadgeTone,
) {
    val isEventTone = tone == GoodsPocketBadgeTone.Event
    Surface(
        shape = CircleShape,
        color = if (isEventTone) {
            MaterialTheme.colorScheme.tertiaryContainer
        } else {
            MaterialTheme.colorScheme.primaryContainer
        },
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 9.sp,
                lineHeight = 11.sp,
            ),
            color = if (isEventTone) HomePurple else HomeOrange,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
        )
    }
}

@Composable
private fun HomeSectionHeader(
    title: String,
    onViewAll: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(HomeReferenceMetrics.SectionHeaderHeight)
            .padding(horizontal = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge.copy(
                fontSize = HomeReferenceMetrics.SectionHeaderFontSize,
                lineHeight = 17.sp,
            ),
            color = HomeInk,
            fontWeight = FontWeight.Bold,
        )
        Row(
            modifier = Modifier.clickable(onClick = onViewAll),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = tr(Res.string.home_view_all),
                style = MaterialTheme.typography.labelLarge.copy(
                    fontSize = 10.sp,
                    lineHeight = 13.sp,
                ),
                color = HomeMuted,
                fontWeight = FontWeight.Medium,
            )
            HomeViewAllChevron(modifier = Modifier.size(width = 5.dp, height = 9.dp))
        }
    }
}

private data class RecentGoodsCardModel(
    val id: String,
    val title: String,
    val subtitle: String,
)

private data class HomeScheduleRowModel(
    val badge: String,
    val title: String,
    val dateLabel: String,
    val dDay: String,
    val tone: GoodsPocketBadgeTone,
)

private fun ActivityRecord.toRecentGoodsCardModel(): RecentGoodsCardModel {
    return RecentGoodsCardModel(
        id = id,
        title = title,
        subtitle = subtitle,
    )
}

@Composable
private fun Event.toHomeScheduleRowModel(index: Int): HomeScheduleRowModel {
    val tone = if (eventType == EventType.OFFLINE_EVENT || index == 0) {
        GoodsPocketBadgeTone.Event
    } else {
        GoodsPocketBadgeTone.Reserved
    }
    return HomeScheduleRowModel(
        badge = eventType.localizedLabel(),
        title = title,
        dateLabel = targetDate.toHomeDateLabel(),
        dDay = "D-day",
        tone = tone,
    )
}

private fun String.toHomeDateLabel(): String {
    val parts = split("-", ".", "/")
    val month = parts.getOrNull(1)?.toIntOrNull()
    val day = parts.getOrNull(2)?.toIntOrNull()
    return if (month != null && day != null) "${month}월 ${day}일" else this
}

@Composable
private fun EmptyStateLine(
    text: String,
    onClick: () -> Unit,
) {
    HomeWhiteCard(
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        contentPadding = 0.dp,
    ) {
        HomeEmptyStateContent(text = text, onClick = onClick)
    }
}

@Composable
private fun HomeEmptyStateContent(
    text: String,
    onClick: () -> Unit,
) {
    Text(
        text = text,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        style = MaterialTheme.typography.bodyMedium,
        color = HomeMuted,
    )
}
