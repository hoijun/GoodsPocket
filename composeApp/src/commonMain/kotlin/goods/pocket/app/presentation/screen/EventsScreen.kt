package goods.pocket.app.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import goods.pocket.app.domain.model.Event
import goods.pocket.app.domain.model.EventType
import goods.pocket.app.presentation.designsystem.GoodsPocketVisualTokens
import goods.pocket.app.presentation.i18n.formatDate
import goods.pocket.app.presentation.i18n.localizedLabel
import goods.pocket.app.presentation.i18n.tr
import goodspocket.composeapp.generated.resources.Res
import goodspocket.composeapp.generated.resources.common_all
import goodspocket.composeapp.generated.resources.common_no_linked_place
import goodspocket.composeapp.generated.resources.events_empty_filter
import goodspocket.composeapp.generated.resources.events_highlight
import goodspocket.composeapp.generated.resources.events_timeline
import goodspocket.composeapp.generated.resources.nav_events

@Composable
fun EventsScreen(
    events: List<Event>,
    selectedType: EventType?,
    onTypeChange: (EventType?) -> Unit,
    onEventClick: (String) -> Unit,
) {
    val overview = remember(events, selectedType) {
        buildEventJournalOverview(events, selectedType)
    }
    val filters = listOf<EventType?>(
        null,
        EventType.RELEASE,
        EventType.PAYMENT_DUE,
        EventType.DELIVERY,
        EventType.OFFLINE_EVENT,
    )
    val filterLabels = filters.map { type ->
        type?.localizedLabel() ?: tr(Res.string.common_all)
    }
    val visibleCount = remember(events, selectedType) {
        events.visibleBy(selectedType).size
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(GoodsPocketVisualTokens.Background)),
        contentPadding = PaddingValues(bottom = EventsReferenceMetrics.BottomContentPadding),
    ) {
        item {
            EventsHeader(
                pageTitle = tr(Res.string.nav_events),
                monthLabel = overview.headlineMonth,
                overviewTitle = tr(Res.string.events_highlight),
                count = visibleCount.toString(),
                filterLabels = filterLabels,
                selectedFilterIndex = filters.indexOf(selectedType),
                onFilterSelected = { index -> onTypeChange(filters[index]) },
            )
        }

        val featuredEvent = overview.featuredEvent
        if (featuredEvent == null) {
            item {
                EventsEmptyCard(message = tr(Res.string.events_empty_filter))
            }
        } else {
            item {
                EventsFeaturedCard(
                    event = featuredEvent,
                    date = formatDate(featuredEvent.targetDate),
                    typeLabel = featuredEvent.eventType.localizedLabel(),
                    location = featuredEvent.locationOrStore
                        ?: tr(Res.string.common_no_linked_place),
                    onClick = { onEventClick(featuredEvent.id) },
                )
            }

            if (overview.timelineEvents.isNotEmpty()) {
                item {
                    EventsTimelineSection(
                        title = tr(Res.string.events_timeline),
                        events = overview.timelineEvents,
                        missingLocation = tr(Res.string.common_no_linked_place),
                        onEventClick = onEventClick,
                    )
                }
            }
        }
    }
}
