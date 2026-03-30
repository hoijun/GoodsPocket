package goods.pocket.app.data

import goods.pocket.app.data.local.InMemoryGoodsPocketLocalDataSource
import goods.pocket.app.data.repository.GoodsPocketRepository
import goods.pocket.app.domain.repository.CollectionRepository
import goods.pocket.app.domain.repository.EventRepository
import goods.pocket.app.domain.repository.PreorderRepository
import goods.pocket.app.domain.repository.SettingsRepository

class InMemoryGoodsPocketRepository(
    private val delegate: GoodsPocketRepository = GoodsPocketRepository(InMemoryGoodsPocketLocalDataSource()),
) : CollectionRepository by delegate,
    PreorderRepository by delegate,
    EventRepository by delegate,
    SettingsRepository by delegate
