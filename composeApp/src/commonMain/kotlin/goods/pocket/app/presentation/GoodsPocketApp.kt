package goods.pocket.app.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import goods.pocket.app.presentation.designsystem.goodsPocketChromeFor
import goods.pocket.app.presentation.component.EventDetailSheet
import goods.pocket.app.presentation.component.EventEditorSheet
import goods.pocket.app.presentation.component.ItemDetailSheet
import goods.pocket.app.presentation.component.ItemEditorSheet
import goods.pocket.app.presentation.component.PreorderDetailSheet
import goods.pocket.app.presentation.component.PreorderEditorSheet
import goods.pocket.app.presentation.component.QuickAddSheet
import goods.pocket.app.presentation.component.TransactionDetailSheet
import goods.pocket.app.presentation.component.TransactionEditorSheet
import goods.pocket.app.presentation.i18n.ProvideLocalizedResources
import goods.pocket.app.presentation.i18n.localizedLabel
import goods.pocket.app.presentation.i18n.tr
import goods.pocket.app.presentation.navigation.AppDestination
import goods.pocket.app.presentation.screen.CollectionScreen
import goods.pocket.app.presentation.screen.EventsScreen
import goods.pocket.app.presentation.screen.HomeScreen
import goods.pocket.app.presentation.screen.MyScreen
import goods.pocket.app.presentation.screen.PreordersScreen
import goods.pocket.app.presentation.screen.SettingsScreen
import goods.pocket.app.presentation.screen.TransactionsScreen
import goods.pocket.app.presentation.state.ActiveDetail
import goods.pocket.app.presentation.state.ActiveEditor
import goods.pocket.app.presentation.state.GoodsPocketAppStateHolder
import goods.pocket.app.presentation.state.GoodsPocketUiState
import goods.pocket.app.presentation.state.PendingDelete
import goodspocket.composeapp.generated.resources.*
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoodsPocketApp(
    appStateHolder: GoodsPocketAppStateHolder = koinInject(),
) {
    val uiState by appStateHolder.state.collectAsState()

    ProvideLocalizedResources(languageCode = uiState.appPreferences.languageCode) {
        val chrome = goodsPocketChromeFor(uiState.currentDestination)
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            topBar = {
                TopAppBar(
                    expandedHeight = 56.dp,
                    navigationIcon = {
                        if (chrome.showBackButton) {
                            val onBack = if (uiState.currentDestination == AppDestination.Settings) {
                                appStateHolder::closeSettings
                            } else {
                                { appStateHolder.selectDestination(uiState.selectedPrimaryDestination) }
                            }
                            TextButton(onClick = onBack) {
                                Text(tr(Res.string.action_back))
                            }
                        }
                    },
                    title = {
                        Text(
                            text = uiState.currentDestination.localizedLabel(),
                            fontWeight = FontWeight.SemiBold,
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        titleContentColor = MaterialTheme.colorScheme.onBackground,
                        navigationIconContentColor = MaterialTheme.colorScheme.primary,
                    ),
                )
            },
            bottomBar = {
                if (chrome.showBottomBar) {
                    GoodsPocketBottomBar(
                        selectedPrimaryDestination = uiState.selectedPrimaryDestination,
                        onSelectDestination = appStateHolder::selectDestination,
                    )
                }
            },
            floatingActionButton = {
                if (chrome.showFab) {
                    FloatingActionButton(
                        onClick = { appStateHolder.openQuickAdd() },
                        shape = MaterialTheme.shapes.large,
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                    ) {
                        Text(
                            text = "+",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                }
            },
        ) { innerPadding ->
            GoodsPocketNavHost(
                uiState = uiState,
                appStateHolder = appStateHolder,
                innerPadding = innerPadding,
            )
        }

        if (uiState.isQuickAddOpen) {
            QuickAddSheet(
                target = uiState.quickAddTarget,
                onTargetChange = appStateHolder::selectQuickAddTarget,
                onDismiss = appStateHolder::closeQuickAdd,
                onSubmitItem = appStateHolder::submitItem,
                onSubmitPreorder = appStateHolder::submitPreorder,
                onSubmitTransaction = appStateHolder::submitTransaction,
                onSubmitEvent = appStateHolder::submitEvent,
            )
        }

        ActiveDetailSheet(
            uiState = uiState,
            appStateHolder = appStateHolder,
        )
        ActiveEditorSheet(
            uiState = uiState,
            appStateHolder = appStateHolder,
        )
        PendingDeleteDialog(
            uiState = uiState,
            appStateHolder = appStateHolder,
        )
    }
}

@Composable
private fun GoodsPocketBottomBar(
    selectedPrimaryDestination: AppDestination,
    onSelectDestination: (AppDestination) -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
    ) {
        NavigationBar(
            containerColor = Color.Transparent,
            tonalElevation = 0.dp,
        ) {
            AppDestination.primaryDestinations.forEach { destination ->
                val label = destination.localizedLabel()
                val selected = destination.route == selectedPrimaryDestination.route
                GoodsPocketBottomBarItem(
                    label = label,
                    selected = selected,
                    onClick = { onSelectDestination(destination) },
                )
            }
        }
    }
}

@Composable
private fun RowScope.GoodsPocketBottomBarItem(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = Modifier
            .weight(1f)
            .selectable(
                selected = selected,
                onClick = onClick,
                role = Role.Tab,
                interactionSource = interactionSource,
                indication = null,
            )
            .padding(vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        BottomNavGlyph(
            label = label,
            selected = selected,
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = if (selected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            },
        )
    }
}

@Composable
private fun BottomNavGlyph(
    label: String,
    selected: Boolean,
) {
    Surface(
        shape = CircleShape,
        color = if (selected) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surfaceContainerLow
        },
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
            text = label.take(1),
            style = MaterialTheme.typography.labelMedium,
            color = if (selected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            },
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun GoodsPocketNavHost(
    uiState: GoodsPocketUiState,
    appStateHolder: GoodsPocketAppStateHolder,
    innerPadding: PaddingValues,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
    ) {
        when (uiState.currentDestination) {
            AppDestination.Home -> HomeScreen(
                dashboardSummary = uiState.homeSummary,
                upcomingEvents = uiState.upcomingEvents,
                onMonthlySummaryClick = appStateHolder::openTransactionsOverview,
                onUpcomingEventsClick = appStateHolder::openEventsOverview,
                onUpcomingEventClick = appStateHolder::openEventFromHome,
                onRecentActivityClick = appStateHolder::openActivity,
                onPreordersClick = { appStateHolder.selectDestination(AppDestination.Preorders) },
                onQuickAddClick = appStateHolder::openQuickAdd,
            )
            AppDestination.Collection -> CollectionScreen(
                items = uiState.collectionItems,
                query = uiState.collectionQuery,
                onQueryChange = appStateHolder::updateCollectionQuery,
                onItemClick = appStateHolder::openItemDetail,
            )
            AppDestination.Preorders -> PreordersScreen(
                preorders = uiState.preorders,
                selectedStatus = uiState.preorderStatusFilter,
                onStatusChange = appStateHolder::updatePreorderStatusFilter,
                onPreorderClick = appStateHolder::openPreorderDetail,
            )
            AppDestination.My -> MyScreen(
                myPage = uiState.myPage,
                onOpenSettings = appStateHolder::openSettings,
            )
            AppDestination.Transactions -> TransactionsScreen(
                transactions = uiState.transactions,
                selectedType = uiState.transactionTypeFilter,
                onTypeChange = appStateHolder::updateTransactionTypeFilter,
                onTransactionClick = appStateHolder::openTransactionDetail,
            )
            AppDestination.Events -> EventsScreen(
                events = uiState.events,
                selectedType = uiState.eventTypeFilter,
                onTypeChange = appStateHolder::updateEventTypeFilter,
                onEventClick = appStateHolder::openEventDetail,
            )
            AppDestination.Settings -> SettingsScreen(
                storageLocations = uiState.storageLocations,
                appPreferences = uiState.appPreferences,
                onStartTabChange = appStateHolder::updateStartTab,
                onLanguageChange = appStateHolder::updateLanguage,
            )
        }
    }
}

@Composable
private fun ActiveDetailSheet(
    uiState: GoodsPocketUiState,
    appStateHolder: GoodsPocketAppStateHolder,
) {
    when (val detail = uiState.activeDetail) {
        is ActiveDetail.ItemDetail -> {
            val item = uiState.collectionItems.firstOrNull { it.id == detail.itemId } ?: return
            ItemDetailSheet(
                item = item,
                linkedTransactions = appStateHolder.itemTransactions(item.id),
                onDismiss = appStateHolder::closeDetail,
                onEdit = { appStateHolder.openItemEditor(item.id) },
                onDelete = { appStateHolder.requestDeleteItem(item.id) },
            )
        }

        is ActiveDetail.PreorderDetail -> {
            val preorder = uiState.preorders.firstOrNull { it.id == detail.preorderId } ?: return
            PreorderDetailSheet(
                preorder = preorder,
                onDismiss = appStateHolder::closeDetail,
                onMarkReceived = { appStateHolder.markPreorderReceived(preorder.id) },
                onEdit = { appStateHolder.openPreorderEditor(preorder.id) },
                onCancel = { appStateHolder.requestCancelPreorder(preorder.id) },
            )
        }

        is ActiveDetail.TransactionDetail -> {
            val transaction = uiState.transactions.firstOrNull { it.id == detail.transactionId } ?: return
            TransactionDetailSheet(
                transaction = transaction,
                onDismiss = appStateHolder::closeDetail,
                onEdit = { appStateHolder.openTransactionEditor(transaction.id) },
                onDelete = { appStateHolder.requestDeleteTransaction(transaction.id) },
            )
        }

        is ActiveDetail.EventDetail -> {
            val event = uiState.events.firstOrNull { it.id == detail.eventId } ?: return
            EventDetailSheet(
                event = event,
                onDismiss = appStateHolder::closeDetail,
                onEdit = { appStateHolder.openEventEditor(event.id) },
                onDelete = { appStateHolder.requestDeleteEvent(event.id) },
            )
        }

        null -> Unit
    }
}

@Composable
private fun ActiveEditorSheet(
    uiState: GoodsPocketUiState,
    appStateHolder: GoodsPocketAppStateHolder,
) {
    when (val editor = uiState.activeEditor) {
        is ActiveEditor.ItemEditor -> {
            val item = uiState.collectionItems.firstOrNull { it.id == editor.itemId } ?: return
            ItemEditorSheet(
                item = item,
                onDismiss = appStateHolder::closeEditor,
                onSave = { name, category, seriesName, characterName, purchaseStore ->
                    appStateHolder.saveEditedItem(
                        itemId = item.id,
                        name = name,
                        category = category,
                        seriesName = seriesName,
                        characterName = characterName,
                        purchaseStore = purchaseStore,
                    )
                },
            )
        }

        is ActiveEditor.PreorderEditor -> {
            val preorder = uiState.preorders.firstOrNull { it.id == editor.preorderId } ?: return
            PreorderEditorSheet(
                preorder = preorder,
                onDismiss = appStateHolder::closeEditor,
                onSave = { name, storeName, releaseDate ->
                    appStateHolder.saveEditedPreorder(
                        preorderId = preorder.id,
                        name = name,
                        storeName = storeName,
                        releaseDate = releaseDate,
                    )
                },
            )
        }

        is ActiveEditor.TransactionEditor -> {
            val transaction = uiState.transactions.firstOrNull { it.id == editor.transactionId } ?: return
            TransactionEditorSheet(
                transaction = transaction,
                onDismiss = appStateHolder::closeEditor,
                onSave = { amount, type, transactionDate ->
                    appStateHolder.saveEditedTransaction(
                        transactionId = transaction.id,
                        amount = amount,
                        type = type,
                        transactionDate = transactionDate,
                    )
                },
            )
        }

        is ActiveEditor.EventEditor -> {
            val event = uiState.events.firstOrNull { it.id == editor.eventId } ?: return
            EventEditorSheet(
                event = event,
                onDismiss = appStateHolder::closeEditor,
                onSave = { title, targetDate, eventType ->
                    appStateHolder.saveEditedEvent(
                        eventId = event.id,
                        title = title,
                        targetDate = targetDate,
                        eventType = eventType,
                    )
                },
            )
        }

        null -> Unit
    }
}

@Composable
private fun PendingDeleteDialog(
    uiState: GoodsPocketUiState,
    appStateHolder: GoodsPocketAppStateHolder,
) {
    val pendingDelete = uiState.pendingDelete ?: return
    val title: String
    val body: String
    when (pendingDelete) {
        is PendingDelete.ItemDelete -> {
            title = tr(Res.string.dialog_delete_item_title)
            body = if (pendingDelete.linkedTransactionCount > 0) {
                tr(Res.string.dialog_delete_item_body_linked, pendingDelete.linkedTransactionCount.toString())
            } else {
                tr(Res.string.dialog_delete_item_body)
            }
        }
        is PendingDelete.PreorderCancel -> {
            title = tr(Res.string.dialog_cancel_preorder_title)
            body = tr(Res.string.dialog_cancel_preorder_body)
        }
        is PendingDelete.TransactionDelete -> {
            title = tr(Res.string.dialog_delete_transaction_title)
            body = tr(Res.string.dialog_delete_transaction_body)
        }
        is PendingDelete.EventDelete -> {
            title = tr(Res.string.dialog_delete_event_title)
            body = tr(Res.string.dialog_delete_event_body)
        }
    }

    AlertDialog(
        onDismissRequest = appStateHolder::dismissPendingDelete,
        containerColor = MaterialTheme.colorScheme.surface,
        iconContentColor = MaterialTheme.colorScheme.error,
        titleContentColor = MaterialTheme.colorScheme.onSurface,
        textContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
        },
        text = { Text(body, style = MaterialTheme.typography.bodyMedium) },
        confirmButton = {
            TextButton(onClick = appStateHolder::confirmPendingDelete) {
                Text(
                    when (pendingDelete) {
                        is PendingDelete.PreorderCancel -> tr(Res.string.action_cancel_preorder)
                        else -> tr(Res.string.action_delete)
                    },
                    color = MaterialTheme.colorScheme.error,
                )
            }
        },
        dismissButton = {
            TextButton(onClick = appStateHolder::dismissPendingDelete) {
                Text(tr(Res.string.action_cancel))
            }
        },
    )
}

@Composable
fun ScreenPlaceholder(
    title: String,
    summary: String,
    actionHint: String,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = summary,
            style = MaterialTheme.typography.bodyLarge,
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = actionHint,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
        )
    }
}
