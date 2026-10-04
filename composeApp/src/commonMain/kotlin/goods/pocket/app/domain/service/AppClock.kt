package goods.pocket.app.domain.service

interface AppClock {
    fun currentDate(): String

    fun currentTimestamp(): String

    fun currentMonth(): String = currentDate().take(YEAR_MONTH_LENGTH)
}

private const val YEAR_MONTH_LENGTH = 7
