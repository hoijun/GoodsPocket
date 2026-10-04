package goods.pocket.app.data.repository

import goods.pocket.app.data.local.GoodsPocketStore
import goods.pocket.app.domain.event.Event
import goods.pocket.app.domain.event.EventRepository
import goods.pocket.app.domain.event.EventType
import kotlinx.coroutines.flow.Flow

class SqlEventRepository(private val store: GoodsPocketStore) : EventRepository {
    override fun observeEvents(): Flow<List<Event>> =
        store.observe({ it.goodsPocketDatabaseQueries.selectEvents() }, ::toDomain)

    override suspend fun getEvents(): List<Event> = store.read {
        it.goodsPocketDatabaseQueries.selectEvents().executeAsList().map(::toDomain)
    }

    override suspend fun saveEvent(event: Event): Unit = store.write {
        it.transaction {
            if (it.goodsPocketDatabaseQueries.selectEvent(event.id).executeAsOneOrNull() == null) {
                it.goodsPocketDatabaseQueries.upsertEvent(
                    goods.pocket.app.db.Event(
                        id = event.id,
                        title = event.title,
                        event_type = event.eventType.name,
                        target_date = event.targetDate,
                        related_entry_id = event.relatedEntryId,
                        location_or_store = event.locationOrStore,
                        memo = event.memo,
                        created_at = event.createdAt,
                        updated_at = event.updatedAt,
                    ),
                )
            } else {
                it.goodsPocketDatabaseQueries.updateEvent(
                    id = event.id,
                    title = event.title,
                    event_type = event.eventType.name,
                    target_date = event.targetDate,
                    related_entry_id = event.relatedEntryId,
                    location_or_store = event.locationOrStore,
                    memo = event.memo,
                    updated_at = event.updatedAt,
                )
            }
        }
    }

    override suspend fun deleteEvent(id: String): Unit = store.write {
        it.goodsPocketDatabaseQueries.deleteEvent(id)
    }
}

private fun toDomain(row: goods.pocket.app.db.Event): Event = Event(
    id = row.id,
    title = row.title,
    eventType = EventType.valueOf(row.event_type),
    targetDate = row.target_date,
    relatedEntryId = row.related_entry_id,
    locationOrStore = row.location_or_store,
    memo = row.memo,
    createdAt = row.created_at,
    updatedAt = row.updated_at,
)
