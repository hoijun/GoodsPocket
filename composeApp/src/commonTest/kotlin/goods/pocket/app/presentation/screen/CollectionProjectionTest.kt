package goods.pocket.app.presentation.screen

import goods.pocket.app.domain.model.CollectionEntry
import goods.pocket.app.domain.model.CollectionEntryStatus
import goods.pocket.app.presentation.state.CollectionSegment
import kotlin.test.Test
import kotlin.test.assertEquals

class CollectionProjectionTest {

    @Test
    fun `query and segment are projected from the unified collection list`() {
        val entries = listOf(
            entry("item-1", "Acrylic stand", "Hololive", "Suisei", CollectionEntryStatus.OWNED),
            entry("pre-1", "Birthday set", "Hololive", "Miko", CollectionEntryStatus.RESERVED),
            entry("item-2", "Art book", "Blue Archive", "Hina", CollectionEntryStatus.OWNED),
        )

        assertEquals(
            listOf("item-2"),
            visibleCollectionEntries(entries, "Hina", CollectionSegment.OWNED).map { it.id },
        )
        assertEquals(
            listOf("pre-1"),
            visibleCollectionEntries(entries, "hololive", CollectionSegment.RESERVED).map { it.id },
        )
    }

    @Test
    fun `collection summary reflects owned reserved and purchase totals`() {
        val entries = listOf(
            entry(
                id = "item-1",
                name = "Acrylic stand",
                series = "Hololive",
                character = "Suisei",
                status = CollectionEntryStatus.OWNED,
                purchasePrice = 18_000,
            ),
            entry(
                id = "item-2",
                name = "Art book",
                series = "Blue Archive",
                character = "Hina",
                status = CollectionEntryStatus.PLANNED_CLEANUP,
                purchasePrice = 32_000,
            ),
            entry(
                id = "pre-1",
                name = "Birthday set",
                series = "Hololive",
                character = "Miko",
                status = CollectionEntryStatus.RESERVED,
                purchasePrice = 42_000,
            ),
        )

        assertEquals(
            CollectionSummary(
                ownedCount = 2,
                reservedCount = 1,
                totalPurchaseAmount = 50_000,
            ),
            collectionSummary(entries),
        )
    }

    @Test
    fun `reserved cards project reservation store and release date metadata`() {
        val reserved = entry(
            id = "pre-1",
            name = "Birthday set",
            series = "Hololive",
            character = "Miko",
            status = CollectionEntryStatus.RESERVED,
            reservationStore = "Animate",
            releaseDate = "2026-03-28",
        )

        assertEquals(
            CollectionCardMetadata.Reservation(
                store = "Animate",
                releaseDate = "2026-03-28",
            ),
            reserved.collectionCardMetadata(),
        )
    }

    @Test
    fun `owned cards project catalog metadata`() {
        val owned = entry(
            id = "item-1",
            name = "Acrylic stand",
            series = "Hololive",
            character = "Suisei",
            status = CollectionEntryStatus.OWNED,
        )

        assertEquals(
            CollectionCardMetadata.Catalog(
                seriesName = "Hololive",
                category = "goods",
            ),
            owned.collectionCardMetadata(),
        )
    }
}

private fun entry(
    id: String,
    name: String,
    series: String,
    character: String,
    status: CollectionEntryStatus,
    purchasePrice: Long? = null,
    reservationStore: String? = null,
    releaseDate: String? = null,
): CollectionEntry {
    return CollectionEntry(
        id = id,
        name = name,
        category = "goods",
        status = status,
        seriesName = series,
        characterName = character,
        purchasePrice = purchasePrice,
        reservationStore = reservationStore,
        releaseDate = releaseDate,
        createdAt = "2026-01-01",
        updatedAt = "2026-01-01",
    )
}
