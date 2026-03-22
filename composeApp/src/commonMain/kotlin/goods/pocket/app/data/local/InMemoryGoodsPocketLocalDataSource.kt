package goods.pocket.app.data.local

import goods.pocket.app.domain.model.AppPreference
import goods.pocket.app.domain.model.Event
import goods.pocket.app.domain.model.EventType
import goods.pocket.app.domain.model.Item
import goods.pocket.app.domain.model.ItemStatus
import goods.pocket.app.domain.model.Preorder
import goods.pocket.app.domain.model.PreorderStatus
import goods.pocket.app.domain.model.StorageLocation
import goods.pocket.app.domain.model.Transaction
import goods.pocket.app.domain.model.TransactionType

internal class InMemoryGoodsPocketLocalDataSource : GoodsPocketLocalDataSource {
    private val items = GoodsPocketSeedData.items.toMutableList()
    private val preorders = GoodsPocketSeedData.preorders.toMutableList()
    private val transactions = GoodsPocketSeedData.transactions.toMutableList()
    private val events = GoodsPocketSeedData.events.toMutableList()
    private val storageLocations = GoodsPocketSeedData.storageLocations.toMutableList()
    private var appPreferences = GoodsPocketSeedData.appPreferences

    override fun getItems(filter: String?): List<Item> {
        return items
            .asSequence()
            .filter { item ->
                filter.isNullOrBlank() ||
                    item.name.contains(filter, ignoreCase = true) ||
                    item.seriesName.orEmpty().contains(filter, ignoreCase = true)
            }
            .sortedWith(compareByDescending<Item> { it.updatedAt }.thenByDescending { it.createdAt })
            .toList()
    }

    override fun getItem(id: String): Item? = items.firstOrNull { it.id == id }

    override fun upsertItem(item: Item) {
        items.replaceById(item, Item::id)
    }

    override fun deleteItem(id: String) {
        items.removeAll { it.id == id }
    }

    override fun getItemTransactions(itemId: String): List<Transaction> {
        return transactions
            .asSequence()
            .filter { it.relatedItemId == itemId }
            .sortedWith(compareByDescending<Transaction> { it.transactionDate }.thenByDescending { it.createdAt })
            .toList()
    }

    override fun countOwnedItems(): Int = items.count { it.status == ItemStatus.OWNED }

    override fun getPreorders(status: PreorderStatus?): List<Preorder> {
        return preorders
            .asSequence()
            .filter { status == null || it.status == status }
            .sortedWith(compareBy<Preorder> { it.releaseDate }.thenByDescending { it.updatedAt })
            .toList()
    }

    override fun getPreorder(id: String): Preorder? = preorders.firstOrNull { it.id == id }

    override fun upsertPreorder(preorder: Preorder) {
        preorders.replaceById(preorder, Preorder::id)
    }

    override fun markAsReceived(preorderId: String, receiveDate: String) {
        preorders.updateById(preorderId) { preorder ->
            preorder.copy(
                status = PreorderStatus.RECEIVED,
                receiveDate = receiveDate,
                updatedAt = receiveDate,
            )
        }
    }

    override fun cancelPreorder(preorderId: String) {
        preorders.updateById(preorderId) { preorder ->
            preorder.copy(
                status = PreorderStatus.CANCELED,
                updatedAt = GoodsPocketSeedData.defaultTimestamp,
            )
        }
    }

    override fun countActivePreorders(): Int {
        return preorders.count { it.status == PreorderStatus.ACTIVE || it.status == PreorderStatus.PAYMENT_PENDING }
    }

    override fun getTransactions(monthFilter: String?): List<Transaction> {
        return transactions
            .asSequence()
            .filter { monthFilter.isNullOrBlank() || it.transactionDate.startsWith(monthFilter) }
            .sortedWith(compareByDescending<Transaction> { it.transactionDate }.thenByDescending { it.createdAt })
            .toList()
    }

    override fun getMonthlySummary(monthFilter: String): Long {
        return transactions
            .filter { it.transactionDate.startsWith(monthFilter) }
            .filterNot { it.type == TransactionType.REFUND || it.type == TransactionType.TRANSFER_INCOME }
            .sumOf(Transaction::amount)
    }

    override fun upsertTransaction(transaction: Transaction) {
        transactions.replaceById(transaction, Transaction::id)
    }

    override fun deleteTransaction(id: String) {
        transactions.removeAll { it.id == id }
    }

    override fun getUpcomingEvents(limit: Int): List<Event> {
        return events
            .sortedWith(compareBy<Event> { it.targetDate }.thenByDescending { it.updatedAt })
            .take(limit)
    }

    override fun getEvents(type: EventType?): List<Event> {
        return events
            .asSequence()
            .filter { type == null || it.eventType == type }
            .sortedWith(compareBy<Event> { it.targetDate }.thenByDescending { it.updatedAt })
            .toList()
    }

    override fun upsertEvent(event: Event) {
        events.replaceById(event, Event::id)
    }

    override fun deleteEvent(id: String) {
        events.removeAll { it.id == id }
    }

    override fun getStorageLocations(): List<StorageLocation> {
        return storageLocations.sortedBy(StorageLocation::name)
    }

    override fun upsertStorageLocation(location: StorageLocation) {
        storageLocations.replaceById(location, StorageLocation::id)
    }

    override fun deleteStorageLocation(id: String) {
        storageLocations.removeAll { it.id == id }
    }

    override fun getAppPreferences(): AppPreference = appPreferences

    override fun updateAppPreferences(preferences: AppPreference) {
        appPreferences = preferences
    }
}

private fun <T> MutableList<T>.replaceById(
    value: T,
    idSelector: (T) -> String,
) {
    val index = indexOfFirst { idSelector(it) == idSelector(value) }
    if (index >= 0) {
        this[index] = value
    } else {
        add(value)
    }
}

private fun MutableList<Preorder>.updateById(
    id: String,
    transform: (Preorder) -> Preorder,
) {
    val index = indexOfFirst { it.id == id }
    if (index >= 0) {
        this[index] = transform(this[index])
    }
}
