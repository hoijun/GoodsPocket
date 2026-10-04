package goods.pocket.app.presentation.component

import androidx.compose.runtime.saveable.SaverScope
import goods.pocket.app.domain.collection.CollectionEntryStatus
import goods.pocket.app.domain.event.EventType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class DraftSaveableTest {
    private val scope = SaverScope { it is String }

    @Test
    fun collectionDraftRestoresAllUnsavedFieldsIncludingReservationAndNote() {
        val draft = CollectionDraft(
            "Figure", "Category", CollectionEntryStatus.RESERVED, "Series", "Character",
            "Purchase store", "2026-12-31", "Reservation store", "First line\nSecond line",
        )
        val saved = with(CollectionDraftSaver) { scope.save(draft) }
        assertEquals(draft, CollectionDraftSaver.restore(assertNotNull(saved)))
    }

    @Test
    fun eventDraftRestoresTypeAndIncompleteInputsWithoutValidation() {
        val draft = EventDraft("Unfinished event", "2026-", EventType.PAYMENT_DUE)
        val saved = with(EventDraftSaver) { scope.save(draft) }
        assertEquals(draft, EventDraftSaver.restore(assertNotNull(saved)))
    }
}
