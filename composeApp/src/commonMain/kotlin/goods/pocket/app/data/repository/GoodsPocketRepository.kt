package goods.pocket.app.data.repository

import goods.pocket.app.data.local.GoodsPocketLocalDataSource
import goods.pocket.app.domain.model.AppPreference
import goods.pocket.app.domain.model.CollectionEntry
import goods.pocket.app.domain.model.CollectionEntryStatus
import goods.pocket.app.domain.model.Event
import goods.pocket.app.domain.model.EventType
import goods.pocket.app.domain.model.Item
import goods.pocket.app.domain.model.ItemStatus
import goods.pocket.app.domain.model.Preorder
import goods.pocket.app.domain.model.PreorderStatus
import goods.pocket.app.domain.model.RESERVED_COLLECTION_CATEGORY_CODE
import goods.pocket.app.domain.repository.CollectionRepository
import goods.pocket.app.domain.repository.EventRepository
import goods.pocket.app.domain.repository.PreorderRepository
import goods.pocket.app.domain.repository.SettingsRepository

class GoodsPocketRepository(
    localDataSource: GoodsPocketLocalDataSource,
) : CollectionRepository,
    PreorderRepository,
    EventRepository,
    SettingsRepository {
    private val localDataSource = RepositoryLocalDataSource(localDataSource)

    override suspend fun getEntries(filter: String?): List<CollectionEntry> {
        val items = localDataSource.getItems(filter).map { it.toDomain().toCollectionEntry() }
        val preorders = localDataSource.getPreorders().mapNotNull { it.toDomain().toCollectionEntryOrNull() }
        return (items + preorders)
            .sortedWith(compareByDescending<CollectionEntry> { it.updatedAt }.thenByDescending { it.createdAt })
    }

    override suspend fun getEntry(id: String): CollectionEntry? {
        localDataSource.getItem(id)?.let { item ->
            return item.toDomain().toCollectionEntry()
        }
        return localDataSource.getPreorder(id)?.toDomain()?.toCollectionEntryOrNull()
    }

    override suspend fun saveEntry(entry: CollectionEntry) {
        val item = when (entry.status) {
            CollectionEntryStatus.RESERVED -> {
                null
            }

            CollectionEntryStatus.OWNED,
            CollectionEntryStatus.PLANNED_CLEANUP,
            -> {
                Item(
                    id = entry.id,
                    name = entry.name,
                    category = entry.category,
                    status = entry.status.toItemStatus(),
                    seriesName = entry.seriesName,
                    characterName = entry.characterName,
                    quantity = entry.quantity,
                    purchasePrice = entry.purchasePrice,
                    purchaseDate = entry.purchaseDate,
                    purchaseStore = entry.purchaseStore,
                    storageLocationId = entry.storageLocationId,
                    note = entry.note,
                    createdAt = entry.createdAt,
                    updatedAt = entry.updatedAt,
                ).toLocal()
            }
        }
        val preorder = if (entry.status == CollectionEntryStatus.RESERVED) {
            Preorder(
                id = entry.id,
                name = entry.name,
                storeName = entry.reservationStore ?: entry.purchaseStore.orEmpty(),
                releaseDate = entry.releaseDate ?: entry.updatedAt,
                status = PreorderStatus.ACTIVE,
                seriesName = entry.seriesName,
                characterName = entry.characterName,
                totalPrice = entry.purchasePrice,
                note = entry.note,
                createdAt = entry.createdAt,
                updatedAt = entry.updatedAt,
            ).toLocal()
        } else {
            null
        }
        localDataSource.saveCollectionEntry(
            item = item,
            preorder = preorder,
            changedAt = entry.updatedAt,
        )
    }

    override suspend fun deleteEntry(id: String, deletedAt: String) {
        when {
            localDataSource.getItem(id) != null -> localDataSource.deleteItem(id)
            localDataSource.getPreorder(id) != null -> {
                localDataSource.cancelPreorder(id, canceledAt = deletedAt)
            }
        }
    }

    override suspend fun receiveReservedEntry(
        preorderId: String,
        receivedItem: Item,
        receivedAt: String,
    ) {
        localDataSource.replacePreorderWithItem(
            preorderId = preorderId,
            item = receivedItem.toLocal(),
            receivedAt = receivedAt,
        )
    }

    override suspend fun getItems(filter: String?): List<Item> = localDataSource.getItems(filter).map { it.toDomain() }

    override suspend fun countOwnedItems(): Int = localDataSource.countOwnedItems()

    override suspend fun getPreorders(status: PreorderStatus?): List<Preorder> {
        return localDataSource.getPreorders(status?.name).map { it.toDomain() }
    }

    override suspend fun cancelPreorder(preorderId: String, canceledAt: String) {
        localDataSource.cancelPreorder(preorderId, canceledAt)
    }

    override suspend fun countActivePreorders(): Int = localDataSource.countActivePreorders()

    override suspend fun getUpcomingEvents(onOrAfter: String, limit: Int): List<Event> {
        return localDataSource.getUpcomingEvents(onOrAfter = onOrAfter, limit = limit).map { it.toDomain() }
    }

    override suspend fun getEvents(type: EventType?): List<Event> {
        return localDataSource.getEvents(type?.name).map { it.toDomain() }
    }

    override suspend fun saveEvent(event: Event) {
        localDataSource.upsertEvent(event.toLocal())
    }

    override suspend fun deleteEvent(id: String) {
        localDataSource.deleteEvent(id)
    }

    override suspend fun getAppPreferences(): AppPreference = localDataSource.getAppPreferences().toDomain()

    override suspend fun updateAppPreferences(preferences: AppPreference) {
        localDataSource.updateAppPreferences(preferences.toLocal())
    }
}

private fun Item.toCollectionEntry(): CollectionEntry {
    return CollectionEntry(
        id = id,
        name = name,
        category = category,
        status = status.toCollectionEntryStatus(),
        seriesName = seriesName,
        characterName = characterName,
        quantity = quantity,
        purchasePrice = purchasePrice,
        purchaseDate = purchaseDate,
        purchaseStore = purchaseStore,
        storageLocationId = storageLocationId,
        releaseDate = null,
        reservationStore = null,
        note = note,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}

private fun Preorder.toCollectionEntryOrNull(): CollectionEntry? {
    if (status == PreorderStatus.CANCELED) return null

    return CollectionEntry(
        id = id,
        name = name,
        category = RESERVED_COLLECTION_CATEGORY_CODE,
        status = status.toCollectionEntryStatus(),
        seriesName = seriesName,
        characterName = characterName,
        quantity = 1,
        purchasePrice = totalPrice,
        purchaseDate = orderDate,
        purchaseStore = storeName,
        storageLocationId = null,
        releaseDate = releaseDate,
        reservationStore = storeName,
        note = note,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}

private fun ItemStatus.toCollectionEntryStatus(): CollectionEntryStatus {
    return when (this) {
        ItemStatus.OWNED -> CollectionEntryStatus.OWNED
        ItemStatus.PLANNED_CLEANUP -> CollectionEntryStatus.PLANNED_CLEANUP
    }
}

private fun CollectionEntryStatus.toItemStatus(): ItemStatus {
    return when (this) {
        CollectionEntryStatus.OWNED -> ItemStatus.OWNED
        CollectionEntryStatus.PLANNED_CLEANUP -> ItemStatus.PLANNED_CLEANUP
        CollectionEntryStatus.RESERVED -> ItemStatus.OWNED
    }
}

private fun PreorderStatus.toCollectionEntryStatus(): CollectionEntryStatus {
    return when (this) {
        PreorderStatus.ACTIVE,
        PreorderStatus.PAYMENT_PENDING,
        -> CollectionEntryStatus.RESERVED
        PreorderStatus.RECEIVED -> CollectionEntryStatus.OWNED
        PreorderStatus.CANCELED -> CollectionEntryStatus.RESERVED
    }
}
