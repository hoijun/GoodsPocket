package goods.pocket.app.domain.repository

import goods.pocket.app.domain.model.CollectionEntry
import goods.pocket.app.domain.model.Item

interface CollectionRepository {
    fun getEntries(filter: String? = null): List<CollectionEntry>
    fun getEntry(id: String): CollectionEntry?
    fun saveEntry(entry: CollectionEntry)
    fun deleteEntry(id: String)
    fun markEntryReceived(id: String, receivedAt: String)
    fun countOwnedEntries(): Int
    fun countReservedEntries(): Int

    fun getItems(filter: String? = null): List<Item>
    fun getItem(id: String): Item?
    fun saveItem(item: Item)
    fun deleteItem(id: String)
    fun countOwnedItems(): Int
}
