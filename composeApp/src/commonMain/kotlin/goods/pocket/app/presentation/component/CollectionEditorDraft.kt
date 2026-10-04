package goods.pocket.app.presentation.component

import goods.pocket.app.domain.collection.CollectionEntry
import goods.pocket.app.domain.collection.CollectionEntryStatus

internal fun collectionEditorDraft(entry: CollectionEntry): CollectionDraft = CollectionDraft(
    name = entry.name,
    category = entry.category,
    status = entry.status,
    seriesName = entry.seriesName.orEmpty(),
    characterName = entry.characterName.orEmpty(),
    purchaseStore = entry.purchaseStore.orEmpty(),
    releaseDate = entry.releaseDate.orEmpty(),
    reservationStore = entry.reservationStore.orEmpty(),
    note = entry.note.orEmpty(),
)

internal fun editableCollectionStatuses(
    status: CollectionEntryStatus,
): List<CollectionEntryStatus> = when (status) {
    CollectionEntryStatus.RESERVED -> listOf(CollectionEntryStatus.RESERVED)
    CollectionEntryStatus.OWNED, CollectionEntryStatus.PLANNED_CLEANUP ->
        listOf(CollectionEntryStatus.OWNED, CollectionEntryStatus.PLANNED_CLEANUP)
}
