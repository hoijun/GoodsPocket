# SQLDelight Specification

SQLDelight source lives under composeApp/src/commonMain/sqldelight. Generated rows, queries and drivers remain within data; never edit generated files.

## Persistence

Implement [Local Database Contract](doc09_db_schema.md) with collection, event and preference queries. storage_location_id is optional retained metadata, not a foreign key to a location table. There are no transaction-ledger tables or separate preorder identities in the rebuild.

Queries support entity lookup, visible collection observation, complete history for aggregation, event observation/filtering, preferences, upsert and atomic deletion/receipt. Canceled reservations are hidden from active queries but retained for the preserved amount calculation.

One data-layer mapper owns SQL-to-domain conversion. Preserve nullable values explicitly, stable status codes, integer money, date/instant distinction and IDs. Rows do not contain translated display text.

## Execution

Inject the data dispatcher for database creation, blocking reads/writes and query observation. Use SQLDelight query listeners/Flow for changing reads and suspend for commands. UI does not open drivers or run database work.

Datasources only persist. Repository owns mapping, errors, coordination and transactional storage primitives. Use cases own business decisions. Multi-write operations use real transactions; clear event links and delete goods atomically. Enable and test foreign-key behavior for each driver.

Production databases start empty. Demo fixtures must be explicitly selected. Never infer initialization from a single empty table or silently recreate a database after an error.

## Schema Evolution and Verification

The unreleased initial schema may be redesigned. Once distributed, schema changes require migrations and data-preservation tests. Keep queries, mappings, doc09 and tests aligned.

Use real-driver tests for nullable/link/date/money round trips, reopening persistence, query emissions, canceled-row visibility, same-ID receipt, event-link deletion, failure rollback and released migrations. Source-string assertions and fake repositories do not prove these behaviors.
