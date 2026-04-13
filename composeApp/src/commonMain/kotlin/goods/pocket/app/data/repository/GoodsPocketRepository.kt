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
import goods.pocket.app.domain.model.StorageLocation
import goods.pocket.app.domain.repository.CollectionRepository
import goods.pocket.app.domain.repository.EventRepository
import goods.pocket.app.domain.repository.PreorderRepository
import goods.pocket.app.domain.repository.SettingsRepository

class GoodsPocketRepository(
    private val localDataSource: GoodsPocketLocalDataSource,
) : CollectionRepository,
    PreorderRepository,
    EventRepository,
    SettingsRepository {

    override fun getEntries(filter: String?): List<CollectionEntry> {
        val items = localDataSource.getItems(filter).map(Item::toCollectionEntry)
        val preorders = localDataSource.getPreorders().mapNotNull(Preorder::toCollectionEntryOrNull)
        return (items + preorders)
            .sortedWith(compareByDescending<CollectionEntry> { it.updatedAt }.thenByDescending { it.createdAt })
    }

    override fun getEntry(id: String): CollectionEntry? {
        localDataSource.getItem(id)?.let { item ->
            return item.toCollectionEntry()
        }
        return localDataSource.getPreorder(id)?.toCollectionEntryOrNull()
    }

    override fun saveEntry(entry: CollectionEntry) {
        when (entry.status) {
            CollectionEntryStatus.RESERVED -> {
                localDataSource.upsertPreorder(
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
                    ),
                )
            }

            CollectionEntryStatus.OWNED,
            CollectionEntryStatus.PLANNED_CLEANUP,
            -> {
                localDataSource.upsertItem(
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
                    ),
                )
            }
        }
    }

    override fun deleteEntry(id: String) {
        when {
            localDataSource.getItem(id) != null -> localDataSource.deleteItem(id)
            localDataSource.getPreorder(id) != null -> localDataSource.cancelPreorder(id)
        }
    }

    override fun markEntryReceived(id: String, receivedAt: String) {
        if (localDataSource.getPreorder(id) != null) {
            localDataSource.markAsReceived(preorderId = id, receiveDate = receivedAt)
        }
    }

    override fun countOwnedEntries(): Int {
        val receivedPreorders = localDataSource.getPreorders()
            .count { it.status == PreorderStatus.RECEIVED }
        return localDataSource.countOwnedItems() + receivedPreorders
    }

    override fun countReservedEntries(): Int = localDataSource.countActivePreorders()

    override fun getItems(filter: String?): List<Item> = localDataSource.getItems(filter)

    override fun getItem(id: String): Item? = localDataSource.getItem(id)

    override fun saveItem(item: Item) {
        localDataSource.upsertItem(item)
    }

    override fun deleteItem(id: String) {
        localDataSource.deleteItem(id)
    }

    override fun countOwnedItems(): Int = localDataSource.countOwnedItems()

    override fun getPreorders(status: PreorderStatus?): List<Preorder> = localDataSource.getPreorders(status)

    override fun getPreorder(id: String): Preorder? = localDataSource.getPreorder(id)

    override fun savePreorder(preorder: Preorder) {
        localDataSource.upsertPreorder(preorder)
    }

    override fun markAsReceived(preorderId: String, receiveDate: String) {
        localDataSource.markAsReceived(preorderId, receiveDate)
    }

    override fun cancelPreorder(preorderId: String) {
        localDataSource.cancelPreorder(preorderId)
    }

    override fun countActivePreorders(): Int = localDataSource.countActivePreorders()

    override fun getUpcomingEvents(limit: Int): List<Event> = localDataSource.getUpcomingEvents(limit)

    override fun getEvents(type: EventType?): List<Event> = localDataSource.getEvents(type)

    override fun saveEvent(event: Event) {
        localDataSource.upsertEvent(event)
    }

    override fun deleteEvent(id: String) {
        localDataSource.deleteEvent(id)
    }

    override fun getStorageLocations(): List<StorageLocation> = localDataSource.getStorageLocations()

    override fun saveStorageLocation(location: StorageLocation) {
        localDataSource.upsertStorageLocation(location)
    }

    override fun deleteStorageLocation(id: String) {
        localDataSource.deleteStorageLocation(id)
    }

    override fun getAppPreferences(): AppPreference = localDataSource.getAppPreferences()

    override fun updateAppPreferences(preferences: AppPreference) {
        localDataSource.updateAppPreferences(preferences)
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
        category = "예약 굿즈",
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
