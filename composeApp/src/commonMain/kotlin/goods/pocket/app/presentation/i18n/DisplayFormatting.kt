package goods.pocket.app.presentation.i18n

fun formatCurrencyByPreference(amount: Long, currencyCode: String, languageCode: String): String {
    val grouped = amount.toString().reversed().chunked(3).joinToString(",").reversed()
    return when (currencyCode.uppercase()) {
        "KRW" -> if (isKoreanLanguage(languageCode)) "${grouped}원" else "$grouped KRW"
        "USD" -> "${'$'}$grouped"
        "JPY" -> "¥$grouped"
        "EUR" -> "€$grouped"
        else -> "$grouped ${currencyCode.uppercase()}"
    }
}

fun formatDateByPreference(isoDate: String, dateFormat: String): String {
    val parts = isoDate.split('-')
    if (parts.size != 3) return isoDate
    val (year, month, day) = parts
    if (year.length != 4 ||
        month.length != 2 ||
        day.length != 2 ||
        parts.any { part ->
            part.any { character -> !character.isDigit() }
        }
    ) {
        return isoDate
    }
    return when (dateFormat) {
        "MM/dd/yyyy" -> "$month/$day/$year"
        "dd/MM/yyyy" -> "$day/$month/$year"
        "yyyy.MM.dd" -> "$year.$month.$day"
        else -> isoDate
    }
}

private fun isKoreanLanguage(languageCode: String): Boolean = languageCode.startsWith("ko")
