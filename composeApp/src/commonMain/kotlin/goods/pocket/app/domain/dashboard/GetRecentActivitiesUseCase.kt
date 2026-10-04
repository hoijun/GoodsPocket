package goods.pocket.app.domain.dashboard

import goods.pocket.app.domain.collection.CollectionEntry
import goods.pocket.app.domain.collection.CollectionEntryStatus
import goods.pocket.app.domain.collection.CollectionRepository

class GetRecentActivitiesUseCase(private val collectionRepository: CollectionRepository) {
    suspend operator fun invoke(limit: Int): List<ActivityRecord> =
        calculate(collectionRepository.getEntries(), limit)

    fun calculate(entries: List<CollectionEntry>, limit: Int): List<ActivityRecord> =
        Companion.calculate(entries, limit)

    companion object {
        fun calculate(entries: List<CollectionEntry>, limit: Int): List<ActivityRecord> {
            require(limit >= 0)
            return entries.filter {
                it.canceledAt == null
            }.sortedByDescending { it.updatedAt }.take(limit).map { entry ->
                ActivityRecord(
                    id = entry.id,
                    title = entry.name,
                    kind = when {
                        entry.reservation?.receivedAt != null -> ActivityKind.RECEIVED
                        entry.status == CollectionEntryStatus.RESERVED -> ActivityKind.RESERVED
                        else -> ActivityKind.ADDED
                    },
                    happenedAt = entry.updatedAt,
                    storeName = entry.reservationStore,
                )
            }
        }
    }
}
