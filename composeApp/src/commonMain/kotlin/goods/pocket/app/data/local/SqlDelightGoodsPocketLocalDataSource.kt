package goods.pocket.app.data.local

import goods.pocket.app.data.DatabaseDriverFactory
import goods.pocket.app.db.GoodsPocketDatabase
import goods.pocket.app.domain.model.AppPreference
import goods.pocket.app.domain.model.Event
import goods.pocket.app.domain.model.EventType
import goods.pocket.app.domain.model.Item
import goods.pocket.app.domain.model.ItemStatus
import goods.pocket.app.domain.model.Preorder
import goods.pocket.app.domain.model.PreorderStatus
import goods.pocket.app.domain.model.StorageLocation

class SqlDelightGoodsPocketLocalDataSource(
    driverFactory: DatabaseDriverFactory,
) : GoodsPocketLocalDataSource {
    private val database = GoodsPocketDatabase(driverFactory.createDriver())
    private val queries = database.goodsPocketDatabaseQueries

    init {
        seedIfNeeded()
    }

    override fun getItems(filter: String?): List<Item> {
        val rows = if (filter.isNullOrBlank()) {
            queries.selectAllItems().executeAsList()
        } else {
            queries.selectItemsByFilter(filter).executeAsList()
        }
        return rows.map { row ->
            Item(
                id = row.id,
                name = row.name,
                category = row.category,
                status = row.status.toItemStatus(),
                seriesName = row.series_name,
                characterName = row.character_name,
                quantity = row.quantity.toInt(),
                purchasePrice = row.purchase_price,
                purchaseDate = row.purchase_date,
                purchaseStore = row.purchase_store,
                storageLocationId = row.storage_location_id,
                linkedPreorderId = row.linked_preorder_id,
                note = row.note,
                createdAt = row.created_at,
                updatedAt = row.updated_at,
            )
        }
    }

    override fun getItem(id: String): Item? {
        return queries.selectItemById(id).executeAsOneOrNull()?.let { row ->
            Item(
                id = row.id,
                name = row.name,
                category = row.category,
                status = row.status.toItemStatus(),
                seriesName = row.series_name,
                characterName = row.character_name,
                quantity = row.quantity.toInt(),
                purchasePrice = row.purchase_price,
                purchaseDate = row.purchase_date,
                purchaseStore = row.purchase_store,
                storageLocationId = row.storage_location_id,
                linkedPreorderId = row.linked_preorder_id,
                note = row.note,
                createdAt = row.created_at,
                updatedAt = row.updated_at,
            )
        }
    }

    override fun upsertItem(item: Item) {
        queries.upsertItem(
            id = item.id,
            name = item.name,
            category = item.category,
            status = item.status.name,
            series_name = item.seriesName,
            character_name = item.characterName,
            quantity = item.quantity.toLong(),
            purchase_price = item.purchasePrice,
            purchase_date = item.purchaseDate,
            purchase_store = item.purchaseStore,
            storage_location_id = item.storageLocationId,
            linked_preorder_id = item.linkedPreorderId,
            note = item.note,
            created_at = item.createdAt,
            updated_at = item.updatedAt,
        )
    }

    override fun deleteItem(id: String) {
        queries.deleteItemById(id)
    }

    override fun countOwnedItems(): Int {
        return queries.countOwnedItems().executeAsOne().toInt()
    }

    override fun getPreorders(status: PreorderStatus?): List<Preorder> {
        val rows = when (status) {
            null -> queries.selectAllPreorders().executeAsList()
            else -> queries.selectPreordersByStatus(status.name).executeAsList()
        }
        return rows.map { row ->
            Preorder(
                id = row.id,
                name = row.name,
                storeName = row.store_name,
                releaseDate = row.release_date,
                status = row.status.toPreorderStatus(),
                seriesName = row.series_name,
                characterName = row.character_name,
                totalPrice = row.total_price,
                depositPrice = row.deposit_price,
                remainingPrice = row.remaining_price,
                shippingFee = row.shipping_fee,
                orderDate = row.order_date,
                paymentDueDate = row.payment_due_date,
                receiveDate = row.receive_date,
                reservationNumber = row.reservation_number,
                note = row.note,
                createdAt = row.created_at,
                updatedAt = row.updated_at,
            )
        }
    }

    override fun getPreorder(id: String): Preorder? {
        return queries.selectPreorderById(id).executeAsOneOrNull()?.let { row ->
            Preorder(
                id = row.id,
                name = row.name,
                storeName = row.store_name,
                releaseDate = row.release_date,
                status = row.status.toPreorderStatus(),
                seriesName = row.series_name,
                characterName = row.character_name,
                totalPrice = row.total_price,
                depositPrice = row.deposit_price,
                remainingPrice = row.remaining_price,
                shippingFee = row.shipping_fee,
                orderDate = row.order_date,
                paymentDueDate = row.payment_due_date,
                receiveDate = row.receive_date,
                reservationNumber = row.reservation_number,
                note = row.note,
                createdAt = row.created_at,
                updatedAt = row.updated_at,
            )
        }
    }

    override fun upsertPreorder(preorder: Preorder) {
        queries.upsertPreorder(
            id = preorder.id,
            name = preorder.name,
            store_name = preorder.storeName,
            release_date = preorder.releaseDate,
            status = preorder.status.name,
            series_name = preorder.seriesName,
            character_name = preorder.characterName,
            total_price = preorder.totalPrice,
            deposit_price = preorder.depositPrice,
            remaining_price = preorder.remainingPrice,
            shipping_fee = preorder.shippingFee,
            order_date = preorder.orderDate,
            payment_due_date = preorder.paymentDueDate,
            receive_date = preorder.receiveDate,
            reservation_number = preorder.reservationNumber,
            note = preorder.note,
            created_at = preorder.createdAt,
            updated_at = preorder.updatedAt,
        )
    }

    override fun markAsReceived(preorderId: String, receiveDate: String) {
        queries.markPreorderReceived(receive_date = receiveDate, updated_at = receiveDate, id = preorderId)
    }

    override fun cancelPreorder(preorderId: String) {
        queries.cancelPreorder(updated_at = currentTimestamp(), id = preorderId)
    }

    override fun countActivePreorders(): Int {
        return queries.countActivePreorders().executeAsOne().toInt()
    }

    override fun getUpcomingEvents(limit: Int): List<Event> {
        return queries.selectUpcomingEvents(limit.toLong()).executeAsList().map { row ->
            Event(
                id = row.id,
                title = row.title,
                eventType = row.event_type.toEventType(),
                targetDate = row.target_date,
                relatedItemId = row.related_item_id,
                relatedPreorderId = row.related_preorder_id,
                locationOrStore = row.location_or_store,
                memo = row.memo,
                createdAt = row.created_at,
                updatedAt = row.updated_at,
            )
        }
    }

    override fun getEvents(type: EventType?): List<Event> {
        val rows = when (type) {
            null -> queries.selectAllEvents().executeAsList()
            else -> queries.selectEventsByType(type.name).executeAsList()
        }
        return rows.map { row ->
            Event(
                id = row.id,
                title = row.title,
                eventType = row.event_type.toEventType(),
                targetDate = row.target_date,
                relatedItemId = row.related_item_id,
                relatedPreorderId = row.related_preorder_id,
                locationOrStore = row.location_or_store,
                memo = row.memo,
                createdAt = row.created_at,
                updatedAt = row.updated_at,
            )
        }
    }

    override fun upsertEvent(event: Event) {
        queries.upsertEvent(
            id = event.id,
            title = event.title,
            event_type = event.eventType.name,
            target_date = event.targetDate,
            related_item_id = event.relatedItemId,
            related_preorder_id = event.relatedPreorderId,
            location_or_store = event.locationOrStore,
            memo = event.memo,
            created_at = event.createdAt,
            updated_at = event.updatedAt,
        )
    }

    override fun deleteEvent(id: String) {
        queries.deleteEventById(id)
    }

    override fun getStorageLocations(): List<StorageLocation> {
        return queries.selectAllStorageLocations().executeAsList().map { row ->
            StorageLocation(
                id = row.id,
                name = row.name,
                parentId = row.parent_id,
                memo = row.memo,
                createdAt = row.created_at,
            )
        }
    }

    override fun upsertStorageLocation(location: StorageLocation) {
        queries.upsertStorageLocation(
            id = location.id,
            name = location.name,
            parent_id = location.parentId,
            memo = location.memo,
            created_at = location.createdAt,
        )
    }

    override fun deleteStorageLocation(id: String) {
        queries.deleteStorageLocationById(id)
    }

    override fun getAppPreferences(): AppPreference {
        return queries.selectAppPreferences().executeAsOneOrNull()?.let { row ->
            AppPreference(
                currencyCode = row.currency_code,
                dateFormat = row.date_format,
                languageCode = row.language_code,
            )
        } ?: AppPreference()
    }

    override fun updateAppPreferences(preferences: AppPreference) {
        queries.upsertAppPreferences(
            currency_code = preferences.currencyCode,
            date_format = preferences.dateFormat,
            language_code = preferences.languageCode,
        )
    }

    private fun seedIfNeeded() {
        if (queries.selectAllItems().executeAsList().isNotEmpty()) return

        GoodsPocketSeedData.items.forEach(::upsertItem)
        GoodsPocketSeedData.preorders.forEach(::upsertPreorder)
        GoodsPocketSeedData.events.forEach(::upsertEvent)
        GoodsPocketSeedData.storageLocations.forEach(::upsertStorageLocation)
        updateAppPreferences(GoodsPocketSeedData.appPreferences)
        queries.selectAllItems().executeAsList()
        queries.selectAllEvents().executeAsList()
        queries.selectAllStorageLocations().executeAsList()
        queries.selectAppPreferences().executeAsOneOrNull() ?: updateAppPreferences(GoodsPocketSeedData.appPreferences)
    }

    private fun currentTimestamp(): String = GoodsPocketSeedData.defaultTimestamp

    private fun String.toItemStatus(): ItemStatus {
        return when (this) {
            "PLANNED_TRANSFER" -> ItemStatus.PLANNED_CLEANUP
            "WAITING_DELIVERY" -> ItemStatus.OWNED
            "LOST" -> ItemStatus.OWNED
            else -> ItemStatus.valueOf(this)
        }
    }

    private fun String.toPreorderStatus(): PreorderStatus {
        return PreorderStatus.valueOf(this)
    }

    private fun String.toEventType(): EventType {
        return EventType.valueOf(this)
    }
}
