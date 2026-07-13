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
}

private fun entry(
    id: String,
    name: String,
    series: String,
    character: String,
    status: CollectionEntryStatus,
): CollectionEntry {
    return CollectionEntry(
        id = id,
        name = name,
        category = "goods",
        status = status,
        seriesName = series,
        characterName = character,
        createdAt = "2026-01-01",
        updatedAt = "2026-01-01",
    )
}
