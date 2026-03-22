
ViewModel Design
Shared Design Principles
- 모든 ViewModel은 StateFlow 기반으로 상태를 노출한다.
- 초기 진입 시 loadData()를 호출하고, 저장/삭제 후에는 필요한 목록만 최소 갱신한다.
- 장시간 작업은 loading flag와 submitting flag를 분리해 표현한다.
HomeViewModel
Responsibilities
- 이번 달 지출 총액 계산
- 보유 굿즈 수, 활성 예약 수, 다가오는 일정 목록 로드
- 홈 카드 탭 액션에 맞는 네비게이션 이벤트 발행
UiState
- monthlySpend
- ownedItemCount
- activePreorderCount
- upcomingEvents
- recentActivities
- isLoading
UiActions
- Refresh
- ClickSummaryCard
- ClickUpcomingEvent
- ClickQuickAdd
CollectionViewModel
Responsibilities
- 보유 굿즈 목록 로드와 검색/필터/정렬 관리
- 굿즈 추가/수정/삭제
- 상세 정보와 연결 거래 내역 조회
UiState
- items
- selectedFilters
- searchKeyword
- sortType
- selectedItem
- isLoading
- isSaving
UiActions
- LoadItems
- UpdateSearchKeyword
- ToggleFilter
- ChangeSort
- SaveItem
- DeleteItem
- SelectItem
PreorderViewModel
Responsibilities
- 예약 목록 조회와 상태 필터 적용
- 예약 등록/수정/취소
- 수령 완료 처리 시 Item + Transaction 생성
UiState
- preorders
- selectedStatus
- pendingPaymentCount
- selectedPreorder
- isLoading
- isSaving
UiActions
- LoadPreorders
- FilterByStatus
- SavePreorder
- MarkAsReceived
- CancelPreorder
Important Side Effects
- 수령 완료 후 컬렉션 상세 또는 생성 결과 토스트 노출
- 발매일/결제일 이벤트 자동 생성 또는 갱신
TransactionViewModel
Responsibilities
- 월간 거래 목록 조회
- 합계/유형별 집계 계산
- 거래 추가/수정/삭제
UiState
- currentMonth
- groupedTransactions
- totalExpense
- totalRefund
- filterType
- isLoading
UiActions
- ChangeMonth
- FilterByType
- SaveTransaction
- DeleteTransaction
EventViewModel
Responsibilities
- 다가오는 일정 조회
- 날짜순 정렬
- 이벤트 추가/수정/삭제
UiState
- events
- selectedType
- dateRange
- isLoading
UiActions
- LoadEvents
- FilterByType
- SaveEvent
- DeleteEvent
- OpenLinkedEntity
Validation Policy
- 상품명, 예약 상품명, 이벤트 제목은 필수
- 금액 필드는 0 이상 숫자만 허용
- 예약의 잔금은 총액 - 계약금보다 커질 수 없다
- 이벤트 날짜는 비어 있을 수 없고, 종료일이 시작일보다 빠를 수 없다
- MVP에서는 필수값만 있으면 저장을 허용하고, 나머지 누락 정보는 상세 화면 보완 대상으로 본다.
"doc09_db_schema.docx" = @"
Database Schema
Design Principles
- 로컬 우선 구조로 설계하고 모든 핵심 데이터는 오프라인에서 조회/수정 가능해야 한다.
- 삭제 복구 기능이 없으므로 MVP에서는 hard delete를 사용하되, 향후 soft delete 컬럼 추가 가능성을 고려한다.
- 금액은 Decimal 대신 정수(Long) 단위 저장을 우선 검토해 통화 오차를 줄인다.
Tables
items
- id: TEXT PRIMARY KEY
- name: TEXT NOT NULL
- series_name: TEXT
- character_name: TEXT
- category: TEXT NOT NULL
- status: TEXT NOT NULL
- quantity: INTEGER NOT NULL DEFAULT 1
- purchase_price: INTEGER
- purchase_date: TEXT
- purchase_store: TEXT
- storage_location_id: TEXT
- linked_preorder_id: TEXT
- note: TEXT
- created_at: TEXT NOT NULL
- updated_at: TEXT NOT NULL
preorders
- id: TEXT PRIMARY KEY
- name: TEXT NOT NULL
- series_name: TEXT
- character_name: TEXT
- store_name: TEXT NOT NULL
- total_price: INTEGER
- deposit_price: INTEGER
- remaining_price: INTEGER
- shipping_fee: INTEGER
- order_date: TEXT
- payment_due_date: TEXT
- release_date: TEXT NOT NULL
- receive_date: TEXT
- reservation_number: TEXT
- status: TEXT NOT NULL
- note: TEXT
- created_at: TEXT NOT NULL
- updated_at: TEXT NOT NULL
transactions
- id: TEXT PRIMARY KEY
- type: TEXT NOT NULL
- related_item_id: TEXT
- related_preorder_id: TEXT
- amount: INTEGER NOT NULL
- transaction_date: TEXT NOT NULL
- payment_method: TEXT
- place_name: TEXT
- note: TEXT
- created_at: TEXT NOT NULL
events
- id: TEXT PRIMARY KEY
- title: TEXT NOT NULL
- event_type: TEXT NOT NULL
- target_date: TEXT NOT NULL
- related_item_id: TEXT
- related_preorder_id: TEXT
- location_or_store: TEXT
- memo: TEXT
- created_at: TEXT NOT NULL
- updated_at: TEXT NOT NULL
storage_locations
- id: TEXT PRIMARY KEY
- name: TEXT NOT NULL
- parent_id: TEXT
- memo: TEXT
- created_at: TEXT NOT NULL
Relationships
- items.storage_location_id -> storage_locations.id
- items.linked_preorder_id -> preorders.id
- transactions.related_item_id -> items.id
- transactions.related_preorder_id -> preorders.id
- events.related_item_id -> items.id
- events.related_preorder_id -> preorders.id
Indexes
- items(name, series_name)
- items(status, category)
- preorders(status, release_date)
- transactions(transaction_date, type)
- events(target_date, event_type)
Notes
- 날짜는 ISO-8601 문자열 저장을 기본으로 한다.
- status와 type은 enum 문자열로 관리해 가독성을 높인다.
- 향후 태그 기능이 필요하면 tags, item_tags 테이블을 추가한다.
