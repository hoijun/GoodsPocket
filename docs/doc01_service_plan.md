GoodsPocket Service Plan
GoodsPocket Vision
GoodsPocket는 구매한 굿즈, 현재 보유 중인 굿즈, 예약한 굿즈를 하나의 흐름으로 기록하는 개인 자산 관리 앱이다.
핵심 목표는 "무엇을 샀는지"만 남기는 것이 아니라 "언제 결제했고", "언제 받는지", "지금 어디에 보관 중인지"까지 한 번에 파악하게 만드는 것이다.
애니메이션, 게임, 버추얼 IP, 캐릭터 콜라보 굿즈처럼 발매 일정과 결제 시점이 흩어지는 상품군에 최적화한다.
Problem Statement

- 예약 굿즈는 주문 시점, 잔금 결제, 발매일, 수령일이 분리되어 있어 메모 앱이나 캘린더만으로 추적하기 어렵다.
- 이미 구매한 굿즈는 시리즈, 캐릭터, 보관 위치, 중복 소장 여부를 관리하지 않으면 빠르게 파악이 어려워진다.
- 한 달에 얼마를 굿즈에 쓰는지, 예약 잔액이 얼마나 남았는지 확인하기 어렵다.
- 행사 일정, 팝업스토어, 예약 마감일을 놓치면 재구매 비용이나 기회 손실이 발생한다.
  Target Users
- 애니메이션/게임 굿즈를 꾸준히 구매하는 개인 수집가
- 한 IP에서 여러 캐릭터 혹은 여러 상품군을 병행 수집하는 팬
- 예약 상품과 현장 구매를 동시에 관리해야 하는 사용자
- 월 예산을 정해 굿즈 소비를 통제하고 싶은 사용자
  Core Value Proposition
- 기록 관점: 사용자가 산 굿즈, 가진 굿즈, 예약한 굿즈를 한 흐름으로 남긴다.
- 자산 관점: 굿즈를 단순 사진 앨범이 아니라 구매 이력과 보관 정보가 있는 개인 컬렉션으로 관리한다.
- 일정 관점: 예약 상품의 결제일, 발매일, 수령 예정일, 이벤트 일정을 놓치지 않게 만든다.
- 지출 관점: 계약금, 잔금, 배송비, 현장 구매 금액까지 합산해 실제 소비를 본다.
- 맥락 관점: 작품, 캐릭터, 브랜드, 판매처 기준으로 굿즈를 묶어 기억하기 쉽게 만든다.
  Core Features
  Collection Management
- 보유 굿즈 등록: 이름, 작품명, 캐릭터, 카테고리, 수량, 구매처, 구매일, 가격, 보관 위치, 메모
- 최소 등록 원칙: MVP에서는 상품명, 카테고리, 보유 상태만으로도 저장 가능하게 설계
- 상태 관리: 소장 중, 배송 대기, 양도 예정, 보관 분실 메모 같은 상태 확장 가능
- 중복 확인: 같은 상품을 여러 개 가지고 있는지 수량 기준으로 확인
- 이미지 첨부는 추후 확장 항목으로 두고 MVP에서는 텍스트 중심으로 설계
  Preorder Tracking
- 예약 굿즈 등록: 예약처, 예약일, 발매 예정일, 계약금, 잔금, 수령 방식, 예약 번호
- 최소 등록 원칙: MVP에서는 상품명, 예약처, 발매 예정일만으로도 저장 가능하게 설계
- 예약 상태: 예약 완료, 일부 결제, 발매 대기, 수령 완료, 취소
- 수령 전환: 예약 상품을 실제 수령하면 보유 컬렉션으로 전환하고 거래 내역도 함께 반영
  Spending Records
- 거래 유형: 예약금, 잔금, 일반 구매, 배송비, 환불, 양도 수입
- 최소 등록 원칙: MVP에서는 금액, 거래 유형, 날짜만 먼저 기록 가능하게 설계
- 월별/카테고리별 지출 합계
- 예약 잔액과 실제 확정 지출을 분리 집계
  Schedule Management
- 발매일, 결제 마감일, 택배 수령 예정일, 오프라인 행사 일정 관리
- MVP에서는 제목, 날짜, 유형만으로 일정 저장 가능하게 설계
- 홈 화면과 이벤트 화면에서 다가오는 일정 우선 노출
- 알림 설정은 MVP 이후 확장 항목으로 정의
  Scope
  MVP In Scope
- 로컬 데이터 저장
- 보유 굿즈/예약 굿즈/거래/이벤트 CRUD
- 빠른 등록과 저장 후 상세 보완 플로우
- 상태 변경과 필터/정렬
- 월간 소비 요약과 다가오는 일정 표시
- Android 우선 출시, iOS는 KMP 구조로 확장 준비
  Out of Scope for MVP
- 회원 가입 및 클라우드 동기화
- 사진 OCR, 바코드 스캔, 자동 크롤링
- 커뮤니티 기능, 중고 시세 추적, 자동 환율 변환
  Home Priority

1. 다가오는 일정
2. 이번 달 지출
3. 활성 예약 수
4. 보유 굿즈 수
5. 최근 등록 항목
6. 빠른 등록 액션
   Service Principles

- 홈 화면은 전시보다 처리 중심 대시보드로 설계한다.
- 모든 등록 화면은 최소 입력 저장을 우선한다.
- 예약은 이 서비스의 핵심 엔터티이며, 수령 전환 경험을 가장 중요한 플로우로 본다.
- 돈 흐름은 예약금, 잔금, 일반 구매를 분리해 기록한다.
- 이미지나 OCR 없이도 충분히 쓸 수 있어야 한다.
  Success Metrics
- 빠른 등록 기준 15초 안에 예약 굿즈 1건을 저장할 수 있을 것
- 1주일 이내 30개 이상의 굿즈를 무리 없이 등록할 수 있을 것
- 홈 화면에서 이번 달 지출과 14일 이내 일정이 3초 안에 파악될 것
- 예약 상품 수령 처리 시 컬렉션과 거래 내역이 동시에 갱신될 것
- 필터링을 통해 작품/캐릭터/상태 기준 검색이 자연스럽게 동작할 것
  User Scenarios
  Scenario 1: 예약 빠른 등록
  사용자는 새 굿즈를 예약했을 때 홈의 빠른 등록에서 "예약 추가"를 선택한다.
  상품명, 예약처, 발매 예정일만 입력하고 먼저 저장한 뒤 상세 화면에서 가격과 메모를 나중에 보완한다.
  앱은 예약 항목을 생성하고 발매일과 잔금일이 있으면 관련 이벤트를 자동 생성한다.
  Scenario 2: 예약 수령 전환
  사용자는 예약 상세 화면에서 "수령 완료"를 선택한다.
  앱은 예약 정보에서 이름, 작품명, 캐릭터명을 자동 복사하고 수량, 보관 위치, 상태만 확인받아 보유 굿즈로 전환한다.
  필요하면 관련 거래와 일정 상태도 함께 정리한다.
  Scenario 3: 컬렉션 정리
  사용자는 컬렉션 화면에서 작품명, 캐릭터명, 보관 위치 기준으로 굿즈를 검색하고 필터링한다.
  이미 가진 굿즈를 빠르게 찾아 중복 구매를 피하고 보관 위치를 바로 확인한다.
  Scenario 4: 월말 소비 확인
  사용자는 거래 화면에서 이번 달 굿즈 소비 총액과 거래 유형별 비중을 확인한다.
  예약금, 잔금, 일반 구매를 나누어 보고 다음 달 지출 계획을 세운다.
  Scenario 5: 일정 확인
  사용자는 홈 화면에서 다가오는 발매일, 결제일, 행사 일정을 먼저 확인한다.
  놓치면 안 되는 일정부터 확인하고 필요한 화면으로 바로 이동한다.
  "doc02_ui_structure.docx" = @"
  UI Structure
  Information Architecture
  앱은 "홈", "컬렉션", "예약", "거래", "이벤트", "설정"의 6개 최상위 영역으로 구성한다.
  하단 탭은 빠른 현황 파악과 반복 진입이 많은 화면 위주로 배치하고, 등록/편집 화면은 하위 스택으로 분리한다.
  Main Screens
  Home
- 이번 달 지출 요약
- 보유 굿즈 수량 및 예약 진행 건수
- 다가오는 발매일/결제일/행사 일정
- 최근 추가한 굿즈와 빠른 등록 버튼
  Collection
- 기본 진입은 리스트형 컬렉션 화면
- 상단 검색, 상태 필터, 작품/캐릭터/카테고리 칩 제공
- 상세 화면에서 보관 위치, 거래 이력, 연결된 예약 원본 확인
  Preorders
- 활성 예약, 결제 완료 대기, 수령 완료, 취소 상태 탭 또는 필터 제공
- 예약 상세에서 일정과 결제 금액, 판매처 정보를 보여준다
- 수령 완료 처리 시 컬렉션으로 전환하는 액션 제공
  Transactions
- 월별 그룹 리스트
- 유형 필터: 예약금, 잔금, 일반 구매, 배송비, 환불
- 거래 상세에서 연결된 굿즈/예약/이벤트 이동 가능
  Events
- 리스트와 캘린더형 개념을 함께 고려하되 MVP는 리스트 우선
- 발매일, 결제 마감일, 오프라인 행사, 배송 예정일을 통합 노출
- D-day 순 정렬과 작품별 필터 지원
  Settings
- 통화 단위, 날짜 형식, 시작 탭
- 데이터 백업/내보내기
- 카테고리/보관 위치 사전값 관리
  Tab Roles
  Home
- 지금 처리해야 할 일정과 바로 기록해야 할 액션을 보여주는 대시보드
  Collection
- 현재 가진 굿즈를 검색, 정리, 보관 위치 기준으로 관리하는 화면
  Preorders
- 앞으로 들어올 굿즈의 상태, 금액, 발매 일정 추적 화면
  Transactions
- 굿즈 소비 흐름을 월 단위로 확인하는 기록 화면
  Events
- 발매일, 결제일, 배송 예정, 행사 일정을 모아보는 화면
  Settings
- 앱 기본값과 사전 데이터를 관리하는 화면
  Supporting Flows
  Add Flow
- 홈 FAB 또는 각 리스트 화면 FAB에서 등록 플로우 진입
- 등록 시작점은 "보유 굿즈 추가", "예약 추가", "거래 추가", "이벤트 추가" 네 종류
- 기본 플로우는 최소 입력 저장 후 상세 보완으로 설계한다.
  Quick Entry Rules
- 보유 굿즈: 상품명, 카테고리, 보유 상태만 입력해도 저장 가능
- 예약 굿즈: 상품명, 예약처, 발매 예정일만 입력해도 저장 가능
- 거래 기록: 금액, 거래 유형, 날짜만 입력해도 저장 가능
- 이벤트: 제목, 날짜, 유형만 입력해도 저장 가능
- 저장 후 상세 화면에서 추가 정보를 이어서 입력할 수 있어야 한다.
  Edit Flow
- 상세 화면 우측 상단 편집 액션
- 수정 후 저장하면 이전 화면 목록을 즉시 갱신
  Search & Filter Flow
- 컬렉션과 예약 화면 모두 공통 검색 패턴 사용
- 텍스트 검색과 다중 필터를 동시에 적용 가능하게 설계
- 최근 사용한 예약처, 카테고리, 작품명은 추천값으로 재사용 가능하게 설계
  Screen Hierarchy
  Home Stack
  Home > Quick Add Bottom Sheet > Minimal Form > Detail Screen
  Collection Stack
  Collection List > Collection Detail > Edit Collection
  Collection List > Add Collection
  Collection Detail > Linked Transactions
  Preorder Stack
  Preorder List > Preorder Detail > Edit Preorder
  Preorder Detail > Receive Confirmation Sheet
  Transaction Stack
  Transaction List > Transaction Detail > Edit Transaction
  Event Stack
  Event List > Event Detail > Edit Event
  Settings Stack
  Settings > Storage Locations
  Settings > Categories
  Settings > Backup & Restore
  Cross-Screen Rules
- 모든 리스트 화면은 로딩, 빈 상태, 결과 없음 상태를 분리한다.
- 상세 화면의 핵심 액션은 화면 하단 또는 우측 상단에 1개만 강조한다.
- 삭제는 상세 화면에서만 허용해 오입력을 줄인다.
- 상태 변경으로 인해 엔터티가 다른 화면으로 이동하더라도 원본 상세 화면에서 변경 결과를 확인할 수 있어야 한다.
- 저장 직후에는 사용자가 다음에 자주 하는 행동을 제안한다. 예: 예약 저장 후 거래 추가, 수령 완료 후 보유 굿즈 상세 이동.
- 첫 입력은 짧게 받고, 상세 정보는 저장 후 보완하는 흐름을 모든 등록 화면에 공통 적용한다.
  "doc03_screen_design.docx" = @"
  Screen Design
  Home
  Purpose
  사용자가 앱을 열자마자 굿즈 자산 현황과 이번 주 일정을 빠르게 확인하는 대시보드 역할을 한다.
  Layout
- 상단 앱 바: 앱 이름, 검색 진입, 설정 바로가기
- 요약 카드 영역: 이번 달 지출, 보유 굿즈 수, 활성 예약 수, 이번 주 일정 수
- 다가오는 일정 섹션: D-day 기준 상위 5건
- 최근 등록 섹션: 최근 추가한 굿즈/예약/거래 혼합 리스트
- 하단 FAB: 빠른 등록
  Key Interactions
- 요약 카드를 누르면 관련 리스트 화면으로 이동
- 일정 항목을 누르면 이벤트 또는 예약 상세로 이동
- 빠른 등록에서 엔터티 타입을 선택 후 최소 입력 폼으로 진입
- 저장 직후 상세 화면으로 연결해 추가 정보 입력을 자연스럽게 이어간다.
  Collection
  List View
- 카드 또는 2열 그리드 전환은 추후 확장, MVP는 카드 리스트 우선
- 각 카드에는 상품명, 작품명, 상태 배지, 수량, 보관 위치, 구매가를 표시
- 상단에는 검색 바와 수평 필터 칩 배치
  Detail View
- 헤더: 상품명, 작품명, 캐릭터, 카테고리
- 본문: 구매 정보, 보관 정보, 메모, 연결 거래, 연결 예약 원본
- 하단 액션: 편집, 거래 추가
  Add/Edit Item
- 필수 입력: 상품명, 상태, 카테고리
- 선택 입력: 작품명, 캐릭터, 구매일, 구매처, 가격, 수량, 보관 위치, 메모
- 기본은 짧은 폼으로 시작하고 "추가 정보" 섹션을 펼쳐 상세 입력을 보완한다.
- 저장 시 유효성 검사는 상품명 공백 여부와 가격 숫자 형식 중심으로 단순화
  Preorders
  Reservation List
- 예약 상태 칩: 진행 중, 결제 대기, 수령 완료, 취소
- 카드 정보: 상품명, 판매처, 발매 예정일, 결제 진행도, 남은 금액
  Detail
- 예약 타임라인: 예약일 > 잔금일 > 발매일 > 수령일
- 금액 정보: 총액, 계약금, 잔금, 배송비
- 부가 정보: 예약 번호, 수령 방법, 메모
  Add Reservation
- 필수 입력: 상품명, 예약처, 발매 예정일
- 선택 입력: 총액, 계약금, 잔금 기한, 배송비, 예약 번호, 메모
- 저장 후 필요 시 이벤트 항목 자동 생성
- 저장 직후 "거래도 추가할까요?" 액션을 노출해 예약금 입력을 이어서 유도한다.
  Transactions
  Monthly Ledger
- 월 헤더와 함께 거래를 날짜순 정렬
- 각 행에는 거래 유형, 금액, 연결 엔터티, 결제 수단 메모 표시
- 상단에 월 이동과 필터 버튼 제공
  Transaction Detail
- 거래 기본 정보
- 연결된 굿즈/예약 정보
- 수정/삭제 액션
  Events
  Event List
- 날짜 기준 오름차순
- 이벤트 유형 배지: 발매, 결제, 행사, 배송
- 관련 작품명/굿즈명과 위치 정보를 함께 노출
  Event Detail
- 날짜, 장소 또는 판매처, 메모
- 연결된 예약 혹은 굿즈 상세로 이동
  Empty States
- Home Empty: "첫 예약이나 굿즈를 추가해 기록을 시작해 보세요"
- Collection Empty: "보유 중인 굿즈를 추가해 보세요"
- Preorder Empty: "예약 상품을 추가하면 결제 일정이 정리됩니다"
- Transaction Empty: "구매 내역을 추가하면 월별 소비를 볼 수 있습니다"
- Event Empty: "발매일과 행사 일정을 등록해 놓치지 않게 관리하세요"
  "doc04_ui_design_spec.docx" = @"
  UI Design Specification
  Design Direction
  기록 앱의 안정감과 덕질 서비스의 생동감을 동시에 주기 위해 따뜻한 코랄 계열 포인트와 차분한 네이비 계열 베이스를 사용한다.
  정보량이 많은 화면이 많기 때문에 색보다 구조와 간격으로 우선순위를 전달한다.
  Color Tokens
  Brand
- Primary: #FF6B6B
- Primary Container: #FFE7E4
- Secondary: #2F4858
- Secondary Container: #DCE8EE
- Accent: #F7B267
  Surface
- Background: #FFF9F7
- Surface: #FFFFFF
- Surface Variant: #F5EFEA
- Divider: #E7DDD6
  Semantic
- Success: #3FAE73
- Warning: #D89B1D
- Error: #D94C5A
- Info: #4C84C4
  Typography
- Display/Screen Title: 24sp, SemiBold
- Section Title: 20sp, SemiBold
- Card Title: 16sp, Medium
- Body: 14sp, Regular
- Label/Chip: 12sp, Medium
- Caption/Helper: 11sp, Regular
  Spacing
- 4dp: 아이콘과 텍스트 최소 간격
- 8dp: 같은 그룹 내 요소 간격
- 12dp: 카드 내부 보조 간격
- 16dp: 기본 화면 패딩
- 24dp: 섹션 간격
- 32dp: 큰 그룹 전환 간격
  Shape & Elevation
- Small radius: 8dp
- Medium radius: 12dp
- Large radius: 20dp
- 기본 카드는 elevation 1 또는 border 우선 사용
- FAB와 강조 카드만 elevation 3까지 허용
  Iconography
- Material Symbols Rounded 기준
- 상태 표현은 아이콘 + 텍스트를 함께 사용
- 거래 유형별 대표 아이콘을 고정해 학습 비용을 낮춘다
  Component Behavior
  Buttons
- Primary: 저장, 수령 완료, 추가 같은 주행동
- Secondary: 필터, 편집, 이동
- Text: 취소, 보조 액션
  Chips
- 상태 칩은 색상으로 상태 구분
- 필터 칩은 선택 시 배경 강조, 비선택 시 outline
  Cards
- 카드 상단 1행에 핵심 정보
- 하단 1~2행은 부가 메타데이터
- 액션이 필요한 경우 우측 끝에 작은 CTA 배치
  Form Rules
- 기본 등록 폼은 3~4개 핵심 필드만 먼저 노출한다.
- 나머지 필드는 "추가 정보" 섹션으로 접기/펼치기 처리한다.
- 한 화면에 6개 이상 필드를 노출할 경우 섹션 단위로 묶는다.
- 날짜 입력은 읽기 전용 필드 + 날짜 선택 다이얼로그 조합
- 금액 입력은 숫자 키패드 기준, 통화 단위는 suffix로 표시
- 필수값 누락 오류는 필드 하단 즉시 노출
- 최근 사용한 예약처, 카테고리, 작품명은 자동완성 또는 추천 칩으로 노출한다.
  Responsive Guidance
- 모바일 portrait 우선 설계
- 태블릿에서는 2-pane 상세 보기 확장 가능하도록 리스트 폭을 고정하지 않는다
- iOS와 Android 모두 Compose Multiplatform 컴포넌트 기준으로 동일한 정보 구조를 유지한다
  Accessibility
- 본문 대비비는 WCAG AA 이상 목표
- 상태 색상만으로 의미를 전달하지 않고 라벨 병행
- 터치 타깃 최소 44dp 이상
- 스크린리더를 고려해 카드 요약 문장을 조합 가능한 구조로 설계
- 홈 화면의 핵심 지표와 다가오는 일정은 스크린리더에서 먼저 읽히도록 순서를 고정한다.
  "doc05_ui_component_library.docx" = @"
  UI Component Library
  Design System Components
  SummaryCard
- 용도: 홈 화면 요약 지표 표시
- Props: title, value, subtitle, trendLabel, onClick
- States: default, emphasized, warning
  ItemCard
- 용도: 보유 굿즈 리스트와 검색 결과 표시
- Props: itemName, seriesName, characterName, status, quantity, location, price, onClick
- States: default, selected, dimmed, archived
  PreorderCard
- 용도: 예약 굿즈 진행 현황 표시
- Props: itemName, storeName, releaseDate, paymentProgress, remainAmount, status, onClick
- States: active, awaitingPayment, completed, canceled
  TransactionRow
- 용도: 월간 거래 리스트 한 줄 요약
- Props: type, title, amount, date, linkedEntityLabel, note, onClick
- States: expense, refund, income
  EventRow
- 용도: 일정 목록과 홈의 다가오는 일정 영역 표시
- Props: title, eventType, targetDate, dDayLabel, relatedName, onClick
- States: normal, today, overdue
  SectionHeader
- 용도: 리스트 섹션 제목과 더보기 액션 제공
- Props: title, actionLabel, onActionClick
  StatusBadge
- 용도: 굿즈 상태, 예약 상태, 이벤트 유형을 시각적으로 표기
- Props: label, tone
- Tones: neutral, success, warning, danger, info
  FilterChipRow
- 용도: 다중 필터 칩 그룹
- Props: chips, selectedKeys, onToggle
- Rules: 1개 이상 또는 0개 선택 허용 여부를 화면 단위에서 결정
  EmptyStateView
- 용도: 빈 화면에서 액션 유도
- Props: title, message, actionLabel, illustrationType, onActionClick
  LabeledValue
- 용도: 상세 화면의 키-값 표시
- Props: label, value, emphasizeValue
  MoneyField
- 용도: 금액 입력 전용 폼 컴포넌트
- Props: label, value, currency, placeholder, onValueChange, isError
  DateSelectorField
- 용도: 날짜 선택 필드
- Props: label, valueText, onClick, helperText
  Composition Rules
- 카드 안에는 2개 이상의 강조 색을 동시에 쓰지 않는다.
- 상태 배지는 카드 우측 상단 또는 메타 정보 행에 일관되게 배치한다.
- 필터 칩은 화면 폭을 넘을 경우 가로 스크롤 허용, 줄바꿈은 사용하지 않는다.
  Reuse Policy
- Home, Collection, Preorders, Events는 공통 카드 스타일을 공유한다.
- 상세 화면 정보 블록은 LabeledValue와 SectionHeader 조합으로 통일한다.
- 폼 입력 컴포넌트는 Validation UI와 HelperText 패턴을 공통화한다.
- Quick Add와 각 엔터티 등록 폼은 동일한 최소 입력 원칙을 공유한다.
  "doc06_kmp_structure.docx" = @"
  KMP Project Structure
  Architecture Overview
  프로젝트는 Kotlin Multiplatform + Compose Multiplatform + SQLDelight 기반으로 구성한다.
  비즈니스 로직과 데이터 계층은 shared에 두고, 플랫폼 앱은 진입점과 플랫폼 특화 기능만 가진다.
  Modules
  shared
- commonMain 중심의 도메인, 데이터, 디자인 시스템, 네비게이션 정의
- Android/iOS가 공통으로 사용하는 핵심 코드 위치
  androidApp
- Android Activity, 앱 아이콘, 권한, 알림 채널, 플랫폼 DI 진입점
  iosApp
- iOS App entry, SwiftUI host 또는 Compose launcher 래핑
- 향후 iOS 대응을 위한 최소 진입 구조 유지
  Shared Internal Structure
  core:common
- Result, dispatcher, date formatter interface, logging abstraction
  core:model
- Item, Preorder, Transaction, Event, StorageLocation 등 도메인 모델
  core:database
- SQLDelight database, driver factory, query bindings, migrations
  core:designsystem
- theme, typography, colors, shared components
  core:navigation
- route constants, navigator abstraction, back stack helpers
  domain
- repository interfaces
- use case classes
- validation rules
  data
- repository implementations
- local data source
- entity-to-domain mapper
  feature:home
- dashboard UI와 요약 로직
  feature:collection
- 보유 굿즈 등록/조회/수정
  feature:preorder
- 예약 관리와 수령 전환
  feature:transaction
- 지출 기록과 월별 집계
  feature:event
- 일정 관리
  feature:settings
- 환경설정과 사전 데이터 관리
  platform
- image picker placeholder interface
- notification scheduler interface
- file export/import interface
  Dependency Direction
- feature -> domain, core
- data -> domain, core
- domain -> core
- app modules -> shared
- feature 간 직접 의존은 금지하고 필요 데이터는 repository/use case를 통해 전달
  Recommended Tooling
- DI: Koin 또는 수동 주입 중 프로젝트 규모에 맞게 선택, MVP는 수동 조립도 가능
- Concurrency: Kotlin Coroutines + StateFlow
- Serialization: kotlinx.serialization
- Date/Time: kotlinx-datetime
  Build Strategy
- 공통 로직은 commonMain 우선 구현
- 플랫폼별 차이는 expect/actual 또는 interface + platform implementation으로 분리
- 데이터베이스 스키마 변경은 SQLDelight migration과 문서 갱신을 동시에 수행
