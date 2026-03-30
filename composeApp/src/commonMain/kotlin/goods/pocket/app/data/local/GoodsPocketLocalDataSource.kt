package goods.pocket.app.data.local

import goods.pocket.app.domain.model.AppPreference
import goods.pocket.app.domain.model.Event
import goods.pocket.app.domain.model.EventType
import goods.pocket.app.domain.model.Item
import goods.pocket.app.domain.model.Preorder
import goods.pocket.app.domain.model.PreorderStatus
import goods.pocket.app.domain.model.StorageLocation

interface GoodsPocketLocalDataSource {
    fun getItems(filter: String? = null): List<Item>
    fun getItem(id: String): Item?
    fun upsertItem(item: Item)
    fun deleteItem(id: String)
    fun countOwnedItems(): Int

    fun getPreorders(status: PreorderStatus? = null): List<Preorder>
    fun getPreorder(id: String): Preorder?
    fun upsertPreorder(preorder: Preorder)
    fun markAsReceived(preorderId: String, receiveDate: String)
    fun cancelPreorder(preorderId: String)
    fun countActivePreorders(): Int

    fun getUpcomingEvents(limit: Int): List<Event>
    fun getEvents(type: EventType? = null): List<Event>
    fun upsertEvent(event: Event)
    fun deleteEvent(id: String)

    fun getStorageLocations(): List<StorageLocation>
    fun upsertStorageLocation(location: StorageLocation)
    fun deleteStorageLocation(id: String)

    fun getAppPreferences(): AppPreference
    fun updateAppPreferences(preferences: AppPreference)
}
