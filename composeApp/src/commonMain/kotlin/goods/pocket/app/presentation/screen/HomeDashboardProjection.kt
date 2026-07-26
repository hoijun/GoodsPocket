package goods.pocket.app.presentation.screen

import kotlinx.datetime.LocalDate
import kotlin.math.round
import kotlin.math.roundToInt

internal data class HomeMonth(
    val year: Int,
    val month: Int,
)

internal fun homeMonth(currentDate: String): HomeMonth? {
    val date = runCatching { LocalDate.parse(currentDate) }.getOrNull() ?: return null
    return HomeMonth(year = date.year, month = date.month.ordinal + 1)
}

internal fun dDayLabel(
    today: String,
    targetDate: String,
): String? {
    val current = runCatching { LocalDate.parse(today) }.getOrNull() ?: return null
    val target = runCatching { LocalDate.parse(targetDate) }.getOrNull() ?: return null
    val distance = target.toEpochDays() - current.toEpochDays()
    return when {
        distance > 0 -> "D-$distance"
        distance < 0 -> "D+${-distance}"
        else -> "D-day"
    }
}

internal fun spendingChangePercent(
    current: Long,
    previous: Long,
): Int? {
    if (previous <= 0L) return null
    return (((current - previous).toDouble() / previous.toDouble()) * 100).roundToInt()
}

internal fun spendingBarFractions(amounts: List<Long>): List<Float> {
    val maximum = amounts.maxOrNull()?.coerceAtLeast(0L) ?: 0L
    if (maximum == 0L) return List(amounts.size) { MINIMUM_BAR_FRACTION }
    return amounts.map { amount ->
        val normalized = amount.coerceAtLeast(0L).toFloat() / maximum.toFloat()
        round((MINIMUM_BAR_FRACTION + normalized * BAR_FRACTION_RANGE) * 100) / 100
    }
}

private const val MINIMUM_BAR_FRACTION = 0.18f
private const val BAR_FRACTION_RANGE = 0.82f
