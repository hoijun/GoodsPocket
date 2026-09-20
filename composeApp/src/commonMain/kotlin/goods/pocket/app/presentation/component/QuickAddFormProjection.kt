package goods.pocket.app.presentation.component

import goods.pocket.app.domain.model.CollectionEntryStatus
import goods.pocket.app.domain.model.EventType

data class CollectionDraft(
    val name: String = "",
    val category: String = "",
    val status: CollectionEntryStatus = CollectionEntryStatus.OWNED,
    val seriesName: String = "",
    val characterName: String = "",
    val purchaseStore: String = "",
    val releaseDate: String = "",
    val reservationStore: String = "",
    val note: String = "",
) {
    val isReserved: Boolean
        get() = status == CollectionEntryStatus.RESERVED

    val canSubmit: Boolean
        get() = name.isNotBlank() && category.isNotBlank() &&
            (!isReserved || (reservationStore.isNotBlank() && releaseDate.isNotBlank()))
}

data class EventDraft(
    val title: String = "",
    val targetDate: String = "",
    val eventType: EventType = EventType.RELEASE,
) {
    val canSubmit: Boolean
        get() = title.isNotBlank() && targetDate.isNotBlank()
}
