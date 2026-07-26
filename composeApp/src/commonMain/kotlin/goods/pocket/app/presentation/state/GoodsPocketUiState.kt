package goods.pocket.app.presentation.state

import goods.pocket.app.domain.model.CollectionEntry
import goods.pocket.app.domain.model.Event
import goods.pocket.app.domain.model.HomeSummary
import goods.pocket.app.domain.model.AppPreference
import goods.pocket.app.domain.model.EventType
import goods.pocket.app.presentation.navigation.AppDestination

data class GoodsPocketUiState(
    val isLoading: Boolean = false,
    val failure: GoodsPocketFailure? = null,
    val currentDestination: AppDestination = AppDestination.Home,
    val selectedPrimaryDestination: AppDestination = AppDestination.Home,
    val currentDate: String = "",
    val myPage: MyPageUiModel = MyPageUiModel(),
    val collectionQuery: String = "",
    val collectionSegment: CollectionSegment = CollectionSegment.OWNED,
    val eventTypeFilter: EventType? = null,
    val homeSummary: HomeSummary = HomeSummary(
        monthlySpend = 0,
        previousMonthSpend = 0,
        spendingBuckets = List(9) { 0L },
        ownedItemCount = 0,
        activePreorderCount = 0,
        recentActivities = emptyList(),
    ),
    val upcomingEvents: List<Event> = emptyList(),
    val collectionEntries: List<CollectionEntry> = emptyList(),
    val events: List<Event> = emptyList(),
    val appPreferences: AppPreference = AppPreference(),
    val isQuickAddOpen: Boolean = false,
    val quickAddTarget: QuickAddTarget = QuickAddTarget.COLLECTION_ENTRY,
    val activeDetail: ActiveDetail? = null,
    val activeEditor: ActiveEditor? = null,
    val pendingDelete: PendingDelete? = null,
)

data class GoodsPocketFailure(
    val operation: GoodsPocketOperation,
)

enum class GoodsPocketOperation {
    LOAD,
    SAVE,
    UPDATE,
    DELETE,
}
