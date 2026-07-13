package goods.pocket.app.data

import goods.pocket.app.domain.model.CollectionEntryStatus
import goods.pocket.app.domain.model.Item
import goods.pocket.app.domain.model.ItemStatus
import goods.pocket.app.domain.model.RESERVED_COLLECTION_CATEGORY_CODE
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class CollectionEntryRepositoryTest {

    private val repository = InMemoryGoodsPocketRepository()

    @Test
    fun `repository exposes reserved entries from the unified collection list`() = runTest {
        val entries = repository.getEntries()

        val reservedEntry = entries.first { it.status == CollectionEntryStatus.RESERVED }
        assertEquals(RESERVED_COLLECTION_CATEGORY_CODE, reservedEntry.category)
    }

    @Test
    fun `receiving a reserved entry replaces it with exactly one owned entry`() = runTest {
        repository.receiveReservedEntry(
            preorderId = "pre-1",
            receivedItem = Item(
                id = "pre-1",
                name = "니지산지 애니버서리 배지 세트",
                category = "goods",
                status = ItemStatus.OWNED,
                linkedPreorderId = "pre-1",
                purchaseDate = "2026-04-13",
                createdAt = "2026-02-25",
                updatedAt = "2026-04-13",
            ),
            receivedAt = "2026-04-13",
        )

        val matchingEntries = repository.getEntries().filter { it.id == "pre-1" }

        assertEquals(1, matchingEntries.size)
        assertEquals(CollectionEntryStatus.OWNED, matchingEntries.single().status)
        assertEquals("2026-04-13", matchingEntries.single().purchaseDate)
    }

    @Test
    fun `deleting a reserved entry removes it from the unified repository`() = runTest {
        repository.deleteEntry(id = "pre-2", deletedAt = "2026-07-13")

        assertNull(repository.getEntry("pre-2"))
    }
}
