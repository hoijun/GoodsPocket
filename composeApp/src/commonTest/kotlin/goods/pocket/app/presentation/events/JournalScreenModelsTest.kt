package goods.pocket.app.presentation.events

import goods.pocket.app.domain.event.Event
import goods.pocket.app.domain.event.EventType
import goods.pocket.app.presentation.my.MyHubAction
import goods.pocket.app.presentation.my.MyHubQuickLinkModel
import goods.pocket.app.presentation.my.buildMyHubQuickLinks
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
            currentDate = "2024-05-21",
        )

        assertEquals("2024.05", overview.headlineMonth)
        assertEquals("release", overview.featuredEvent?.id)
        assertEquals(
            listOf("offline", "delivery", "payment"),
            overview.timelineEvents.map(Event::id),
        )
    }

    @Test
    fun `all past events promote the latest valid date before malformed dates`() {
        val overview = buildEventJournalOverview(
            events = listOf(
                event("invalid", EventType.RELEASE, "2026-02-30"),
                event("older", EventType.RELEASE, "2026-06-01"),
                event("latest", EventType.RELEASE, "2026-07-20"),
                event("unknown", EventType.RELEASE, "unknown"),
            ),
            selectedType = null,
            currentDate = "2026-07-26",
        )
        assertEquals("latest", overview.featuredEvent?.id)
        assertEquals(listOf("older", "invalid", "unknown"), overview.timelineEvents.map(Event::id))
        assertEquals("2026.07", overview.headlineMonth)
    }

    @Test
    fun `type filter is applied before choosing todays highlight`() {
        val overview = buildEventJournalOverview(
            events = listOf(
                event("other", EventType.DELIVERY, "2026-07-26"),
                event("today", EventType.RELEASE, "2026-07-26"),
                event("next", EventType.RELEASE, "2026-07-27"),
            ),
            selectedType = EventType.RELEASE,
            currentDate = "2026-07-26",
        )
        assertEquals("today", overview.featuredEvent?.id)
        assertEquals(listOf("next"), overview.timelineEvents.map(Event::id))
    }

    @Test
    fun `empty and unmatched filters preserve current month`() {
        for (events in listOf(
            emptyList(),
            listOf(event("other", EventType.DELIVERY, "2026-08-01")),
        )) {
            val overview = buildEventJournalOverview(events, EventType.RELEASE, "2026-07-26")
            assertEquals(null, overview.featuredEvent)
            assertEquals(emptyList(), overview.timelineEvents)
            assertEquals("2026.07", overview.headlineMonth)
        }
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

    private fun event(id: String, type: EventType, date: String) = Event(
        id = id,
        title = id,
        eventType = type,
        targetDate = date,
        createdAt = date,
        updatedAt = date,
    )
}
