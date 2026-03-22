
Repository and UseCase Design
Repository Interfaces
CollectionRepository
- getItems(filter)
- getItem(id)
- saveItem(item)
- deleteItem(id)
- getItemTransactions(itemId)
- countOwnedItems()
PreorderRepository
- getPreorders(status)
- getPreorder(id)
- savePreorder(preorder)
- markAsReceived(preorderId, receiveDate)
- cancelPreorder(preorderId)
- countActivePreorders()
TransactionRepository
- getTransactions(monthFilter)
- getMonthlySummary(monthFilter)
- saveTransaction(transaction)
- deleteTransaction(id)
EventRepository
- getUpcomingEvents(limit)
- getEvents(dateRange, type)
- saveEvent(event)
- deleteEvent(id)
SettingsRepository
- getStorageLocations()
- saveStorageLocation(location)
- deleteStorageLocation(id)
- getAppPreferences()
- updateAppPreferences(preferences)
UseCases
Home
- GetDashboardSummary
- GetUpcomingEvents
- GetRecentActivities
Collection
- SearchCollectionItems
- GetCollectionDetail
- SaveCollectionItem
- DeleteCollectionItem
Preorder
- GetPreorderList
- SavePreorder
- MarkPreorderReceived
- CancelPreorder
Transaction
- GetMonthlyTransactions
- GetMonthlySpendSummary
- SaveTransaction
- DeleteTransaction
Event
- GetEventList
- SaveEvent
- DeleteEvent
Transactional Rules
- MarkPreorderReceived는 preorder 상태 변경, item 생성, transaction 확정 반영을 하나의 트랜잭션으로 처리한다.
- SavePreorder는 발매일/결제일 기반 이벤트 생성 또는 갱신을 선택적으로 수행한다.
- DeleteCollectionItem는 연결 거래가 있을 경우 경고를 반환하거나 선행 정리 절차를 요구한다.
Error Handling
- repository는 Result 타입 또는 domain error sealed class를 반환한다.
- 폼 저장 실패는 사용자에게 재시도 가능한 메시지로 변환한다.
- DB 제약 조건 실패는 개발 로그와 사용자 메시지를 분리한다.
