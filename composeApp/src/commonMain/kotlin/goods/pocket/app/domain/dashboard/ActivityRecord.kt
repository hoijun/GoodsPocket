package goods.pocket.app.domain.dashboard

data class ActivityRecord(
    val id: String,
    val title: String,
    val kind: ActivityKind,
    val happenedAt: String,
    val storeName: String? = null,
    val seriesName: String? = null,
)

enum class ActivityKind {
    ADDED,
    RESERVED,
    RECEIVED,
}
