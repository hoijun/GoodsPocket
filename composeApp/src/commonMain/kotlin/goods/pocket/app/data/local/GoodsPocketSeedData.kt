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

internal object GoodsPocketSeedData {
    const val defaultTimestamp = "2026-03-18"

    val items: List<Item>
        get() = listOf(
            Item(
                id = "item-1",
                name = "Hoshimachi Suisei Acrylic Stand",
                category = "Acrylic Stand",
                status = ItemStatus.OWNED,
                seriesName = "Hololive",
                characterName = "Suisei",
                purchasePrice = 18000,
                purchaseDate = "2026-03-02",
                purchaseStore = "Animate",
                storageLocationId = "loc-1",
                createdAt = "2026-03-02",
                updatedAt = "2026-03-02",
            ),
            Item(
                id = "item-2",
                name = "Blue Archive Art Book",
                category = "Book",
                status = ItemStatus.OWNED,
                seriesName = "Blue Archive",
                purchasePrice = 32000,
                purchaseDate = "2026-03-06",
                purchaseStore = "Kyobo",
                storageLocationId = "loc-2",
                createdAt = "2026-03-06",
                updatedAt = "2026-03-06",
            ),
        )

    val preorders: List<Preorder>
        get() = listOf(
            Preorder(
                id = "pre-1",
                name = "Nijisanji Anniversary Badge Set",
                storeName = "Aniplus Shop",
                releaseDate = "2026-03-28",
                status = PreorderStatus.ACTIVE,
                totalPrice = 42000,
                depositPrice = 10000,
                remainingPrice = 32000,
                orderDate = "2026-02-25",
                paymentDueDate = "2026-03-20",
                createdAt = "2026-02-25",
                updatedAt = "2026-03-10",
            ),
            Preorder(
                id = "pre-2",
                name = "Project Sekai Limited Tapestry",
                storeName = "Melonbooks",
                releaseDate = "2026-04-11",
                status = PreorderStatus.PAYMENT_PENDING,
                totalPrice = 56000,
                depositPrice = 20000,
                remainingPrice = 36000,
                orderDate = "2026-03-01",
                paymentDueDate = "2026-03-22",
                createdAt = "2026-03-01",
                updatedAt = "2026-03-12",
            ),
        )

    val transactions: List<Transaction>
        get() = listOf(
            Transaction(
                id = "tx-1",
                type = TransactionType.PURCHASE,
                amount = 18000,
                transactionDate = "2026-03-02",
                relatedItemId = "item-1",
                placeName = "Animate",
                createdAt = "2026-03-02",
            ),
            Transaction(
                id = "tx-2",
                type = TransactionType.PURCHASE,
                amount = 32000,
                transactionDate = "2026-03-06",
                relatedItemId = "item-2",
                placeName = "Kyobo",
                createdAt = "2026-03-06",
            ),
            Transaction(
                id = "tx-3",
                type = TransactionType.DEPOSIT,
                amount = 10000,
                transactionDate = "2026-03-10",
                relatedPreorderId = "pre-1",
                placeName = "Aniplus Shop",
                createdAt = "2026-03-10",
            ),
        )

    val events: List<Event>
        get() = listOf(
            Event(
                id = "event-1",
                title = "Nijisanji balance due",
                eventType = EventType.PAYMENT_DUE,
                targetDate = "2026-03-20",
                relatedPreorderId = "pre-1",
                locationOrStore = "Aniplus Shop",
                createdAt = "2026-03-10",
                updatedAt = "2026-03-10",
            ),
            Event(
                id = "event-2",
                title = "Nijisanji release day",
                eventType = EventType.RELEASE,
                targetDate = "2026-03-28",
                relatedPreorderId = "pre-1",
                locationOrStore = "Aniplus Shop",
                createdAt = "2026-03-10",
                updatedAt = "2026-03-10",
            ),
            Event(
                id = "event-3",
                title = "Project Sekai balance due",
                eventType = EventType.PAYMENT_DUE,
                targetDate = "2026-03-22",
                relatedPreorderId = "pre-2",
                locationOrStore = "Melonbooks",
                createdAt = "2026-03-12",
                updatedAt = "2026-03-12",
            ),
        )

    val storageLocations: List<StorageLocation>
        get() = listOf(
            StorageLocation(
                id = "loc-1",
                name = "Glass Cabinet",
                createdAt = "2026-02-01",
            ),
            StorageLocation(
                id = "loc-2",
                name = "Book Shelf",
                createdAt = "2026-02-05",
            ),
        )

    val appPreferences: AppPreference
        get() = AppPreference()
}
