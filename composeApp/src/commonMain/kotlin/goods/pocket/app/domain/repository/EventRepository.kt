package goods.pocket.app.domain.repository

import goods.pocket.app.domain.model.Event
import goods.pocket.app.domain.model.EventType

interface EventRepository {
    suspend fun getUpcomingEvents(onOrAfter: String, limit: Int): List<Event>
    suspend fun getEvents(type: EventType? = null): List<Event>
    suspend fun saveEvent(event: Event)
    suspend fun deleteEvent(id: String)
}
