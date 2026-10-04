package goods.pocket.app.presentation.state

import goods.pocket.app.domain.service.AppClock
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class SessionDate(private val clock: AppClock, scope: CoroutineScope) {
    private val _date = MutableStateFlow(clock.currentDate())
    val date: StateFlow<String> = _date.asStateFlow()

    init {
        scope.launch {
            while (isActive) {
                delay(DATE_REFRESH_INTERVAL_MILLIS)
                refresh()
            }
        }
    }

    fun refresh() {
        _date.value = clock.currentDate()
    }
}

private const val DATE_REFRESH_INTERVAL_MILLIS: Long = 60_000L
