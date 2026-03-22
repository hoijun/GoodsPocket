
Compose Code Structure
Package Layout
feature/home
- HomeRoute.kt
- HomeScreen.kt
- HomeViewModel.kt
- HomeContract.kt
- components/HomeSummarySection.kt
- components/UpcomingEventSection.kt
feature/collection
- CollectionRoute.kt
- CollectionScreen.kt
- CollectionDetailScreen.kt
- CollectionEditScreen.kt
- CollectionViewModel.kt
- CollectionContract.kt
- components/ItemCard.kt
- components/CollectionFilterBar.kt
feature/preorder
- PreorderRoute.kt
- PreorderScreen.kt
- PreorderDetailScreen.kt
- PreorderEditScreen.kt
- PreorderViewModel.kt
- PreorderContract.kt
feature/transaction
- TransactionRoute.kt
- TransactionScreen.kt
- TransactionDetailScreen.kt
- TransactionEditScreen.kt
- TransactionViewModel.kt
- TransactionContract.kt
feature/event
- EventRoute.kt
- EventScreen.kt
- EventDetailScreen.kt
- EventEditScreen.kt
- EventViewModel.kt
- EventContract.kt
feature/settings
- SettingsRoute.kt
- SettingsScreen.kt
- SettingsViewModel.kt
Screen Pattern
- Route: navigation 인자 수신, ViewModel 생성, side effect 처리
- Screen: pure UI rendering
- Contract: UiState, UiAction, UiEffect 정의
- ViewModel: use case 호출, 상태 축소(reduce), 일회성 이벤트 방출
State Management
- 각 화면은 단일 UiState data class를 사용한다.
- 사용자 입력은 UiAction으로 전달하고 ViewModel이 처리한다.
- 토스트/네비게이션/다이얼로그는 UiEffect 또는 screen-level event로 분리한다.
Shared UI Packages
core/designsystem/component
- AppScaffold
- OAAppBar
- OAFilterChip
- OAStatusBadge
- OAEmptyState
- OAMoneyTextField
core/designsystem/theme
- Color.kt
- Typography.kt
- Dimens.kt
- Theme.kt
Naming Rules
- composable 함수는 역할 중심 이름 사용
- 상태 홀더는 "UiState", 액션은 "UiAction", 효과는 "UiEffect" 접미사 통일
- 프리뷰는 컴포넌트 파일 하단에 Preview 접미사 사용
UI Guidelines
- 대형 화면 전환은 AppScaffold 내부 슬롯으로 통일
- LazyColumn item key는 항상 entity id 사용
- 리스트 필터는 rememberSaveable을 이용해 화면 재생성 시 유지
- 폼 화면은 스크롤 가능한 단일 컬럼으로 구현
Testing Strategy
- ViewModel 테스트는 Contract 단위로 작성
- 상태 축소 로직은 fake repository로 검증
- 핵심 컴포넌트는 screenshot test 또는 preview 기반 수동 검증 대상 정의
