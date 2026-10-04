package goods.pocket.app.data.di

import goods.pocket.app.domain.collection.CollectionRepository
import goods.pocket.app.domain.collection.MarkPreorderReceivedUseCase
import goods.pocket.app.domain.dashboard.GetDashboardSummaryUseCase
import goods.pocket.app.domain.dashboard.GetRecentActivitiesUseCase
import goods.pocket.app.domain.service.AppClock
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
class GoodsPocketUseCaseModule {
    @Single
    fun getDashboardSummaryUseCase(
        collectionRepository: CollectionRepository,
    ): GetDashboardSummaryUseCase = GetDashboardSummaryUseCase(collectionRepository)

    @Single
    fun getRecentActivitiesUseCase(
        collectionRepository: CollectionRepository,
    ): GetRecentActivitiesUseCase = GetRecentActivitiesUseCase(collectionRepository)

    @Single
    fun markPreorderReceivedUseCase(
        collectionRepository: CollectionRepository,
        clock: AppClock,
    ): MarkPreorderReceivedUseCase = MarkPreorderReceivedUseCase(collectionRepository, clock)
}
