package goods.pocket.app.presentation.screen

sealed interface HomeAction {
    data object OpenScheduleOverview : HomeAction
    data object OpenAllCollection : HomeAction
    data object OpenOwnedCollection : HomeAction
    data object OpenReservedCollection : HomeAction
    data object OpenRecentCollection : HomeAction
    data object OpenQuickAdd : HomeAction
    data class OpenRecentEntry(val entryId: String) : HomeAction
    data class OpenEvent(val eventId: String) : HomeAction
}
