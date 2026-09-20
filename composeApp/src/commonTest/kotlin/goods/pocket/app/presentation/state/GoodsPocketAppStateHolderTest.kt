package goods.pocket.app.presentation.state

import goods.pocket.app.data.InMemoryGoodsPocketRepository
import goods.pocket.app.domain.model.CollectionEntryStatus
import goods.pocket.app.domain.model.AppPreference
import goods.pocket.app.domain.model.EventType
import goods.pocket.app.presentation.navigation.AppDestination
import goods.pocket.app.domain.usecase.GetDashboardSummaryUseCase
import goods.pocket.app.domain.usecase.GetRecentActivitiesUseCase
import goods.pocket.app.domain.usecase.MarkPreorderReceivedUseCase
import goods.pocket.app.domain.usecase.RECEIVED_ITEM_CATEGORY_CODE
import goods.pocket.app.domain.service.AppClock
import goods.pocket.app.domain.service.IdGenerator
import goods.pocket.app.domain.repository.SettingsRepository
import goods.pocket.app.domain.repository.CollectionRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class GoodsPocketAppStateHolderTest {

    @Test
    fun `collection segment defaults to owned`() {
        val stateHolder = newStateHolder()

        assertEquals(CollectionSegment.OWNED, stateHolder.state.value.collectionSegment)
    }

    @Test
    fun `selecting reserved collection segment opens collection with reserved focus`() {
        val stateHolder = newStateHolder()

        stateHolder.selectCollectionSegment(CollectionSegment.RESERVED)

        val state = stateHolder.state.value
        assertEquals(AppDestination.Collection, state.currentDestination)
        assertEquals(AppDestination.Collection, state.selectedPrimaryDestination)
        assertEquals(CollectionSegment.RESERVED, state.collectionSegment)
    }

    @Test
    fun `collection detail opens and closes without changing segment or query`() {
        val stateHolder = newStateHolder()
        stateHolder.selectCollectionSegment(CollectionSegment.RESERVED)
        stateHolder.updateCollectionQuery("프로젝트")

        stateHolder.openCollectionEntryDetail("pre-1")

        val detailState = stateHolder.state.value
        assertEquals(CollectionSegment.RESERVED, detailState.collectionSegment)
        assertEquals("프로젝트", detailState.collectionQuery)
        assertEquals(ActiveDetail.CollectionEntryDetail("pre-1"), detailState.activeDetail)

        stateHolder.closeDetail()

        val closedState = stateHolder.state.value
        assertEquals(CollectionSegment.RESERVED, closedState.collectionSegment)
        assertEquals("프로젝트", closedState.collectionQuery)
        assertNull(closedState.activeDetail)
    }

    @Test
    fun `marking a reserved collection entry as received keeps the same id and switches it to owned`() {
        val stateHolder = newStateHolder()
        stateHolder.selectCollectionSegment(CollectionSegment.RESERVED)

        stateHolder.markCollectionEntryReceived("pre-1")

        val state = stateHolder.state.value
        val updated = state.collectionEntries.first { it.id == "pre-1" }
        assertEquals(CollectionEntryStatus.OWNED, updated.status)
        assertEquals(CollectionSegment.OWNED, state.collectionSegment)
        assertEquals(ActiveDetail.CollectionEntryDetail("pre-1"), state.activeDetail)
    }

    @Test
    fun `primary destinations expose image locked bottom tabs`() {
        assertEquals(
            listOf(
                AppDestination.Home,
                AppDestination.Collection,
                AppDestination.Events,
                AppDestination.My,
            ),
            AppDestination.primaryDestinations,
        )
    }

    @Test
    fun `closing settings always returns to my after opening from my`() {
        val stateHolder = newStateHolder()

        stateHolder.selectDestination(AppDestination.My)
        stateHolder.openSettings()
        stateHolder.closeSettings()

        assertEquals(AppDestination.My, stateHolder.state.value.currentDestination)
        assertEquals(AppDestination.My, stateHolder.state.value.selectedPrimaryDestination)
    }

    @Test
    fun `my screen can open settings without using the global app bar action`() {
        val stateHolder = newStateHolder()

        stateHolder.selectDestination(AppDestination.My)
        stateHolder.openSettings()

        assertEquals(AppDestination.Settings, stateHolder.state.value.currentDestination)
        assertEquals(AppDestination.My, stateHolder.state.value.selectedPrimaryDestination)
    }

    @Test
    fun `reload exposes my page summary from existing app state`() {
        val stateHolder = newStateHolder()

        val myPage = stateHolder.state.value.myPage

        assertEquals("로컬 프로필", myPage.displayName)
        assertEquals("연결되지 않음", myPage.syncStatusLabel)
        assertEquals(2, myPage.ownedItemCount)
        assertEquals(2, myPage.activePreorderCount)
        assertEquals(106_000L, myPage.monthlySpend)
        assertEquals(3, myPage.upcomingEventCount)
        assertEquals("2026-03-15", stateHolder.state.value.currentDate)
    }

    @Test
    fun `my page upcoming count is not capped by preview limit`() {
        val stateHolder = newStateHolder()

        stateHolder.submitEvent(
            title = "Extra delivery 1",
            targetDate = "2026-03-28",
            eventType = EventType.DELIVERY,
        )
        stateHolder.submitEvent(
            title = "Extra delivery 2",
            targetDate = "2026-03-29",
            eventType = EventType.DELIVERY,
        )

        val state = stateHolder.state.value
        assertEquals(3, state.upcomingEvents.size)
        assertEquals(5, state.myPage.upcomingEventCount)
    }

    @Test
    fun `home upcoming schedule excludes events before the injected current date`() {
        val stateHolder = newStateHolder(clock = AfterSeedEventsClock)

        val state = stateHolder.state.value
        assertTrue(state.upcomingEvents.isEmpty())
        assertEquals(0, state.myPage.upcomingEventCount)
        assertEquals(3, state.events.size)
    }

    @Test
    fun `events overview is a primary destination`() {
        val stateHolder = newStateHolder()

        stateHolder.selectDestination(AppDestination.My)
        stateHolder.openEventsOverview()
        assertEquals(AppDestination.Events, stateHolder.state.value.currentDestination)
        assertEquals(AppDestination.Events, stateHolder.state.value.selectedPrimaryDestination)
    }

    @Test
    fun `home collection links clear query and select every requested segment`() {
        val stateHolder = newStateHolder()

        CollectionSegment.entries.forEach { segment ->
            stateHolder.updateCollectionQuery("미쿠")
            stateHolder.openCollectionFromHome(segment)

            val state = stateHolder.state.value
            assertEquals(AppDestination.Collection, state.currentDestination)
            assertEquals(AppDestination.Collection, state.selectedPrimaryDestination)
            assertEquals(segment, state.collectionSegment)
            assertEquals("", state.collectionQuery)
        }
    }

    @Test
    fun `home event link clears filter and opens selected event`() {
        val stateHolder = newStateHolder()
        stateHolder.updateEventTypeFilter(EventType.DELIVERY)

        stateHolder.openEventFromHome("event-1")

        val state = stateHolder.state.value
        assertEquals(AppDestination.Events, state.currentDestination)
        assertEquals(AppDestination.Events, state.selectedPrimaryDestination)
        assertNull(state.eventTypeFilter)
        assertEquals(ActiveDetail.EventDetail("event-1"), state.activeDetail)
    }

    @Test
    fun `home recent activity derives collection segment from loaded entry status`() {
        val stateHolder = newStateHolder()
        stateHolder.updateCollectionQuery("남아 있으면 안 됨")

        stateHolder.openActivity("pre-1")

        val state = stateHolder.state.value
        assertEquals(AppDestination.Collection, state.currentDestination)
        assertEquals(CollectionSegment.RESERVED, state.collectionSegment)
        assertEquals("", state.collectionQuery)
        assertEquals(ActiveDetail.CollectionEntryDetail("pre-1"), state.activeDetail)
    }

    @Test
    fun `unknown home recent activity leaves navigation unchanged`() {
        val stateHolder = newStateHolder()
        val before = stateHolder.state.value

        stateHolder.openActivity("missing-entry")

        assertEquals(before, stateHolder.state.value)
    }

    @Test
    fun `quick add resets target and closes every other overlay`() {
        val stateHolder = newStateHolder()
        stateHolder.selectQuickAddTarget(QuickAddTarget.EVENT)
        stateHolder.openCollectionEntryDetail("item-1")

        stateHolder.openQuickAdd()

        val state = stateHolder.state.value
        assertTrue(state.isQuickAddOpen)
        assertEquals(QuickAddTarget.COLLECTION_ENTRY, state.quickAddTarget)
        assertNull(state.activeDetail)
        assertNull(state.activeEditor)
        assertNull(state.pendingDelete)
    }

    @Test
    fun `primary destination closes overlays and resets destination filters`() {
        val stateHolder = newStateHolder()
        stateHolder.updateCollectionQuery("미쿠")
        stateHolder.selectCollectionSegment(CollectionSegment.RESERVED)
        stateHolder.openCollectionEntryDetail("pre-1")

        stateHolder.selectDestination(AppDestination.Collection)

        val collectionState = stateHolder.state.value
        assertEquals(CollectionSegment.OWNED, collectionState.collectionSegment)
        assertEquals("", collectionState.collectionQuery)
        assertNull(collectionState.activeDetail)

        stateHolder.updateEventTypeFilter(EventType.DELIVERY)
        stateHolder.openEventDetail("event-1")
        stateHolder.selectDestination(AppDestination.Events)

        val eventsState = stateHolder.state.value
        assertNull(eventsState.eventTypeFilter)
        assertNull(eventsState.activeDetail)
    }

    @Test
    fun quickAddItemUpdatesCollectionAndDashboard() {
        val stateHolder = newStateHolder()
        val before = stateHolder.state.value

        stateHolder.submitCollectionEntry(
            name = "Uma Musume Postcard",
            category = "Postcard",
            status = CollectionEntryStatus.OWNED,
            seriesName = "Uma Musume",
            characterName = "Special Week",
            purchaseStore = "Animate",
            releaseDate = "",
            reservationStore = "",
            note = "",
        )

        val after = stateHolder.state.value
        assertEquals(before.collectionEntries.size + 1, after.collectionEntries.size)
        assertEquals(before.homeSummary.ownedItemCount + 1, after.homeSummary.ownedItemCount)
        assertEquals(AppDestination.Collection.route, after.currentDestination.route)
        assertEquals(ActiveDetail.CollectionEntryDetail::class, after.activeDetail!!::class)
    }

    @Test
    fun openDetailAndEditorFollowExpectedStateTransitions() {
        val stateHolder = newStateHolder()

        stateHolder.openCollectionEntryDetail("item-1")
        assertEquals(ActiveDetail.CollectionEntryDetail("item-1"), stateHolder.state.value.activeDetail)

        stateHolder.openCollectionEntryEditor("item-1")
        assertNull(stateHolder.state.value.activeDetail)
        assertEquals(ActiveEditor.CollectionEntryEditor("item-1"), stateHolder.state.value.activeEditor)

        stateHolder.closeEditor()
        assertNull(stateHolder.state.value.activeEditor)
    }

    @Test
    fun `event quick add clears the active filter and opens the newly saved event`() {
        val stateHolder = newStateHolder()
        stateHolder.selectDestination(AppDestination.Events)
        val filter = EventType.entries.first { it != EventType.DELIVERY }
        stateHolder.updateEventTypeFilter(filter)
        stateHolder.openQuickAdd()
        stateHolder.selectQuickAddTarget(QuickAddTarget.EVENT)
        assertEquals(filter, stateHolder.state.value.eventTypeFilter)

        stateHolder.submitEvent("New delivery", "2026-03-28", EventType.DELIVERY)

        val state = stateHolder.state.value
        val saved = state.events.single { it.title == "New delivery" }
        assertEquals(EventType.DELIVERY, saved.eventType)
        assertEquals(AppDestination.Events, state.currentDestination)
        assertEquals(AppDestination.Events, state.selectedPrimaryDestination)
        assertEquals(ActiveDetail.EventDetail(saved.id), state.activeDetail)
        assertFalse(state.isQuickAddOpen)
        assertNull(state.eventTypeFilter)
    }

    @Test
    fun `editing a collection entry preserves its related link through save and reload`() = runTest {
        val repository = InMemoryGoodsPocketRepository()
        val original = requireNotNull(repository.getEntry("item-1")).copy(
            relatedLink = "https://example.com/items/item-1",
        )
        repository.saveEntry(original)
        val stateHolder = newStateHolder(repository = repository)
        stateHolder.openCollectionEntryEditor(original.id)

        stateHolder.saveEditedCollectionEntry(
            entryId = original.id,
            name = "Updated linked item",
            category = original.category,
            status = original.status,
            seriesName = original.seriesName.orEmpty(),
            characterName = original.characterName.orEmpty(),
            purchaseStore = original.purchaseStore.orEmpty(),
            releaseDate = original.releaseDate.orEmpty(),
            reservationStore = original.reservationStore.orEmpty(),
            note = "Updated note",
        )

        val saved = requireNotNull(repository.getEntry(original.id))
        assertEquals("Updated linked item", saved.name)
        assertEquals("Updated note", saved.note)
        assertEquals(original.relatedLink, saved.relatedLink)
        assertEquals(saved, stateHolder.state.value.collectionEntries.single { it.id == original.id })
        val reloaded = newStateHolder(repository = repository)
        assertEquals(saved, reloaded.state.value.collectionEntries.single { it.id == original.id })
    }

    @Test
    fun editedItemIsReflectedInState() {
        val stateHolder = newStateHolder()

        stateHolder.saveEditedCollectionEntry(
            entryId = "item-1",
            name = "Updated Acrylic Stand",
            category = "Figure",
            status = CollectionEntryStatus.PLANNED_CLEANUP,
            seriesName = "Hololive",
            characterName = "Suisei",
            purchaseStore = "Animate International",
            releaseDate = "",
            reservationStore = "",
            note = "정리 후보",
        )

        val updatedItem = stateHolder.state.value.collectionEntries.first { it.id == "item-1" }
        assertEquals("Updated Acrylic Stand", updatedItem.name)
        assertEquals("Figure", updatedItem.category)
        assertEquals(CollectionEntryStatus.PLANNED_CLEANUP, updatedItem.status)
        assertEquals("Animate International", updatedItem.purchaseStore)
        assertEquals(ActiveDetail.CollectionEntryDetail("item-1"), stateHolder.state.value.activeDetail)
    }

    @Test
    fun cancelPreorderSetsPendingDeleteAndCanBeConfirmed() {
        val stateHolder = newStateHolder()

        stateHolder.requestDeleteCollectionEntry("pre-1")
        assertEquals(PendingDelete.PreorderCancel("pre-1"), stateHolder.state.value.pendingDelete)

        stateHolder.confirmPendingDelete()

        assertNull(stateHolder.state.value.collectionEntries.firstOrNull { it.id == "pre-1" })
        assertNull(stateHolder.state.value.pendingDelete)
        assertNull(stateHolder.state.value.activeDetail)
    }

    @Test
    fun `dismissing item deletion restores the same detail and host state`() {
        val stateHolder = newStateHolder()
        stateHolder.selectCollectionSegment(CollectionSegment.OWNED)
        stateHolder.updateCollectionQuery("item query")
        stateHolder.updateEventTypeFilter(EventType.DELIVERY)
        stateHolder.openCollectionEntryDetail("item-1")
        val before = stateHolder.state.value

        stateHolder.requestDeleteCollectionEntry("item-1")

        assertEquals(
            before.copy(activeDetail = null, pendingDelete = PendingDelete.ItemDelete("item-1")),
            stateHolder.state.value,
        )

        stateHolder.dismissPendingDelete()

        assertEquals(before, stateHolder.state.value)
    }

    @Test
    fun `dismissing preorder cancellation restores the same detail and host state`() {
        val stateHolder = newStateHolder()
        stateHolder.selectCollectionSegment(CollectionSegment.RESERVED)
        stateHolder.updateCollectionQuery("preorder query")
        stateHolder.updateEventTypeFilter(EventType.DELIVERY)
        stateHolder.openCollectionEntryDetail("pre-1")
        val before = stateHolder.state.value

        stateHolder.requestDeleteCollectionEntry("pre-1")

        assertEquals(
            before.copy(activeDetail = null, pendingDelete = PendingDelete.PreorderCancel("pre-1")),
            stateHolder.state.value,
        )

        stateHolder.dismissPendingDelete()

        assertEquals(before, stateHolder.state.value)
    }

    @Test
    fun `dismissing event deletion restores the same detail and host state`() {
        val stateHolder = newStateHolder()
        stateHolder.selectCollectionSegment(CollectionSegment.RESERVED)
        stateHolder.selectDestination(AppDestination.Events)
        stateHolder.updateCollectionQuery("retained query")
        stateHolder.updateEventTypeFilter(EventType.DELIVERY)
        stateHolder.openEventDetail("event-1")
        val before = stateHolder.state.value

        stateHolder.requestDeleteEvent("event-1")

        assertEquals(
            before.copy(activeDetail = null, pendingDelete = PendingDelete.EventDelete("event-1")),
            stateHolder.state.value,
        )

        stateHolder.dismissPendingDelete()

        assertEquals(before, stateHolder.state.value)
    }

    @Test
    fun `dismissing without a pending deletion leaves state unchanged`() {
        val stateHolder = newStateHolder()
        stateHolder.selectCollectionSegment(CollectionSegment.RESERVED)
        stateHolder.updateCollectionQuery("retained query")
        stateHolder.updateEventTypeFilter(EventType.DELIVERY)
        val hostState = stateHolder.state.value

        stateHolder.dismissPendingDelete()

        assertEquals(hostState, stateHolder.state.value)

        stateHolder.openCollectionEntryDetail("pre-1")
        val detailState = stateHolder.state.value

        stateHolder.dismissPendingDelete()

        assertEquals(detailState, stateHolder.state.value)
    }

    @Test
    fun `dismissing collection deletion keeps the entry and selected segment`() {
        val stateHolder = newStateHolder()
        stateHolder.selectCollectionSegment(CollectionSegment.RESERVED)
        val entries = stateHolder.state.value.collectionEntries

        stateHolder.requestDeleteCollectionEntry("pre-1")
        stateHolder.dismissPendingDelete()

        val state = stateHolder.state.value
        assertEquals(entries, state.collectionEntries)
        assertEquals(CollectionSegment.RESERVED, state.collectionSegment)
        assertNull(state.pendingDelete)
    }

    @Test
    fun `collection query is presentation state and does not reload the repository`() {
        val repository = InMemoryGoodsPocketRepository()
        val collectionRepository = CountingCollectionRepository(repository)
        val stateHolder = newStateHolder(
            repository = repository,
            collectionRepository = collectionRepository,
        )
        val loadCount = collectionRepository.getEntriesCount
        val entries = stateHolder.state.value.collectionEntries

        stateHolder.updateCollectionQuery("블루 아카이브")

        assertEquals("블루 아카이브", stateHolder.state.value.collectionQuery)
        assertEquals(entries, stateHolder.state.value.collectionEntries)
        assertEquals(loadCount, collectionRepository.getEntriesCount)
    }

    @Test
    fun markPreorderReceivedMovesFlowToCollection() {
        val stateHolder = newStateHolder()
        val before = stateHolder.state.value

        stateHolder.markCollectionEntryReceived("pre-1")

        val after = stateHolder.state.value
        val receivedItem = after.collectionEntries.first { it.id == "pre-1" }
        assertEquals(before.homeSummary.ownedItemCount + 1, after.homeSummary.ownedItemCount)
        assertEquals(AppDestination.Collection.route, after.currentDestination.route)
        assertEquals(ActiveDetail.CollectionEntryDetail::class, after.activeDetail!!::class)
        assertEquals(RECEIVED_ITEM_CATEGORY_CODE, receivedItem.category)
        assertEquals(CollectionEntryStatus.OWNED, receivedItem.status)
    }

    @Test
    fun `recent activities are localized for the default korean app language`() {
        val stateHolder = newStateHolder()

        val recentActivities = stateHolder.state.value.homeSummary.recentActivities

        assertEquals("프로젝트 세카이 한정 태피스트리", recentActivities[0].title)
        assertEquals("멜론북스 예약 추적", recentActivities[0].subtitle)
        assertEquals("블루 아카이브 아트북", recentActivities[2].title)
        assertEquals("컬렉션 굿즈 추가", recentActivities[2].subtitle)
    }

    @Test
    fun `failed initial load exposes a retryable failure and retry recovers`() {
        val repository = InMemoryGoodsPocketRepository()
        val settingsRepository = RecoveringSettingsRepository(repository)
        val stateHolder = newStateHolder(
            repository = repository,
            settingsRepository = settingsRepository,
        )

        assertEquals(GoodsPocketOperation.LOAD, stateHolder.state.value.failure?.operation)

        settingsRepository.shouldFail = false
        stateHolder.retry()

        assertFalse(stateHolder.state.value.isLoading)
        assertNull(stateHolder.state.value.failure)
        assertEquals(2, stateHolder.state.value.homeSummary.ownedItemCount)
    }

    @Test
    fun `failed quick add keeps the sheet and retries the same entry once`() = runTest {
        val repository = InMemoryGoodsPocketRepository()
        val recovering = RecoveringCollectionRepository(repository)
        val stateHolder = newStateHolder(repository, collectionRepository = recovering)
        val before = repository.getEntries()
        stateHolder.openQuickAdd()
        recovering.shouldFail = true

        stateHolder.submitCollectionEntry(
            name = "Retry item", category = "goods", status = CollectionEntryStatus.OWNED,
            seriesName = "", characterName = "", purchaseStore = "", releaseDate = "",
            reservationStore = "", note = "Kept draft",
        )

        assertEquals(GoodsPocketOperation.SAVE, stateHolder.state.value.failure?.operation)
        assertTrue(stateHolder.state.value.isQuickAddOpen)
        assertEquals(before, repository.getEntries())
        recovering.shouldFail = false
        stateHolder.retry()

        val saved = repository.getEntries().single { it.name == "Retry item" }
        assertEquals(listOf(saved.id, saved.id), recovering.savedIds)
        assertEquals(before.size + 1, repository.getEntries().size)
        assertEquals("Kept draft", saved.note)
        assertNull(stateHolder.state.value.failure)
        assertFalse(stateHolder.state.value.isQuickAddOpen)
        assertEquals(AppDestination.Collection, stateHolder.state.value.currentDestination)
        assertEquals(ActiveDetail.CollectionEntryDetail(saved.id), stateHolder.state.value.activeDetail)
    }

    @Test
    fun `failed delete preserves confirmation and retries only the requested entry`() = runTest {
        val repository = InMemoryGoodsPocketRepository()
        val recovering = RecoveringCollectionRepository(repository)
        val stateHolder = newStateHolder(repository, collectionRepository = recovering)
        val before = repository.getEntries()
        stateHolder.openCollectionEntryDetail("item-1")
        stateHolder.requestDeleteCollectionEntry("item-1")
        val pending = stateHolder.state.value.pendingDelete
        recovering.shouldFail = true

        stateHolder.confirmPendingDelete()

        assertEquals(GoodsPocketOperation.DELETE, stateHolder.state.value.failure?.operation)
        assertEquals(pending, stateHolder.state.value.pendingDelete)
        assertEquals(before, repository.getEntries())
        recovering.shouldFail = false
        stateHolder.retry()

        assertEquals(before.filterNot { it.id == "item-1" }, repository.getEntries())
        assertNull(stateHolder.state.value.failure)
        assertNull(stateHolder.state.value.pendingDelete)
        assertNull(stateHolder.state.value.activeDetail)
        assertEquals(AppDestination.Collection, stateHolder.state.value.currentDestination)
    }

    private fun newStateHolder(
        repository: InMemoryGoodsPocketRepository = InMemoryGoodsPocketRepository(),
        collectionRepository: CollectionRepository = repository,
        settingsRepository: SettingsRepository = repository,
        clock: AppClock = StateHolderTestClock,
    ): GoodsPocketAppStateHolder {
        return GoodsPocketAppStateHolder(
            collectionRepository = collectionRepository,
            preorderRepository = repository,
            eventRepository = repository,
            settingsRepository = settingsRepository,
            contentLoader = GoodsPocketContentLoader(
                collectionRepository = collectionRepository,
                eventRepository = repository,
                settingsRepository = settingsRepository,
                getDashboardSummaryUseCase = GetDashboardSummaryUseCase(collectionRepository, repository),
                getRecentActivitiesUseCase = GetRecentActivitiesUseCase(collectionRepository, repository),
                clock = clock,
            ),
            markPreorderReceivedUseCase = MarkPreorderReceivedUseCase(collectionRepository, clock),
            clock = clock,
            idGenerator = StateHolderTestIds,
            coroutineScope = CoroutineScope(Dispatchers.Unconfined),
        )
    }
}

private class CountingCollectionRepository(
    private val delegate: CollectionRepository,
) : CollectionRepository by delegate {
    var getEntriesCount: Int = 0

    override suspend fun getEntries(filter: String?): List<goods.pocket.app.domain.model.CollectionEntry> {
        getEntriesCount += 1
        return delegate.getEntries(filter)
    }
}

private class RecoveringCollectionRepository(
    private val delegate: CollectionRepository,
) : CollectionRepository by delegate {
    var shouldFail: Boolean = false
    val savedIds = mutableListOf<String>()

    override suspend fun saveEntry(entry: goods.pocket.app.domain.model.CollectionEntry) {
        savedIds += entry.id
        if (shouldFail) error("Test save failure")
        delegate.saveEntry(entry)
    }

    override suspend fun deleteEntry(id: String, deletedAt: String) {
        if (shouldFail) error("Test delete failure")
        delegate.deleteEntry(id, deletedAt)
    }
}

private class RecoveringSettingsRepository(
    private val delegate: SettingsRepository,
) : SettingsRepository by delegate {
    var shouldFail: Boolean = true

    override suspend fun getAppPreferences(): AppPreference {
        if (shouldFail) error("Test load failure")
        return delegate.getAppPreferences()
    }
}

private object StateHolderTestClock : AppClock {
    override fun currentDate(): String = "2026-03-15"
}

private object AfterSeedEventsClock : AppClock {
    override fun currentDate(): String = "2026-07-26"
}

private object StateHolderTestIds : IdGenerator {
    private var nextValue: Int = 0

    override fun generate(prefix: String): String {
        nextValue += 1
        return "$prefix-test-$nextValue"
    }
}
