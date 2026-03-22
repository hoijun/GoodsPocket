package goods.pocket.app.data.di

import goods.pocket.app.data.DatabaseDriverFactory
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
actual class PlatformDataModule {
    @Single
    actual fun databaseDriverFactory(): DatabaseDriverFactory = DatabaseDriverFactory()
}
