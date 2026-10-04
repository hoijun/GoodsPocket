package goods.pocket.app.data.repository

import goods.pocket.app.data.local.GoodsPocketStore
import goods.pocket.app.data.local.save
import goods.pocket.app.data.local.toDomain
import goods.pocket.app.domain.collection.CollectionEntry
import goods.pocket.app.domain.collection.CollectionRepository
import goods.pocket.app.domain.collection.ReservationResult
import kotlinx.coroutines.flow.Flow

class SqlCollectionRepository(private val store: GoodsPocketStore) : CollectionRepository {
    override fun observeEntries(): Flow<List<CollectionEntry>> =
        store.observe({ it.goodsPocketDatabaseQueries.selectEntries() }) { it.toDomain() }

    override fun observeAllEntries(): Flow<List<CollectionEntry>> =
        store.observe({ it.goodsPocketDatabaseQueries.selectAllEntries() }) { it.toDomain() }

    override suspend fun getEntries(): List<CollectionEntry> = store.read {
        it.goodsPocketDatabaseQueries.selectEntries().executeAsList().map { row -> row.toDomain() }
    }

    override suspend fun getAllEntries(): List<CollectionEntry> = store.read {
        it.goodsPocketDatabaseQueries.selectAllEntries().executeAsList().map { row ->
            row.toDomain()
        }
    }

    override suspend fun getEntry(id: String): CollectionEntry? = store.read {
        it.goodsPocketDatabaseQueries.selectEntry(id).executeAsOneOrNull()?.toDomain()
    }

    override suspend fun saveEntry(entry: CollectionEntry): Unit = store.write {
        it.transaction { it.goodsPocketDatabaseQueries.save(entry) }
    }

    override suspend fun deleteEntry(id: String): Unit = store.write { database ->
        database.transaction {
            database.goodsPocketDatabaseQueries.clearEventLinks(id)
            database.goodsPocketDatabaseQueries.deleteEntry(id)
        }
    }

    override suspend fun cancelReservation(id: String, canceledAt: String): ReservationResult =
        store.write { database ->
            database.transactionWithResult {
                val queries = database.goodsPocketDatabaseQueries
                val row = queries.selectEntry(id).executeAsOneOrNull()
                    ?: return@transactionWithResult ReservationResult.NOT_FOUND
                if (row.status != "RESERVED" || row.canceled_at != null) {
                    return@transactionWithResult ReservationResult.NOT_RESERVED
                }
                queries.cancelEntry(canceledAt, id)
                ReservationResult.UPDATED
            }
        }

    override suspend fun receiveReservation(
        id: String,
        receivedAt: String,
        receivedDate: String,
    ): ReservationResult = store.write { database ->
        database.transactionWithResult {
            val queries = database.goodsPocketDatabaseQueries
            val row = queries.selectEntry(id).executeAsOneOrNull()
                ?: return@transactionWithResult ReservationResult.NOT_FOUND
            if (row.received_at !=
                null
            ) {
                return@transactionWithResult ReservationResult.ALREADY_RECEIVED
            }
            if (row.status != "RESERVED" || row.canceled_at != null) {
                return@transactionWithResult ReservationResult.NOT_RESERVED
            }
            queries.receiveEntry(receivedAt = receivedAt, receivedDate = receivedDate, id = id)
            ReservationResult.UPDATED
        }
    }
}
