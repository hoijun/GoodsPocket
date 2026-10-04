package goods.pocket.app.domain.event

data class Event(
    val id: String,
    val title: String,
    val eventType: EventType,
    val targetDate: String,
    val relatedEntryId: String? = null,
    val locationOrStore: String? = null,
    val memo: String? = null,
    val createdAt: String,
    val updatedAt: String,
)

enum class EventType {
    RELEASE,
    PAYMENT_DUE,
    DELIVERY,
    OFFLINE_EVENT,
}
