package goods.pocket.app.domain.usecase

import goods.pocket.app.domain.model.Event
import goods.pocket.app.domain.model.EventType
import goods.pocket.app.domain.repository.EventRepository

class GetEventListUseCase(
    private val eventRepository: EventRepository,
) {
    operator fun invoke(type: EventType? = null): List<Event> = eventRepository.getEvents(type)
}
