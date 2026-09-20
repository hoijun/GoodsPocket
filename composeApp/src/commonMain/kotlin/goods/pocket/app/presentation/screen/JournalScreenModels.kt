package goods.pocket.app.presentation.screen

import goods.pocket.app.domain.model.Event
import goods.pocket.app.domain.model.EventType
import kotlinx.datetime.LocalDate

internal data class EventJournalOverview(
    val headlineMonth: String,
    val featuredEvent: Event?,
    val timelineEvents: List<Event>,
)

internal enum class MyHubAction {
    SYNC_BACKUP,
    NOTIFICATIONS,
    SETTINGS,
}

internal data class MyHubQuickLinkModel(
    val action: MyHubAction,
    val badgeCount: Int? = null,
)

internal fun buildEventJournalOverview(
    events: List<Event>,
    selectedType: EventType?,
    currentDate: String,
): EventJournalOverview {
    val today = currentDate.journalDateOrNull()
    val visibleEvents = events.visibleBy(selectedType).sortedWith(
        compareBy<Event> { event ->
            val date = event.targetDate.journalDateOrNull()
            when {
                date == null -> 2
                today == null || date >= today -> 0
                else -> 1
            }
        }.thenComparator { first, second ->
            val firstDate = first.targetDate.journalDateOrNull()
            val secondDate = second.targetDate.journalDateOrNull()
            when {
                firstDate == null || secondDate == null -> 0
                today != null && firstDate < today && secondDate < today -> secondDate.compareTo(firstDate)
                else -> firstDate.compareTo(secondDate)
            }
        },
    )
    val featuredEvent = visibleEvents.firstOrNull()
    val remainingEvents = visibleEvents.drop(1)

    return EventJournalOverview(
        headlineMonth = today?.let { "${it.year}.${(it.month.ordinal + 1).toString().padStart(2, '0')}" }.orEmpty(),
        featuredEvent = featuredEvent,
        timelineEvents = remainingEvents,
    )
}

internal fun buildMyHubQuickLinks(): List<MyHubQuickLinkModel> {
    return listOf(
        MyHubQuickLinkModel(action = MyHubAction.SYNC_BACKUP),
        MyHubQuickLinkModel(action = MyHubAction.NOTIFICATIONS),
        MyHubQuickLinkModel(action = MyHubAction.SETTINGS),
    )
}

internal fun List<Event>.visibleBy(selectedType: EventType?): List<Event> {
    return if (selectedType == null) this else filter { it.eventType == selectedType }
}

private fun String.journalDateOrNull(): LocalDate? {
    return try {
        LocalDate.parse(replace('.', '-'))
    } catch (_: IllegalArgumentException) {
        null
    }
}
