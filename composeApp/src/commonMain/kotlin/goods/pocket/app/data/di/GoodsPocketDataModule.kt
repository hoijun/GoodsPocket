package goods.pocket.app.data.di

import goods.pocket.app.data.local.GoodsPocketLocalDataSource
import goods.pocket.app.data.local.SqlDelightGoodsPocketLocalDataSource
import goods.pocket.app.data.repository.GoodsPocketRepository
import goods.pocket.app.data.RandomIdGenerator
import goods.pocket.app.data.SystemAppClock
import goods.pocket.app.domain.repository.CollectionRepository
import goods.pocket.app.domain.repository.EventRepository
import goods.pocket.app.domain.repository.PreorderRepository
import goods.pocket.app.domain.repository.SettingsRepository
import goods.pocket.app.domain.service.AppClock
import goods.pocket.app.domain.service.IdGenerator
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
class GoodsPocketDataModule {
    @Single(binds = [AppClock::class])
    fun appClock(): SystemAppClock = SystemAppClock()

    @Single(binds = [IdGenerator::class])
    fun idGenerator(): RandomIdGenerator = RandomIdGenerator()

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
