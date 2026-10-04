package goods.pocket.app.presentation.component

import goods.pocket.app.domain.collection.CollectionEntry
import goods.pocket.app.domain.collection.CollectionEntryStatus
import goods.pocket.app.domain.event.Event

internal enum class EventDetailFieldKind {
    TARGET_DATE,
    RELATED_PREORDER,
    RELATED_ITEM,
    LOCATION,
    MEMO,
}

internal data class EventDetailField(val kind: EventDetailFieldKind, val value: String)

internal fun eventDetailFields(
    event: Event,
    entries: List<CollectionEntry>,
): List<EventDetailField> = buildList {
    add(EventDetailField(EventDetailFieldKind.TARGET_DATE, event.targetDate))
    event.relatedEntryId?.takeIf { it.isNotBlank() }?.let { id ->
        val entry = entries.firstOrNull { it.id == id }
        val kind = if (entry?.status == CollectionEntryStatus.RESERVED) {
            EventDetailFieldKind.RELATED_PREORDER
        } else {
            EventDetailFieldKind.RELATED_ITEM
        }
        add(EventDetailField(kind, entry?.name ?: id))
    }
    event.locationOrStore?.takeIf { it.isNotBlank() }?.let { location ->
        add(EventDetailField(EventDetailFieldKind.LOCATION, location))
    }
    event.memo?.takeIf { it.isNotBlank() }?.let { memo ->
        add(EventDetailField(EventDetailFieldKind.MEMO, memo))
    }
}
