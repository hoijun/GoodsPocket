package goods.pocket.app.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import goods.pocket.app.domain.model.Event
import goods.pocket.app.domain.model.EventType
import goods.pocket.app.presentation.designsystem.GoodsPocketFilterChip
import goods.pocket.app.presentation.designsystem.GoodsPocketSectionCard
import goods.pocket.app.presentation.designsystem.GoodsPocketTonalBadge
import goods.pocket.app.presentation.i18n.localizedLabel
import goods.pocket.app.presentation.i18n.tr
import goodspocket.composeapp.generated.resources.Res
import goodspocket.composeapp.generated.resources.common_all
import goodspocket.composeapp.generated.resources.common_no_linked_place
import goodspocket.composeapp.generated.resources.events_empty_filter
import goodspocket.composeapp.generated.resources.events_row_subtitle

@Composable
fun EventsScreen(
    events: List<Event>,
    selectedType: EventType?,
    onTypeChange: (EventType?) -> Unit,
    onEventClick: (String) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
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
        if (events.isEmpty()) {
            item {
                GoodsPocketSectionCard(containerColor = MaterialTheme.colorScheme.surfaceContainerLow) {
                    Text(
                        text = tr(Res.string.events_empty_filter),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
        items(events, key = Event::id) { event ->
            GoodsPocketSectionCard(
                onClick = { onEventClick(event.id) },
                containerColor = MaterialTheme.colorScheme.surface,
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = event.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                    GoodsPocketTonalBadge(text = event.eventType.localizedLabel())
                }
                Text(
                    text = tr(
                        Res.string.events_row_subtitle,
                        event.eventType.localizedLabel(),
                        event.targetDate,
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                )
                Text(
                    text = event.locationOrStore ?: tr(Res.string.common_no_linked_place),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
