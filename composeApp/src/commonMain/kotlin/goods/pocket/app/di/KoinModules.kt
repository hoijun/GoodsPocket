package goods.pocket.app.di

import goods.pocket.app.data.di.GoodsPocketDataModule
import goods.pocket.app.data.di.PlatformDataModule
import goods.pocket.app.data.di.GoodsPocketUseCaseModule
import goods.pocket.app.presentation.state.GoodsPocketAppStateHolder
import goods.pocket.app.presentation.state.GoodsPocketContentLoader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
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
    fun applicationScope(): CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    @Single
    fun goodsPocketContentLoader(
        collectionRepository: goods.pocket.app.domain.repository.CollectionRepository,
        eventRepository: goods.pocket.app.domain.repository.EventRepository,
        settingsRepository: goods.pocket.app.domain.repository.SettingsRepository,
        getDashboardSummaryUseCase: goods.pocket.app.domain.usecase.GetDashboardSummaryUseCase,
        getRecentActivitiesUseCase: goods.pocket.app.domain.usecase.GetRecentActivitiesUseCase,
        clock: goods.pocket.app.domain.service.AppClock,
    ) = GoodsPocketContentLoader(
        collectionRepository = collectionRepository,
        eventRepository = eventRepository,
        settingsRepository = settingsRepository,
        getDashboardSummaryUseCase = getDashboardSummaryUseCase,
        getRecentActivitiesUseCase = getRecentActivitiesUseCase,
        clock = clock,
    )

    @Single
    fun goodsPocketAppStateHolder(
        collectionRepository: goods.pocket.app.domain.repository.CollectionRepository,
        preorderRepository: goods.pocket.app.domain.repository.PreorderRepository,
        eventRepository: goods.pocket.app.domain.repository.EventRepository,
        settingsRepository: goods.pocket.app.domain.repository.SettingsRepository,
        contentLoader: GoodsPocketContentLoader,
        markPreorderReceivedUseCase: goods.pocket.app.domain.usecase.MarkPreorderReceivedUseCase,
        clock: goods.pocket.app.domain.service.AppClock,
        idGenerator: goods.pocket.app.domain.service.IdGenerator,
        coroutineScope: CoroutineScope,
    ) = GoodsPocketAppStateHolder(
        collectionRepository = collectionRepository,
        preorderRepository = preorderRepository,
        eventRepository = eventRepository,
        settingsRepository = settingsRepository,
        contentLoader = contentLoader,
        markPreorderReceivedUseCase = markPreorderReceivedUseCase,
        clock = clock,
        idGenerator = idGenerator,
        coroutineScope = coroutineScope,
    )
}
