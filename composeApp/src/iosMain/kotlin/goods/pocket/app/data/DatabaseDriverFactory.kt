package goods.pocket.app.data

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import goods.pocket.app.db.GoodsPocketDatabase

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver = NativeSqliteDriver(
        schema = GoodsPocketDatabase.Schema,
        name = "GoodsPocket-v2.db",
        onConfiguration = { configuration ->
            configuration.copy(
                extendedConfig = configuration.extendedConfig.copy(
                    foreignKeyConstraints = true,
                ),
            )
        },
    )
}
