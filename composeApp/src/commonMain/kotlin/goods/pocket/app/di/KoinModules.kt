package goods.pocket.app.di

import goods.pocket.app.data.di.GoodsPocketDataModule
import goods.pocket.app.data.di.PlatformDataModule
import goods.pocket.app.data.di.GoodsPocketUseCaseModule
import goods.pocket.app.presentation.state.GoodsPocketAppStateHolder
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module(
    includes = [
        PlatformDataModule::class,
        GoodsPocketDataModule::class,
        GoodsPocketUseCaseModule::class,
        GoodsPocketPresentationModule::class,
    ],
)
class GoodsPocketAppModule

@Module
class GoodsPocketPresentationModule {
    @Single
    fun goodsPocketAppStateHolder(
        getCollectionItemsUseCase: goods.pocket.app.domain.usecase.GetCollectionItemsUseCase,
        getAppPreferencesUseCase: goods.pocket.app.domain.usecase.GetAppPreferencesUseCase,
        getDashboardSummaryUseCase: goods.pocket.app.domain.usecase.GetDashboardSummaryUseCase,
        getEventListUseCase: goods.pocket.app.domain.usecase.GetEventListUseCase,
        getItemTransactionsUseCase: goods.pocket.app.domain.usecase.GetItemTransactionsUseCase,
        getMonthlyTransactionsUseCase: goods.pocket.app.domain.usecase.GetMonthlyTransactionsUseCase,
        getPreorderListUseCase: goods.pocket.app.domain.usecase.GetPreorderListUseCase,
        getRecentActivitiesUseCase: goods.pocket.app.domain.usecase.GetRecentActivitiesUseCase,
        getStorageLocationsUseCase: goods.pocket.app.domain.usecase.GetStorageLocationsUseCase,
        getUpcomingEventsUseCase: goods.pocket.app.domain.usecase.GetUpcomingEventsUseCase,
        cancelPreorderUseCase: goods.pocket.app.domain.usecase.CancelPreorderUseCase,
        deleteCollectionItemUseCase: goods.pocket.app.domain.usecase.DeleteCollectionItemUseCase,
        deleteEventUseCase: goods.pocket.app.domain.usecase.DeleteEventUseCase,
        deleteTransactionUseCase: goods.pocket.app.domain.usecase.DeleteTransactionUseCase,
        markPreorderReceivedUseCase: goods.pocket.app.domain.usecase.MarkPreorderReceivedUseCase,
        saveCollectionItemUseCase: goods.pocket.app.domain.usecase.SaveCollectionItemUseCase,
        saveEventUseCase: goods.pocket.app.domain.usecase.SaveEventUseCase,
        savePreorderUseCase: goods.pocket.app.domain.usecase.SavePreorderUseCase,
        saveTransactionUseCase: goods.pocket.app.domain.usecase.SaveTransactionUseCase,
        updateAppPreferencesUseCase: goods.pocket.app.domain.usecase.UpdateAppPreferencesUseCase,
    ) = GoodsPocketAppStateHolder(
        getCollectionItemsUseCase = getCollectionItemsUseCase,
        getAppPreferencesUseCase = getAppPreferencesUseCase,
        getDashboardSummaryUseCase = getDashboardSummaryUseCase,
        getEventListUseCase = getEventListUseCase,
        getItemTransactionsUseCase = getItemTransactionsUseCase,
        getMonthlyTransactionsUseCase = getMonthlyTransactionsUseCase,
        getPreorderListUseCase = getPreorderListUseCase,
        getRecentActivitiesUseCase = getRecentActivitiesUseCase,
        getStorageLocationsUseCase = getStorageLocationsUseCase,
        getUpcomingEventsUseCase = getUpcomingEventsUseCase,
        cancelPreorderUseCase = cancelPreorderUseCase,
        deleteCollectionItemUseCase = deleteCollectionItemUseCase,
        deleteEventUseCase = deleteEventUseCase,
        deleteTransactionUseCase = deleteTransactionUseCase,
        markPreorderReceivedUseCase = markPreorderReceivedUseCase,
        saveCollectionItemUseCase = saveCollectionItemUseCase,
        saveEventUseCase = saveEventUseCase,
        savePreorderUseCase = savePreorderUseCase,
        saveTransactionUseCase = saveTransactionUseCase,
        updateAppPreferencesUseCase = updateAppPreferencesUseCase,
    )
}
