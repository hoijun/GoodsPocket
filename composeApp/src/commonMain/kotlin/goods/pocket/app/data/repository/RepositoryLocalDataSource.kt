package goods.pocket.app.data.repository

import goods.pocket.app.data.local.GoodsPocketLocalDataSource
import goods.pocket.app.data.local.model.LocalAppPreferenceRecord
import goods.pocket.app.data.local.model.LocalEventRecord
import goods.pocket.app.data.local.model.LocalItemRecord
import goods.pocket.app.data.local.model.LocalPreorderRecord
import goods.pocket.app.data.local.model.LocalStorageLocationRecord
import goods.pocket.app.domain.repository.RepositoryFailure
import goods.pocket.app.domain.repository.RepositoryOperation
import kotlinx.coroutines.CancellationException

internal class RepositoryLocalDataSource(
    private val delegate: GoodsPocketLocalDataSource,
) : GoodsPocketLocalDataSource {
    override fun getItems(filter: String?): List<LocalItemRecord> = read { delegate.getItems(filter) }

    override fun getItem(id: String): LocalItemRecord? = read { delegate.getItem(id) }

    override fun upsertItem(item: LocalItemRecord) = write { delegate.upsertItem(item) }

    override fun deleteItem(id: String) = write { delegate.deleteItem(id) }

    override fun countOwnedItems(): Int = read { delegate.countOwnedItems() }

    override fun getPreorders(status: String?): List<LocalPreorderRecord> =
        read { delegate.getPreorders(status) }

    override fun getPreorder(id: String): LocalPreorderRecord? = read { delegate.getPreorder(id) }

    override fun upsertPreorder(preorder: LocalPreorderRecord) = write {
        delegate.upsertPreorder(preorder)
    }

    override fun saveCollectionEntry(
        item: LocalItemRecord?,
        preorder: LocalPreorderRecord?,
        changedAt: String,
    ) = write {
        delegate.saveCollectionEntry(item, preorder, changedAt)
    }

    override fun markAsReceived(preorderId: String, receiveDate: String) = write {
        delegate.markAsReceived(preorderId, receiveDate)
    }

    override fun cancelPreorder(preorderId: String, canceledAt: String) = write {
        delegate.cancelPreorder(preorderId, canceledAt)
    }

    override fun replacePreorderWithItem(
        preorderId: String,
        item: LocalItemRecord,
        receivedAt: String,
    ) = write {
        delegate.replacePreorderWithItem(preorderId, item, receivedAt)
    }

    override fun countActivePreorders(): Int = read { delegate.countActivePreorders() }

    override fun getUpcomingEvents(limit: Int): List<LocalEventRecord> =
        read { delegate.getUpcomingEvents(limit) }

    override fun getEvents(type: String?): List<LocalEventRecord> = read { delegate.getEvents(type) }

    override fun upsertEvent(event: LocalEventRecord) = write { delegate.upsertEvent(event) }

    override fun deleteEvent(id: String) = write { delegate.deleteEvent(id) }

    override fun getStorageLocations(): List<LocalStorageLocationRecord> =
        read { delegate.getStorageLocations() }

    override fun upsertStorageLocation(location: LocalStorageLocationRecord) = write {
        delegate.upsertStorageLocation(location)
    }

    override fun deleteStorageLocation(id: String) = write { delegate.deleteStorageLocation(id) }

    override fun getAppPreferences(): LocalAppPreferenceRecord = read { delegate.getAppPreferences() }

    override fun updateAppPreferences(preferences: LocalAppPreferenceRecord) = write {
        delegate.updateAppPreferences(preferences)
    }
}

private inline fun <T> read(block: () -> T): T = translate(RepositoryOperation.READ, block)

private inline fun <T> write(block: () -> T): T = translate(RepositoryOperation.WRITE, block)

private inline fun <T> translate(
    operation: RepositoryOperation,
    block: () -> T,
): T {
    return try {
        block()
    } catch (error: CancellationException) {
        throw error
    } catch (error: RepositoryFailure) {
        throw error
    } catch (error: Throwable) {
        throw RepositoryFailure(operation, error)
    }
}
