package goods.pocket.app.data.local

import goods.pocket.app.data.DatabaseDriverFactory
import goods.pocket.app.db.GoodsPocketDatabase
import goods.pocket.app.data.local.model.LocalAppPreferenceRecord
import goods.pocket.app.data.local.model.LocalEventRecord
import goods.pocket.app.data.local.model.LocalItemRecord
import goods.pocket.app.data.local.model.LocalPreorderRecord
import goods.pocket.app.data.local.model.LocalStorageLocationRecord

class SqlDelightGoodsPocketLocalDataSource(
    driverFactory: DatabaseDriverFactory,
) : GoodsPocketLocalDataSource {
    private val database = GoodsPocketDatabase(driverFactory.createDriver())
    private val queries = database.goodsPocketDatabaseQueries

    init {
        seedIfNeeded()
    }

    override fun getItems(filter: String?): List<LocalItemRecord> {
        val rows = if (filter.isNullOrBlank()) {
            queries.selectAllItems().executeAsList()
        } else {
            queries.selectItemsByFilter(filter).executeAsList()
        }
        return rows.map { row ->
            LocalItemRecord(
                id = row.id,
                name = row.name,
                category = row.category,
                status = row.status,
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

    override fun getItem(id: String): LocalItemRecord? {
        return queries.selectItemById(id).executeAsOneOrNull()?.let { row ->
            LocalItemRecord(
                id = row.id,
                name = row.name,
                category = row.category,
                status = row.status,
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

    override fun upsertItem(item: LocalItemRecord) {
        queries.upsertItem(
            id = item.id,
            name = item.name,
            category = item.category,
            status = item.status,
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

    override fun getPreorders(status: String?): List<LocalPreorderRecord> {
        val rows = when (status) {
            null -> queries.selectAllPreorders().executeAsList()
            else -> queries.selectPreordersByStatus(status).executeAsList()
        }
        return rows.map { row ->
            LocalPreorderRecord(
                id = row.id,
                name = row.name,
                storeName = row.store_name,
                releaseDate = row.release_date,
                status = row.status,
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

    override fun getPreorder(id: String): LocalPreorderRecord? {
        return queries.selectPreorderById(id).executeAsOneOrNull()?.let { row ->
            LocalPreorderRecord(
                id = row.id,
                name = row.name,
                storeName = row.store_name,
                releaseDate = row.release_date,
                status = row.status,
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

    override fun upsertPreorder(preorder: LocalPreorderRecord) {
        queries.upsertPreorder(
            id = preorder.id,
            name = preorder.name,
            store_name = preorder.storeName,
            release_date = preorder.releaseDate,
            status = preorder.status,
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

    override fun saveCollectionEntry(
        item: LocalItemRecord?,
        preorder: LocalPreorderRecord?,
        changedAt: String,
    ) {
        require((item == null) != (preorder == null))
        database.transaction {
            if (item != null) {
                upsertItem(item)
                cancelPreorder(preorderId = item.id, canceledAt = changedAt)
            } else if (preorder != null) {
                upsertPreorder(preorder)
                deleteItem(preorder.id)
            }
        }
    }

    override fun markAsReceived(preorderId: String, receiveDate: String) {
        queries.markPreorderReceived(receive_date = receiveDate, updated_at = receiveDate, id = preorderId)
    }

    override fun cancelPreorder(preorderId: String, canceledAt: String) {
        queries.cancelPreorder(updated_at = canceledAt, id = preorderId)
    }

    override fun replacePreorderWithItem(
        preorderId: String,
        item: LocalItemRecord,
        receivedAt: String,
    ) {
        database.transaction {
            upsertItem(item)
            cancelPreorder(preorderId = preorderId, canceledAt = receivedAt)
        }
    }

    override fun countActivePreorders(): Int {
        return queries.countActivePreorders().executeAsOne().toInt()
    }

    override fun getUpcomingEvents(limit: Int): List<LocalEventRecord> {
        return queries.selectUpcomingEvents(limit.toLong()).executeAsList().map { row ->
            LocalEventRecord(
                id = row.id,
                title = row.title,
                eventType = row.event_type,
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

    override fun getEvents(type: String?): List<LocalEventRecord> {
        val rows = when (type) {
            null -> queries.selectAllEvents().executeAsList()
            else -> queries.selectEventsByType(type).executeAsList()
        }
        return rows.map { row ->
            LocalEventRecord(
                id = row.id,
                title = row.title,
                eventType = row.event_type,
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

    override fun upsertEvent(event: LocalEventRecord) {
        queries.upsertEvent(
            id = event.id,
            title = event.title,
            event_type = event.eventType,
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

    override fun getStorageLocations(): List<LocalStorageLocationRecord> {
        return queries.selectAllStorageLocations().executeAsList().map { row ->
            LocalStorageLocationRecord(
                id = row.id,
                name = row.name,
                parentId = row.parent_id,
                memo = row.memo,
                createdAt = row.created_at,
            )
        }
    }

    override fun upsertStorageLocation(location: LocalStorageLocationRecord) {
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

    override fun getAppPreferences(): LocalAppPreferenceRecord {
        return queries.selectAppPreferences().executeAsOneOrNull()?.let { row ->
            LocalAppPreferenceRecord(
                currencyCode = row.currency_code,
                dateFormat = row.date_format,
                languageCode = row.language_code,
            )
        } ?: LocalAppPreferenceRecord()
    }

    override fun updateAppPreferences(preferences: LocalAppPreferenceRecord) {
        queries.upsertAppPreferences(
            currency_code = preferences.currencyCode,
            date_format = preferences.dateFormat,
            language_code = preferences.languageCode,
        )
    }

    private fun seedIfNeeded() {
        val shouldSeed = shouldSeedGoodsPocketDatabase(
            hasItems = queries.selectAllItems().executeAsList().isNotEmpty(),
            hasPreorders = queries.selectAllPreorders().executeAsList().isNotEmpty(),
            hasEvents = queries.selectAllEvents().executeAsList().isNotEmpty(),
            hasStorageLocations = queries.selectAllStorageLocations().executeAsList().isNotEmpty(),
            hasPreferences = queries.selectAppPreferences().executeAsOneOrNull() != null,
        )
        if (!shouldSeed) return

        database.transaction {
            GoodsPocketSeedData.items.forEach(::upsertItem)
            GoodsPocketSeedData.preorders.forEach(::upsertPreorder)
            GoodsPocketSeedData.events.forEach(::upsertEvent)
            GoodsPocketSeedData.storageLocations.forEach(::upsertStorageLocation)
            updateAppPreferences(GoodsPocketSeedData.appPreferences)
        }
    }
}

internal fun shouldSeedGoodsPocketDatabase(
    hasItems: Boolean,
    hasPreorders: Boolean,
    hasEvents: Boolean,
    hasStorageLocations: Boolean,
    hasPreferences: Boolean,
): Boolean {
    return !hasItems && !hasPreorders && !hasEvents && !hasStorageLocations && !hasPreferences
}
