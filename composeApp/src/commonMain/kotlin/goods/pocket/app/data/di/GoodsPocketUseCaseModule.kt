package goods.pocket.app.data.di

import goods.pocket.app.domain.usecase.CancelPreorderUseCase
import goods.pocket.app.domain.usecase.DeleteCollectionItemUseCase
import goods.pocket.app.domain.usecase.DeleteEventUseCase
import goods.pocket.app.domain.usecase.GetAppPreferencesUseCase
import goods.pocket.app.domain.usecase.GetCollectionItemsUseCase
import goods.pocket.app.domain.usecase.GetDashboardSummaryUseCase
import goods.pocket.app.domain.usecase.GetEventListUseCase
import goods.pocket.app.domain.usecase.GetPreorderListUseCase
import goods.pocket.app.domain.usecase.GetRecentActivitiesUseCase
import goods.pocket.app.domain.usecase.GetStorageLocationsUseCase
import goods.pocket.app.domain.usecase.GetUpcomingEventsUseCase
import goods.pocket.app.domain.usecase.MarkPreorderReceivedUseCase
import goods.pocket.app.domain.usecase.SaveCollectionItemUseCase
import goods.pocket.app.domain.usecase.SaveEventUseCase
import goods.pocket.app.domain.usecase.SavePreorderUseCase
import goods.pocket.app.domain.usecase.UpdateAppPreferencesUseCase
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
class GoodsPocketUseCaseModule {
    @Single
    fun getCollectionItemsUseCase(
        collectionRepository: goods.pocket.app.domain.repository.CollectionRepository,
    ) = GetCollectionItemsUseCase(collectionRepository)

    @Single
    fun getAppPreferencesUseCase(
        settingsRepository: goods.pocket.app.domain.repository.SettingsRepository,
    ) = GetAppPreferencesUseCase(settingsRepository)

    @Single
    fun getDashboardSummaryUseCase(
        collectionRepository: goods.pocket.app.domain.repository.CollectionRepository,
        preorderRepository: goods.pocket.app.domain.repository.PreorderRepository,
    ) = GetDashboardSummaryUseCase(collectionRepository, preorderRepository)

    @Single
    fun getEventListUseCase(
        eventRepository: goods.pocket.app.domain.repository.EventRepository,
    ) = GetEventListUseCase(eventRepository)

    @Single
    fun getPreorderListUseCase(
        preorderRepository: goods.pocket.app.domain.repository.PreorderRepository,
    ) = GetPreorderListUseCase(preorderRepository)

    @Single
    fun getRecentActivitiesUseCase(
        collectionRepository: goods.pocket.app.domain.repository.CollectionRepository,
        preorderRepository: goods.pocket.app.domain.repository.PreorderRepository,
    ) = GetRecentActivitiesUseCase(collectionRepository, preorderRepository)

    @Single
    fun getStorageLocationsUseCase(
        settingsRepository: goods.pocket.app.domain.repository.SettingsRepository,
    ) = GetStorageLocationsUseCase(settingsRepository)

    @Single
    fun getUpcomingEventsUseCase(
        eventRepository: goods.pocket.app.domain.repository.EventRepository,
    ) = GetUpcomingEventsUseCase(eventRepository)

    @Single
    fun cancelPreorderUseCase(
        preorderRepository: goods.pocket.app.domain.repository.PreorderRepository,
    ) = CancelPreorderUseCase(preorderRepository)

    @Single
    fun deleteCollectionItemUseCase(
        collectionRepository: goods.pocket.app.domain.repository.CollectionRepository,
    ) = DeleteCollectionItemUseCase(collectionRepository)

    @Single
    fun deleteEventUseCase(
        eventRepository: goods.pocket.app.domain.repository.EventRepository,
    ) = DeleteEventUseCase(eventRepository)

    @Single
    fun markPreorderReceivedUseCase(
        preorderRepository: goods.pocket.app.domain.repository.PreorderRepository,
        collectionRepository: goods.pocket.app.domain.repository.CollectionRepository,
    ) = MarkPreorderReceivedUseCase(preorderRepository, collectionRepository)

    @Single
    fun saveCollectionItemUseCase(
        collectionRepository: goods.pocket.app.domain.repository.CollectionRepository,
    ) = SaveCollectionItemUseCase(collectionRepository)

    @Single
    fun saveEventUseCase(
        eventRepository: goods.pocket.app.domain.repository.EventRepository,
    ) = SaveEventUseCase(eventRepository)

    @Single
    fun savePreorderUseCase(
        preorderRepository: goods.pocket.app.domain.repository.PreorderRepository,
    ) = SavePreorderUseCase(preorderRepository)

    @Single
    fun updateAppPreferencesUseCase(
        settingsRepository: goods.pocket.app.domain.repository.SettingsRepository,
    ) = UpdateAppPreferencesUseCase(settingsRepository)
}
