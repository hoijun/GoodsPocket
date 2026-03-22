
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
