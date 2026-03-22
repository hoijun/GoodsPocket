package goods.pocket.app.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.CircleShape
import goods.pocket.app.domain.model.ActivityRecord
import goods.pocket.app.domain.model.Event
import goods.pocket.app.presentation.i18n.formatCurrency
import goods.pocket.app.presentation.state.MyPageUiModel
import goodspocket.composeapp.generated.resources.Res
import goodspocket.composeapp.generated.resources.my_local_profile_label
import goodspocket.composeapp.generated.resources.my_notifications_disabled
import goodspocket.composeapp.generated.resources.my_notifications_enabled
import goodspocket.composeapp.generated.resources.my_profile_hint
import goodspocket.composeapp.generated.resources.my_section_profile
import goodspocket.composeapp.generated.resources.my_section_summary
import goodspocket.composeapp.generated.resources.my_section_tools
import goodspocket.composeapp.generated.resources.my_summary_hint
import goodspocket.composeapp.generated.resources.my_summary_active_preorders
import goodspocket.composeapp.generated.resources.my_summary_monthly_spend
import goodspocket.composeapp.generated.resources.my_summary_owned_items
import goodspocket.composeapp.generated.resources.my_summary_upcoming_events
import goodspocket.composeapp.generated.resources.my_sync_not_connected
import goodspocket.composeapp.generated.resources.my_tools_hint
import goodspocket.composeapp.generated.resources.my_utility_notifications
import goodspocket.composeapp.generated.resources.my_utility_recent_activity
import goodspocket.composeapp.generated.resources.my_utility_recent_activity_empty
import goodspocket.composeapp.generated.resources.my_utility_recent_activity_count
import goodspocket.composeapp.generated.resources.my_utility_settings
import goodspocket.composeapp.generated.resources.my_utility_settings_subtitle
import goodspocket.composeapp.generated.resources.my_utility_spending_report
import goodspocket.composeapp.generated.resources.my_utility_spending_report_subtitle
import goodspocket.composeapp.generated.resources.my_utility_sync_backup
import goodspocket.composeapp.generated.resources.my_utility_sync_backup_subtitle
import goodspocket.composeapp.generated.resources.my_utility_upcoming_events
import goodspocket.composeapp.generated.resources.my_utility_upcoming_events_count
import goodspocket.composeapp.generated.resources.my_utility_upcoming_events_empty
import goods.pocket.app.presentation.i18n.tr

@Composable
fun MyScreen(
    myPage: MyPageUiModel,
    recentActivities: List<ActivityRecord>,
    upcomingEvents: List<Event>,
    onOpenTransactions: () -> Unit,
    onOpenEvents: () -> Unit,
    onOpenSettings: () -> Unit,
) {
    LazyColumn(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        item {
            ProfileStatusCard(
                displayName = localizedDisplayName(myPage),
                syncStatusLabel = localizedSyncStatusLabel(myPage),
                notificationsLabel = if (myPage.notificationsEnabled) {
                    tr(Res.string.my_notifications_enabled)
                } else {
                    tr(Res.string.my_notifications_disabled)
                },
            )
        }
        item {
            SummaryCard(myPage = myPage)
        }
        item {
            UtilityLinksCard(
                recentActivities = recentActivities,
                upcomingEvents = upcomingEvents,
                monthlySpend = myPage.monthlySpend,
                upcomingEventCount = myPage.upcomingEventCount,
                syncStatusLabel = localizedSyncStatusLabel(myPage),
                notificationsLabel = if (myPage.notificationsEnabled) {
                    tr(Res.string.my_notifications_enabled)
                } else {
                    tr(Res.string.my_notifications_disabled)
                },
                onOpenTransactions = onOpenTransactions,
                onOpenEvents = onOpenEvents,
                onOpenSettings = onOpenSettings,
            )
        }
    }
}

@Composable
private fun ProfileStatusCard(
    displayName: String,
    syncStatusLabel: String,
    notificationsLabel: String,
) {
    Card(
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
            SectionHeader(
                title = tr(Res.string.my_section_profile),
                subtitle = tr(Res.string.my_profile_hint),
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Surface(
                    modifier = Modifier.size(54.dp),
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.32f),
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = displayName.take(1),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = displayName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        text = syncStatusLabel,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Text(
                        text = notificationsLabel,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
        }
    }
}

@Composable
private fun SummaryCard(
    myPage: MyPageUiModel,
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            SectionHeader(
                title = tr(Res.string.my_section_summary),
                subtitle = tr(Res.string.my_summary_hint),
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                SummaryStatCard(
                    label = tr(Res.string.my_summary_owned_items),
                    value = myPage.ownedItemCount.toString(),
                )
                SummaryStatCard(
                    label = tr(Res.string.my_summary_active_preorders),
                    value = myPage.activePreorderCount.toString(),
                )
                SummaryStatCard(
                    label = tr(Res.string.my_summary_monthly_spend),
                    value = formatCurrency(myPage.monthlySpend),
                )
                SummaryStatCard(
                    label = tr(Res.string.my_summary_upcoming_events),
                    value = myPage.upcomingEventCount.toString(),
                )
            }
        }
    }
}

@Composable
private fun UtilityLinksCard(
    recentActivities: List<ActivityRecord>,
    upcomingEvents: List<Event>,
    monthlySpend: Long,
    upcomingEventCount: Int,
    syncStatusLabel: String,
    notificationsLabel: String,
    onOpenTransactions: () -> Unit,
    onOpenEvents: () -> Unit,
    onOpenSettings: () -> Unit,
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            SectionHeader(
                title = tr(Res.string.my_section_tools),
                subtitle = tr(Res.string.my_tools_hint),
            )
            UtilityRow(
                label = tr(Res.string.my_utility_recent_activity),
                subtitle = if (recentActivities.isEmpty()) {
                    tr(Res.string.my_utility_recent_activity_empty)
                } else {
                    tr(Res.string.my_utility_recent_activity_count, recentActivities.size)
                },
            )
            UtilityRow(
                label = tr(Res.string.my_utility_upcoming_events),
                subtitle = if (upcomingEvents.isEmpty()) {
                    tr(Res.string.my_utility_upcoming_events_empty)
                } else {
                    tr(Res.string.my_utility_upcoming_events_count, upcomingEventCount)
                },
                onClick = onOpenEvents,
            )
            UtilityRow(
                label = tr(Res.string.my_utility_spending_report),
                subtitle = formatCurrency(monthlySpend),
                onClick = onOpenTransactions,
            )
            UtilityRow(
                label = tr(Res.string.my_utility_sync_backup),
                subtitle = if (syncStatusLabel == tr(Res.string.my_sync_not_connected)) {
                    tr(Res.string.my_utility_sync_backup_subtitle)
                } else {
                    syncStatusLabel
                },
            )
            UtilityRow(
                label = tr(Res.string.my_utility_notifications),
                subtitle = notificationsLabel,
            )
            UtilityRow(
                label = tr(Res.string.my_utility_settings),
                subtitle = tr(Res.string.my_utility_settings_subtitle),
                onClick = onOpenSettings,
            )
        }
    }
}

@Composable
private fun SummaryStatCard(
    label: String,
    value: String,
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.38f),
        ),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
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
private fun UtilityRow(
    label: String,
    subtitle: String,
    onClick: (() -> Unit)? = null,
) {
    if (onClick == null) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.32f),
            ),
        ) {
            UtilityRowContent(
                label = label,
                subtitle = subtitle,
                showArrow = false,
            )
        }
        return
    }

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.32f),
        ),
    ) {
        UtilityRowContent(
            label = label,
            subtitle = subtitle,
            showArrow = true,
        )
    }
}

@Composable
private fun UtilityRowContent(
    label: String,
    subtitle: String,
    showArrow: Boolean,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        if (showArrow) {
            Text(
                text = ">",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
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
private fun localizedDisplayName(
    myPage: MyPageUiModel,
): String {
    return myPage.displayName.takeIf { it != "Local Profile" }
        ?: tr(Res.string.my_local_profile_label)
}

@Composable
private fun localizedSyncStatusLabel(
    myPage: MyPageUiModel,
): String {
    return myPage.syncStatusLabel.takeIf { it != "Not connected" }
        ?: tr(Res.string.my_sync_not_connected)
}
