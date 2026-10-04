# Domain and Data Rules

## Domain

- Keep domain models as plain Kotlin types without framework or persistence annotations.
- Define repository interfaces in `domain` and implement them in `data`.
- Keep persisted identifiers and domain status values stable and language-independent.
- Use a use case only for business rules, validation, multi-repository orchestration, state transitions, or transaction boundaries.
- For simple read, save, update, and delete operations, call the domain repository directly from the owning feature StateHolder.
- Do not create a use case that only forwards arguments to one repository method.
- Keep dashboard aggregation, recent activity building, and preorder receiving as use cases.

## State Transitions and Transactions

- Keep each multi-step state transition behind one use-case entry point.
- Preorder receiving must go through `MarkPreorderReceivedUseCase`; keep business decisions there and atomic storage primitives in the repository/datasource.
- Receipt retains the collection ID and reservation metadata. Duplicate receipt is a no-op; missing IDs and canceled reservations have explicit outcomes.
- Cancellation archives reservations outside active UI. Permanent collection deletion clears event links atomically and retains the events.
- Operations that must update multiple records atomically must use a transaction boundary.
- A failed multi-write operation must not leave partially updated state.
- Test successful, invalid or no-op, and failure outcomes for business state transitions.

## Repository and Datasource

- Repository implementations own datasource selection, cross-source coordination, persistence-to-domain mapping, and infrastructure error translation.
- Datasources own persistence operations only.
- Do not put validation, UI decisions, multi-repository orchestration, or business workflows in a datasource.
- Do not expose SQLDelight-generated rows, drivers, or query types outside the data layer.
- Define one owner for each domain projection or model conversion; do not duplicate the same conversion in repository and presentation code.

## Persistence and Schema

- Treat a released database schema as a persistent user-data contract.
- Add a SQLDelight migration when changing a released table or persisted representation. The approved unreleased rebuild may replace the development initial schema; it does not authorize automatic device-database deletion.
- Do not use destructive database recreation as a migration strategy.
- Keep schema changes, queries, mappings, migrations, and SQLDelight tests consistent.
- Edit SQLDelight source `.sq` files when needed, but never edit generated SQLDelight code directly.

## Seed Data

- Production databases start empty. Seed sample data only in an explicitly selected demo, test, or preview environment.
- Never overwrite or repopulate partial user data based only on one empty table.
- Do not use seed timestamps or fixture values as production timestamps.
- Keep seed, fixture, and preview data deterministic and separate from runtime business logic.

## Time, IDs, Dates, and Money

- Obtain the current date and time through an injectable clock or time provider.
- Do not hardcode current dates, months, years, timestamps, or time zones in production logic.
- Fixed values are allowed in tests, previews, fixtures, and seed data.
- Store dates and timestamps in a stable ISO-8601 representation; do not persist locale-formatted display text.
- Make time-zone conversion explicit when deriving dates or month filters.
- Generate production IDs through a dedicated injectable ID generator, not directly through random values in presentation code.
- Represent monetary values with integers in the smallest supported currency unit; do not use `Float` or `Double` for money.
- Preserve the documented purchase/reservation aggregation contract in `docs/doc11_repository_usecase.md`; it is not a payment ledger.

## Concurrency and Errors

- Do not perform database or other blocking I/O work on the UI thread.
- Data owns an injected I/O dispatcher, including database creation and initialization. Tests replace it with controlled dispatchers.
- Use structured concurrency for asynchronous work and never use `GlobalScope`.
- Use `suspend` for genuinely asynchronous one-shot operations and `Flow` for genuinely observable streams; do not convert APIs mechanically.
- Do not silently swallow persistence or business-operation failures.
- Convert infrastructure failures into a domain-appropriate error contract before exposing them to presentation.
- Do not expose raw SQLDelight, Android, iOS, or database exceptions to UI code.
- Preserve cancellation and original causes. Reuse a draft's ID on retries and distinguish command success from subsequent observation failure.
- Observe changing persisted data with Flow; avoid full application reload after every write.
- Keep future cloud SDKs and DTOs in data. Do not add placeholder remote adapters, authentication or sync metadata before those features are specified.
