package goods.pocket.app.domain

import goods.pocket.app.data.InMemoryCollectionRepository
import goods.pocket.app.domain.collection.CollectionEntry
import goods.pocket.app.domain.collection.CollectionEntryStatus
import goods.pocket.app.domain.collection.CollectionRepository
import goods.pocket.app.domain.collection.MarkPreorderReceivedUseCase
import goods.pocket.app.domain.collection.ReservationDetails
import goods.pocket.app.domain.collection.ReservationResult
import goods.pocket.app.domain.repository.RepositoryFailure
import goods.pocket.app.domain.repository.RepositoryOperation
import goods.pocket.app.domain.service.AppClock
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlinx.coroutines.test.runTest

class ReceiveReservationTest {
    private val clock = object : AppClock {
        override fun currentDate(): String = "2026-10-04"
        override fun currentTimestamp(): String = "2026-10-03T15:05:00Z"
    }

    @Test
    fun `receipt keeps stable identity and reservation while using local clock date`() = runTest {
        val original = entry()
        val repository = InMemoryCollectionRepository(listOf(original))
        val useCase = MarkPreorderReceivedUseCase(repository, clock)
        assertEquals(ReservationResult.UPDATED, useCase(original.id))
        assertEquals(
            original.copy(
                status = CollectionEntryStatus.OWNED,
                purchaseDate = "2026-10-04",
                updatedAt = "2026-10-03T15:05:00Z",
                reservation = original.reservation?.copy(receivedAt = "2026-10-03T15:05:00Z"),
            ),
            repository.getEntry(original.id),
        )
        assertEquals(ReservationResult.ALREADY_RECEIVED, useCase(original.id))
    }

    @Test
    fun `missing owned and canceled entries are not received`() = runTest {
        val repository = InMemoryCollectionRepository(
            listOf(
                entry().copy(id = "owned", status = CollectionEntryStatus.OWNED),
                entry().copy(id = "canceled", canceledAt = "2026-10-01T00:00:00Z"),
            ),
        )
        val useCase = MarkPreorderReceivedUseCase(repository, clock)
        assertEquals(ReservationResult.NOT_FOUND, useCase("missing"))
        assertEquals(ReservationResult.NOT_RESERVED, useCase("owned"))
        assertEquals(ReservationResult.NOT_RESERVED, useCase("canceled"))
    }

    @Test
    fun `failed atomic receipt preserves original entry and error cause`() = runTest {
        val original = entry()
        val backing = InMemoryCollectionRepository(listOf(original))
        val cause = IllegalStateException("disk unavailable")
        val repository = object : CollectionRepository by backing {
            override suspend fun receiveReservation(
                id: String,
                receivedAt: String,
                receivedDate: String,
            ): ReservationResult = throw RepositoryFailure(RepositoryOperation.WRITE, cause)
        }
        val failure =
            assertFailsWith<RepositoryFailure> {
                MarkPreorderReceivedUseCase(repository, clock)(original.id)
            }
        assertEquals(cause, failure.cause)
        assertEquals(original, backing.getEntry(original.id))
    }

    private fun entry(): CollectionEntry = CollectionEntry(
        id = "one", name = "Goods", category = "goods", status = CollectionEntryStatus.RESERVED,
        relatedLink = "https://example.com", note = "Keep", createdAt = "2026-09-30T00:00:00Z",
        updatedAt = "2026-09-30T00:00:00Z",
        reservation = ReservationDetails(
            orderDate = "2026-09-30",
            totalPrice = 20000,
        ),
    )
}
