package goods.pocket.app.data.runtime

import goods.pocket.app.domain.service.AppClock
import goods.pocket.app.domain.service.IdGenerator
import kotlin.time.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

class SystemAppClock : AppClock {
    override fun currentDate(): String =
        Clock.System.todayIn(TimeZone.currentSystemDefault()).toString()

    override fun currentTimestamp(): String = Clock.System.now().toString()
}

class RandomIdGenerator : IdGenerator {
    @OptIn(ExperimentalUuidApi::class)
    override fun generate(prefix: String): String = "$prefix-${Uuid.random()}"
}
