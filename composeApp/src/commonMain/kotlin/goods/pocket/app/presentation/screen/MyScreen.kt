package goods.pocket.app.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import goods.pocket.app.presentation.designsystem.GoodsPocketVisualTokens
import goods.pocket.app.presentation.i18n.formatCurrency
import goods.pocket.app.presentation.i18n.tr
import goods.pocket.app.presentation.state.MyPageUiModel
import goodspocket.composeapp.generated.resources.Res
import goodspocket.composeapp.generated.resources.my_account_management
import goodspocket.composeapp.generated.resources.my_local_profile_label
import goodspocket.composeapp.generated.resources.my_notifications_disabled
import goodspocket.composeapp.generated.resources.my_profile_hint
import goodspocket.composeapp.generated.resources.my_section_summary
import goodspocket.composeapp.generated.resources.my_summary_active_preorders
import goodspocket.composeapp.generated.resources.my_summary_monthly_spend
import goodspocket.composeapp.generated.resources.my_summary_owned_items
import goodspocket.composeapp.generated.resources.my_summary_upcoming_events
import goodspocket.composeapp.generated.resources.my_sync_not_connected
import goodspocket.composeapp.generated.resources.my_utility_notifications
import goodspocket.composeapp.generated.resources.my_utility_settings
import goodspocket.composeapp.generated.resources.my_utility_settings_subtitle
import goodspocket.composeapp.generated.resources.my_utility_sync_backup
import goodspocket.composeapp.generated.resources.my_utility_sync_backup_subtitle
import goodspocket.composeapp.generated.resources.nav_my

@Composable
fun MyScreen(
    myPage: MyPageUiModel,
    onOpenSettings: () -> Unit,
) {
    val displayName = myPage.displayName.ifBlank { tr(Res.string.my_local_profile_label) }
    val syncStatus = myPage.syncStatusLabel.ifBlank { tr(Res.string.my_sync_not_connected) }
    val summaryItems = listOf(
        MySummaryItem(
            label = tr(Res.string.my_summary_owned_items),
            value = myPage.ownedItemCount.toString(),
            tone = MySummaryTone.Owned,
            icon = MyReferenceIconKind.Collection,
        ),
        MySummaryItem(
            label = tr(Res.string.my_summary_active_preorders),
            value = myPage.activePreorderCount.toString(),
            tone = MySummaryTone.Reserved,
            icon = MyReferenceIconKind.Calendar,
        ),
        MySummaryItem(
            label = tr(Res.string.my_summary_monthly_spend),
            value = formatCurrency(myPage.monthlySpend),
            tone = MySummaryTone.Spending,
            icon = MyReferenceIconKind.Spending,
        ),
        MySummaryItem(
            label = tr(Res.string.my_summary_upcoming_events),
            value = myPage.upcomingEventCount.toString(),
            tone = MySummaryTone.Upcoming,
            icon = MyReferenceIconKind.Clock,
        ),
    )
    val managementItems = buildMyHubQuickLinks().map { link ->
        when (link.action) {
            MyHubAction.SYNC_BACKUP -> MyManagementItem(
                action = link.action,
                title = tr(Res.string.my_utility_sync_backup),
                subtitle = if (syncStatus == tr(Res.string.my_sync_not_connected)) {
                    tr(Res.string.my_utility_sync_backup_subtitle)
                } else {
                    syncStatus
                },
                icon = MyReferenceIconKind.Sync,
            )
            MyHubAction.NOTIFICATIONS -> MyManagementItem(
                action = link.action,
                title = tr(Res.string.my_utility_notifications),
                subtitle = tr(Res.string.my_notifications_disabled),
                icon = MyReferenceIconKind.Bell,
            )
            MyHubAction.SETTINGS -> MyManagementItem(
                action = link.action,
                title = tr(Res.string.my_utility_settings),
                subtitle = tr(Res.string.my_utility_settings_subtitle),
                icon = MyReferenceIconKind.Settings,
            )
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(GoodsPocketVisualTokens.Background)),
        contentPadding = PaddingValues(bottom = MyReferenceMetrics.BottomContentPadding),
    ) {
        item {
            MyPageHeader(title = tr(Res.string.nav_my))
        }
        item {
            MyProfileCard(
                displayName = displayName,
                syncStatus = syncStatus,
                hint = tr(Res.string.my_profile_hint),
            )
        }
        item {
            MySectionHeader(
                title = tr(Res.string.my_section_summary),
                topSpacing = MyReferenceMetrics.ProfileToSummaryTitleSpacing,
                bottomSpacing = MyReferenceMetrics.SectionTitleToCardSpacing,
            )
        }
        item {
            MySummaryCard(items = summaryItems)
        }
        item {
            MySectionHeader(
                title = tr(Res.string.my_account_management),
                topSpacing = MyReferenceMetrics.SummaryToManagementTitleSpacing,
                bottomSpacing = MyReferenceMetrics.ManagementTitleToCardSpacing,
            )
        }
        item {
            MyManagementCard(
                items = managementItems,
                onOpenSettings = onOpenSettings,
            )
        }
    }
}
