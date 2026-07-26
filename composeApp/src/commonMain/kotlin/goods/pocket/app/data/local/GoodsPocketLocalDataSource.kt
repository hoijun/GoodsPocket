package goods.pocket.app.data.local

import goods.pocket.app.data.local.model.LocalAppPreferenceRecord
import goods.pocket.app.data.local.model.LocalEventRecord
import goods.pocket.app.data.local.model.LocalItemRecord
import goods.pocket.app.data.local.model.LocalPreorderRecord
import goods.pocket.app.data.local.model.LocalStorageLocationRecord

interface GoodsPocketLocalDataSource {
    fun getItems(filter: String? = null): List<LocalItemRecord>
    fun getItem(id: String): LocalItemRecord?
    fun upsertItem(item: LocalItemRecord)
    fun deleteItem(id: String)
    fun countOwnedItems(): Int

    fun getPreorders(status: String? = null): List<LocalPreorderRecord>
    fun getPreorder(id: String): LocalPreorderRecord?
    fun upsertPreorder(preorder: LocalPreorderRecord)
    fun saveCollectionEntry(
        item: LocalItemRecord?,
        preorder: LocalPreorderRecord?,
        changedAt: String,
    )
    fun markAsReceived(preorderId: String, receiveDate: String)
    fun cancelPreorder(preorderId: String, canceledAt: String)
    fun replacePreorderWithItem(
        preorderId: String,
        item: LocalItemRecord,
        receivedAt: String,
    )
    fun countActivePreorders(): Int

    fun getUpcomingEvents(onOrAfter: String, limit: Int): List<LocalEventRecord>
    fun getEvents(type: String? = null): List<LocalEventRecord>
    fun upsertEvent(event: LocalEventRecord)
    fun deleteEvent(id: String)

    fun getStorageLocations(): List<LocalStorageLocationRecord>
    fun upsertStorageLocation(location: LocalStorageLocationRecord)
    fun deleteStorageLocation(id: String)

    fun getAppPreferences(): LocalAppPreferenceRecord
    fun updateAppPreferences(preferences: LocalAppPreferenceRecord)
}
