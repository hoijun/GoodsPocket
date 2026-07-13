package goods.pocket.app.i18n

const val DEFAULT_LANGUAGE_CODE = "ko"

fun formatCurrencyByLanguage(amount: Long, languageCode: String): String {
    return formatCurrencyByPreference(amount, currencyCode = "KRW", languageCode = languageCode)
}

fun formatCurrencyByPreference(
    amount: Long,
    currencyCode: String,
    languageCode: String,
): String {
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
    if (year.length != 4 || month.length != 2 || day.length != 2 || parts.any { part ->
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

fun localizedLocalProfileLabel(languageCode: String): String {
    return if (isKoreanLanguage(languageCode)) "로컬 프로필" else "Local Profile"
}

fun localizedSyncNotConnectedLabel(languageCode: String): String {
    return if (isKoreanLanguage(languageCode)) "연결되지 않음" else "Not connected"
}

fun localizedRecentItemAddedSubtitle(languageCode: String): String {
    return if (isKoreanLanguage(languageCode)) "컬렉션 굿즈 추가" else "Collection item added"
}

fun localizedRecentPreorderTrackedSubtitle(languageCode: String, storeName: String): String {
    return if (isKoreanLanguage(languageCode)) {
        "$storeName 예약 추적"
    } else {
        "Preorder tracked at $storeName"
    }
}

private fun isKoreanLanguage(languageCode: String): Boolean {
    return languageCode.startsWith(DEFAULT_LANGUAGE_CODE)
}
