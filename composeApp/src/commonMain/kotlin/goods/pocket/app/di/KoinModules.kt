package goods.pocket.app.di

import goods.pocket.app.data.di.GoodsPocketDataModule
import goods.pocket.app.data.di.GoodsPocketUseCaseModule
import goods.pocket.app.data.di.PlatformDataModule
import goods.pocket.app.domain.collection.CollectionRepository
import goods.pocket.app.domain.collection.MarkPreorderReceivedUseCase
import goods.pocket.app.domain.dashboard.GetDashboardSummaryUseCase
import goods.pocket.app.domain.dashboard.GetRecentActivitiesUseCase
import goods.pocket.app.domain.event.EventRepository
import goods.pocket.app.domain.service.AppClock
import goods.pocket.app.domain.service.IdGenerator
import goods.pocket.app.domain.settings.SettingsRepository
import goods.pocket.app.presentation.PresentationSessionFactory
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module

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
    @Factory
    fun presentationSessionFactory(
        collectionRepository: CollectionRepository,
        eventRepository: EventRepository,
        settingsRepository: SettingsRepository,
        clock: AppClock,
        idGenerator: IdGenerator,
        receive: MarkPreorderReceivedUseCase,
        dashboard: GetDashboardSummaryUseCase,
        activities: GetRecentActivitiesUseCase,
    ): PresentationSessionFactory = PresentationSessionFactory(
        collectionRepository,
        eventRepository,
        settingsRepository,
        clock,
        idGenerator,
        receive,
        dashboard,
        activities,
    )
}
