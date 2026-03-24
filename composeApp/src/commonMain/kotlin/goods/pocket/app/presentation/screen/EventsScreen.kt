package goods.pocket.app.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import goods.pocket.app.domain.model.Event
import goods.pocket.app.domain.model.EventType
import goods.pocket.app.presentation.designsystem.GoodsPocketFilterChip
import goods.pocket.app.presentation.designsystem.GoodsPocketListRow
import goods.pocket.app.presentation.designsystem.GoodsPocketSectionCard
import goods.pocket.app.presentation.designsystem.GoodsPocketSectionHeader
import goods.pocket.app.presentation.designsystem.GoodsPocketTonalBadge
import goods.pocket.app.presentation.designsystem.goodsPocketScreenModifier
import goods.pocket.app.presentation.i18n.localizedLabel
import goods.pocket.app.presentation.i18n.tr
import goodspocket.composeapp.generated.resources.Res
import goodspocket.composeapp.generated.resources.common_all
import goodspocket.composeapp.generated.resources.common_no_linked_place
import goodspocket.composeapp.generated.resources.events_empty_filter
import goodspocket.composeapp.generated.resources.events_highlight
import goodspocket.composeapp.generated.resources.events_timeline
import goodspocket.composeapp.generated.resources.home_upcoming

@Composable
fun EventsScreen(
    events: List<Event>,
    selectedType: EventType?,
    onTypeChange: (EventType?) -> Unit,
    onEventClick: (String) -> Unit,
) {
    val overview = buildEventJournalOverview(
        events = events,
        selectedType = selectedType,
    )
    val visibleCount = listOfNotNull(overview.featuredEvent).size +
        overview.secondaryEvents.size +
        overview.timelineEvents.size

    LazyColumn(
        modifier = goodsPocketScreenModifier(),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        item {
            GoodsPocketSectionCard(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                contentColor = MaterialTheme.colorScheme.onSurface,
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = overview.headlineMonth.ifBlank { tr(Res.string.home_upcoming) },
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Text(
                            text = tr(Res.string.events_highlight),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                    GoodsPocketTonalBadge(
                        text = visibleCount.toString(),
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
            }
        }
        item {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                GoodsPocketFilterChip(
                    selected = selectedType == null,
                    onClick = { onTypeChange(null) },
                    label = tr(Res.string.common_all),
                )
                listOf(
                    EventType.RELEASE,
                    EventType.PAYMENT_DUE,
                    EventType.DELIVERY,
                    EventType.OFFLINE_EVENT,
                ).forEach { type ->
                    GoodsPocketFilterChip(
                        selected = selectedType == type,
                        onClick = { onTypeChange(type) },
                        label = type.localizedLabel(),
                    )
                }
            }
        }
        val featuredEvent = overview.featuredEvent
        if (featuredEvent == null) {
            item {
                GoodsPocketSectionCard(containerColor = MaterialTheme.colorScheme.surfaceContainerLow) {
                    Text(
                        text = tr(Res.string.events_empty_filter),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        } else {
            item {
                FeaturedEventCard(
                    event = featuredEvent,
                    onClick = { onEventClick(featuredEvent.id) },
                )
            }
            if (overview.secondaryEvents.isNotEmpty()) {
                item {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        overview.secondaryEvents.forEach { event ->
                            SecondaryEventCard(
                                event = event,
                                onClick = { onEventClick(event.id) },
                            )
                        }
                    }
                }
            }
            if (overview.timelineEvents.isNotEmpty()) {
                item {
                    GoodsPocketSectionCard(containerColor = MaterialTheme.colorScheme.surface) {
                        GoodsPocketSectionHeader(title = tr(Res.string.events_timeline))
                        overview.timelineEvents.forEach { event ->
                            GoodsPocketListRow(
                                title = event.title,
                                subtitle = event.locationOrStore ?: tr(Res.string.common_no_linked_place),
                                trailing = event.targetDate,
                                onClick = { onEventClick(event.id) },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FeaturedEventCard(
    event: Event,
    onClick: () -> Unit,
) {
    GoodsPocketSectionCard(
        onClick = onClick,
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
        ) {
            GoodsPocketTonalBadge(
                text = event.eventType.localizedLabel(),
                containerColor = if (event.eventType == EventType.PAYMENT_DUE) {
                    MaterialTheme.colorScheme.tertiaryContainer
                } else {
                    MaterialTheme.colorScheme.secondaryContainer
                },
                contentColor = if (event.eventType == EventType.PAYMENT_DUE) {
                    MaterialTheme.colorScheme.onTertiaryContainer
                } else {
                    MaterialTheme.colorScheme.onSecondaryContainer
                },
            )
            Text(
                text = event.targetDate,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Text(
            text = event.title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
        )
        Text(
            text = event.locationOrStore ?: tr(Res.string.common_no_linked_place),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun SecondaryEventCard(
    event: Event,
    onClick: () -> Unit,
) {
    GoodsPocketSectionCard(
        modifier = Modifier.widthIn(min = 150.dp),
        onClick = onClick,
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        Text(
            text = event.eventType.localizedLabel(),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = event.title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
        )
        Text(
            text = event.targetDate,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
