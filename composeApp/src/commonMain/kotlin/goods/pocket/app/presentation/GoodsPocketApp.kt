package goods.pocket.app.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import goods.pocket.app.domain.collection.CollectionEntryStatus
import goods.pocket.app.presentation.collection.CollectionRoute
import goods.pocket.app.presentation.designsystem.GoodsPocketImageLockedBottomBar
import goods.pocket.app.presentation.designsystem.goodsPocketChromeFor
import goods.pocket.app.presentation.events.EventsRoute
import goods.pocket.app.presentation.home.HomeAction
import goods.pocket.app.presentation.home.HomeRoute
import goods.pocket.app.presentation.i18n.ProvideLocalizedResources
import goods.pocket.app.presentation.my.MyRoute
import goods.pocket.app.presentation.navigation.AppDestination
import goods.pocket.app.presentation.settings.SettingsRoute
import goods.pocket.app.presentation.state.ActiveDetail
import goods.pocket.app.presentation.state.CollectionSegment

@Composable
fun GoodsPocketApp(session: PresentationSession) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle, session) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) session.date.refresh()
        }
        lifecycle.addObserver(observer)
        onDispose { lifecycle.removeObserver(observer) }
    }
    val shell by session.shell.state.collectAsState()
    val settings by session.settings.state.collectAsState()
    val preferences = settings.preferences
    ProvideLocalizedResources(
        preferences.languageCode,
        preferences.currencyCode,
        preferences.dateFormat,
    ) {
        val chrome = goodsPocketChromeFor(shell.destination)
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            contentWindowInsets = WindowInsets.safeDrawing,
            bottomBar = {
                if (chrome.showBottomBar) {
                    GoodsPocketImageLockedBottomBar(
                        selectedPrimaryDestination = shell.primaryDestination,
                        onSelectDestination = { destination ->
                            session.date.refresh()
                            session.collection.command.cancel()
                            session.events.command.cancel()
                            if (destination ==
                                AppDestination.Collection
                            ) {
                                session.collection.resetFilters()
                            }
                            if (destination ==
                                AppDestination.Events
                            ) {
                                session.events.updateType(null)
                            }
                            session.shell.navigate(destination)
                        },
                        onQuickAdd = {
                            session.collection.beginCreate()
                            session.events.beginCreate()
                            session.shell.openQuickAdd()
                        },
                    )
                }
            },
        ) { innerPadding ->
            Box(
                Modifier.fillMaxSize()
                    .padding(innerPadding)
                    .consumeWindowInsets(innerPadding),
            ) {
                when (shell.destination) {
                    AppDestination.Home -> HomeRoute(session.home) { session.handleHomeAction(it) }
                    AppDestination.Collection -> CollectionRoute(session.collection) {
                        session.shell.showDetail(ActiveDetail.CollectionEntryDetail(it))
                    }
                    AppDestination.Events -> EventsRoute(session.events) {
                        session.shell.showDetail(ActiveDetail.EventDetail(it))
                    }
                    AppDestination.My -> MyRoute(session.my) {
                        session.shell.navigate(AppDestination.Settings)
                    }
                    AppDestination.Settings -> SettingsRoute(session.settings, session.shell::back)
                }
            }
        }
        GoodsPocketOverlays(session)
    }
}

private fun PresentationSession.handleHomeAction(action: HomeAction) {
    when (action) {
        HomeAction.OpenQuickAdd -> {
            collection.beginCreate()
            events.beginCreate()
            shell.openQuickAdd()
        }
        HomeAction.OpenScheduleOverview -> {
            events.updateType(null)
            shell.navigate(AppDestination.Events)
        }
        is HomeAction.OpenEvent -> {
            events.updateType(null)
            shell.navigate(AppDestination.Events)
            shell.showDetail(ActiveDetail.EventDetail(action.eventId))
        }
        is HomeAction.OpenRecentEntry -> {
            val entry =
                collection.state.value.entries.firstOrNull { it.id == action.entryId } ?: return
            collection.resetFilters(
                if (entry.status ==
                    CollectionEntryStatus.RESERVED
                ) {
                    CollectionSegment.RESERVED
                } else {
                    CollectionSegment.OWNED
                },
            )
            shell.navigate(AppDestination.Collection)
            shell.showDetail(ActiveDetail.CollectionEntryDetail(entry.id))
        }
        else -> {
            collection.resetFilters(
                when (action) {
                    HomeAction.OpenOwnedCollection -> CollectionSegment.OWNED
                    HomeAction.OpenReservedCollection -> CollectionSegment.RESERVED
                    HomeAction.OpenRecentCollection,
                    HomeAction.OpenAllCollection,
                    -> CollectionSegment.ALL
                },
            )
            shell.navigate(AppDestination.Collection)
        }
    }
}
