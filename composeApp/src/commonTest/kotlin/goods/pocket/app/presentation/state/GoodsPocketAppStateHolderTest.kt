package goods.pocket.app.presentation.state

import goods.pocket.app.data.InMemoryGoodsPocketRepository
import goods.pocket.app.presentation.navigation.AppDestination
import goods.pocket.app.domain.model.PreorderStatus
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

        stateHolder.updateStartTab("transaction/list")

        val state = stateHolder.state.value
        assertEquals("transaction/list", state.appPreferences.startTabRoute)
        assertEquals("transaction/list", state.currentDestination.route)
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

    private fun newStateHolder(): GoodsPocketAppStateHolder {
        val repository = InMemoryGoodsPocketRepository()
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
