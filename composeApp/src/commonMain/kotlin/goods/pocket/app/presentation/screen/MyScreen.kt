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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import goods.pocket.app.domain.model.ActivityRecord
import goods.pocket.app.domain.model.Event
import goods.pocket.app.presentation.designsystem.GoodsPocketListRow
import goods.pocket.app.presentation.designsystem.GoodsPocketMetricPill
import goods.pocket.app.presentation.designsystem.GoodsPocketSectionCard
import goods.pocket.app.presentation.designsystem.GoodsPocketSectionHeader
import goods.pocket.app.presentation.i18n.formatCurrency
import goods.pocket.app.presentation.i18n.tr
import goods.pocket.app.presentation.state.MyPageUiModel
import goodspocket.composeapp.generated.resources.Res
import goodspocket.composeapp.generated.resources.my_local_profile_label
import goodspocket.composeapp.generated.resources.my_notifications_disabled
import goodspocket.composeapp.generated.resources.my_notifications_enabled
import goodspocket.composeapp.generated.resources.my_profile_hint
import goodspocket.composeapp.generated.resources.my_section_profile
import goodspocket.composeapp.generated.resources.my_section_summary
import goodspocket.composeapp.generated.resources.my_section_tools
import goodspocket.composeapp.generated.resources.my_summary_active_preorders
import goodspocket.composeapp.generated.resources.my_summary_hint
import goodspocket.composeapp.generated.resources.my_summary_monthly_spend
import goodspocket.composeapp.generated.resources.my_summary_owned_items
import goodspocket.composeapp.generated.resources.my_summary_upcoming_events
import goodspocket.composeapp.generated.resources.my_sync_not_connected
import goodspocket.composeapp.generated.resources.my_tools_hint
import goodspocket.composeapp.generated.resources.my_utility_notifications
import goodspocket.composeapp.generated.resources.my_utility_recent_activity
import goodspocket.composeapp.generated.resources.my_utility_recent_activity_count
import goodspocket.composeapp.generated.resources.my_utility_recent_activity_empty
import goodspocket.composeapp.generated.resources.my_utility_settings
import goodspocket.composeapp.generated.resources.my_utility_settings_subtitle
import goodspocket.composeapp.generated.resources.my_utility_spending_report
import goodspocket.composeapp.generated.resources.my_utility_sync_backup
import goodspocket.composeapp.generated.resources.my_utility_sync_backup_subtitle
import goodspocket.composeapp.generated.resources.my_utility_upcoming_events
import goodspocket.composeapp.generated.resources.my_utility_upcoming_events_count
import goodspocket.composeapp.generated.resources.my_utility_upcoming_events_empty

@Composable
fun MyScreen(
    myPage: MyPageUiModel,
    recentActivities: List<ActivityRecord>,
    upcomingEvents: List<Event>,
    onOpenTransactions: () -> Unit,
    onOpenEvents: () -> Unit,
    onOpenSettings: () -> Unit,
) {
    val notificationsLabel = if (myPage.notificationsEnabled) {
        tr(Res.string.my_notifications_enabled)
    } else {
        tr(Res.string.my_notifications_disabled)
    }

    LazyColumn(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            GoodsPocketSectionCard(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            ) {
                GoodsPocketSectionHeader(
                    title = tr(Res.string.my_section_profile),
                    subtitle = tr(Res.string.my_profile_hint),
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Surface(
                        modifier = Modifier.size(58.dp),
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.22f),
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = localizedDisplayName(myPage).take(1),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = localizedDisplayName(myPage),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                        )
                        Text(
                            text = localizedSyncStatusLabel(myPage),
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
        item {
            GoodsPocketSectionCard(containerColor = MaterialTheme.colorScheme.surface) {
                GoodsPocketSectionHeader(
                    title = tr(Res.string.my_section_summary),
                    subtitle = tr(Res.string.my_summary_hint),
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    GoodsPocketMetricPill(
                        label = tr(Res.string.my_summary_owned_items),
                        value = myPage.ownedItemCount.toString(),
                    )
                    GoodsPocketMetricPill(
                        label = tr(Res.string.my_summary_active_preorders),
                        value = myPage.activePreorderCount.toString(),
                    )
                    GoodsPocketMetricPill(
                        label = tr(Res.string.my_summary_monthly_spend),
                        value = formatCurrency(myPage.monthlySpend),
                    )
                    GoodsPocketMetricPill(
                        label = tr(Res.string.my_summary_upcoming_events),
                        value = myPage.upcomingEventCount.toString(),
                    )
                }
            }
        }
        item {
            GoodsPocketSectionCard(containerColor = MaterialTheme.colorScheme.surface) {
                GoodsPocketSectionHeader(
                    title = tr(Res.string.my_section_tools),
                    subtitle = tr(Res.string.my_tools_hint),
                )
                GoodsPocketListRow(
                    title = tr(Res.string.my_utility_recent_activity),
                    subtitle = if (recentActivities.isEmpty()) {
                        tr(Res.string.my_utility_recent_activity_empty)
                    } else {
                        tr(Res.string.my_utility_recent_activity_count, recentActivities.size)
                    },
                )
                GoodsPocketListRow(
                    title = tr(Res.string.my_utility_upcoming_events),
                    subtitle = if (upcomingEvents.isEmpty()) {
                        tr(Res.string.my_utility_upcoming_events_empty)
                    } else {
                        tr(Res.string.my_utility_upcoming_events_count, myPage.upcomingEventCount)
                    },
                    onClick = onOpenEvents,
                )
                GoodsPocketListRow(
                    title = tr(Res.string.my_utility_spending_report),
                    subtitle = formatCurrency(myPage.monthlySpend),
                    onClick = onOpenTransactions,
                )
                GoodsPocketListRow(
                    title = tr(Res.string.my_utility_sync_backup),
                    subtitle = if (localizedSyncStatusLabel(myPage) == tr(Res.string.my_sync_not_connected)) {
                        tr(Res.string.my_utility_sync_backup_subtitle)
                    } else {
                        localizedSyncStatusLabel(myPage)
                    },
                )
                GoodsPocketListRow(
                    title = tr(Res.string.my_utility_notifications),
                    subtitle = notificationsLabel,
                )
                GoodsPocketListRow(
                    title = tr(Res.string.my_utility_settings),
                    subtitle = tr(Res.string.my_utility_settings_subtitle),
                    onClick = onOpenSettings,
                )
            }
        }
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
