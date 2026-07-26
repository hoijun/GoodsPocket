package goods.pocket.app.domain.usecase

import goods.pocket.app.domain.model.ActivityRecord
import goods.pocket.app.domain.model.HomeSummary
import goods.pocket.app.domain.model.Preorder
import goods.pocket.app.domain.repository.CollectionRepository
import goods.pocket.app.domain.repository.PreorderRepository
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus

class GetDashboardSummaryUseCase(
    private val collectionRepository: CollectionRepository,
    private val preorderRepository: PreorderRepository,
) {
    suspend operator fun invoke(monthFilter: String, recentActivities: List<ActivityRecord>): HomeSummary {
        val spendingRecords = spendingRecords()
        return HomeSummary(
            monthlySpend = spendingRecords.totalFor(monthFilter),
            previousMonthSpend = spendingRecords.totalFor(previousMonth(monthFilter)),
            spendingBuckets = spendingRecords.bucketsFor(monthFilter),
            ownedItemCount = collectionRepository.countOwnedItems(),
            activePreorderCount = preorderRepository.countActivePreorders(),
            recentActivities = recentActivities,
        )
    }

    private suspend fun spendingRecords(): List<SpendingRecord> {
        val preorders = preorderRepository.getPreorders()
        val preorderIds = preorders.mapTo(mutableSetOf(), Preorder::id)
        val itemRecords = collectionRepository.getItems()
            .mapNotNull { item ->
                val date = item.purchaseDate ?: return@mapNotNull null
                if (item.linkedPreorderId in preorderIds) return@mapNotNull null
                SpendingRecord(
                    date = date,
                    amount = item.purchasePrice ?: 0L,
                )
            }
        val preorderRecords = preorders.mapNotNull { preorder ->
            val date = preorder.orderDate ?: return@mapNotNull null
            SpendingRecord(
                date = date,
                amount = preorder.totalPrice ?: 0L,
            )
        }
        return itemRecords + preorderRecords
    }
}

private data class SpendingRecord(
    val date: String,
    val amount: Long,
)

private fun List<SpendingRecord>.totalFor(monthFilter: String): Long {
    return filter { it.date.startsWith(monthFilter) }.sumOf(SpendingRecord::amount)
}

private fun List<SpendingRecord>.bucketsFor(monthFilter: String): List<Long> {
    val buckets = MutableList(SPENDING_BUCKET_COUNT) { 0L }
    filter { it.date.startsWith(monthFilter) }.forEach { record ->
        val date = runCatching { LocalDate.parse(record.date) }.getOrNull() ?: return@forEach
        val bucketIndex = ((date.day - 1) / DAYS_PER_BUCKET)
            .coerceAtMost(SPENDING_BUCKET_COUNT - 1)
        buckets[bucketIndex] += record.amount
    }
    return buckets
}

private fun previousMonth(monthFilter: String): String {
    return LocalDate.parse("$monthFilter-01")
        .minus(DatePeriod(months = 1))
        .toString()
        .take(YEAR_MONTH_LENGTH)
}

private const val SPENDING_BUCKET_COUNT = 9
private const val DAYS_PER_BUCKET = 3
private const val YEAR_MONTH_LENGTH = 7
