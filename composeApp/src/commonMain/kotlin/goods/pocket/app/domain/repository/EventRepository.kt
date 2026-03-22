package goods.pocket.app.domain.repository

import goods.pocket.app.domain.model.Event
import goods.pocket.app.domain.model.EventType

interface EventRepository {
    fun getUpcomingEvents(limit: Int): List<Event>
    fun getEvents(type: EventType? = null): List<Event>
    fun saveEvent(event: Event)
    fun deleteEvent(id: String)
}
