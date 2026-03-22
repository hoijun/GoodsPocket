package goods.pocket.app.data

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import goods.pocket.app.db.GoodsPocketDatabase

internal lateinit var androidDatabaseContext: android.content.Context

internal fun initializeAndroidDatabaseContext(context: android.content.Context) {
    androidDatabaseContext = context.applicationContext
}

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(
            schema = GoodsPocketDatabase.Schema,
            context = androidDatabaseContext,
            name = "GoodsPocket.db",
        )
    }
}
