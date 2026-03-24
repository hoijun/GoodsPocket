package goods.pocket.app.presentation.screen

import goods.pocket.app.domain.model.ActivityRecord
import goods.pocket.app.domain.model.Event
import goods.pocket.app.domain.model.EventType
import goods.pocket.app.domain.model.Preorder
import goods.pocket.app.domain.model.PreorderStatus
import goods.pocket.app.domain.model.Transaction
import goods.pocket.app.domain.model.TransactionType
import goods.pocket.app.presentation.state.MyPageUiModel

internal data class PreorderJournalOverview(
    val visibleCount: Int,
    val pendingPaymentCount: Int,
    val arrivingCount: Int,
    val nextReleaseDate: String?,
    val visibleRemainingTotal: Long,
)

internal data class TransactionJournalOverview(
    val headlineMonth: String,
    val visibleTotalAmount: Long,
    val outgoingTotal: Long,
    val incomingTotal: Long,
    val dayGroups: List<TransactionDayGroup>,
)

internal data class TransactionDayGroup(
    val date: String,
    val transactions: List<Transaction>,
)

internal data class EventJournalOverview(
    val headlineMonth: String,
    val featuredEvent: Event?,
    val secondaryEvents: List<Event>,
    val timelineEvents: List<Event>,
)

internal enum class MyHubAction {
    RECENT_ACTIVITY,
    UPCOMING_EVENTS,
    SPENDING_REPORT,
    SYNC_BACKUP,
    NOTIFICATIONS,
    SETTINGS,
}

internal data class MyHubQuickLinkModel(
    val action: MyHubAction,
    val badgeCount: Int? = null,
)

internal fun buildPreorderJournalOverview(
    preorders: List<Preorder>,
    selectedStatus: PreorderStatus?,
): PreorderJournalOverview {
    val visiblePreorders = preorders.visibleBy(selectedStatus).sortedBy { journalDateKey(it.releaseDate) }

    return PreorderJournalOverview(
        visibleCount = visiblePreorders.size,
        pendingPaymentCount = visiblePreorders.count { it.status == PreorderStatus.PAYMENT_PENDING },
        arrivingCount = visiblePreorders.count {
            it.status == PreorderStatus.ACTIVE || it.status == PreorderStatus.PAYMENT_PENDING
        },
        nextReleaseDate = visiblePreorders.firstOrNull()?.releaseDate,
        visibleRemainingTotal = visiblePreorders.sumOf { it.remainingPrice ?: it.totalPrice ?: 0L },
    )
}

internal fun buildTransactionJournalOverview(
    transactions: List<Transaction>,
    selectedType: TransactionType?,
): TransactionJournalOverview {
    val visibleTransactions = transactions.visibleBy(selectedType).sortedWith(
        compareByDescending<Transaction> { journalDateKey(it.transactionDate) }
            .thenByDescending { journalDateKey(it.createdAt) },
    )
    val groupedByDay = visibleTransactions
        .groupBy(Transaction::transactionDate)
        .toList()
        .sortedByDescending { (date, _) -> journalDateKey(date) }
        .map { (date, entries) -> TransactionDayGroup(date = date, transactions = entries) }

    return TransactionJournalOverview(
        headlineMonth = visibleTransactions.firstOrNull()?.transactionDate?.journalMonthLabel().orEmpty(),
        visibleTotalAmount = visibleTransactions.sumOf(Transaction::amount),
        outgoingTotal = visibleTransactions.filterNot { it.type.isIncoming() }.sumOf(Transaction::amount),
        incomingTotal = visibleTransactions.filter { it.type.isIncoming() }.sumOf(Transaction::amount),
        dayGroups = groupedByDay,
    )
}

internal fun buildEventJournalOverview(
    events: List<Event>,
    selectedType: EventType?,
): EventJournalOverview {
    val visibleEvents = events.visibleBy(selectedType).sortedBy { journalDateKey(it.targetDate) }
    val featuredEvent = visibleEvents.firstOrNull()
    val remainingEvents = visibleEvents.drop(1)

    return EventJournalOverview(
        headlineMonth = featuredEvent?.targetDate?.journalMonthLabel().orEmpty(),
        featuredEvent = featuredEvent,
        secondaryEvents = remainingEvents.take(2),
        timelineEvents = remainingEvents.drop(2),
    )
}

internal fun buildMyHubQuickLinks(
    myPage: MyPageUiModel,
    recentActivities: List<ActivityRecord>,
    upcomingEvents: List<Event>,
): List<MyHubQuickLinkModel> {
    return listOf(
        MyHubQuickLinkModel(
            action = MyHubAction.RECENT_ACTIVITY,
            badgeCount = recentActivities.size.takeIf { it > 0 },
        ),
        MyHubQuickLinkModel(
            action = MyHubAction.UPCOMING_EVENTS,
            badgeCount = upcomingEvents.size.takeIf { it > 0 } ?: myPage.upcomingEventCount.takeIf { it > 0 },
        ),
        MyHubQuickLinkModel(action = MyHubAction.SPENDING_REPORT),
        MyHubQuickLinkModel(action = MyHubAction.SYNC_BACKUP),
        MyHubQuickLinkModel(action = MyHubAction.NOTIFICATIONS),
        MyHubQuickLinkModel(action = MyHubAction.SETTINGS),
    )
}

internal fun List<Preorder>.visibleBy(selectedStatus: PreorderStatus?): List<Preorder> {
    return if (selectedStatus == null) this else filter { it.status == selectedStatus }
}

internal fun List<Transaction>.visibleBy(selectedType: TransactionType?): List<Transaction> {
    return if (selectedType == null) this else filter { it.type == selectedType }
}

internal fun List<Event>.visibleBy(selectedType: EventType?): List<Event> {
    return if (selectedType == null) this else filter { it.eventType == selectedType }
}

private fun TransactionType.isIncoming(): Boolean {
    return this == TransactionType.REFUND || this == TransactionType.TRANSFER_INCOME
}

private fun String.journalMonthLabel(): String {
    val key = journalDateKey(this)
    if (key.year == 0 || key.month == 0) return ""
    return "${key.year}.${key.month.toString().padStart(2, '0')}"
}

private fun journalDateKey(rawValue: String): JournalDateKey {
    val parts = rawValue.split(nonDigitRegex).filter(String::isNotBlank)

    return JournalDateKey(
        year = parts.getOrNull(0)?.toIntOrNull() ?: 0,
        month = parts.getOrNull(1)?.toIntOrNull() ?: 0,
        day = parts.getOrNull(2)?.toIntOrNull() ?: 0,
    )
}

private data class JournalDateKey(
    val year: Int,
    val month: Int,
    val day: Int,
) : Comparable<JournalDateKey> {
    override fun compareTo(other: JournalDateKey): Int {
        return compareValuesBy(this, other, JournalDateKey::year, JournalDateKey::month, JournalDateKey::day)
    }
}

private val nonDigitRegex = Regex("[^0-9]+")
