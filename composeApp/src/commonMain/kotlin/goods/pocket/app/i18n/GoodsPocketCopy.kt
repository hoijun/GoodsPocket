package goods.pocket.app.i18n

const val DEFAULT_LANGUAGE_CODE = "ko"

fun formatCurrencyByLanguage(amount: Long, languageCode: String): String {
    val grouped = amount.toString().reversed().chunked(3).joinToString(",").reversed()
    return if (isKoreanLanguage(languageCode)) {
        "${grouped}원"
    } else {
        "$grouped KRW"
    }
}

fun localizedLocalProfileLabel(languageCode: String): String {
    return if (isKoreanLanguage(languageCode)) "로컬 프로필" else "Local Profile"
}

fun localizedSyncNotConnectedLabel(languageCode: String): String {
    return if (isKoreanLanguage(languageCode)) "연결되지 않음" else "Not connected"
}

fun localizedReceivedItemCategory(languageCode: String): String {
    return if (isKoreanLanguage(languageCode)) "예약 굿즈" else "Preorder Item"
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
