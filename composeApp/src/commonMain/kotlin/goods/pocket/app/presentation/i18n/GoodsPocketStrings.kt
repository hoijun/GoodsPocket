package goods.pocket.app.presentation.i18n

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.key
import androidx.compose.runtime.staticCompositionLocalOf
import goods.pocket.app.domain.model.EventType
import goods.pocket.app.domain.model.ItemStatus
import goods.pocket.app.domain.model.PreorderStatus
import goods.pocket.app.domain.model.TransactionType
import goods.pocket.app.presentation.navigation.AppDestination
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
    val grouped = amount.toString().reversed().chunked(3).joinToString(",").reversed()
    return if (LocalAppLanguageCode.current == AppLanguage.KOREAN.code) {
        "${grouped}원"
    } else {
        "$grouped KRW"
    }
}

@Composable
fun AppDestination.localizedLabel(): String {
    return when (this) {
        AppDestination.Home -> tr(Res.string.nav_home)
        AppDestination.Collection -> tr(Res.string.nav_collection)
        AppDestination.Preorders -> tr(Res.string.nav_preorders)
        AppDestination.Transactions -> tr(Res.string.nav_transactions)
        AppDestination.Events -> tr(Res.string.nav_events)
        AppDestination.Settings -> tr(Res.string.nav_settings)
    }
}

@Composable
fun QuickAddTarget.localizedLabel(): String {
    return when (this) {
        QuickAddTarget.ITEM -> tr(Res.string.target_item)
        QuickAddTarget.PREORDER -> tr(Res.string.target_preorder)
        QuickAddTarget.TRANSACTION -> tr(Res.string.target_transaction)
        QuickAddTarget.EVENT -> tr(Res.string.target_event)
    }
}

@Composable
fun ItemStatus.localizedLabel(): String {
    return when (this) {
        ItemStatus.OWNED -> tr(Res.string.item_status_owned)
        ItemStatus.WAITING_DELIVERY -> tr(Res.string.item_status_waiting_delivery)
        ItemStatus.PLANNED_TRANSFER -> tr(Res.string.item_status_planned_transfer)
        ItemStatus.LOST -> tr(Res.string.item_status_lost)
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
fun TransactionType.localizedLabel(): String {
    return when (this) {
        TransactionType.DEPOSIT -> tr(Res.string.transaction_type_deposit)
        TransactionType.BALANCE -> tr(Res.string.transaction_type_balance)
        TransactionType.PURCHASE -> tr(Res.string.transaction_type_purchase)
        TransactionType.SHIPPING -> tr(Res.string.transaction_type_shipping)
        TransactionType.REFUND -> tr(Res.string.transaction_type_refund)
        TransactionType.TRANSFER_INCOME -> tr(Res.string.transaction_type_transfer_income)
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
