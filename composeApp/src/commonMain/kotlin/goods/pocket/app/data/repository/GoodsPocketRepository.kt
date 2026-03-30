package goods.pocket.app.data.repository

import goods.pocket.app.data.local.GoodsPocketLocalDataSource
import goods.pocket.app.domain.model.AppPreference
import goods.pocket.app.domain.model.Event
import goods.pocket.app.domain.model.EventType
import goods.pocket.app.domain.model.Item
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
