package goods.pocket.app.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import goods.pocket.app.presentation.designsystem.GoodsPocketSectionCard
import goods.pocket.app.presentation.designsystem.GoodsPocketSectionHeader
import goods.pocket.app.presentation.designsystem.goodsPocketPrimaryScrollContentPadding
import goods.pocket.app.presentation.designsystem.goodsPocketScreenModifier
import goods.pocket.app.presentation.i18n.tr
import goods.pocket.app.presentation.state.MyPageUiModel
import goodspocket.composeapp.generated.resources.Res
import goodspocket.composeapp.generated.resources.my_account_management
import goodspocket.composeapp.generated.resources.my_local_profile_label
import goodspocket.composeapp.generated.resources.my_notifications_disabled
import goodspocket.composeapp.generated.resources.my_notifications_enabled
import goodspocket.composeapp.generated.resources.my_profile_hint
import goodspocket.composeapp.generated.resources.my_summary_active_preorders
import goodspocket.composeapp.generated.resources.my_summary_owned_items
import goodspocket.composeapp.generated.resources.my_sync_not_connected
import goodspocket.composeapp.generated.resources.my_utility_notifications
import goodspocket.composeapp.generated.resources.my_utility_settings
import goodspocket.composeapp.generated.resources.my_utility_settings_subtitle
import goodspocket.composeapp.generated.resources.my_utility_sync_backup
import goodspocket.composeapp.generated.resources.my_utility_sync_backup_subtitle

@Composable
fun MyScreen(
    myPage: MyPageUiModel,
    onOpenSettings: () -> Unit,
) {
    val notificationsLabel = if (myPage.notificationsEnabled) {
        tr(Res.string.my_notifications_enabled)
    } else {
        tr(Res.string.my_notifications_disabled)
    }
    val quickLinks = buildMyHubQuickLinks(
        myPage = myPage,
        recentActivities = emptyList(),
        upcomingEvents = emptyList(),
    )

    LazyColumn(
        modifier = goodsPocketScreenModifier(),
        contentPadding = goodsPocketPrimaryScrollContentPadding(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            GoodsPocketSectionCard(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                contentColor = MaterialTheme.colorScheme.onSurface,
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Surface(
                        modifier = Modifier.size(76.dp),
                        shape = MaterialTheme.shapes.large,
                        color = MaterialTheme.colorScheme.surface,
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = localizedDisplayName(myPage).take(1),
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                            )
                        }
                    }
                    Text(
                        text = localizedDisplayName(myPage),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        text = localizedSyncStatusLabel(myPage),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        text = tr(Res.string.my_profile_hint),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                MyHubStatCard(
                    modifier = Modifier.weight(1f),
                    label = tr(Res.string.my_summary_owned_items),
                    value = myPage.ownedItemCount.toString(),
                )
                MyHubStatCard(
                    modifier = Modifier.weight(1f),
                    label = tr(Res.string.my_summary_active_preorders),
                    value = myPage.activePreorderCount.toString(),
                )
            }
        }
        item {
            GoodsPocketSectionCard(containerColor = MaterialTheme.colorScheme.surface) {
                GoodsPocketSectionHeader(title = tr(Res.string.my_account_management))
                quickLinks.forEach { link ->
                    MyHubQuickLinkRow(
                        link = link,
                        subtitle = quickLinkSubtitle(
                            link = link,
                            myPage = myPage,
                            notificationsLabel = notificationsLabel,
                        ),
                        accentColor = quickLinkAccentColor(link.action),
                        onClick = when (link.action) {
                            MyHubAction.SETTINGS -> onOpenSettings
                            else -> null
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun MyHubStatCard(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
) {
    GoodsPocketSectionCard(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.SemiBold,
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun MyHubQuickLinkRow(
    link: MyHubQuickLinkModel,
    subtitle: String,
    accentColor: Color,
    onClick: (() -> Unit)?,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
            .then(
                if (onClick != null) {
                    Modifier.clickable(onClick = onClick)
                } else {
                    Modifier
                },
            ),
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        shape = MaterialTheme.shapes.medium,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(
                modifier = Modifier.size(30.dp),
                color = accentColor,
                shape = MaterialTheme.shapes.small,
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = quickLinkShortLabel(link.action),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(3.dp),
            ) {
                Text(
                    text = quickLinkTitle(link.action),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Text(
                text = link.badgeCount?.toString() ?: ">",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun quickLinkTitle(action: MyHubAction): String {
    return when (action) {
        MyHubAction.SYNC_BACKUP -> tr(Res.string.my_utility_sync_backup)
        MyHubAction.NOTIFICATIONS -> tr(Res.string.my_utility_notifications)
        MyHubAction.SETTINGS -> tr(Res.string.my_utility_settings)
    }
}

@Composable
private fun quickLinkSubtitle(
    link: MyHubQuickLinkModel,
    myPage: MyPageUiModel,
    notificationsLabel: String,
): String {
    return when (link.action) {
        MyHubAction.SYNC_BACKUP -> if (localizedSyncStatusLabel(myPage) == tr(Res.string.my_sync_not_connected)) {
            tr(Res.string.my_utility_sync_backup_subtitle)
        } else {
            localizedSyncStatusLabel(myPage)
        }

        MyHubAction.NOTIFICATIONS -> notificationsLabel
        MyHubAction.SETTINGS -> tr(Res.string.my_utility_settings_subtitle)
    }
}

@Composable
private fun quickLinkAccentColor(action: MyHubAction): Color {
    return when (action) {
        MyHubAction.SYNC_BACKUP -> MaterialTheme.colorScheme.surfaceContainerHigh
        MyHubAction.NOTIFICATIONS -> MaterialTheme.colorScheme.surfaceContainerHigh
        MyHubAction.SETTINGS -> MaterialTheme.colorScheme.surfaceContainerHigh
    }
}

@Composable
private fun quickLinkShortLabel(action: MyHubAction): String {
    return quickLinkTitle(action).take(1)
}

@Composable
private fun localizedDisplayName(
    myPage: MyPageUiModel,
): String {
    return myPage.displayName.takeIf { it.isNotBlank() }
        ?: tr(Res.string.my_local_profile_label)
}

@Composable
private fun localizedSyncStatusLabel(
    myPage: MyPageUiModel,
): String {
    return myPage.syncStatusLabel.takeIf { it.isNotBlank() }
        ?: tr(Res.string.my_sync_not_connected)
}
