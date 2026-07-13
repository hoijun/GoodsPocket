package goods.pocket.app.domain.usecase

import goods.pocket.app.domain.model.CollectionEntry
import goods.pocket.app.domain.model.CollectionEntryStatus
import goods.pocket.app.domain.model.Item
import goods.pocket.app.domain.repository.CollectionRepository
import goods.pocket.app.domain.service.AppClock
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class MarkPreorderReceivedUseCaseTest {

    @Test
    fun `reserved entry is replaced atomically with an owned item using the injected date`() = runTest {
        val repository = RecordingCollectionRepository(reservedEntry())
        val useCase = MarkPreorderReceivedUseCase(
            collectionRepository = repository,
            clock = FixedAppClock("2026-07-13"),
        )

        val result = useCase("pre-1")

        assertEquals(MarkPreorderReceivedResult.RECEIVED, result)
        assertEquals("pre-1", repository.receivedPreorderId)
        assertEquals("pre-1", repository.receivedItem?.id)
        assertEquals("pre-1", repository.receivedItem?.linkedPreorderId)
        assertEquals("2026-07-13", repository.receivedItem?.purchaseDate)
        assertEquals("2026-07-13", repository.receivedAt)
    }

    @Test
    fun `missing entry does not start a receive transaction`() = runTest {
        val repository = RecordingCollectionRepository(entry = null)
        val useCase = MarkPreorderReceivedUseCase(repository, FixedAppClock("2026-07-13"))

        val result = useCase("missing")

        assertEquals(MarkPreorderReceivedResult.NOT_FOUND, result)
        assertNull(repository.receivedItem)
    }

    @Test
    fun `owned entry does not start a receive transaction`() = runTest {
        val repository = RecordingCollectionRepository(
            reservedEntry().copy(status = CollectionEntryStatus.OWNED),
        )
        val useCase = MarkPreorderReceivedUseCase(repository, FixedAppClock("2026-07-13"))

        val result = useCase("pre-1")

        assertEquals(MarkPreorderReceivedResult.NOT_RECEIVABLE, result)
        assertNull(repository.receivedItem)
    }
}

private class FixedAppClock(
    private val date: String,
) : AppClock {
    override fun currentDate(): String = date
}

private class RecordingCollectionRepository(
    private val entry: CollectionEntry?,
) : CollectionRepository {
    var receivedPreorderId: String? = null
    var receivedItem: Item? = null
    var receivedAt: String? = null

    override suspend fun getEntries(filter: String?): List<CollectionEntry> = listOfNotNull(entry)

    override suspend fun getEntry(id: String): CollectionEntry? = entry?.takeIf { it.id == id }

    override suspend fun saveEntry(entry: CollectionEntry) = Unit

    override suspend fun deleteEntry(id: String, deletedAt: String) = Unit

    override suspend fun receiveReservedEntry(
        preorderId: String,
        receivedItem: Item,
        receivedAt: String,
    ) {
        this.receivedPreorderId = preorderId
        this.receivedItem = receivedItem
        this.receivedAt = receivedAt
    }

    override suspend fun getItems(filter: String?): List<Item> = emptyList()

    override suspend fun countOwnedItems(): Int = 0
}

private fun reservedEntry(): CollectionEntry {
    return CollectionEntry(
        id = "pre-1",
        name = "Reserved goods",
        category = "preorder",
        status = CollectionEntryStatus.RESERVED,
        seriesName = "Series",
        characterName = "Character",
        purchasePrice = 12_000,
        purchaseStore = "Store",
        reservationStore = "Store",
        createdAt = "2026-06-01",
        updatedAt = "2026-06-01",
    )
}
