package goods.pocket.app.data

import goods.pocket.app.domain.model.CollectionEntryStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class CollectionEntryRepositoryTest {

    private val repository = InMemoryGoodsPocketRepository()

    @Test
    fun `repository exposes reserved entries from the unified collection list`() {
        val entries = repository.getEntries()

        assertTrue(entries.any { it.status == CollectionEntryStatus.RESERVED })
    }

    @Test
    fun `marking a reserved entry as received keeps the same entry and moves it to owned`() {
        repository.markEntryReceived(
            id = "pre-1",
            receivedAt = "2026-04-13",
        )

        val updated = repository.getEntry("pre-1")

        assertNotNull(updated)
        assertEquals(CollectionEntryStatus.OWNED, updated.status)
    }

    @Test
    fun `deleting a reserved entry removes it from the unified repository`() {
        repository.deleteEntry("pre-2")

        assertNull(repository.getEntry("pre-2"))
    }
}
