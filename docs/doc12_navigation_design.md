
Navigation Design
Top-Level Navigation
하단 바는 Home, Collection, Preorders, Transactions의 4개 주요 탭으로 구성한다.
Events와 Settings는 하단 탭에서 제외하고, 홈 카드 진입 또는 상단 앱 바 액션 등 보조 진입 경로로 접근한다.
초기 진입 탭은 Home이며, 사용자가 설정에서 기본 탭을 바꿀 수 있게 확장 가능하다.
Routes
Home
- home
Collection
- collection/list
- collection/detail/{itemId}
- collection/edit
- collection/edit/{itemId}
Preorders
- preorder/list
- preorder/detail/{preorderId}
- preorder/edit
- preorder/edit/{preorderId}
- preorder/receive/{preorderId}
Transactions
- transaction/list
- transaction/detail/{transactionId}
- transaction/edit
- transaction/edit/{transactionId}
Events
- event/list
- event/detail/{eventId}
- event/edit
- event/edit/{eventId}
Settings
- settings
- settings/storage-locations
- settings/categories
- settings/backup
Navigation Rules
- 설정 화면 진입 시 하단 바는 직전에 선택된 주요 탭 상태를 유지한다.
- 설정 화면 종료 시 직전에 선택된 주요 탭으로 복귀한다.
- 하단 탭 재선택 시 해당 탭의 루트 화면으로 복귀한다.
- 상세 화면에서 저장 완료 후 이전 화면으로 pop 하되, 목록은 최신 상태를 반영해야 한다.
- 홈 카드에서 진입한 리스트 화면은 해당 필터가 미리 적용된 상태로 열린다.
- 예약 상세에서 수령 완료 플로우를 마치면 컬렉션 상세 또는 예약 목록으로 복귀 전략을 선택 가능하게 설계한다.
Modal/Sheet Destinations
- Quick Add Bottom Sheet
- Confirm Delete Dialog
- Date Picker Dialog
- Receive Confirmation Dialog
Deep Link Candidates
- 향후 알림 연동 시 preorder/detail/{id}와 event/detail/{id}를 딥링크 대상으로 정의
- MVP에서는 내부 라우팅 상수만 정리하고 외부 딥링크 처리는 보류
Back Behavior
- 편집 화면에서 미저장 변경이 있으면 이탈 확인 다이얼로그 표시
- 루트 탭 화면에서 뒤로 가기 시 앱 종료 또는 백그라운드 이동은 플랫폼 기본 정책을 따른다
