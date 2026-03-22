package goods.pocket.app.domain.usecase

import goods.pocket.app.domain.model.Event
import goods.pocket.app.domain.repository.EventRepository

class GetUpcomingEventsUseCase(
    private val eventRepository: EventRepository,
) {
    operator fun invoke(limit: Int): List<Event> = eventRepository.getUpcomingEvents(limit)
}
