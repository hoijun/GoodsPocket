package goods.pocket.app.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import goods.pocket.app.domain.model.Event
import goods.pocket.app.domain.model.EventType
import goods.pocket.app.presentation.designsystem.GoodsPocketFilterChip
import goods.pocket.app.presentation.designsystem.GoodsPocketModalBottomSheet
import goods.pocket.app.presentation.designsystem.goodsPocketOutlinedFieldColors
import goods.pocket.app.presentation.i18n.localizedLabel
import goods.pocket.app.presentation.i18n.tr
import goodspocket.composeapp.generated.resources.Res
import goodspocket.composeapp.generated.resources.action_save_changes
import goodspocket.composeapp.generated.resources.editor_event_title
import goodspocket.composeapp.generated.resources.field_target_date
import goodspocket.composeapp.generated.resources.field_title

@Composable
fun EventEditorSheet(
    event: Event,
    onDismiss: () -> Unit,
    onSave: (String, String, EventType) -> Unit,
) {
    var title by remember(event.id) { mutableStateOf(event.title) }
    var targetDate by remember(event.id) { mutableStateOf(event.targetDate) }
    var eventType by remember(event.id) { mutableStateOf(event.eventType) }

    GoodsPocketModalBottomSheet(
        title = tr(Res.string.editor_event_title),
        onDismiss = onDismiss,
        contentSpacing = 14.dp,
    ) {
        EventEditorField(title, { title = it }, tr(Res.string.field_title))
        EventEditorField(targetDate, { targetDate = it }, tr(Res.string.field_target_date))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            listOf(EventType.RELEASE, EventType.PAYMENT_DUE, EventType.DELIVERY).forEach { type ->
                GoodsPocketFilterChip(
                    selected = eventType == type,
                    onClick = { eventType = type },
                    label = type.localizedLabel(),
                )
            }
        }
        Button(
            enabled = title.isNotBlank() && targetDate.isNotBlank(),
            onClick = { onSave(title, targetDate, eventType) },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(tr(Res.string.action_save_changes))
        }
    }
}

@Composable
private fun EventEditorField(value: String, onValueChange: (String) -> Unit, label: String) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label) },
        colors = goodsPocketOutlinedFieldColors(),
        shape = MaterialTheme.shapes.small,
        singleLine = true,
    )
}
