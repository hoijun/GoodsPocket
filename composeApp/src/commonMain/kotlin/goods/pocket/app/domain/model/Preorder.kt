package goods.pocket.app.domain.model

data class Preorder(
    val id: String,
    val name: String,
    val storeName: String,
    val releaseDate: String,
    val status: PreorderStatus,
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

enum class PreorderStatus {
    ACTIVE,
    PAYMENT_PENDING,
    RECEIVED,
    CANCELED,
}
