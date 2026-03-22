
UI Structure
Information Architecture
앱은 "홈", "컬렉션", "예약", "거래", "이벤트", "설정"의 6개 최상위 영역으로 구성한다.
하단 탭은 빠른 현황 파악과 반복 진입이 많은 Home, Collection, Preorders, Transactions의 4개 화면 위주로 배치한다.
Events와 Settings는 보조 진입 경로로 분리한다.
등록/편집 화면은 하위 스택 또는 시트로 분리한다.
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
