package goods.pocket.app.domain.usecase

import goods.pocket.app.domain.model.Event
import goods.pocket.app.domain.repository.EventRepository

class SaveEventUseCase(
    private val eventRepository: EventRepository,
) {
    operator fun invoke(event: Event) {
        eventRepository.saveEvent(event)
    }
}
