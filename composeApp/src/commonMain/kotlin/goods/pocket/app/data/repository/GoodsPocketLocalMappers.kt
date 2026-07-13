package goods.pocket.app.data.repository

import goods.pocket.app.data.local.model.LocalAppPreferenceRecord
import goods.pocket.app.data.local.model.LocalEventRecord
import goods.pocket.app.data.local.model.LocalItemRecord
import goods.pocket.app.data.local.model.LocalPreorderRecord
import goods.pocket.app.domain.model.AppPreference
import goods.pocket.app.domain.model.Event
import goods.pocket.app.domain.model.EventType
import goods.pocket.app.domain.model.Item
import goods.pocket.app.domain.model.ItemStatus
import goods.pocket.app.domain.model.Preorder
import goods.pocket.app.domain.model.PreorderStatus

internal fun LocalItemRecord.toDomain(): Item {
    return Item(
        id = id,
        name = name,
        category = category,
        status = status.toItemStatus(),
        seriesName = seriesName,
        characterName = characterName,
        quantity = quantity,
        purchasePrice = purchasePrice,
        purchaseDate = purchaseDate,
        purchaseStore = purchaseStore,
        storageLocationId = storageLocationId,
        linkedPreorderId = linkedPreorderId,
        note = note,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}

internal fun Item.toLocal(): LocalItemRecord {
    return LocalItemRecord(
        id = id,
        name = name,
        category = category,
        status = status.name,
        seriesName = seriesName,
        characterName = characterName,
        quantity = quantity,
        purchasePrice = purchasePrice,
        purchaseDate = purchaseDate,
        purchaseStore = purchaseStore,
        storageLocationId = storageLocationId,
        linkedPreorderId = linkedPreorderId,
        note = note,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}

internal fun LocalPreorderRecord.toDomain(): Preorder {
    return Preorder(
        id = id,
        name = name,
        storeName = storeName,
        releaseDate = releaseDate,
        status = PreorderStatus.valueOf(status),
        seriesName = seriesName,
        characterName = characterName,
        totalPrice = totalPrice,
        depositPrice = depositPrice,
        remainingPrice = remainingPrice,
        shippingFee = shippingFee,
        orderDate = orderDate,
        paymentDueDate = paymentDueDate,
        receiveDate = receiveDate,
        reservationNumber = reservationNumber,
        note = note,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}

internal fun Preorder.toLocal(): LocalPreorderRecord {
    return LocalPreorderRecord(
        id = id,
        name = name,
        storeName = storeName,
        releaseDate = releaseDate,
        status = status.name,
        seriesName = seriesName,
        characterName = characterName,
        totalPrice = totalPrice,
        depositPrice = depositPrice,
        remainingPrice = remainingPrice,
        shippingFee = shippingFee,
        orderDate = orderDate,
        paymentDueDate = paymentDueDate,
        receiveDate = receiveDate,
        reservationNumber = reservationNumber,
        note = note,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}

internal fun LocalEventRecord.toDomain(): Event {
    return Event(
        id = id,
        title = title,
        eventType = EventType.valueOf(eventType),
        targetDate = targetDate,
        relatedItemId = relatedItemId,
        relatedPreorderId = relatedPreorderId,
        locationOrStore = locationOrStore,
        memo = memo,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}

internal fun Event.toLocal(): LocalEventRecord {
    return LocalEventRecord(
        id = id,
        title = title,
        eventType = eventType.name,
        targetDate = targetDate,
        relatedItemId = relatedItemId,
        relatedPreorderId = relatedPreorderId,
        locationOrStore = locationOrStore,
        memo = memo,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}

internal fun LocalAppPreferenceRecord.toDomain(): AppPreference {
    return AppPreference(
        currencyCode = currencyCode,
        dateFormat = dateFormat,
        languageCode = languageCode,
    )
}

internal fun AppPreference.toLocal(): LocalAppPreferenceRecord {
    return LocalAppPreferenceRecord(
        currencyCode = currencyCode,
        dateFormat = dateFormat,
        languageCode = languageCode,
    )
}

private fun String.toItemStatus(): ItemStatus {
    return when (this) {
        "PLANNED_TRANSFER" -> ItemStatus.PLANNED_CLEANUP
        "WAITING_DELIVERY", "LOST" -> ItemStatus.OWNED
        else -> ItemStatus.valueOf(this)
    }
}
