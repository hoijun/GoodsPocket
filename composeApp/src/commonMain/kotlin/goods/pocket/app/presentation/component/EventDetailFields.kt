package goods.pocket.app.presentation.component

import goods.pocket.app.domain.model.CollectionEntry
import goods.pocket.app.domain.model.Event

internal enum class EventDetailFieldKind {
    TARGET_DATE,
    RELATED_PREORDER,
    RELATED_ITEM,
    LOCATION,
    MEMO,
}

internal data class EventDetailField(
    val kind: EventDetailFieldKind,
    val value: String,
)

internal fun eventDetailFields(event: Event, entries: List<CollectionEntry>): List<EventDetailField> = buildList {
    add(EventDetailField(EventDetailFieldKind.TARGET_DATE, event.targetDate))
    event.relatedPreorderId?.takeIf { it.isNotBlank() }?.let { id ->
        add(EventDetailField(EventDetailFieldKind.RELATED_PREORDER, entries.firstOrNull { it.id == id }?.name ?: id))
    }
    event.relatedItemId?.takeIf { it.isNotBlank() }?.let { id ->
        add(EventDetailField(EventDetailFieldKind.RELATED_ITEM, entries.firstOrNull { it.id == id }?.name ?: id))
    }
    event.locationOrStore?.takeIf { it.isNotBlank() }?.let { location ->
        add(EventDetailField(EventDetailFieldKind.LOCATION, location))
    }
    event.memo?.takeIf { it.isNotBlank() }?.let { memo ->
        add(EventDetailField(EventDetailFieldKind.MEMO, memo))
    }
}
