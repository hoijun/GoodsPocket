package goods.pocket.app.presentation.screen

import goods.pocket.app.domain.model.ActivityRecord
import goods.pocket.app.domain.model.Event
import goods.pocket.app.domain.model.EventType
import goods.pocket.app.domain.model.Preorder
import goods.pocket.app.domain.model.PreorderStatus
import goods.pocket.app.domain.model.Transaction
import goods.pocket.app.domain.model.TransactionType
import goods.pocket.app.presentation.state.MyPageUiModel
import kotlin.test.Test
import kotlin.test.assertEquals

class JournalScreenModelsTest {

    @Test
    fun `preorder journal overview summarizes the currently visible entries`() {
        val overview = buildPreorderJournalOverview(
            preorders = listOf(
                preorder(
                    id = "active",
                    status = PreorderStatus.ACTIVE,
                    releaseDate = "2024.06.21",
                    remainingPrice = 12000,
                ),
                preorder(
                    id = "pending",
                    status = PreorderStatus.PAYMENT_PENDING,
                    releaseDate = "2024.05.28",
                    remainingPrice = 34000,
                ),
                preorder(
                    id = "received",
                    status = PreorderStatus.RECEIVED,
                    releaseDate = "2024.05.12",
                    remainingPrice = 0,
                ),
            ),
            selectedStatus = null,
        )

        assertEquals(3, overview.visibleCount)
        assertEquals(1, overview.pendingPaymentCount)
        assertEquals(2, overview.arrivingCount)
        assertEquals("2024.05.12", overview.nextReleaseDate)
        assertEquals(46000, overview.visibleRemainingTotal)
    }

    @Test
    fun `transaction journal overview groups visible entries by date and direction`() {
        val overview = buildTransactionJournalOverview(
            transactions = listOf(
                transaction(
                    id = "purchase",
                    type = TransactionType.PURCHASE,
                    amount = 42000,
                    date = "2024.05.28",
                ),
                transaction(
                    id = "refund",
                    type = TransactionType.REFUND,
                    amount = 6000,
                    date = "2024.05.28",
                ),
                transaction(
                    id = "shipping",
                    type = TransactionType.SHIPPING,
                    amount = 3000,
                    date = "2024.05.20",
                ),
                transaction(
                    id = "income",
                    type = TransactionType.TRANSFER_INCOME,
                    amount = 15000,
                    date = "2024.05.29",
                ),
            ),
            selectedType = null,
        )

        assertEquals(66000, overview.visibleTotalAmount)
        assertEquals(45000, overview.outgoingTotal)
        assertEquals(21000, overview.incomingTotal)
        assertEquals(listOf("2024.05.29", "2024.05.28", "2024.05.20"), overview.dayGroups.map { it.date })
        assertEquals(listOf("income"), overview.dayGroups.first().transactions.map(Transaction::id))
        assertEquals(listOf("purchase", "refund"), overview.dayGroups[1].transactions.map(Transaction::id))
    }

    @Test
    fun `event journal overview promotes the nearest event into the highlight slot`() {
        val overview = buildEventJournalOverview(
            events = listOf(
                event(
                    id = "delivery",
                    type = EventType.DELIVERY,
                    date = "2024.06.02",
                ),
                event(
                    id = "release",
                    type = EventType.RELEASE,
                    date = "2024.05.28",
                ),
                event(
                    id = "offline",
                    type = EventType.OFFLINE_EVENT,
                    date = "2024.05.31",
                ),
                event(
                    id = "payment",
                    type = EventType.PAYMENT_DUE,
                    date = "2024.05.20",
                ),
            ),
            selectedType = null,
        )

        assertEquals("2024.05", overview.headlineMonth)
        assertEquals("payment", overview.featuredEvent?.id)
        assertEquals(listOf("release", "offline"), overview.secondaryEvents.map(Event::id))
        assertEquals(listOf("delivery"), overview.timelineEvents.map(Event::id))
    }

    @Test
    fun `my hub quick links keep activity shortcuts ahead of account management shortcuts`() {
        val links = buildMyHubQuickLinks(
            myPage = MyPageUiModel(
                monthlySpend = 98000,
                upcomingEventCount = 3,
            ),
            recentActivities = listOf(
                ActivityRecord("1", "recent-1", "subtitle", "2024.05.18"),
                ActivityRecord("2", "recent-2", "subtitle", "2024.05.12"),
            ),
            upcomingEvents = listOf(
                event(id = "event-1", type = EventType.RELEASE, date = "2024.05.20"),
            ),
        )

        assertEquals(
            listOf(
                MyHubAction.RECENT_ACTIVITY,
                MyHubAction.UPCOMING_EVENTS,
                MyHubAction.SPENDING_REPORT,
                MyHubAction.SYNC_BACKUP,
                MyHubAction.NOTIFICATIONS,
                MyHubAction.SETTINGS,
            ),
            links.map(MyHubQuickLinkModel::action),
        )
        assertEquals(2, links.first().badgeCount)
        assertEquals(1, links[1].badgeCount)
    }

    private fun preorder(
        id: String,
        status: PreorderStatus,
        releaseDate: String,
        remainingPrice: Long,
    ) = Preorder(
        id = id,
        name = id,
        storeName = "AmiAmi",
        releaseDate = releaseDate,
        status = status,
        remainingPrice = remainingPrice,
        createdAt = "2024.05.01",
        updatedAt = "2024.05.01",
    )

    private fun transaction(
        id: String,
        type: TransactionType,
        amount: Long,
        date: String,
    ) = Transaction(
        id = id,
        type = type,
        amount = amount,
        transactionDate = date,
        createdAt = date,
    )

    private fun event(
        id: String,
        type: EventType,
        date: String,
    ) = Event(
        id = id,
        title = id,
        eventType = type,
        targetDate = date,
        createdAt = date,
        updatedAt = date,
    )
}
