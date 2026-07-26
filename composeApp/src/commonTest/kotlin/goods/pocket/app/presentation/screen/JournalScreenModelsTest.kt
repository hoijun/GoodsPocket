package goods.pocket.app.presentation.screen

import goods.pocket.app.domain.model.Event
import goods.pocket.app.domain.model.EventType
import kotlin.test.Test
import kotlin.test.assertEquals

class JournalScreenModelsTest {

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
        assertEquals(
            listOf("release", "offline", "delivery"),
            overview.timelineEvents.map(Event::id),
        )
    }

    @Test
    fun `my hub quick links keep activity shortcuts ahead of account management shortcuts`() {
        val links = buildMyHubQuickLinks()

        assertEquals(
            listOf(
                MyHubAction.SYNC_BACKUP,
                MyHubAction.NOTIFICATIONS,
                MyHubAction.SETTINGS,
            ),
            links.map(MyHubQuickLinkModel::action),
        )
        assertEquals(null, links.first().badgeCount)
        assertEquals(null, links[1].badgeCount)
    }

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
