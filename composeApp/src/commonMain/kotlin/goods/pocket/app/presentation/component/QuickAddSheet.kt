package goods.pocket.app.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import goods.pocket.app.domain.collection.CollectionEntryStatus
import goods.pocket.app.domain.event.EventType
import goods.pocket.app.presentation.i18n.localizedLabel
import goods.pocket.app.presentation.i18n.tr
import goods.pocket.app.presentation.state.CommandState
import goods.pocket.app.presentation.state.QuickAddTarget
import goodspocket.composeapp.generated.resources.Res
import goodspocket.composeapp.generated.resources.field_category
import goodspocket.composeapp.generated.resources.field_character
import goodspocket.composeapp.generated.resources.field_note
import goodspocket.composeapp.generated.resources.field_series
import goodspocket.composeapp.generated.resources.field_status
import goodspocket.composeapp.generated.resources.field_target_date
import goodspocket.composeapp.generated.resources.nav_collection
import goodspocket.composeapp.generated.resources.nav_events
import goodspocket.composeapp.generated.resources.quick_add_event_name
import goodspocket.composeapp.generated.resources.quick_add_event_type
import goodspocket.composeapp.generated.resources.quick_add_item_name
import goodspocket.composeapp.generated.resources.quick_add_owned
import goodspocket.composeapp.generated.resources.quick_add_release_date
import goodspocket.composeapp.generated.resources.quick_add_reservation_store
import goodspocket.composeapp.generated.resources.quick_add_reserved
import goodspocket.composeapp.generated.resources.quick_add_store

@Composable
fun QuickAddSheet(
    target: QuickAddTarget,
    commandState: CommandState = CommandState(),
    onRetry: () -> Unit = {},
    onTargetChange: (QuickAddTarget) -> Unit,
    onDismiss: () -> Unit,
    onSubmitCollectionEntry: (
        name: String,
        category: String,
        status: CollectionEntryStatus,
        seriesName: String,
        characterName: String,
        purchaseStore: String,
        releaseDate: String,
        reservationStore: String,
        note: String,
    ) -> Unit,
    onSubmitEvent: (String, String, EventType) -> Unit,
) {
    var entry by rememberSaveable(stateSaver = CollectionDraftSaver) {
        mutableStateOf(CollectionDraft())
    }
    var event by rememberSaveable(stateSaver = EventDraftSaver) { mutableStateOf(EventDraft()) }
    val isCollection = target == QuickAddTarget.COLLECTION_ENTRY
    QuickAddReferenceSheet(
        canSubmit =
        !commandState.isRunning && if (isCollection) entry.canSubmit else event.canSubmit,
        onDismiss = onDismiss,
        onSubmit = {
            if (isCollection) {
                onSubmitCollectionEntry(
                    entry.name, entry.category, entry.status, entry.seriesName,
                    entry.characterName,
                    entry.purchaseStore,
                    entry.releaseDate,
                    entry.reservationStore,
                    entry.note,
                )
            } else {
                onSubmitEvent(event.title, event.targetDate, event.eventType)
            }
        },
    ) {
        FeatureFeedback(commandState.isRunning, commandState.hasFailure, onRetry)
        QuickAddSegments(
            labels = listOf(tr(Res.string.nav_collection), tr(Res.string.nav_events)),
            selectedIndex = if (isCollection) 0 else 1,
            onSelected = {
                onTargetChange(
                    if (it ==
                        0
                    ) {
                        QuickAddTarget.COLLECTION_ENTRY
                    } else {
                        QuickAddTarget.EVENT
                    },
                )
            },
            modifier = Modifier.padding(bottom = 5.dp),
        )
        if (isCollection) {
            QuickAddField(entry.name, tr(Res.string.quick_add_item_name), {
                entry =
                    entry.copy(name = it)
            }, minHeight = 40.dp)
            QuickAddField(entry.category, tr(Res.string.field_category), {
                entry =
                    entry.copy(category = it)
            })
            val statuses =
                listOf(
                    CollectionEntryStatus.OWNED,
                    CollectionEntryStatus.RESERVED,
                    CollectionEntryStatus.PLANNED_CLEANUP,
                )
            Column(
                Modifier.padding(bottom = 4.dp),
                verticalArrangement = Arrangement.spacedBy(7.dp),
            ) {
                Text(
                    tr(Res.string.field_status),
                    color = Color(0xFF8A8F9B),
                    fontSize = 13.sp,
                    lineHeight = 14.sp,
                )
                QuickAddSegments(
                    listOf(
                        tr(Res.string.quick_add_owned),
                        tr(Res.string.quick_add_reserved),
                        CollectionEntryStatus.PLANNED_CLEANUP.localizedLabel(),
                    ),
                    statuses.indexOf(entry.status),
                    { entry = entry.copy(status = statuses[it]) },
                    outlined = true,
                )
            }
            QuickAddField(entry.seriesName, tr(Res.string.field_series), {
                entry =
                    entry.copy(seriesName = it)
            })
            QuickAddField(entry.characterName, tr(Res.string.field_character), {
                entry =
                    entry.copy(characterName = it)
            })
            QuickAddField(
                if (entry.isReserved) entry.reservationStore else entry.purchaseStore,
                tr(
                    if (entry.isReserved) {
                        Res.string.quick_add_reservation_store
                    } else {
                        Res.string.quick_add_store
                    },
                ),
                {
                    entry =
                        if (entry.isReserved) {
                            entry.copy(
                                reservationStore = it,
                            )
                        } else {
                            entry.copy(purchaseStore = it)
                        }
                },
                minHeight = 44.dp,
            )
            if (entry.isReserved) {
                QuickAddField(entry.releaseDate, tr(Res.string.quick_add_release_date), {
                    entry =
                        entry.copy(releaseDate = it)
                })
            }
            QuickAddField(entry.note, tr(Res.string.field_note), {
                entry = entry.copy(note = it)
            }, multiline = true)
        } else {
            QuickAddField(event.title, tr(Res.string.quick_add_event_name), {
                event =
                    event.copy(title = it)
            }, minHeight = 40.dp)
            QuickAddField(event.targetDate, tr(Res.string.field_target_date), {
                event =
                    event.copy(targetDate = it)
            })
            val types = listOf(EventType.RELEASE, EventType.PAYMENT_DUE, EventType.DELIVERY)
            Column(verticalArrangement = Arrangement.spacedBy(7.dp)) {
                Text(
                    tr(Res.string.quick_add_event_type),
                    color = Color(0xFF8A8F9B),
                    fontSize = 13.sp,
                    lineHeight = 14.sp,
                )
                QuickAddSegments(
                    types.map { it.localizedLabel() },
                    types.indexOf(event.eventType),
                    { event = event.copy(eventType = types[it]) },
                    outlined = true,
                )
            }
        }
    }
}
