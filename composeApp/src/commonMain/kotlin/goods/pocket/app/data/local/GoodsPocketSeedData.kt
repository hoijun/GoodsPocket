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
                name = "호시마치 스이세이 아크릴 스탠드",
                category = "아크릴 스탠드",
                status = ItemStatus.OWNED,
                seriesName = "홀로라이브",
                characterName = "스이세이",
                purchasePrice = 18000,
                purchaseDate = "2026-03-02",
                purchaseStore = "애니메이트",
                storageLocationId = "loc-1",
                createdAt = "2026-03-02",
                updatedAt = "2026-03-02",
            ),
            Item(
                id = "item-2",
                name = "블루 아카이브 아트북",
                category = "도서",
                status = ItemStatus.OWNED,
                seriesName = "블루 아카이브",
                purchasePrice = 32000,
                purchaseDate = "2026-03-06",
                purchaseStore = "교보문고",
                storageLocationId = "loc-2",
                createdAt = "2026-03-06",
                updatedAt = "2026-03-06",
            ),
        )

    val preorders: List<Preorder>
        get() = listOf(
            Preorder(
                id = "pre-1",
                name = "니지산지 애니버서리 배지 세트",
                storeName = "애니플러스 샵",
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
                name = "프로젝트 세카이 한정 태피스트리",
                storeName = "멜론북스",
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
                placeName = "애니메이트",
                createdAt = "2026-03-02",
            ),
            Transaction(
                id = "tx-2",
                type = TransactionType.PURCHASE,
                amount = 32000,
                transactionDate = "2026-03-06",
                relatedItemId = "item-2",
                placeName = "교보문고",
                createdAt = "2026-03-06",
            ),
            Transaction(
                id = "tx-3",
                type = TransactionType.DEPOSIT,
                amount = 10000,
                transactionDate = "2026-03-10",
                relatedPreorderId = "pre-1",
                placeName = "애니플러스 샵",
                createdAt = "2026-03-10",
            ),
        )

    val events: List<Event>
        get() = listOf(
            Event(
                id = "event-1",
                title = "니지산지 잔금 결제",
                eventType = EventType.PAYMENT_DUE,
                targetDate = "2026-03-20",
                relatedPreorderId = "pre-1",
                locationOrStore = "애니플러스 샵",
                createdAt = "2026-03-10",
                updatedAt = "2026-03-10",
            ),
            Event(
                id = "event-2",
                title = "니지산지 발매일",
                eventType = EventType.RELEASE,
                targetDate = "2026-03-28",
                relatedPreorderId = "pre-1",
                locationOrStore = "애니플러스 샵",
                createdAt = "2026-03-10",
                updatedAt = "2026-03-10",
            ),
            Event(
                id = "event-3",
                title = "프로젝트 세카이 잔금 결제",
                eventType = EventType.PAYMENT_DUE,
                targetDate = "2026-03-22",
                relatedPreorderId = "pre-2",
                locationOrStore = "멜론북스",
                createdAt = "2026-03-12",
                updatedAt = "2026-03-12",
            ),
        )

    val storageLocations: List<StorageLocation>
        get() = listOf(
            StorageLocation(
                id = "loc-1",
                name = "유리 장식장",
                createdAt = "2026-02-01",
            ),
            StorageLocation(
                id = "loc-2",
                name = "책장",
                createdAt = "2026-02-05",
            ),
        )

    val appPreferences: AppPreference
        get() = AppPreference()
}
