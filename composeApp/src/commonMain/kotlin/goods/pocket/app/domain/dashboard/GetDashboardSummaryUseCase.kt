package goods.pocket.app.domain.dashboard

import goods.pocket.app.domain.collection.CollectionEntry
import goods.pocket.app.domain.collection.CollectionEntryStatus
import goods.pocket.app.domain.collection.CollectionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus

class GetDashboardSummaryUseCase(private val collectionRepository: CollectionRepository) {
    suspend operator fun invoke(
        monthFilter: String,
        recentActivities: List<ActivityRecord> = emptyList(),
    ): HomeSummary = calculate(collectionRepository.getAllEntries(), monthFilter, recentActivities)

    fun observe(monthFilter: String): Flow<HomeSummary> =
        collectionRepository.observeAllEntries().map { entries ->
            calculate(entries, monthFilter, GetRecentActivitiesUseCase.calculate(entries, 5))
        }

    fun calculate(
        entries: List<CollectionEntry>,
        monthFilter: String,
        recentActivities: List<ActivityRecord> = emptyList(),
    ): HomeSummary = Companion.calculate(entries, monthFilter, recentActivities)

    companion object {
        fun calculate(
            entries: List<CollectionEntry>,
            monthFilter: String,
            recentActivities: List<ActivityRecord> = emptyList(),
        ): HomeSummary {
            val previousMonth = LocalDate.parse(
                "$monthFilter-01",
            ).minus(DatePeriod(months = 1)).toString().take(7)
            val records = entries.mapNotNull { entry ->
                val date = if (entry.reservation !=
                    null
                ) {
                    entry.reservation.orderDate
                } else {
                    entry.purchaseDate
                }
                val amount = if (entry.reservation !=
                    null
                ) {
                    entry.reservation.totalPrice
                } else {
                    entry.purchasePrice
                }
                date?.let { SpendingRecord(it, amount ?: 0L) }
            }
            val buckets = MutableList(9) { 0L }
            records.filter { it.date.startsWith(monthFilter) }.forEach { record ->
                val date = LocalDate.parse(record.date)
                val index = ((date.day - 1) / 3).coerceAtMost(8)
                buckets[index] += record.amount
            }
            val visible = entries.filter { it.canceledAt == null }
            return HomeSummary(
                monthlySpend = records.filter {
                    it.date.startsWith(monthFilter)
                }.sumOf { it.amount },
                previousMonthSpend = records.filter {
                    it.date.startsWith(previousMonth)
                }.sumOf { it.amount },
                spendingBuckets = buckets,
                ownedItemCount = visible.count { it.status == CollectionEntryStatus.OWNED },
                activePreorderCount = visible.count { it.status == CollectionEntryStatus.RESERVED },
                recentActivities = recentActivities,
            )
        }
    }
}

private data class SpendingRecord(val date: String, val amount: Long)
