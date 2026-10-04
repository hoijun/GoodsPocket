package goods.pocket.app.data.di

import goods.pocket.app.data.DatabaseDriverFactory
import goods.pocket.app.data.local.GoodsPocketStore
import goods.pocket.app.data.repository.SqlCollectionRepository
import goods.pocket.app.data.repository.SqlEventRepository
import goods.pocket.app.data.repository.SqlSettingsRepository
import goods.pocket.app.data.runtime.RandomIdGenerator
import goods.pocket.app.data.runtime.SystemAppClock
import goods.pocket.app.domain.collection.CollectionRepository
import goods.pocket.app.domain.event.EventRepository
import goods.pocket.app.domain.service.AppClock
import goods.pocket.app.domain.service.IdGenerator
import goods.pocket.app.domain.settings.SettingsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
class GoodsPocketDataModule {
    @Single(binds = [AppClock::class])
    fun appClock(): SystemAppClock = SystemAppClock()

    @Single(binds = [IdGenerator::class])
    fun idGenerator(): RandomIdGenerator = RandomIdGenerator()

    @Single
    fun databaseDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Single
    fun goodsPocketStore(
        driverFactory: DatabaseDriverFactory,
        dispatcher: CoroutineDispatcher,
    ): GoodsPocketStore = GoodsPocketStore(driverFactory::createDriver, dispatcher)

    @Single(binds = [CollectionRepository::class])
    fun collectionRepository(store: GoodsPocketStore): SqlCollectionRepository =
        SqlCollectionRepository(store)

    @Single(binds = [EventRepository::class])
    fun eventRepository(store: GoodsPocketStore): SqlEventRepository = SqlEventRepository(store)

    @Single(binds = [SettingsRepository::class])
    fun settingsRepository(store: GoodsPocketStore): SqlSettingsRepository =
        SqlSettingsRepository(store)
}
