package goods.pocket.app.presentation.component

import goods.pocket.app.domain.collection.CollectionEntry
import goods.pocket.app.domain.collection.CollectionEntryStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CollectionEditorDraftTest {
    @Test
    fun `reservation draft preserves store date and note and requires reservation fields`() {
        val entry = CollectionEntry(
            id = "reserved",
            name = "Reserved book",
            category = "book",
            status = CollectionEntryStatus.RESERVED,
            reservationStore = "Shop",
            releaseDate = "2026-10-01",
            note = "Keep packaging",
            createdAt = "2026-01-01",
            updatedAt = "2026-01-01",
        )
        val draft = collectionEditorDraft(entry)
        assertTrue(draft.isReserved)
        assertEquals("Shop", draft.reservationStore)
        assertEquals("2026-10-01", draft.releaseDate)
        assertEquals("Keep packaging", draft.note)
        assertTrue(draft.canSubmit)
        assertFalse(draft.copy(reservationStore = " ").canSubmit)
        assertFalse(draft.copy(releaseDate = " ").canSubmit)
    }

    @Test
    fun `owned and cleanup edits cannot convert into reservations`() {
        val expected = listOf(CollectionEntryStatus.OWNED, CollectionEntryStatus.PLANNED_CLEANUP)
        assertEquals(expected, editableCollectionStatuses(CollectionEntryStatus.OWNED))
        assertEquals(expected, editableCollectionStatuses(CollectionEntryStatus.PLANNED_CLEANUP))
        assertEquals(
            listOf(CollectionEntryStatus.RESERVED),
            editableCollectionStatuses(CollectionEntryStatus.RESERVED),
        )
    }

    @Test
    fun `draft starts with existing editable values and validates required fields`() {
        val entry = CollectionEntry(
            id = "owned",
            name = "Art book",
            category = "book",
            status = CollectionEntryStatus.OWNED,
            seriesName = "Series",
            characterName = "Character",
            purchaseStore = "Store",
            note = "Note",
            createdAt = "2026-01-01",
            updatedAt = "2026-01-01",
        )
        val draft = collectionEditorDraft(entry)
        assertEquals("Art book", draft.name)
        assertEquals("Series", draft.seriesName)
        assertEquals("Character", draft.characterName)
        assertEquals("Store", draft.purchaseStore)
        assertEquals("Note", draft.note)
        assertTrue(draft.canSubmit)
        assertFalse(draft.copy(name = " ").canSubmit)
        assertFalse(draft.copy(category = " ").canSubmit)
    }
}
