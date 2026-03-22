package goods.pocket.app.domain.model

data class Item(
    val id: String,
    val name: String,
    val category: String,
    val status: ItemStatus,
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

enum class ItemStatus {
    OWNED,
    WAITING_DELIVERY,
    PLANNED_TRANSFER,
    LOST,
}
