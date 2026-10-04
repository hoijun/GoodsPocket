package goods.pocket.app.presentation.i18n

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.key
import androidx.compose.runtime.staticCompositionLocalOf
import goods.pocket.app.domain.collection.CollectionEntry
import goods.pocket.app.domain.collection.CollectionEntryStatus
import goods.pocket.app.domain.collection.GOODS_COLLECTION_CATEGORY_CODE
import goods.pocket.app.domain.collection.RESERVED_COLLECTION_CATEGORY_CODE
import goods.pocket.app.domain.event.EventType
import goods.pocket.app.presentation.navigation.AppDestination
import goods.pocket.app.presentation.state.CollectionSegment
import goods.pocket.app.presentation.state.QuickAddTarget
import goodspocket.composeapp.generated.resources.Res
import goodspocket.composeapp.generated.resources.collection_category_goods
import goodspocket.composeapp.generated.resources.collection_category_reserved
import goodspocket.composeapp.generated.resources.collection_entry_status_owned
import goodspocket.composeapp.generated.resources.collection_entry_status_planned_cleanup
import goodspocket.composeapp.generated.resources.collection_entry_status_reserved
import goodspocket.composeapp.generated.resources.collection_segment_all
import goodspocket.composeapp.generated.resources.collection_segment_owned
import goodspocket.composeapp.generated.resources.collection_segment_reserved
import goodspocket.composeapp.generated.resources.event_type_delivery
import goodspocket.composeapp.generated.resources.event_type_offline_event
import goodspocket.composeapp.generated.resources.event_type_payment_due
import goodspocket.composeapp.generated.resources.event_type_release
import goodspocket.composeapp.generated.resources.nav_collection
import goodspocket.composeapp.generated.resources.nav_events
import goodspocket.composeapp.generated.resources.nav_home
import goodspocket.composeapp.generated.resources.nav_my
import goodspocket.composeapp.generated.resources.nav_settings
import goodspocket.composeapp.generated.resources.target_collection_entry
import goodspocket.composeapp.generated.resources.target_event
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

enum class AppLanguage(val code: String) {
    KOREAN("ko"),
    ENGLISH("en"),
}

val LocalAppLanguageCode = staticCompositionLocalOf { AppLanguage.KOREAN.code }
val LocalCurrencyCode = staticCompositionLocalOf { "KRW" }
val LocalDateFormat = staticCompositionLocalOf { "yyyy-MM-dd" }

@Composable
fun ProvideLocalizedResources(
    languageCode: String,
    currencyCode: String,
    dateFormat: String,
    content: @Composable () -> Unit,
) {
    val appliedLanguageCode = ApplyAppLanguage(languageCode)
    CompositionLocalProvider(
        LocalAppLanguageCode provides appliedLanguageCode,
        LocalCurrencyCode provides currencyCode,
        LocalDateFormat provides dateFormat,
    ) {
        key(appliedLanguageCode) {
            content()
        }
    }
}

@Composable
expect fun ApplyAppLanguage(languageCode: String): String

@Composable
fun tr(resource: StringResource, vararg args: Any): String = stringResource(resource, *args)

@Composable
fun formatCurrency(amount: Long): String = formatCurrencyByPreference(
    amount = amount,
    currencyCode = LocalCurrencyCode.current,
    languageCode = LocalAppLanguageCode.current,
)

@Composable
fun formatDate(isoDate: String): String = formatDateByPreference(isoDate, LocalDateFormat.current)

@Composable
fun AppDestination.localizedLabel(): String = when (this) {
    AppDestination.Home -> tr(Res.string.nav_home)
    AppDestination.Collection -> tr(Res.string.nav_collection)
    AppDestination.My -> tr(Res.string.nav_my)
    AppDestination.Events -> tr(Res.string.nav_events)
    AppDestination.Settings -> tr(Res.string.nav_settings)
}

@Composable
fun QuickAddTarget.localizedLabel(): String = when (this) {
    QuickAddTarget.COLLECTION_ENTRY -> tr(Res.string.target_collection_entry)
    QuickAddTarget.EVENT -> tr(Res.string.target_event)
}

@Composable
fun CollectionSegment.localizedLabel(): String = when (this) {
    CollectionSegment.OWNED -> tr(Res.string.collection_segment_owned)
    CollectionSegment.RESERVED -> tr(Res.string.collection_segment_reserved)
    CollectionSegment.ALL -> tr(Res.string.collection_segment_all)
}

@Composable
fun CollectionEntryStatus.localizedLabel(): String = when (this) {
    CollectionEntryStatus.RESERVED -> tr(Res.string.collection_entry_status_reserved)
    CollectionEntryStatus.OWNED -> tr(Res.string.collection_entry_status_owned)
    CollectionEntryStatus.PLANNED_CLEANUP -> tr(
        Res.string.collection_entry_status_planned_cleanup,
    )
}

@Composable
fun CollectionEntry.localizedCategory(): String = when (category) {
    RESERVED_COLLECTION_CATEGORY_CODE -> tr(Res.string.collection_category_reserved)
    GOODS_COLLECTION_CATEGORY_CODE -> tr(Res.string.collection_category_goods)
    else -> category
}

@Composable
fun EventType.localizedLabel(): String = when (this) {
    EventType.RELEASE -> tr(Res.string.event_type_release)
    EventType.PAYMENT_DUE -> tr(Res.string.event_type_payment_due)
    EventType.DELIVERY -> tr(Res.string.event_type_delivery)
    EventType.OFFLINE_EVENT -> tr(Res.string.event_type_offline_event)
}
