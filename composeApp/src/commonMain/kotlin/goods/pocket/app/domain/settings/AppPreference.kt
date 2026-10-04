package goods.pocket.app.domain.settings

data class AppPreference(
    val currencyCode: String = "KRW",
    val dateFormat: String = "yyyy-MM-dd",
    val languageCode: String = "ko",
)
