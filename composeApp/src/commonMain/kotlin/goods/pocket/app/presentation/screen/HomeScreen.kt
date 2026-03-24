package goods.pocket.app.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import goods.pocket.app.domain.model.ActivityRecord
import goods.pocket.app.domain.model.Event
import goods.pocket.app.domain.model.HomeSummary
import goods.pocket.app.presentation.designsystem.GoodsPocketHeroCard
import goods.pocket.app.presentation.designsystem.GoodsPocketListRow
import goods.pocket.app.presentation.designsystem.GoodsPocketSectionCard
import goods.pocket.app.presentation.designsystem.GoodsPocketSectionHeader
import goods.pocket.app.presentation.designsystem.GoodsPocketTonalBadge
import goods.pocket.app.presentation.designsystem.goodsPocketScreenModifier
import goods.pocket.app.presentation.i18n.formatCurrency
import goods.pocket.app.presentation.i18n.localizedLabel
import goods.pocket.app.presentation.i18n.tr
import goodspocket.composeapp.generated.resources.Res
import goodspocket.composeapp.generated.resources.home_monthly_overview
import goodspocket.composeapp.generated.resources.home_monthly_summary_hint
import goodspocket.composeapp.generated.resources.home_no_recent_activity
import goodspocket.composeapp.generated.resources.home_no_upcoming_events
import goodspocket.composeapp.generated.resources.home_owned_count_unit
import goodspocket.composeapp.generated.resources.home_recent_activity
import goodspocket.composeapp.generated.resources.home_recent_activity_subtitle
import goodspocket.composeapp.generated.resources.home_stat_active_preorder_subtitle
import goodspocket.composeapp.generated.resources.home_stat_active_preorder
import goodspocket.composeapp.generated.resources.home_stat_owned
import goodspocket.composeapp.generated.resources.home_upcoming
import goodspocket.composeapp.generated.resources.home_upcoming_subtitle

@Composable
fun HomeScreen(
    dashboardSummary: HomeSummary,
    upcomingEvents: List<Event>,
    onMonthlySummaryClick: () -> Unit = {},
    onUpcomingEventsClick: () -> Unit = {},
    onRecentActivitiesClick: () -> Unit = {},
    onUpcomingEventClick: (String) -> Unit = {},
    onRecentActivityClick: (String) -> Unit = {},
) {
    LazyColumn(
        modifier = goodsPocketScreenModifier(),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        item {
            SummaryOverviewRow(
                dashboardSummary = dashboardSummary,
                onClick = onMonthlySummaryClick,
            )
        }
        item {
            PlannerBoardCard(
                dashboardSummary = dashboardSummary,
                upcomingEvents = upcomingEvents,
                onMonthlySummaryClick = onMonthlySummaryClick,
                onUpcomingEventsClick = onUpcomingEventsClick,
            )
        }
        item {
            UpcomingEventsCard(
                events = upcomingEvents,
                onCardClick = onUpcomingEventsClick,
                onEventClick = onUpcomingEventClick,
            )
        }
        item {
            RecentActivityCard(
                activities = dashboardSummary.recentActivities,
                onCardClick = onRecentActivitiesClick,
                onActivityClick = onRecentActivityClick,
            )
        }
    }
}

@Composable
private fun SummaryOverviewRow(
    dashboardSummary: HomeSummary,
    onClick: () -> Unit,
) {
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        GoodsPocketHeroCard(
            modifier = Modifier.weight(1.15f),
            onClick = onClick,
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                GoodsPocketTonalBadge(
                    text = tr(Res.string.home_stat_owned),
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                )
                Text(
                    text = dashboardSummary.ownedItemCount.toString(),
                    style = MaterialTheme.typography.displayLarge,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = tr(Res.string.home_owned_count_unit),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        GoodsPocketSectionCard(
            modifier = Modifier.weight(1f),
            onClick = onClick,
            containerColor = MaterialTheme.colorScheme.surface,
        ) {
            GoodsPocketTonalBadge(
                text = tr(Res.string.home_monthly_overview),
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
            )
            Text(
                text = formatCurrency(dashboardSummary.monthlySpend),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = tr(Res.string.home_monthly_summary_hint),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun PlannerBoardCard(
    dashboardSummary: HomeSummary,
    upcomingEvents: List<Event>,
    onMonthlySummaryClick: () -> Unit,
    onUpcomingEventsClick: () -> Unit,
) {
    GoodsPocketSectionCard(containerColor = MaterialTheme.colorScheme.surface) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            GoodsPocketSectionHeader(
                title = tr(Res.string.home_stat_active_preorder),
                subtitle = tr(
                    Res.string.home_stat_active_preorder_subtitle,
                    dashboardSummary.activePreorderCount,
                ),
            )
            GoodsPocketTonalBadge(
                text = dashboardSummary.activePreorderCount.toString(),
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        )
        {
            PlannerMiniCard(
                title = "수령 대기",
                subtitle = "${dashboardSummary.activePreorderCount}건",
            )
            PlannerMiniCard(
                title = "지출 보기",
                subtitle = formatCurrency(dashboardSummary.monthlySpend),
                onClick = onMonthlySummaryClick,
            )
            PlannerMiniCard(
                title = "일정 보기",
                subtitle = "${upcomingEvents.size}건",
                onClick = onUpcomingEventsClick,
            )
            PlannerMiniCard(
                title = "빠른 추가",
                subtitle = "+",
            )
        }
    }
}

@Composable
private fun PlannerMiniCard(
    title: String,
    subtitle: String,
    onClick: (() -> Unit)? = null,
) {
    GoodsPocketSectionCard(
        modifier = Modifier.widthIn(min = 132.dp),
        onClick = onClick,
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Surface(
            color = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(16.dp),
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 14.dp),
                text = subtitle,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

@Composable
private fun UpcomingEventsCard(
    events: List<Event>,
    onCardClick: () -> Unit,
    onEventClick: (String) -> Unit,
) {
    GoodsPocketSectionCard(
        onClick = onCardClick,
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        GoodsPocketSectionHeader(
            title = tr(Res.string.home_upcoming),
            subtitle = tr(Res.string.home_upcoming_subtitle),
        )
        if (events.isEmpty()) {
            EmptyStateLine(tr(Res.string.home_no_upcoming_events))
        } else {
            events.take(3).forEach { event ->
                GoodsPocketListRow(
                    title = event.title,
                    subtitle = event.eventType.localizedLabel(),
                    onClick = { onEventClick(event.id) },
                    trailing = event.targetDate,
                )
            }
        }
    }
}

@Composable
private fun RecentActivityCard(
    activities: List<ActivityRecord>,
    onCardClick: () -> Unit,
    onActivityClick: (String) -> Unit,
) {
    GoodsPocketSectionCard(
        onClick = onCardClick,
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        GoodsPocketSectionHeader(
            title = tr(Res.string.home_recent_activity),
            subtitle = tr(Res.string.home_recent_activity_subtitle),
        )
        if (activities.isEmpty()) {
            EmptyStateLine(tr(Res.string.home_no_recent_activity))
        } else {
            activities.take(4).forEach { activity ->
                GoodsPocketListRow(
                    title = activity.title,
                    subtitle = activity.subtitle,
                    onClick = { onActivityClick(activity.id) },
                    trailing = activity.happenedAt,
                )
            }
        }
    }
}

@Composable
private fun EmptyStateLine(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
