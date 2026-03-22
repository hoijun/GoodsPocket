
Navigation Design
Top-Level Navigation
하단 바는 Home, Collection, Preorders, My의 4개 주요 탭으로 구성한다.
Transactions와 Events는 보조 화면으로 유지하고, Settings는 My에서만 진입하는 하위 화면으로 둔다.
초기 진입 탭은 Home이며, 사용자는 설정에서 Home, Collection, Preorders, My 중 하나를 기본 탭으로 선택할 수 있다.
Routes
Home
- home
Collection
- collection/list
Preorders
- preorder/list
My
- my
Transactions
- transaction/list
Events
- event/list
Settings
- settings
Modal / Sheet Destinations
- Quick Add Bottom Sheet
- Item Detail Sheet
- Preorder Detail Sheet
- Transaction Detail Sheet
- Event Detail Sheet
- Item / Preorder / Transaction / Event Editor Sheet
- Confirm Delete Dialog
Navigation Rules
- 설정 화면은 My에서만 진입한다.
- 설정 화면 진입 시 하단 바는 My 선택 상태를 유지한다.
- 설정 화면 종료 시 항상 My로 복귀한다.
- 하단 탭 재선택 시 해당 탭의 루트 화면으로 복귀한다.
- Transactions와 Events는 보조 화면이므로 선택된 하단 탭 상태를 바꾸지 않는다.
- 상세/편집은 독립 라우트 대신 시트로 열고, 저장 완료 후 목록은 최신 상태를 반영해야 한다.
Deep Link Candidates
- 향후 알림 연동 시 preorder/detail/{id}와 event/detail/{id}를 딥링크 대상으로 정의
- MVP에서는 내부 라우팅 상수만 정리하고 외부 딥링크 처리는 보류
Back Behavior
- 시트 편집 화면에서 미저장 변경이 있으면 이탈 확인 다이얼로그 표시
- 루트 탭 화면에서 뒤로 가기 시 앱 종료 또는 백그라운드 이동은 플랫폼 기본 정책을 따른다
