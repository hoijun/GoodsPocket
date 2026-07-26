package goods.pocket.app.data.local

import goods.pocket.app.data.local.model.LocalAppPreferenceRecord
import goods.pocket.app.data.local.model.LocalEventRecord
import goods.pocket.app.data.local.model.LocalItemRecord
import goods.pocket.app.data.local.model.LocalPreorderRecord
import goods.pocket.app.data.local.model.LocalStorageLocationRecord

internal class InMemoryGoodsPocketLocalDataSource : GoodsPocketLocalDataSource {
    private val items = GoodsPocketSeedData.items.toMutableList()
    private val preorders = GoodsPocketSeedData.preorders.toMutableList()
    private val events = GoodsPocketSeedData.events.toMutableList()
    private val storageLocations = GoodsPocketSeedData.storageLocations.toMutableList()
    private var appPreferences = GoodsPocketSeedData.appPreferences

    override fun getItems(filter: String?): List<LocalItemRecord> {
        return items
            .asSequence()
            .filter { item ->
                filter.isNullOrBlank() ||
                    item.name.contains(filter, ignoreCase = true) ||
                    item.seriesName.orEmpty().contains(filter, ignoreCase = true)
            }
            .sortedWith(compareByDescending<LocalItemRecord> { it.updatedAt }.thenByDescending { it.createdAt })
            .toList()
    }

    override fun getItem(id: String): LocalItemRecord? = items.firstOrNull { it.id == id }

    override fun upsertItem(item: LocalItemRecord) {
        items.replaceById(item, LocalItemRecord::id)
    }

    override fun deleteItem(id: String) {
        items.removeAll { it.id == id }
    }

    override fun countOwnedItems(): Int = items.count { it.status == "OWNED" }

    override fun getPreorders(status: String?): List<LocalPreorderRecord> {
        return preorders
            .asSequence()
            .filter { status == null || it.status == status }
            .sortedWith(compareBy<LocalPreorderRecord> { it.releaseDate }.thenByDescending { it.updatedAt })
            .toList()
    }

    override fun getPreorder(id: String): LocalPreorderRecord? = preorders.firstOrNull { it.id == id }

    override fun upsertPreorder(preorder: LocalPreorderRecord) {
        preorders.replaceById(preorder, LocalPreorderRecord::id)
    }

    override fun saveCollectionEntry(
        item: LocalItemRecord?,
        preorder: LocalPreorderRecord?,
        changedAt: String,
    ) {
        require((item == null) != (preorder == null))
        if (item != null) {
            upsertItem(item)
            cancelPreorder(preorderId = item.id, canceledAt = changedAt)
        } else if (preorder != null) {
            upsertPreorder(preorder)
            deleteItem(preorder.id)
        }
    }

    override fun markAsReceived(preorderId: String, receiveDate: String) {
        preorders.updateById(preorderId) { preorder ->
            preorder.copy(
                status = "RECEIVED",
                receiveDate = receiveDate,
                updatedAt = receiveDate,
            )
        }
    }

    override fun cancelPreorder(preorderId: String, canceledAt: String) {
        preorders.updateById(preorderId) { preorder ->
            preorder.copy(
                status = "CANCELED",
                updatedAt = canceledAt,
            )
        }
    }

    override fun replacePreorderWithItem(
        preorderId: String,
        item: LocalItemRecord,
        receivedAt: String,
    ) {
        upsertItem(item)
        cancelPreorder(preorderId = preorderId, canceledAt = receivedAt)
    }

    override fun countActivePreorders(): Int {
        return preorders.count { it.status == "ACTIVE" || it.status == "PAYMENT_PENDING" }
    }

    override fun getUpcomingEvents(onOrAfter: String, limit: Int): List<LocalEventRecord> {
        return events
            .filter { it.targetDate >= onOrAfter }
            .sortedWith(compareBy<LocalEventRecord> { it.targetDate }.thenByDescending { it.updatedAt })
            .take(limit)
    }

    override fun getEvents(type: String?): List<LocalEventRecord> {
        return events
            .asSequence()
            .filter { type == null || it.eventType == type }
            .sortedWith(compareBy<LocalEventRecord> { it.targetDate }.thenByDescending { it.updatedAt })
            .toList()
    }

    override fun upsertEvent(event: LocalEventRecord) {
        events.replaceById(event, LocalEventRecord::id)
    }

    override fun deleteEvent(id: String) {
        events.removeAll { it.id == id }
    }

    override fun getStorageLocations(): List<LocalStorageLocationRecord> {
        return storageLocations.sortedBy(LocalStorageLocationRecord::name)
    }

    override fun upsertStorageLocation(location: LocalStorageLocationRecord) {
        storageLocations.replaceById(location, LocalStorageLocationRecord::id)
    }

    override fun deleteStorageLocation(id: String) {
        storageLocations.removeAll { it.id == id }
    }

    override fun getAppPreferences(): LocalAppPreferenceRecord = appPreferences

    override fun updateAppPreferences(preferences: LocalAppPreferenceRecord) {
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

private fun MutableList<LocalPreorderRecord>.updateById(
    id: String,
    transform: (LocalPreorderRecord) -> LocalPreorderRecord,
) {
    val index = indexOfFirst { it.id == id }
    if (index >= 0) {
        this[index] = transform(this[index])
    }
}
