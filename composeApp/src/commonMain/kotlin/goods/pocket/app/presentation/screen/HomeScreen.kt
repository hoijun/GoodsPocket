package goods.pocket.app.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
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
import goods.pocket.app.presentation.designsystem.GoodsPocketMetricPill
import goods.pocket.app.presentation.designsystem.GoodsPocketSectionCard
import goods.pocket.app.presentation.designsystem.GoodsPocketSectionHeader
import goods.pocket.app.presentation.designsystem.GoodsPocketTonalBadge
import goods.pocket.app.presentation.i18n.formatCurrency
import goods.pocket.app.presentation.i18n.localizedLabel
import goods.pocket.app.presentation.i18n.tr
import goodspocket.composeapp.generated.resources.Res
import goodspocket.composeapp.generated.resources.home_monthly_overview
import goodspocket.composeapp.generated.resources.home_monthly_summary_hint
import goodspocket.composeapp.generated.resources.home_no_recent_activity
import goodspocket.composeapp.generated.resources.home_no_upcoming_events
import goodspocket.composeapp.generated.resources.home_recent_activity
import goodspocket.composeapp.generated.resources.home_recent_activity_subtitle
import goodspocket.composeapp.generated.resources.home_stat_active_preorder
import goodspocket.composeapp.generated.resources.home_stat_owned
import goodspocket.composeapp.generated.resources.home_stat_recent
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
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            DashboardHeroCard(
                dashboardSummary = dashboardSummary,
                onClick = onMonthlySummaryClick,
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
private fun DashboardHeroCard(
    dashboardSummary: HomeSummary,
    onClick: () -> Unit,
) {
    GoodsPocketHeroCard(onClick = onClick) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            GoodsPocketTonalBadge(
                text = tr(Res.string.home_monthly_overview),
                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.14f),
                contentColor = MaterialTheme.colorScheme.onPrimary,
            )
            Text(
                text = formatCurrency(dashboardSummary.monthlySpend),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = tr(Res.string.home_monthly_summary_hint),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.82f),
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                GoodsPocketMetricPill(
                    label = tr(Res.string.home_stat_owned),
                    value = dashboardSummary.ownedItemCount.toString(),
                )
                GoodsPocketMetricPill(
                    label = tr(Res.string.home_stat_active_preorder),
                    value = dashboardSummary.activePreorderCount.toString(),
                )
                GoodsPocketMetricPill(
                    label = tr(Res.string.home_stat_recent),
                    value = dashboardSummary.recentActivities.size.toString(),
                )
            }
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
                    subtitle = "${event.targetDate} · ${event.eventType.localizedLabel()}",
                    onClick = { onEventClick(event.id) },
                    trailing = null,
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
                    subtitle = "${activity.happenedAt} · ${activity.subtitle}",
                    onClick = { onActivityClick(activity.id) },
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
