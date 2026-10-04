package goods.pocket.app.domain.collection

import goods.pocket.app.domain.service.AppClock

class MarkPreorderReceivedUseCase(
    private val collectionRepository: CollectionRepository,
    private val clock: AppClock,
) {
    suspend operator fun invoke(id: String): ReservationResult {
        val entry = collectionRepository.getEntry(id) ?: return ReservationResult.NOT_FOUND
        if (entry.reservation?.receivedAt != null) return ReservationResult.ALREADY_RECEIVED
        if (entry.status != CollectionEntryStatus.RESERVED || entry.canceledAt != null) {
            return ReservationResult.NOT_RESERVED
        }
        return collectionRepository.receiveReservation(
            id,
            clock.currentTimestamp(),
            clock.currentDate(),
        )
    }
}
