package goods.pocket.app.domain.collection

data class CollectionEntry(
    val id: String,
    val name: String,
    val category: String,
    val status: CollectionEntryStatus,
    val seriesName: String? = null,
    val characterName: String? = null,
    val quantity: Int = 1,
    val purchasePrice: Long? = null,
    val purchaseDate: String? = null,
    val purchaseStore: String? = null,
    val storageLocationId: String? = null,
    val releaseDate: String? = null,
    val reservationStore: String? = null,
    val relatedLink: String? = null,
    val note: String? = null,
    val createdAt: String,
    val updatedAt: String,
    val reservation: ReservationDetails? = null,
    val canceledAt: String? = null,
    val currencyCode: String = "KRW",
)

data class ReservationDetails(
    val orderDate: String? = null,
    val totalPrice: Long? = null,
    val depositPrice: Long? = null,
    val remainingPrice: Long? = null,
    val shippingFee: Long? = null,
    val reservationNumber: String? = null,
    val receivedAt: String? = null,
)

enum class CollectionEntryStatus {
    RESERVED,
    OWNED,
    PLANNED_CLEANUP,
}

const val RESERVED_COLLECTION_CATEGORY_CODE: String = "reserved"
const val GOODS_COLLECTION_CATEGORY_CODE: String = "goods"
