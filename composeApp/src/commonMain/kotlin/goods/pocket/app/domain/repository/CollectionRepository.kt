package goods.pocket.app.domain.repository

import goods.pocket.app.domain.model.Item

interface CollectionRepository {
    fun getItems(filter: String? = null): List<Item>
    fun getItem(id: String): Item?
    fun saveItem(item: Item)
    fun deleteItem(id: String)
    fun countOwnedItems(): Int
}
