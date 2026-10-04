package goods.pocket.app.data.local

import app.cash.sqldelight.Query
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.db.SqlDriver
import goods.pocket.app.db.GoodsPocketDatabase
import goods.pocket.app.domain.repository.RepositoryFailure
import goods.pocket.app.domain.repository.RepositoryOperation
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

class GoodsPocketStore(
    private val driverProvider: () -> SqlDriver,
    private val dispatcher: CoroutineDispatcher,
) {
    private val mutex = Mutex()
    private val database: GoodsPocketDatabase by lazy {
        val driver = driverProvider()
        driver.execute(null, "PRAGMA foreign_keys = ON", 0)
        GoodsPocketDatabase(driver)
    }

    suspend fun <T> read(block: (GoodsPocketDatabase) -> T): T =
        execute(RepositoryOperation.READ, block)

    suspend fun <T> write(block: (GoodsPocketDatabase) -> T): T =
        execute(RepositoryOperation.WRITE, block)

    fun <Row : Any, Model> observe(
        query: (GoodsPocketDatabase) -> Query<Row>,
        mapper: (Row) -> Model,
    ): Flow<List<Model>> = flow {
        val observable = read(query)
        emitAll(observable.asFlow().mapToList(dispatcher).map { rows -> rows.map(mapper) })
    }.flowOn(dispatcher).catch { error -> throw translated(RepositoryOperation.READ, error) }

    private suspend fun <T> execute(
        operation: RepositoryOperation,
        block: (GoodsPocketDatabase) -> T,
    ): T = withContext(dispatcher) {
        mutex.withLock {
            try {
                block(database)
            } catch (error: Exception) {
                throw translated(operation, error)
            }
        }
    }
}

private fun translated(operation: RepositoryOperation, error: Throwable): Throwable = when (error) {
    is CancellationException, is RepositoryFailure -> error
    else -> RepositoryFailure(operation, error)
}
