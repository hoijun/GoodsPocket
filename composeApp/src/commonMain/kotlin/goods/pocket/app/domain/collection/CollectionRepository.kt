package goods.pocket.app.domain.collection

import kotlinx.coroutines.flow.Flow

interface CollectionRepository {
    fun observeEntries(): Flow<List<CollectionEntry>>
    fun observeAllEntries(): Flow<List<CollectionEntry>>
    suspend fun getEntries(): List<CollectionEntry>
    suspend fun getAllEntries(): List<CollectionEntry>
    suspend fun getEntry(id: String): CollectionEntry?
    suspend fun saveEntry(entry: CollectionEntry)
    suspend fun deleteEntry(id: String)
    suspend fun cancelReservation(id: String, canceledAt: String): ReservationResult
    suspend fun receiveReservation(
        id: String,
        receivedAt: String,
        receivedDate: String,
    ): ReservationResult
}

enum class ReservationResult {
    UPDATED,
    NOT_FOUND,
    NOT_RESERVED,
    ALREADY_RECEIVED,
}
