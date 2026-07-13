package goods.pocket.app.domain.model

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
    val note: String? = null,
    val createdAt: String,
    val updatedAt: String,
)

enum class CollectionEntryStatus {
    RESERVED,
    OWNED,
    PLANNED_CLEANUP,
}

const val RESERVED_COLLECTION_CATEGORY_CODE = "reserved"
const val GOODS_COLLECTION_CATEGORY_CODE = "goods"
