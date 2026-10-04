package goods.pocket.app.data.local

import goods.pocket.app.domain.collection.CollectionEntry
import goods.pocket.app.domain.collection.CollectionEntryStatus
import goods.pocket.app.domain.collection.ReservationDetails
import goods.pocket.app.domain.event.Event
import goods.pocket.app.domain.event.EventType

/** Explicit fixture values for previews and demos; application startup never installs these. */
object DemoData {
    val entries: List<CollectionEntry> = listOf(
        CollectionEntry(
            id = "demo-badge",
            name = "Character badge",
            category = "goods",
            status = CollectionEntryStatus.OWNED,
            seriesName = "Pocket collection",
            characterName = "Mina",
            purchasePrice = 12000,
            purchaseDate = "2026-10-01",
            createdAt = "2026-10-01T03:00:00Z",
            updatedAt = "2026-10-01T03:00:00Z",
        ),
        CollectionEntry(
            id = "demo-stand",
            name = "Acrylic stand",
            category = "goods",
            status = CollectionEntryStatus.RESERVED,
            seriesName = "Pocket collection",
            characterName = "Sora",
            purchasePrice = 22000,
            releaseDate = "2026-10-20",
            reservationStore = "Official store",
            reservation = ReservationDetails(orderDate = "2026-10-02", totalPrice = 22000),
            createdAt = "2026-10-02T03:00:00Z",
            updatedAt = "2026-10-02T03:00:00Z",
        ),
    )

    val events: List<Event> = listOf(
        Event(
            id = "demo-release",
            title = "Acrylic stand release",
            eventType = EventType.RELEASE,
            targetDate = "2026-10-20",
            relatedEntryId = "demo-stand",
            locationOrStore = "Official store",
            createdAt = "2026-10-02T03:00:00Z",
            updatedAt = "2026-10-02T03:00:00Z",
        ),
    )
}
