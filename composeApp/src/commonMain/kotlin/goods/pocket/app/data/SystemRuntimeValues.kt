package goods.pocket.app.data

import goods.pocket.app.domain.service.AppClock
import goods.pocket.app.domain.service.IdGenerator
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlin.random.Random
import kotlin.time.Clock

class SystemAppClock : AppClock {
    override fun currentDate(): String = Clock.System.todayIn(TimeZone.currentSystemDefault()).toString()
}

class RandomIdGenerator : IdGenerator {
    override fun generate(prefix: String): String {
        val suffix = Random.nextLong().toULong().toString(radix = ID_RADIX)
        return "$prefix-$suffix"
    }
}

private const val ID_RADIX = 36
