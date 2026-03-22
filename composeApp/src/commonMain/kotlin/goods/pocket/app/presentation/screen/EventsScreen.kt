package goods.pocket.app.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import goods.pocket.app.domain.model.Event
import goods.pocket.app.domain.model.EventType
import goods.pocket.app.presentation.i18n.localizedLabel
import goods.pocket.app.presentation.i18n.tr
import goodspocket.composeapp.generated.resources.*

@Composable
fun EventsScreen(
    events: List<Event>,
    selectedType: EventType?,
    onTypeChange: (EventType?) -> Unit,
    onEventClick: (String) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            androidx.compose.foundation.layout.Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                FilterChip(
                    selected = selectedType == null,
                    onClick = { onTypeChange(null) },
                    label = { Text(tr(Res.string.common_all)) },
                )
                listOf(
                    EventType.RELEASE,
                    EventType.PAYMENT_DUE,
                    EventType.DELIVERY,
                    EventType.OFFLINE_EVENT,
                ).forEach { type ->
                    FilterChip(
                        selected = selectedType == type,
                        onClick = { onTypeChange(type) },
                        label = { Text(type.localizedLabel()) },
                    )
                }
            }
        }
        if (events.isEmpty()) {
            item {
                Text(
                    text = tr(Res.string.events_empty_filter),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
        items(events, key = Event::id) { event ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onEventClick(event.id) },
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Text(text = event.title, style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = tr(Res.string.events_row_subtitle, event.eventType.localizedLabel(), event.targetDate),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Text(
                        text = event.locationOrStore ?: tr(Res.string.common_no_linked_place),
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
        }
    }
}
