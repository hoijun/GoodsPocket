package goods.pocket.app.presentation.state

import goods.pocket.app.data.InMemoryGoodsPocketRepository
import goods.pocket.app.domain.model.AppPreference
import goods.pocket.app.domain.model.EventType
import goods.pocket.app.domain.model.PreorderStatus
import goods.pocket.app.presentation.navigation.AppDestination
import goods.pocket.app.domain.usecase.CancelPreorderUseCase
import goods.pocket.app.domain.usecase.DeleteCollectionItemUseCase
import goods.pocket.app.domain.usecase.DeleteEventUseCase
import goods.pocket.app.domain.usecase.DeleteTransactionUseCase
import goods.pocket.app.domain.usecase.GetAppPreferencesUseCase
import goods.pocket.app.domain.usecase.GetCollectionItemsUseCase
import goods.pocket.app.domain.usecase.GetDashboardSummaryUseCase
import goods.pocket.app.domain.usecase.GetEventListUseCase
import goods.pocket.app.domain.usecase.GetItemTransactionsUseCase
import goods.pocket.app.domain.usecase.GetMonthlyTransactionsUseCase
import goods.pocket.app.domain.usecase.GetPreorderListUseCase
import goods.pocket.app.domain.usecase.GetRecentActivitiesUseCase
import goods.pocket.app.domain.usecase.GetStorageLocationsUseCase
import goods.pocket.app.domain.usecase.GetUpcomingEventsUseCase
import goods.pocket.app.domain.usecase.MarkPreorderReceivedUseCase
import goods.pocket.app.domain.usecase.SaveCollectionItemUseCase
import goods.pocket.app.domain.usecase.SaveEventUseCase
import goods.pocket.app.domain.usecase.SavePreorderUseCase
import goods.pocket.app.domain.usecase.SaveTransactionUseCase
import goods.pocket.app.domain.usecase.UpdateAppPreferencesUseCase
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class GoodsPocketAppStateHolderTest {

    @Test
    fun `primary destinations expose my instead of transactions`() {
        assertEquals(
            listOf(
                AppDestination.Home,
                AppDestination.Collection,
                AppDestination.Preorders,
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
    fun `reload exposes my page summary from existing app state`() {
        val stateHolder = newStateHolder()

        val myPage = stateHolder.state.value.myPage

        assertEquals("Local Profile", myPage.displayName)
        assertEquals("Not connected", myPage.syncStatusLabel)
        assertTrue(myPage.notificationsEnabled)
        assertEquals(2, myPage.ownedItemCount)
        assertEquals(2, myPage.activePreorderCount)
        assertEquals(60_000L, myPage.monthlySpend)
        assertEquals(3, myPage.upcomingEventCount)
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
    fun quickAddItemUpdatesCollectionAndDashboard() {
        val stateHolder = newStateHolder()
        val before = stateHolder.state.value

        stateHolder.submitItem(
            name = "Uma Musume Postcard",
            category = "Postcard",
        )

        val after = stateHolder.state.value
        assertEquals(before.collectionItems.size + 1, after.collectionItems.size)
        assertEquals(before.homeSummary.ownedItemCount + 1, after.homeSummary.ownedItemCount)
        assertEquals(AppDestination.Collection.route, after.currentDestination.route)
        assertEquals(ActiveDetail.ItemDetail::class, after.activeDetail!!::class)
    }

    @Test
    fun openDetailAndEditorFollowExpectedStateTransitions() {
        val stateHolder = newStateHolder()

        stateHolder.openItemDetail("item-1")
        assertEquals(ActiveDetail.ItemDetail("item-1"), stateHolder.state.value.activeDetail)

        stateHolder.openItemEditor("item-1")
        assertNull(stateHolder.state.value.activeDetail)
        assertEquals(ActiveEditor.ItemEditor("item-1"), stateHolder.state.value.activeEditor)

        stateHolder.closeEditor()
        assertNull(stateHolder.state.value.activeEditor)
    }

    @Test
    fun editedItemIsReflectedInState() {
        val stateHolder = newStateHolder()

        stateHolder.saveEditedItem(
            itemId = "item-1",
            name = "Updated Acrylic Stand",
            category = "Figure",
            seriesName = "Hololive",
            characterName = "Suisei",
            purchaseStore = "Animate International",
        )

        val updatedItem = stateHolder.state.value.collectionItems.first { it.id == "item-1" }
        assertEquals("Updated Acrylic Stand", updatedItem.name)
        assertEquals("Figure", updatedItem.category)
        assertEquals("Animate International", updatedItem.purchaseStore)
        assertEquals(ActiveDetail.ItemDetail("item-1"), stateHolder.state.value.activeDetail)
    }

    @Test
    fun deleteTransactionRemovesItFromCurrentMonthList() {
        val stateHolder = newStateHolder()
        val before = stateHolder.state.value.transactions.size

        stateHolder.requestDeleteTransaction("tx-1")
        assertEquals(PendingDelete.TransactionDelete("tx-1"), stateHolder.state.value.pendingDelete)

        stateHolder.confirmPendingDelete()

        val after = stateHolder.state.value.transactions.size
        assertEquals(before - 1, after)
        assertNull(stateHolder.state.value.pendingDelete)
        assertEquals(AppDestination.Transactions.route, stateHolder.state.value.currentDestination.route)
    }

    @Test
    fun cancelPreorderSetsPendingDeleteAndCanBeConfirmed() {
        val stateHolder = newStateHolder()

        stateHolder.requestCancelPreorder("pre-1")
        assertEquals(PendingDelete.PreorderCancel("pre-1"), stateHolder.state.value.pendingDelete)

        stateHolder.confirmPendingDelete()

        val preorder = stateHolder.state.value.preorders.first { it.id == "pre-1" }
        assertEquals(PreorderStatus.CANCELED, preorder.status)
        assertNull(stateHolder.state.value.pendingDelete)
        assertEquals(ActiveDetail.PreorderDetail("pre-1"), stateHolder.state.value.activeDetail)
    }

    @Test
    fun collectionQueryFiltersItems() {
        val stateHolder = newStateHolder()

        stateHolder.updateCollectionQuery("Blue Archive")

        val items = stateHolder.state.value.collectionItems
        assertEquals(1, items.size)
        assertEquals("item-2", items.first().id)
    }

    @Test
    fun preorderStatusFilterLimitsVisibleList() {
        val stateHolder = newStateHolder()

        stateHolder.updatePreorderStatusFilter(PreorderStatus.PAYMENT_PENDING)

        val preorders = stateHolder.state.value.preorders
        assertEquals(1, preorders.size)
        assertEquals("pre-2", preorders.first().id)
    }

    @Test
    fun startTabPreferenceUpdatesCurrentDestination() {
        val stateHolder = newStateHolder()

        stateHolder.updateStartTab("my")

        val state = stateHolder.state.value
        assertEquals("my", state.appPreferences.startTabRoute)
        assertEquals("my", state.currentDestination.route)
    }

    @Test
    fun legacyTransactionStartTabIsRewrittenToHomeOnInit() {
        val repository = InMemoryGoodsPocketRepository()
        UpdateAppPreferencesUseCase(repository)(
            AppPreference(startTabRoute = AppDestination.Transactions.route),
        )

        val stateHolder = newStateHolder(repository)
        val state = stateHolder.state.value

        assertEquals(AppDestination.Home.route, state.appPreferences.startTabRoute)
        assertEquals(AppDestination.Home.route, state.currentDestination.route)
        assertEquals(
            AppDestination.Home.route,
            GetAppPreferencesUseCase(repository)().startTabRoute,
        )
    }

    @Test
    fun markPreorderReceivedMovesFlowToCollection() {
        val stateHolder = newStateHolder()
        val before = stateHolder.state.value

        stateHolder.markPreorderReceived("pre-1")

        val after = stateHolder.state.value
        assertEquals(before.homeSummary.ownedItemCount + 1, after.homeSummary.ownedItemCount)
        assertEquals(AppDestination.Collection.route, after.currentDestination.route)
        assertEquals(ActiveDetail.ItemDetail::class, after.activeDetail!!::class)
        assertTrue(after.collectionItems.any { it.linkedPreorderId == "pre-1" })
    }

    private fun newStateHolder(
        repository: InMemoryGoodsPocketRepository = InMemoryGoodsPocketRepository(),
    ): GoodsPocketAppStateHolder {
        return GoodsPocketAppStateHolder(
            getCollectionItemsUseCase = GetCollectionItemsUseCase(repository),
            getAppPreferencesUseCase = GetAppPreferencesUseCase(repository),
            getDashboardSummaryUseCase = GetDashboardSummaryUseCase(repository, repository, repository),
            getEventListUseCase = GetEventListUseCase(repository),
            getItemTransactionsUseCase = GetItemTransactionsUseCase(repository),
            getMonthlyTransactionsUseCase = GetMonthlyTransactionsUseCase(repository),
            getPreorderListUseCase = GetPreorderListUseCase(repository),
            getRecentActivitiesUseCase = GetRecentActivitiesUseCase(repository, repository, repository),
            getStorageLocationsUseCase = GetStorageLocationsUseCase(repository),
            getUpcomingEventsUseCase = GetUpcomingEventsUseCase(repository),
            cancelPreorderUseCase = CancelPreorderUseCase(repository),
            deleteCollectionItemUseCase = DeleteCollectionItemUseCase(repository),
            deleteEventUseCase = DeleteEventUseCase(repository),
            deleteTransactionUseCase = DeleteTransactionUseCase(repository),
            markPreorderReceivedUseCase = MarkPreorderReceivedUseCase(repository, repository, repository),
            saveCollectionItemUseCase = SaveCollectionItemUseCase(repository),
            saveEventUseCase = SaveEventUseCase(repository),
            savePreorderUseCase = SavePreorderUseCase(repository),
            saveTransactionUseCase = SaveTransactionUseCase(repository),
            updateAppPreferencesUseCase = UpdateAppPreferencesUseCase(repository),
        )
    }
}
