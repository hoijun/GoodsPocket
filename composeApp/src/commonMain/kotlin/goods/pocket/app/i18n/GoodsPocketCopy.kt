package goods.pocket.app.i18n

import goods.pocket.app.domain.model.TransactionType

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

fun localizedRecentTransactionTitle(languageCode: String, amount: Long): String {
    return formatCurrencyByLanguage(amount, languageCode)
}

fun localizedRecentTransactionSubtitle(languageCode: String, type: TransactionType): String {
    return if (isKoreanLanguage(languageCode)) {
        "${localizedTransactionTypeLabel(type, languageCode)} 거래 기록"
    } else {
        "Transaction recorded as ${localizedTransactionTypeLabel(type, languageCode)}"
    }
}

fun localizedTransactionTypeLabel(type: TransactionType, languageCode: String): String {
    return if (isKoreanLanguage(languageCode)) {
        when (type) {
            TransactionType.DEPOSIT -> "예약금"
            TransactionType.BALANCE -> "잔금"
            TransactionType.PURCHASE -> "구매"
            TransactionType.SHIPPING -> "배송비"
            TransactionType.REFUND -> "환불"
            TransactionType.TRANSFER_INCOME -> "양도 수입"
        }
    } else {
        when (type) {
            TransactionType.DEPOSIT -> "Deposit"
            TransactionType.BALANCE -> "Balance"
            TransactionType.PURCHASE -> "Purchase"
            TransactionType.SHIPPING -> "Shipping"
            TransactionType.REFUND -> "Refund"
            TransactionType.TRANSFER_INCOME -> "Transfer Income"
        }
    }
}

private fun isKoreanLanguage(languageCode: String): Boolean {
    return languageCode.startsWith(DEFAULT_LANGUAGE_CODE)
}
