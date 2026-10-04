package goods.pocket.app.domain.event

import kotlinx.coroutines.flow.Flow

interface EventRepository {
    fun observeEvents(): Flow<List<Event>>
    suspend fun getEvents(): List<Event>
    suspend fun saveEvent(event: Event)
    suspend fun deleteEvent(id: String)
}
