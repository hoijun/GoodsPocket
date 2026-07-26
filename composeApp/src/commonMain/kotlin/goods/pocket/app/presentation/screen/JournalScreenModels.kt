package goods.pocket.app.presentation.screen

import goods.pocket.app.domain.model.Event
import goods.pocket.app.domain.model.EventType

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
): EventJournalOverview {
    val visibleEvents = events.visibleBy(selectedType).sortedBy { journalDateKey(it.targetDate) }
    val featuredEvent = visibleEvents.firstOrNull()
    val remainingEvents = visibleEvents.drop(1)

    return EventJournalOverview(
        headlineMonth = featuredEvent?.targetDate?.journalMonthLabel().orEmpty(),
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
