package goods.pocket.app.data.local

import goods.pocket.app.data.local.model.LocalAppPreferenceRecord
import goods.pocket.app.data.local.model.LocalEventRecord
import goods.pocket.app.data.local.model.LocalItemRecord
import goods.pocket.app.data.local.model.LocalPreorderRecord
import goods.pocket.app.data.local.model.LocalStorageLocationRecord

internal object GoodsPocketSeedData {
    val items: List<LocalItemRecord>
        get() = listOf(
            LocalItemRecord(
                id = "item-1",
                name = "호시마치 스이세이 아크릴 스탠드",
                category = "아크릴 스탠드",
                status = "OWNED",
                seriesName = "홀로라이브",
                characterName = "스이세이",
                purchasePrice = 18000,
                purchaseDate = "2026-03-02",
                purchaseStore = "애니메이트",
                storageLocationId = "loc-1",
                createdAt = "2026-03-02",
                updatedAt = "2026-03-02",
            ),
            LocalItemRecord(
                id = "item-2",
                name = "블루 아카이브 아트북",
                category = "도서",
                status = "OWNED",
                seriesName = "블루 아카이브",
                purchasePrice = 32000,
                purchaseDate = "2026-03-06",
                purchaseStore = "교보문고",
                storageLocationId = "loc-2",
                createdAt = "2026-03-06",
                updatedAt = "2026-03-06",
            ),
        )

    val preorders: List<LocalPreorderRecord>
        get() = listOf(
            LocalPreorderRecord(
                id = "pre-1",
                name = "니지산지 애니버서리 배지 세트",
                storeName = "애니플러스 샵",
                releaseDate = "2026-03-28",
                status = "ACTIVE",
                totalPrice = 42000,
                depositPrice = 10000,
                remainingPrice = 32000,
                orderDate = "2026-02-25",
                paymentDueDate = "2026-03-20",
                createdAt = "2026-02-25",
                updatedAt = "2026-03-10",
            ),
            LocalPreorderRecord(
                id = "pre-2",
                name = "프로젝트 세카이 한정 태피스트리",
                storeName = "멜론북스",
                releaseDate = "2026-04-11",
                status = "PAYMENT_PENDING",
                totalPrice = 56000,
                depositPrice = 20000,
                remainingPrice = 36000,
                orderDate = "2026-03-01",
                paymentDueDate = "2026-03-22",
                createdAt = "2026-03-01",
                updatedAt = "2026-03-12",
            ),
        )

    val events: List<LocalEventRecord>
        get() = listOf(
            LocalEventRecord(
                id = "event-1",
                title = "니지산지 잔금 결제",
                eventType = "PAYMENT_DUE",
                targetDate = "2026-03-20",
                relatedPreorderId = "pre-1",
                locationOrStore = "애니플러스 샵",
                createdAt = "2026-03-10",
                updatedAt = "2026-03-10",
            ),
            LocalEventRecord(
                id = "event-2",
                title = "니지산지 발매일",
                eventType = "RELEASE",
                targetDate = "2026-03-28",
                relatedPreorderId = "pre-1",
                locationOrStore = "애니플러스 샵",
                createdAt = "2026-03-10",
                updatedAt = "2026-03-10",
            ),
            LocalEventRecord(
                id = "event-3",
                title = "프로젝트 세카이 잔금 결제",
                eventType = "PAYMENT_DUE",
                targetDate = "2026-03-22",
                relatedPreorderId = "pre-2",
                locationOrStore = "멜론북스",
                createdAt = "2026-03-12",
                updatedAt = "2026-03-12",
            ),
        )

    val storageLocations: List<LocalStorageLocationRecord>
        get() = listOf(
            LocalStorageLocationRecord(
                id = "loc-1",
                name = "유리 장식장",
                createdAt = "2026-02-01",
            ),
            LocalStorageLocationRecord(
                id = "loc-2",
                name = "책장",
                createdAt = "2026-02-05",
            ),
        )

    val appPreferences: LocalAppPreferenceRecord
        get() = LocalAppPreferenceRecord()
}
