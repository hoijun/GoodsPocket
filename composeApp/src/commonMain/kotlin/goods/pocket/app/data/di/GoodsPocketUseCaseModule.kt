package goods.pocket.app.data.di

import goods.pocket.app.domain.repository.CollectionRepository
import goods.pocket.app.domain.repository.PreorderRepository
import goods.pocket.app.domain.service.AppClock
import goods.pocket.app.domain.usecase.GetDashboardSummaryUseCase
import goods.pocket.app.domain.usecase.GetRecentActivitiesUseCase
import goods.pocket.app.domain.usecase.MarkPreorderReceivedUseCase
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
class GoodsPocketUseCaseModule {
    @Single
    fun getDashboardSummaryUseCase(
        collectionRepository: CollectionRepository,
        preorderRepository: PreorderRepository,
    ) = GetDashboardSummaryUseCase(collectionRepository, preorderRepository)

    @Single
    fun getRecentActivitiesUseCase(
        collectionRepository: CollectionRepository,
        preorderRepository: PreorderRepository,
    ) = GetRecentActivitiesUseCase(collectionRepository, preorderRepository)

    @Single
    fun markPreorderReceivedUseCase(
        collectionRepository: CollectionRepository,
        clock: AppClock,
    ) = MarkPreorderReceivedUseCase(collectionRepository, clock)
}
