package goods.pocket.app.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import goods.pocket.app.domain.model.ActivityRecord
import goods.pocket.app.domain.model.Event
import goods.pocket.app.domain.model.HomeSummary
import goods.pocket.app.presentation.i18n.formatCurrency
import goods.pocket.app.presentation.i18n.localizedLabel
import goods.pocket.app.presentation.i18n.tr
import goodspocket.composeapp.generated.resources.*

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
        verticalArrangement = Arrangement.spacedBy(14.dp),
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
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        ),
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Text(
                text = tr(Res.string.home_monthly_overview),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = formatCurrency(dashboardSummary.monthlySpend),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                StatPill(
                    label = tr(Res.string.home_stat_owned),
                    value = dashboardSummary.ownedItemCount.toString(),
                )
                StatPill(
                    label = tr(Res.string.home_stat_active_preorder),
                    value = dashboardSummary.activePreorderCount.toString(),
                )
                StatPill(
                    label = tr(Res.string.home_stat_recent),
                    value = dashboardSummary.recentActivities.size.toString(),
                )
            }
            Text(
                text = tr(Res.string.home_monthly_summary_hint),
                style = MaterialTheme.typography.bodyMedium,
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
    Card(
        onClick = onCardClick,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            SectionHeader(
                title = tr(Res.string.home_upcoming),
                subtitle = tr(Res.string.home_upcoming_subtitle),
            )
            if (events.isEmpty()) {
                EmptyStateLine(tr(Res.string.home_no_upcoming_events))
            } else {
                events.forEach { event ->
                    ActionRow(
                        primary = event.title,
                        secondary = "${event.targetDate} · ${event.eventType.localizedLabel()}",
                        onClick = { onEventClick(event.id) },
                    )
                }
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
    Card(
        onClick = onCardClick,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            SectionHeader(
                title = tr(Res.string.home_recent_activity),
                subtitle = tr(Res.string.home_recent_activity_subtitle),
            )
            if (activities.isEmpty()) {
                EmptyStateLine(tr(Res.string.home_no_recent_activity))
            } else {
                activities.forEach { activity ->
                    ActionRow(
                        primary = activity.title,
                        secondary = "${activity.happenedAt} · ${activity.subtitle}",
                        onClick = { onActivityClick(activity.id) },
                    )
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    subtitle: String,
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun ActionRow(
    primary: String,
    secondary: String,
    onClick: () -> Unit,
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.38f),
        ),
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = primary,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
            )
            Text(
                text = secondary,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun StatPill(
    label: String,
    value: String,
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.32f),
        ),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
            )
        }
    }
}

@Composable
private fun EmptyStateLine(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}
