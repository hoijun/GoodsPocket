package goods.pocket.app.domain.repository

import goods.pocket.app.domain.model.CollectionEntry
import goods.pocket.app.domain.model.Item

interface CollectionRepository {
    suspend fun getEntries(filter: String? = null): List<CollectionEntry>
    suspend fun getEntry(id: String): CollectionEntry?
    suspend fun saveEntry(entry: CollectionEntry)
    suspend fun deleteEntry(id: String, deletedAt: String)
    suspend fun receiveReservedEntry(
        preorderId: String,
        receivedItem: Item,
        receivedAt: String,
    )
    suspend fun getItems(filter: String? = null): List<Item>
    suspend fun countOwnedItems(): Int
}
