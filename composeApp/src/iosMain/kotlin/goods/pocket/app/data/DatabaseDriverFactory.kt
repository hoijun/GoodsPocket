package goods.pocket.app.data

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import goods.pocket.app.db.GoodsPocketDatabase

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(
            schema = GoodsPocketDatabase.Schema,
            name = "GoodsPocket.db",
        )
    }
}
