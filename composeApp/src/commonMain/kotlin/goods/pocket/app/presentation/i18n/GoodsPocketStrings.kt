package goods.pocket.app.presentation.i18n

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.key
import androidx.compose.runtime.staticCompositionLocalOf
import goods.pocket.app.domain.model.CollectionEntryStatus
import goods.pocket.app.domain.model.EventType
import goods.pocket.app.domain.model.ItemStatus
import goods.pocket.app.domain.model.PreorderStatus
import goods.pocket.app.i18n.formatCurrencyByLanguage
import goods.pocket.app.presentation.navigation.AppDestination
import goods.pocket.app.presentation.state.CollectionSegment
import goods.pocket.app.presentation.state.QuickAddTarget
import goodspocket.composeapp.generated.resources.*
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

enum class AppLanguage(val code: String) {
    KOREAN("ko"),
    ENGLISH("en"),
}

val LocalAppLanguageCode = staticCompositionLocalOf { AppLanguage.KOREAN.code }

@Composable
fun ProvideLocalizedResources(
    languageCode: String,
    content: @Composable () -> Unit,
) {
    val appliedLanguageCode = ApplyAppLanguage(languageCode)
    CompositionLocalProvider(
        LocalAppLanguageCode provides appliedLanguageCode,
    ) {
        key(appliedLanguageCode) {
            content()
        }
    }
}

@Composable
expect fun ApplyAppLanguage(languageCode: String): String

@Composable
fun tr(
    resource: StringResource,
    vararg args: Any,
): String {
    return stringResource(resource, *args)
}

@Composable
fun formatCurrency(amount: Long): String {
    return formatCurrencyByLanguage(amount, LocalAppLanguageCode.current)
}

@Composable
fun AppDestination.localizedLabel(): String {
    return when (this) {
        AppDestination.Home -> tr(Res.string.nav_home)
        AppDestination.Collection -> tr(Res.string.nav_collection)
        AppDestination.My -> tr(Res.string.nav_my)
        AppDestination.Events -> tr(Res.string.nav_events)
        AppDestination.Settings -> tr(Res.string.nav_settings)
    }
}

@Composable
fun QuickAddTarget.localizedLabel(): String {
    return when (this) {
        QuickAddTarget.COLLECTION_ENTRY -> tr(Res.string.target_collection_entry)
        QuickAddTarget.EVENT -> tr(Res.string.target_event)
    }
}

@Composable
fun CollectionSegment.localizedLabel(): String {
    return when (this) {
        CollectionSegment.OWNED -> tr(Res.string.collection_segment_owned)
        CollectionSegment.RESERVED -> tr(Res.string.collection_segment_reserved)
        CollectionSegment.ALL -> tr(Res.string.collection_segment_all)
    }
}

@Composable
fun CollectionEntryStatus.localizedLabel(): String {
    return when (this) {
        CollectionEntryStatus.RESERVED -> tr(Res.string.collection_entry_status_reserved)
        CollectionEntryStatus.OWNED -> tr(Res.string.collection_entry_status_owned)
        CollectionEntryStatus.PLANNED_CLEANUP -> tr(Res.string.collection_entry_status_planned_cleanup)
    }
}

@Composable
fun ItemStatus.localizedLabel(): String {
    return when (this) {
        ItemStatus.OWNED -> tr(Res.string.item_status_owned)
        ItemStatus.PLANNED_CLEANUP -> tr(Res.string.item_status_planned_cleanup)
    }
}

@Composable
fun PreorderStatus.localizedLabel(): String {
    return when (this) {
        PreorderStatus.ACTIVE -> tr(Res.string.preorder_status_active)
        PreorderStatus.PAYMENT_PENDING -> tr(Res.string.preorder_status_payment_pending)
        PreorderStatus.RECEIVED -> tr(Res.string.preorder_status_received)
        PreorderStatus.CANCELED -> tr(Res.string.preorder_status_canceled)
    }
}

@Composable
fun EventType.localizedLabel(): String {
    return when (this) {
        EventType.RELEASE -> tr(Res.string.event_type_release)
        EventType.PAYMENT_DUE -> tr(Res.string.event_type_payment_due)
        EventType.DELIVERY -> tr(Res.string.event_type_delivery)
        EventType.OFFLINE_EVENT -> tr(Res.string.event_type_offline_event)
    }
}
