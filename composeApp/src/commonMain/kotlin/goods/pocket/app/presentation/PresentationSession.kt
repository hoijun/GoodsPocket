package goods.pocket.app.presentation

import goods.pocket.app.domain.collection.CollectionRepository
import goods.pocket.app.domain.collection.MarkPreorderReceivedUseCase
import goods.pocket.app.domain.dashboard.GetDashboardSummaryUseCase
import goods.pocket.app.domain.dashboard.GetRecentActivitiesUseCase
import goods.pocket.app.domain.event.EventRepository
import goods.pocket.app.domain.service.AppClock
import goods.pocket.app.domain.service.IdGenerator
import goods.pocket.app.domain.settings.SettingsRepository
import goods.pocket.app.presentation.collection.CollectionStateHolder
import goods.pocket.app.presentation.events.EventsStateHolder
import goods.pocket.app.presentation.home.HomeStateHolder
import goods.pocket.app.presentation.my.MyStateHolder
import goods.pocket.app.presentation.navigation.ShellStateHolder
import goods.pocket.app.presentation.settings.SettingsStateHolder
import goods.pocket.app.presentation.state.SessionDate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

class PresentationSessionFactory(
    private val collection: CollectionRepository,
    private val events: EventRepository,
    private val settings: SettingsRepository,
    private val clock: AppClock,
    private val ids: IdGenerator,
    private val receive: MarkPreorderReceivedUseCase,
    private val dashboard: GetDashboardSummaryUseCase,
    private val activities: GetRecentActivitiesUseCase,
) {
    fun create(parentScope: CoroutineScope): PresentationSession {
        val scope = CoroutineScope(
            parentScope.coroutineContext + SupervisorJob(parentScope.coroutineContext[Job]),
        )
        val date = SessionDate(clock, scope)
        return PresentationSession(
            scope = scope,
            date = date,
            collection = CollectionStateHolder(collection, receive, clock, ids, scope),
            events = EventsStateHolder(events, clock, ids, scope, date.date),
            settings = SettingsStateHolder(settings, scope),
            home = HomeStateHolder(
                collection,
                events,
                clock,
                scope,
                date.date,
                dashboard,
                activities,
            ),
            my = MyStateHolder(collection, events, clock, scope, date.date, dashboard),
        )
    }
}

class PresentationSession internal constructor(
    private val scope: CoroutineScope,
    val date: SessionDate,
    val collection: CollectionStateHolder,
    val events: EventsStateHolder,
    val settings: SettingsStateHolder,
    val home: HomeStateHolder,
    val my: MyStateHolder,
) {
    val shell = ShellStateHolder()

    fun close() {
        scope.cancel()
    }
}
