package goods.pocket.app.data.di

import goods.pocket.app.data.local.GoodsPocketLocalDataSource
import goods.pocket.app.data.local.SqlDelightGoodsPocketLocalDataSource
import goods.pocket.app.data.repository.GoodsPocketRepository
import goods.pocket.app.domain.repository.CollectionRepository
import goods.pocket.app.domain.repository.EventRepository
import goods.pocket.app.domain.repository.PreorderRepository
import goods.pocket.app.domain.repository.SettingsRepository
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
class GoodsPocketDataModule {
    @Single(binds = [GoodsPocketLocalDataSource::class])
    fun goodsPocketLocalDataSource(
        driverFactory: goods.pocket.app.data.DatabaseDriverFactory,
    ): SqlDelightGoodsPocketLocalDataSource = SqlDelightGoodsPocketLocalDataSource(driverFactory)

    @Single(
        binds = [
            CollectionRepository::class,
            PreorderRepository::class,
            EventRepository::class,
            SettingsRepository::class,
        ],
    )
    fun goodsPocketRepository(
        localDataSource: GoodsPocketLocalDataSource,
    ): GoodsPocketRepository = GoodsPocketRepository(localDataSource)
}
