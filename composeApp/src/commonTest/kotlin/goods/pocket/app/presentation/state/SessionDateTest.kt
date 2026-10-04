package goods.pocket.app.presentation.state

import goods.pocket.app.domain.service.AppClock
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
class SessionDateTest {
    @Test
    fun exposesCurrentDateImmediately() = runTest {
        val clock = MutableDateClock("2026-10-31")
        val session = SessionDate(clock, backgroundScope)

        assertEquals("2026-10-31", session.date.value)
    }

    @Test
    fun pollingUpdatesAcrossMidnightAndMonthBoundaryWithoutDatabaseChanges() = runTest {
        val clock = MutableDateClock("2026-10-31")
        val session = SessionDate(clock, backgroundScope)
        runCurrent()

        clock.today = "2026-11-01"
        advanceTimeBy(59_999)
        runCurrent()
        assertEquals("2026-10-31", session.date.value)

        advanceTimeBy(1)
        runCurrent()
        assertEquals("2026-11-01", session.date.value)
    }

    @Test
    fun refreshUpdatesImmediatelyAndUnchangedDatesDoNotEmitAgain() = runTest {
        val clock = MutableDateClock("2026-12-31")
        val session = SessionDate(clock, backgroundScope)
        val dates = mutableListOf<String>()
        backgroundScope.launch { session.date.collect { dates += it } }
        runCurrent()

        session.refresh()
        advanceTimeBy(60_000)
        runCurrent()
        assertEquals(listOf("2026-12-31"), dates)

        clock.today = "2027-01-01"
        session.refresh()
        runCurrent()
        assertEquals(listOf("2026-12-31", "2027-01-01"), dates)
    }

    @Test
    fun disposingOwnerStopsPolling() = runTest {
        val clock = MutableDateClock("2026-10-31")
        val owner = CoroutineScope(backgroundScope.coroutineContext + Job())
        val session = SessionDate(clock, owner)
        runCurrent()
        owner.cancel()
        runCurrent()
        val readsAtDisposal = clock.dateReads

        clock.today = "2026-11-01"
        advanceTimeBy(180_000)
        runCurrent()

        assertEquals("2026-10-31", session.date.value)
        assertEquals(readsAtDisposal, clock.dateReads)
    }
}

private class MutableDateClock(var today: String) : AppClock {
    var dateReads: Int = 0
        private set

    override fun currentDate(): String {
        dateReads += 1
        return today
    }

    override fun currentTimestamp(): String = "${today}T00:00:00Z"
}
