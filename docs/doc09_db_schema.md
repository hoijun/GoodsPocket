# Local Database Contract

This is the approved rebuild contract for an unreleased schema. Update it with the actual SQLDelight source when implementation names differ. Released schemas require migrations; startup must never delete a failing database.

## Collection Storage

The `collection_entry` table stores one row per stable client-generated ID. Reservation metadata is stored in nullable columns on that row, not a second goods identity. The `has_reservation` flag distinguishes no reservation from a reservation whose optional values are all null. Receipt changes ownership to OWNED and retains the reservation fields.

| Fields | Storage and nullability |
| --- | --- |
| id | TEXT primary key, non-null |
| name, category, status | TEXT non-null; category/status use stable codes |
| series_name, character_name | Nullable TEXT |
| quantity | INTEGER non-null, positive, default 1 |
| purchase_price | Nullable INTEGER, smallest currency unit |
| currency_code | TEXT non-null, supported currency code |
| purchase_date, purchase_store | Nullable ISO date / TEXT |
| storage_location_id | Nullable TEXT identifier retained as metadata; no storage-location table or foreign key in this schema |
| related_link, note | Nullable TEXT |
| reservation_store, release_date | Nullable TEXT / ISO date; required for a reserved entry |
| has_reservation | Non-null INTEGER flag, default 0; maps to nullable ReservationDetails |
| total_price, deposit_price, remaining_price, shipping_fee | Nullable INTEGER, smallest currency unit |
| order_date | Nullable ISO date in ReservationDetails |
| received_at | Nullable ISO instant in ReservationDetails; set on receipt |
| canceled_at | Nullable ISO instant on CollectionEntry; set on cancellation |
| reservation_number | Nullable TEXT |
| created_at, updated_at | Non-null ISO instants |

Collection ownership uses OWNED, RESERVED and PLANNED_CLEANUP. Canceled reservations retain RESERVED status with a non-null canceled_at. Visible list queries exclude canceled rows; ID lookup and complete-history queries retain access for business validation and aggregation. There is no separate reservation-status enum, payment_due_date or receive_date column. releaseDate and reservationStore are top-level domain fields; receivedAt belongs to ReservationDetails. Do not confuse archival cancellation with permanent deletion.

## Events

| Fields | Storage and nullability |
| --- | --- |
| id | TEXT primary key, non-null |
| title, event_type, target_date | Non-null TEXT; type stable code, date ISO |
| related_entry_id | Nullable TEXT foreign key to collection |
| location_or_store, memo | Nullable TEXT |
| created_at, updated_at | Non-null ISO instants |

Types remain RELEASE, PAYMENT_DUE, DELIVERY and OFFLINE_EVENT. The `event` table has target_date and related_entry_id indexes. related_entry_id references collection_entry(id) with ON DELETE SET NULL; the repository also clears links in its deletion transaction. Canceling or receiving a reservation does not automatically modify manual events.

## Supporting Storage

The `app_preference` table has a singleton id constrained to 1 and non-null currency_code, date_format and language_code. Only language is editable in the approved Settings screen. Storage-location management is deferred; storage_location_id remains optional metadata without lookup or location-deletion commands.

The current collection index is (status, updated_at). Event indexes are target_date and related_entry_id. Enforce declared foreign keys on every platform connection. Do not claim indexes for category, search, reservation date or location that are absent from the SQL source.

## Transitions

| Command | Result |
| --- | --- |
| Receive active reservation | Same ID becomes OWNED; set has_reservation=1, received_at and updated_at to the supplied instant; retain existing purchase_date, or fill it from the separately supplied local clock date; preserve other reservation fields |
| Receive already received/owned entry | Explicit no-op, no duplicate row |
| Receive missing ID | Explicit not-found |
| Receive canceled reservation | Invalid/no-op result, no resurrection |
| Cancel reservation | Set canceled_at and updated_at; retain RESERVED and history; hide from active UI |
| Permanently delete goods | Atomically clear event links and remove goods; keep events |
| Edit | Preserve absent/unexposed input fields and creation time |

Transaction failures leave no partial writes. Production has no automatic sample seed. Demo/test fixtures are opt-in. Ledger, automatic event creation, photos, cloud identity and sync metadata are deferred, not placeholder tables.

Receipt timestamps and purchase dates have different semantics: never derive the local purchase date by taking the prefix of a UTC instant. The use case supplies the injected clock's local date independently.
