package goods.pocket.app.presentation.events

import goods.pocket.app.domain.event.Event
import goods.pocket.app.domain.event.EventRepository
import goods.pocket.app.domain.event.EventType
import goods.pocket.app.domain.service.AppClock
import goods.pocket.app.domain.service.IdGenerator
import goods.pocket.app.presentation.component.EventDraft
import goods.pocket.app.presentation.state.FeatureCommand
import goods.pocket.app.presentation.state.FeatureObservation
import goods.pocket.app.presentation.state.PendingSavedContent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class EventsStateHolder(
    private val repository: EventRepository,
    private val clock: AppClock,
    private val ids: IdGenerator,
    scope: CoroutineScope,
    date: kotlinx.coroutines.flow.StateFlow<String> = MutableStateFlow(clock.currentDate()),
) {
    private val content = FeatureObservation(scope, emptyList<Event>(), repository::observeEvents)
    private val filter = MutableStateFlow<EventType?>(null)
    private val savedEvent = MutableStateFlow<PendingSavedContent<Event>?>(null)
    val command: FeatureCommand = FeatureCommand(scope)
    val state: kotlinx.coroutines.flow.StateFlow<EventsUiState> = combine(
        content.state,
        filter,
        command.state,
        date,
        savedEvent,
    ) {
            data,
            type,
            command,
            today,
            saved,
        ->
        EventsUiState(
            data.value,
            type,
            today,
            data.isLoading,
            data.hasFailure,
            command,
            saved?.value,
            hasLoaded = data.hasLoaded,
        )
    }.stateIn(scope, SharingStarted.Eagerly, EventsUiState(currentDate = clock.currentDate()))
    private var newEventId: String? = null

    init {
        scope.launch {
            combine(content.state, savedEvent) { observed, saved ->
                saved?.takeIf { pending ->
                    observed.version > pending.observationVersion ||
                        observed.value.any { it == pending.value }
                }
            }.collect { acknowledged ->
                if (acknowledged != null &&
                    savedEvent.value == acknowledged
                ) {
                    savedEvent.value = null
                }
            }
        }
    }

    fun updateType(type: EventType?) {
        filter.value = type
    }

    fun retryLoad() = content.retry()

    fun beginCreate() {
        newEventId = ids.generate("event")
        command.cancel()
    }

    fun save(draft: EventDraft, eventId: String? = null, onSuccess: (String) -> Unit) {
        if (!draft.canSubmit || command.state.value.isRunning) return
        val existing = eventId?.let { id -> state.value.event(id) }
        if (eventId != null && existing == null) return
        val id = eventId ?: newEventId ?: ids.generate("event").also { newEventId = it }
        val now = clock.currentTimestamp()
        val event = (
            existing ?: Event(
                id = id,
                title = draft.title.trim(),
                targetDate = draft.targetDate.trim(),
                eventType = draft.eventType,
                createdAt = now,
                updatedAt = now,
            )
            ).copy(
            title = draft.title.trim(),
            targetDate = draft.targetDate.trim(),
            eventType = draft.eventType,
            updatedAt = now,
        )
        command.run(onSuccess = {
            savedEvent.value = PendingSavedContent(event, content.state.value.version)
            if (eventId == null) newEventId = null
            onSuccess(id)
        }) { repository.saveEvent(event) }
    }

    fun delete(id: String, onSuccess: () -> Unit) {
        command.run(onSuccess = {
            if (savedEvent.value?.value?.id == id) savedEvent.value = null
            onSuccess()
        }) { repository.deleteEvent(id) }
    }
}
