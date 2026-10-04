package goods.pocket.app.data.local

import goods.pocket.app.db.Collection_entry
import goods.pocket.app.db.GoodsPocketDatabaseQueries
import goods.pocket.app.domain.collection.CollectionEntry
import goods.pocket.app.domain.collection.CollectionEntryStatus
import goods.pocket.app.domain.collection.ReservationDetails

internal fun Collection_entry.toDomain(): CollectionEntry = CollectionEntry(
    id = id,
    name = name,
    category = category,
    status = CollectionEntryStatus.valueOf(status),
    seriesName = series_name,
    characterName = character_name,
    quantity = quantity.toInt(),
    purchasePrice = purchase_price,
    purchaseDate = purchase_date,
    purchaseStore = purchase_store,
    storageLocationId = storage_location_id,
    releaseDate = release_date,
    reservationStore = reservation_store,
    relatedLink = related_link,
    note = note,
    createdAt = created_at,
    updatedAt = updated_at,
    currencyCode = currency_code,
    canceledAt = canceled_at,
    reservation = if (has_reservation == 1L) {
        ReservationDetails(
            order_date,
            total_price,
            deposit_price,
            remaining_price,
            shipping_fee,
            reservation_number,
            received_at,
        )
    } else {
        null
    },
)

internal fun GoodsPocketDatabaseQueries.save(entry: CollectionEntry) {
    if (selectEntry(entry.id).executeAsOneOrNull() == null) {
        upsertEntry(
            Collection_entry(
                id = entry.id,
                name = entry.name,
                category = entry.category,
                status = entry.status.name,
                series_name = entry.seriesName,
                character_name = entry.characterName,
                quantity = entry.quantity.toLong(),
                purchase_price = entry.purchasePrice,
                purchase_date = entry.purchaseDate,
                purchase_store = entry.purchaseStore,
                storage_location_id = entry.storageLocationId,
                release_date = entry.releaseDate,
                reservation_store = entry.reservationStore,
                related_link = entry.relatedLink,
                note = entry.note,
                created_at = entry.createdAt,
                updated_at = entry.updatedAt,
                currency_code = entry.currencyCode,
                canceled_at = entry.canceledAt,
                has_reservation = if (entry.reservation != null) 1 else 0,
                order_date = entry.reservation?.orderDate,
                total_price = entry.reservation?.totalPrice,
                deposit_price = entry.reservation?.depositPrice,
                remaining_price = entry.reservation?.remainingPrice,
                shipping_fee = entry.reservation?.shippingFee,
                reservation_number = entry.reservation?.reservationNumber,
                received_at = entry.reservation?.receivedAt,
            ),
        )
    } else {
        updateEntry(
            id = entry.id,
            name = entry.name,
            category = entry.category,
            status = entry.status.name,
            series_name = entry.seriesName,
            character_name = entry.characterName,
            quantity = entry.quantity.toLong(),
            purchase_price = entry.purchasePrice,
            purchase_date = entry.purchaseDate,
            purchase_store = entry.purchaseStore,
            storage_location_id = entry.storageLocationId,
            release_date = entry.releaseDate,
            reservation_store = entry.reservationStore,
            related_link = entry.relatedLink,
            note = entry.note,
            updated_at = entry.updatedAt,
            currency_code = entry.currencyCode,
            canceled_at = entry.canceledAt,
            has_reservation = if (entry.reservation != null) 1 else 0,
            order_date = entry.reservation?.orderDate,
            total_price = entry.reservation?.totalPrice,
            deposit_price = entry.reservation?.depositPrice,
            remaining_price = entry.reservation?.remainingPrice,
            shipping_fee = entry.reservation?.shippingFee,
            reservation_number = entry.reservation?.reservationNumber,
            received_at = entry.reservation?.receivedAt,
        )
    }
}
