package goods.pocket.app.presentation.component

import goods.pocket.app.domain.model.CollectionEntry
import goods.pocket.app.domain.model.CollectionEntryStatus
import goods.pocket.app.domain.model.Event
import goods.pocket.app.domain.model.EventType
import kotlin.test.Test
import kotlin.test.assertEquals

class EventDetailFieldsTest {
    @Test
    fun absentOrBlankOptionalValuesLeaveOnlyTargetDate() {
        listOf(null, "", " \t\n").forEach { blank ->
            val event = event().copy(
                relatedPreorderId = blank,
                relatedItemId = blank,
                locationOrStore = blank,
                memo = blank,
            )

            val fields = eventDetailFields(event, emptyList())

            assertEquals(listOf(EventDetailField(EventDetailFieldKind.TARGET_DATE, event.targetDate)), fields)
        }
    }

    @Test
    fun targetDateIsIncludedEvenWhenBlank() {
        val fields = eventDetailFields(event().copy(targetDate = ""), emptyList())

        assertEquals(listOf(EventDetailField(EventDetailFieldKind.TARGET_DATE, "")), fields)
    }

    @Test
    fun populatedLocationAndMemoPreserveOriginalText() {
        val event = event().copy(locationOrStore = "  Store  ", memo = "  Bring receipt\nSecond line  ")

        val fields = eventDetailFields(event, emptyList())

        assertEquals(
            listOf(
                EventDetailField(EventDetailFieldKind.TARGET_DATE, event.targetDate),
                EventDetailField(EventDetailFieldKind.LOCATION, "  Store  "),
                EventDetailField(EventDetailFieldKind.MEMO, "  Bring receipt\nSecond line  "),
            ),
            fields,
        )
    }

    @Test
    fun linkedEntriesResolveNamesByIdInMetadataOrder() {
        val event = event().copy(relatedPreorderId = "preorder", relatedItemId = "item")
        val entries = listOf(entry("other", "Unrelated"), entry("item", "Item name"), entry("preorder", "Preorder name"))

        val fields = eventDetailFields(event, entries)

        assertEquals(
            listOf(
                EventDetailField(EventDetailFieldKind.TARGET_DATE, event.targetDate),
                EventDetailField(EventDetailFieldKind.RELATED_PREORDER, "Preorder name"),
                EventDetailField(EventDetailFieldKind.RELATED_ITEM, "Item name"),
            ),
            fields,
        )
    }

    @Test
    fun missingLinkedEntriesFallBackToOriginalIds() {
        val event = event().copy(relatedPreorderId = " missing-preorder ", relatedItemId = "missing-item")

        val fields = eventDetailFields(event, listOf(entry("other", "Unrelated")))

        assertEquals(
            listOf(
                EventDetailField(EventDetailFieldKind.TARGET_DATE, event.targetDate),
                EventDetailField(EventDetailFieldKind.RELATED_PREORDER, " missing-preorder "),
                EventDetailField(EventDetailFieldKind.RELATED_ITEM, "missing-item"),
            ),
            fields,
        )
    }

    private fun event() = Event(
        id = "event",
        title = "Release",
        eventType = EventType.RELEASE,
        targetDate = "2026-04-27",
        createdAt = "2026-04-01T00:00:00Z",
        updatedAt = "2026-04-01T00:00:00Z",
    )

    private fun entry(id: String, name: String) = CollectionEntry(
        id = id,
        name = name,
        category = "goods",
        status = CollectionEntryStatus.OWNED,
        createdAt = "2026-04-01T00:00:00Z",
        updatedAt = "2026-04-01T00:00:00Z",
    )
}
