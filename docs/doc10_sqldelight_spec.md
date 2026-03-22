
SQLDelight Specification
File Layout
Database.sq
- DB 생성 진입 파일
- PRAGMA, 공통 schema version, migration 설명 주석 포함
items.sq
- items 테이블 정의
- 검색/필터/정렬 쿼리
- 상세 조회와 upsert/delete 쿼리
preorders.sq
- preorders 테이블 정의
- 상태별 목록 조회
- release_date/payment_due_date 기준 정렬 쿼리
- 수령 완료 전환 시 상태 업데이트 쿼리
transactions.sq
- transactions 테이블 정의
- 월별 조회
- 유형별 합계 집계
- 아이템/예약 연결 기준 조회
events.sq
- events 테이블 정의
- 다가오는 일정 쿼리
- 날짜 범위 조회
- 연결 엔터티 기반 조회
storage_locations.sq
- storage_locations 테이블 정의
- 계층형 위치 목록 조회
Query Recommendations
items.sq
- selectAllItems(search, status, category, sort)
- selectItemById(id)
- insertItem(...)
- updateItem(...)
- deleteItem(id)
- countOwnedItems()
preorders.sq
- selectPreordersByStatus(status)
- selectUpcomingPreorders(limit)
- insertPreorder(...)
- updatePreorder(...)
- markPreorderReceived(id, receiveDate)
- cancelPreorder(id)
transactions.sq
- selectTransactionsByMonth(startDate, endDate)
- selectTransactionSummaryByMonth(startDate, endDate)
- selectTransactionsForItem(itemId)
- insertTransaction(...)
- deleteTransaction(id)
events.sq
- selectUpcomingEvents(today, limit)
- selectEventsBetween(startDate, endDate)
- insertEvent(...)
- updateEvent(...)
- deleteEvent(id)
Mapper Rules
- SQL row는 data layer entity로 받고 domain model로 변환한다.
- nullable 컬럼은 domain에서 기본값으로 숨기지 말고 명시적으로 처리한다.
- money 필드는 Long 기반 Money value object로 감싸는 것을 고려한다.
Migration Strategy
- schema 변경 시 .sqm migration 파일 추가
- breaking change는 데이터 백업 경로와 함께 문서화
- 문서 doc09_db_schema와 SQLDelight 정의를 항상 같이 갱신
