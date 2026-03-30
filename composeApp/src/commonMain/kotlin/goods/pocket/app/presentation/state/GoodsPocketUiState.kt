package goods.pocket.app.presentation.state

import goods.pocket.app.domain.model.Event
import goods.pocket.app.domain.model.HomeSummary
import goods.pocket.app.domain.model.Item
import goods.pocket.app.domain.model.ItemStatus
import goods.pocket.app.domain.model.Preorder
import goods.pocket.app.domain.model.PreorderStatus
import goods.pocket.app.domain.model.AppPreference
import goods.pocket.app.domain.model.StorageLocation
import goods.pocket.app.domain.model.EventType
import goods.pocket.app.presentation.navigation.AppDestination

data class GoodsPocketUiState(
    val currentDestination: AppDestination = AppDestination.Home,
    val selectedPrimaryDestination: AppDestination = AppDestination.Home,
    val myPage: MyPageUiModel = MyPageUiModel(),
    val collectionQuery: String = "",
    val collectionStatusFilter: ItemStatus = ItemStatus.OWNED,
    val preorderStatusFilter: PreorderStatus? = null,
    val eventTypeFilter: EventType? = null,
    val homeSummary: HomeSummary = HomeSummary(
        monthlySpend = 0,
        ownedItemCount = 0,
        activePreorderCount = 0,
        recentActivities = emptyList(),
    ),
    val upcomingEvents: List<Event> = emptyList(),
    val collectionItems: List<Item> = emptyList(),
    val preorders: List<Preorder> = emptyList(),
    val events: List<Event> = emptyList(),
    val storageLocations: List<StorageLocation> = emptyList(),
    val appPreferences: AppPreference = AppPreference(),
    val isQuickAddOpen: Boolean = false,
    val quickAddTarget: QuickAddTarget = QuickAddTarget.ITEM,
    val activeDetail: ActiveDetail? = null,
    val activeEditor: ActiveEditor? = null,
    val pendingDelete: PendingDelete? = null,
)
