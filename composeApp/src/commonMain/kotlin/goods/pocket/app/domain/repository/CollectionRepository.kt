package goods.pocket.app.domain.repository

import goods.pocket.app.domain.model.Item
import goods.pocket.app.domain.model.Transaction

interface CollectionRepository {
    fun getItems(filter: String? = null): List<Item>
    fun getItem(id: String): Item?
    fun saveItem(item: Item)
    fun deleteItem(id: String)
    fun getItemTransactions(itemId: String): List<Transaction>
    fun countOwnedItems(): Int
}
