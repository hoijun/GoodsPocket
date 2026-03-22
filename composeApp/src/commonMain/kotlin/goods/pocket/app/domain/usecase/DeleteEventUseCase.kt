package goods.pocket.app.domain.usecase

import goods.pocket.app.domain.repository.EventRepository

class DeleteEventUseCase(
    private val eventRepository: EventRepository,
) {
    operator fun invoke(eventId: String) {
        eventRepository.deleteEvent(eventId)
    }
}
