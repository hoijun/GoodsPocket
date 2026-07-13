package goods.pocket.app.data.local.model

data class LocalItemRecord(
    val id: String,
    val name: String,
    val category: String,
    val status: String,
    val seriesName: String? = null,
    val characterName: String? = null,
    val quantity: Int = 1,
    val purchasePrice: Long? = null,
    val purchaseDate: String? = null,
    val purchaseStore: String? = null,
    val storageLocationId: String? = null,
    val linkedPreorderId: String? = null,
    val note: String? = null,
    val createdAt: String,
    val updatedAt: String,
)

data class LocalPreorderRecord(
    val id: String,
    val name: String,
    val storeName: String,
    val releaseDate: String,
    val status: String,
    val seriesName: String? = null,
    val characterName: String? = null,
    val totalPrice: Long? = null,
    val depositPrice: Long? = null,
    val remainingPrice: Long? = null,
    val shippingFee: Long? = null,
    val orderDate: String? = null,
    val paymentDueDate: String? = null,
    val receiveDate: String? = null,
    val reservationNumber: String? = null,
    val note: String? = null,
    val createdAt: String,
    val updatedAt: String,
)

data class LocalEventRecord(
    val id: String,
    val title: String,
    val eventType: String,
    val targetDate: String,
    val relatedItemId: String? = null,
    val relatedPreorderId: String? = null,
    val locationOrStore: String? = null,
    val memo: String? = null,
    val createdAt: String,
    val updatedAt: String,
)

data class LocalStorageLocationRecord(
    val id: String,
    val name: String,
    val parentId: String? = null,
    val memo: String? = null,
    val createdAt: String,
)

data class LocalAppPreferenceRecord(
    val currencyCode: String = "KRW",
    val dateFormat: String = "yyyy-MM-dd",
    val languageCode: String = "ko",
)
