package goods.pocket.app.data

import goods.pocket.app.data.local.GoodsPocketLocalDataSource
import goods.pocket.app.data.local.InMemoryGoodsPocketLocalDataSource
import goods.pocket.app.data.local.model.LocalItemRecord
import goods.pocket.app.data.repository.GoodsPocketRepository
import goods.pocket.app.domain.repository.RepositoryFailure
import goods.pocket.app.domain.repository.RepositoryOperation
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class GoodsPocketRepositoryFailureTest {

    @Test
    fun `datasource exceptions are translated at the repository boundary`() = runTest {
        val repository = GoodsPocketRepository(ThrowingItemsLocalDataSource())

        val failure = assertFailsWith<RepositoryFailure> {
            repository.getItems()
        }

        assertEquals(RepositoryOperation.READ, failure.operation)
    }
}

private class ThrowingItemsLocalDataSource(
    delegate: GoodsPocketLocalDataSource = InMemoryGoodsPocketLocalDataSource(),
) : GoodsPocketLocalDataSource by delegate {
    override fun getItems(filter: String?): List<LocalItemRecord> {
        error("Database unavailable")
    }
}
