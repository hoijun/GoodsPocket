package goods.pocket.app.presentation.component

import goods.pocket.app.domain.collection.CollectionEntryStatus
import goods.pocket.app.domain.event.EventType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class QuickAddFormProjectionTest {

    @Test
    fun `collection defaults to an empty owned draft`() {
        val draft = CollectionDraft()

        assertEquals(CollectionEntryStatus.OWNED, draft.status)
        assertEquals(
            List(8) { "" },
            listOf(
                draft.name,
                draft.category,
                draft.seriesName,
                draft.characterName,
                draft.purchaseStore,
                draft.releaseDate,
                draft.reservationStore,
                draft.note,
            ),
        )
        assertFalse(draft.canSubmit)
    }

    @Test
    fun `only reserved status selects reservation fields`() {
        assertTrue(CollectionDraft(status = CollectionEntryStatus.RESERVED).isReserved)
        assertFalse(CollectionDraft(status = CollectionEntryStatus.OWNED).isReserved)
        assertFalse(CollectionDraft(status = CollectionEntryStatus.PLANNED_CLEANUP).isReserved)
    }

    @Test
    fun `owned and cleanup require only name and category`() {
        listOf(
            CollectionEntryStatus.OWNED,
            CollectionEntryStatus.PLANNED_CLEANUP,
        ).forEach { status ->
            val draft = CollectionDraft(name = " Stand ", category = " goods ", status = status)

            assertTrue(draft.canSubmit)
            listOf("", " ", "\t\n").forEach { blank ->
                assertFalse(draft.copy(name = blank).canSubmit)
                assertFalse(draft.copy(category = blank).canSubmit)
            }
        }
    }

    @Test
    fun `reserved requires name category reservation store and release date`() {
        val draft = CollectionDraft(
            name = " Figure ",
            category = " goods ",
            status = CollectionEntryStatus.RESERVED,
            reservationStore = " Store ",
            releaseDate = " 2026-09-30 ",
        )

        assertTrue(draft.canSubmit)
        listOf("", " ", "\t\n").forEach { blank ->
            assertFalse(draft.copy(name = blank).canSubmit)
            assertFalse(draft.copy(category = blank).canSubmit)
            assertFalse(draft.copy(reservationStore = blank, purchaseStore = "Store").canSubmit)
            assertFalse(draft.copy(releaseDate = blank).canSubmit)
        }
    }

    @Test
    fun `reserved accepts nonblank release dates without parsing`() {
        val draft = CollectionDraft(
            name = "Figure",
            category = "goods",
            status = CollectionEntryStatus.RESERVED,
            reservationStore = "Store",
            releaseDate = "not-a-date",
        )

        assertTrue(draft.canSubmit)
    }

    @Test
    fun `status copies retain fields without changing the original draft`() {
        val owned = CollectionDraft(
            name = " Stand ",
            category = " goods ",
            seriesName = " Series ",
            characterName = " Character ",
            purchaseStore = " Purchase store ",
            releaseDate = " 2026-09-30 ",
            reservationStore = " Reservation store ",
            note = " Note ",
        )

        val reserved = owned.copy(status = CollectionEntryStatus.RESERVED)

        assertEquals(CollectionEntryStatus.OWNED, owned.status)
        assertTrue(reserved.isReserved)
        assertTrue(reserved.canSubmit)
        assertEquals(owned, reserved.copy(status = CollectionEntryStatus.OWNED))
        assertEquals(" Stand ", reserved.name)
        assertEquals(" Reservation store ", reserved.reservationStore)
    }

    @Test
    fun `event defaults to an empty release draft`() {
        val draft = EventDraft()

        assertEquals("", draft.title)
        assertEquals("", draft.targetDate)
        assertEquals(EventType.RELEASE, draft.eventType)
        assertFalse(draft.canSubmit)
    }

    @Test
    fun `every event type requires nonblank title and target date`() {
        EventType.entries.forEach { eventType ->
            val draft = EventDraft(
                title = " Release ",
                targetDate = " 2026-09-30 ",
                eventType = eventType,
            )

            assertTrue(draft.canSubmit)
            listOf("", " ", "\t\n").forEach { blank ->
                assertFalse(draft.copy(title = blank).canSubmit)
                assertFalse(draft.copy(targetDate = blank).canSubmit)
            }
        }
    }

    @Test
    fun `event accepts nonblank dates without parsing or changing draft text`() {
        val draft = EventDraft(title = " Release ", targetDate = " not-a-date ")

        assertTrue(draft.canSubmit)
        assertEquals(" Release ", draft.title)
        assertEquals(" not-a-date ", draft.targetDate)
    }
}
