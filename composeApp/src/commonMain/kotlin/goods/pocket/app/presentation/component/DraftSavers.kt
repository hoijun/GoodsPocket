package goods.pocket.app.presentation.component

import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import goods.pocket.app.domain.collection.CollectionEntryStatus
import goods.pocket.app.domain.event.EventType

internal val CollectionDraftSaver: Saver<CollectionDraft, Any> = listSaver(
    save = {
        listOf(
            it.name, it.category, it.status.name, it.seriesName, it.characterName,
            it.purchaseStore, it.releaseDate, it.reservationStore, it.note,
        )
    },
    restore = {
        CollectionDraft(
            name = it[0],
            category = it[1],
            status = CollectionEntryStatus.valueOf(it[2]),
            seriesName = it[3],
            characterName = it[4],
            purchaseStore = it[5],
            releaseDate = it[6],
            reservationStore = it[7],
            note = it[8],
        )
    },
)

internal val EventDraftSaver: Saver<EventDraft, Any> = listSaver(
    save = { listOf(it.title, it.targetDate, it.eventType.name) },
    restore = { EventDraft(it[0], it[1], EventType.valueOf(it[2])) },
)
