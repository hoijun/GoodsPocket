package goods.pocket.app.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import goods.pocket.app.domain.model.CollectionEntryStatus
import goods.pocket.app.presentation.designsystem.GoodsPocketImageLockedBottomBar
import goods.pocket.app.presentation.designsystem.goodsPocketChromeFor
import goods.pocket.app.presentation.component.CollectionEntryDetailSheet
import goods.pocket.app.presentation.component.CollectionEntryEditorSheet
import goods.pocket.app.presentation.component.EventDetailSheet
import goods.pocket.app.presentation.component.EventEditorSheet
import goods.pocket.app.presentation.component.QuickAddSheet
import goods.pocket.app.presentation.i18n.ProvideLocalizedResources
import goods.pocket.app.presentation.i18n.localizedLabel
import goods.pocket.app.presentation.i18n.tr
import goods.pocket.app.presentation.navigation.AppDestination
import goods.pocket.app.presentation.screen.CollectionScreen
import goods.pocket.app.presentation.screen.EventsScreen
import goods.pocket.app.presentation.screen.HomeScreen
import goods.pocket.app.presentation.screen.MyScreen
import goods.pocket.app.presentation.screen.SettingsScreen
import goods.pocket.app.presentation.state.ActiveDetail
import goods.pocket.app.presentation.state.ActiveEditor
import goods.pocket.app.presentation.state.CollectionSegment
import goods.pocket.app.presentation.state.GoodsPocketAppStateHolder
import goods.pocket.app.presentation.state.GoodsPocketUiState
import goods.pocket.app.presentation.state.PendingDelete
import goodspocket.composeapp.generated.resources.*
import org.koin.compose.koinInject

@Composable
fun GoodsPocketApp(
    appStateHolder: GoodsPocketAppStateHolder = koinInject(),
) {
    val uiState by appStateHolder.state.collectAsState()

    ProvideLocalizedResources(
        languageCode = uiState.appPreferences.languageCode,
        currencyCode = uiState.appPreferences.currencyCode,
        dateFormat = uiState.appPreferences.dateFormat,
    ) {
        val snackbarHostState = remember { SnackbarHostState() }
        val failureMessage = tr(Res.string.error_operation_failed)
        val retryLabel = tr(Res.string.action_retry)
        LaunchedEffect(uiState.failure) {
            if (uiState.failure == null) return@LaunchedEffect
            val result = snackbarHostState.showSnackbar(
                message = failureMessage,
                actionLabel = retryLabel,
                duration = SnackbarDuration.Long,
            )
            if (result == SnackbarResult.ActionPerformed) {
                appStateHolder.retry()
            } else {
                appStateHolder.dismissFailure()
            }
        }
        val chrome = goodsPocketChromeFor(uiState.currentDestination)
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            snackbarHost = { SnackbarHost(snackbarHostState) },
            bottomBar = {
                if (chrome.showBottomBar) {
                    GoodsPocketImageLockedBottomBar(
                        selectedPrimaryDestination = uiState.selectedPrimaryDestination,
                        onSelectDestination = appStateHolder::selectDestination,
                        onQuickAdd = appStateHolder::openQuickAdd,
                    )
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
                onSubmitCollectionEntry = appStateHolder::submitCollectionEntry,
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
private fun GoodsPocketNavHost(
    uiState: GoodsPocketUiState,
    appStateHolder: GoodsPocketAppStateHolder,
    innerPadding: PaddingValues,
) {
    val screenPadding = if (uiState.currentDestination == AppDestination.Home) {
        homeScreenPadding(innerPadding)
    } else {
        innerPadding
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(screenPadding),
    ) {
        when (uiState.currentDestination) {
            AppDestination.Home -> HomeScreen(
                dashboardSummary = uiState.homeSummary,
                upcomingEvents = uiState.upcomingEvents,
                onUpcomingEventsClick = appStateHolder::openEventsOverview,
                onUpcomingEventClick = appStateHolder::openEventFromHome,
                onRecentActivityClick = appStateHolder::openActivity,
                onPreordersClick = { appStateHolder.selectCollectionSegment(CollectionSegment.RESERVED) },
                onQuickAddClick = appStateHolder::openQuickAdd,
            )
            AppDestination.Collection -> CollectionScreen(
                entries = uiState.collectionEntries,
                selectedSegment = uiState.collectionSegment,
                query = uiState.collectionQuery,
                onSegmentChange = appStateHolder::selectCollectionSegment,
                onQueryChange = appStateHolder::updateCollectionQuery,
                onEntryClick = appStateHolder::openCollectionEntryDetail,
            )
            AppDestination.My -> MyScreen(
                myPage = uiState.myPage,
                onOpenSettings = appStateHolder::openSettings,
            )
            AppDestination.Events -> EventsScreen(
                events = uiState.events,
                selectedType = uiState.eventTypeFilter,
                onTypeChange = appStateHolder::updateEventTypeFilter,
                onEventClick = appStateHolder::openEventDetail,
            )
            AppDestination.Settings -> SettingsScreen(
                appPreferences = uiState.appPreferences,
                onLanguageChange = appStateHolder::updateLanguage,
                onBack = appStateHolder::closeSettings,
            )
        }
    }
}

private fun homeScreenPadding(
    innerPadding: PaddingValues,
): PaddingValues {
    return PaddingValues(
        top = (innerPadding.calculateTopPadding() - 25.dp).coerceAtLeast(0.dp),
        bottom = innerPadding.calculateBottomPadding(),
    )
}

@Composable
private fun ActiveDetailSheet(
    uiState: GoodsPocketUiState,
    appStateHolder: GoodsPocketAppStateHolder,
) {
    when (val detail = uiState.activeDetail) {
        is ActiveDetail.CollectionEntryDetail -> {
            val entry = uiState.collectionEntries.firstOrNull { it.id == detail.entryId } ?: return
            CollectionEntryDetailSheet(
                entry = entry,
                onDismiss = appStateHolder::closeDetail,
                onEdit = { appStateHolder.openCollectionEntryEditor(entry.id) },
                onDelete = { appStateHolder.requestDeleteCollectionEntry(entry.id) },
                onMarkReceived = if (entry.status == CollectionEntryStatus.RESERVED) {
                    { appStateHolder.markCollectionEntryReceived(entry.id) }
                } else {
                    null
                },
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
        is ActiveEditor.CollectionEntryEditor -> {
            val entry = uiState.collectionEntries.firstOrNull { it.id == editor.entryId } ?: return
            CollectionEntryEditorSheet(
                entry = entry,
                onDismiss = appStateHolder::closeEditor,
                onSave = { name, category, status, seriesName, characterName, purchaseStore, releaseDate, reservationStore, note ->
                    appStateHolder.saveEditedCollectionEntry(
                        entryId = entry.id,
                        name = name,
                        category = category,
                        status = status,
                        seriesName = seriesName,
                        characterName = characterName,
                        purchaseStore = purchaseStore,
                        releaseDate = releaseDate,
                        reservationStore = reservationStore,
                        note = note,
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
            body = tr(Res.string.dialog_delete_item_body)
        }
        is PendingDelete.PreorderCancel -> {
            title = tr(Res.string.dialog_cancel_preorder_title)
            body = tr(Res.string.dialog_cancel_preorder_body)
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
