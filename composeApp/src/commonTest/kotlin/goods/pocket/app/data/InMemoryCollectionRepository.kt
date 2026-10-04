package goods.pocket.app.data

import goods.pocket.app.domain.collection.CollectionEntry
import goods.pocket.app.domain.collection.CollectionEntryStatus
import goods.pocket.app.domain.collection.CollectionRepository
import goods.pocket.app.domain.collection.ReservationDetails
import goods.pocket.app.domain.collection.ReservationResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class InMemoryCollectionRepository(initial: List<CollectionEntry> = emptyList()) :
    CollectionRepository {
    private val entries = MutableStateFlow(initial.toList())

    override fun observeEntries(): Flow<List<CollectionEntry>> = entries.map { all ->
        all.filter {
            it.canceledAt ==
                null
        }
    }

    override fun observeAllEntries(): Flow<List<CollectionEntry>> = entries

    override suspend fun getEntries(): List<CollectionEntry> = entries.value.filter {
        it.canceledAt ==
            null
    }

    override suspend fun getAllEntries(): List<CollectionEntry> = entries.value.toList()

    override suspend fun getEntry(id: String): CollectionEntry? = entries.value.find { it.id == id }

    override suspend fun saveEntry(entry: CollectionEntry) {
        entries.update { previous ->
            (previous.filterNot { it.id == entry.id } + entry).sortedByDescending { it.updatedAt }
        }
    }

    override suspend fun deleteEntry(id: String) {
        entries.update { previous -> previous.filterNot { it.id == id } }
    }

    override suspend fun cancelReservation(id: String, canceledAt: String): ReservationResult {
        val entry = getEntry(id) ?: return ReservationResult.NOT_FOUND
        if (entry.status != CollectionEntryStatus.RESERVED ||
            entry.canceledAt != null
        ) {
            return ReservationResult.NOT_RESERVED
        }
        saveEntry(entry.copy(canceledAt = canceledAt, updatedAt = canceledAt))
        return ReservationResult.UPDATED
    }

    override suspend fun receiveReservation(
        id: String,
        receivedAt: String,
        receivedDate: String,
    ): ReservationResult {
        val entry = getEntry(id) ?: return ReservationResult.NOT_FOUND
        if (entry.reservation?.receivedAt != null) return ReservationResult.ALREADY_RECEIVED
        if (entry.status != CollectionEntryStatus.RESERVED ||
            entry.canceledAt != null
        ) {
            return ReservationResult.NOT_RESERVED
        }
        saveEntry(
            entry.copy(
                status = CollectionEntryStatus.OWNED,
                purchaseDate = entry.purchaseDate ?: receivedDate,
                reservation = (entry.reservation ?: ReservationDetails()).copy(
                    receivedAt = receivedAt,
                ),
                updatedAt = receivedAt,
            ),
        )
        return ReservationResult.UPDATED
    }
}
