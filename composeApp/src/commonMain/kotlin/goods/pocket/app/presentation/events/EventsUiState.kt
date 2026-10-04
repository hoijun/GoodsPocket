package goods.pocket.app.presentation.events

import goods.pocket.app.domain.event.Event
import goods.pocket.app.domain.event.EventType
import goods.pocket.app.presentation.state.CommandState

data class EventsUiState(
    val events: List<Event> = emptyList(),
    val selectedType: EventType? = null,
    val currentDate: String = "",
    val isLoading: Boolean = true,
    val hasLoadFailure: Boolean = false,
    val command: CommandState = CommandState(),
    val savedEvent: Event? = null,
    val hasLoaded: Boolean = false,
) {
    fun event(id: String): Event? {
        val observed = events.firstOrNull { it.id == id }
        val saved = savedEvent?.takeIf { it.id == id } ?: return observed
        return saved
    }
}
