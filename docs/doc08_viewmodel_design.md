# Presentation State Design

The target has feature StateHolders, not one application-wide owner of business state. This historical filename remains for existing links.

| Owner | State |
| --- | --- |
| Shell | Destination and overlay kind/target ID |
| Home | Dashboard, recent entries, upcoming events, load/failure |
| Collection | Entries, segment, query, operation state |
| Collection editor | Target, original values, draft, validation/save/failure |
| Events | Events, type filter, supplied current date, load/failure |
| Event editor | Target, draft, validation/save/failure |
| My | Derived profile/summary and load/failure |
| Settings | Preferences and language update |

Expose immutable StateFlow backed by private mutable state. Do not maintain a writable aggregate compatibility snapshot.

## Lifetimes

Tab StateHolders belong to the host session and survive tab changes. Editors belong to overlays and dispose on close. Scope creation and cancellation have one explicit owner. Host disposal cancels observations and work; no unowned global scope or scope created on each recomposition.

## Commands and Failures

Observe changing data with Flow and issue one-shot suspend writes. Targeted mutations update observers without reloadAll. Search/filter state remains feature-local.

Initialize a draft once. Preserve unexposed fields from the persisted entity. Generate a creation ID once per draft and reuse it on retries.

Invalid input, absence and duplicate receipt return explicit results. Infrastructure failures become domain errors while preserving causes; cancellation is rethrown. Save failure preserves draft and target. A successful save followed by observation failure never triggers another write. Close sheets only after success. Localize errors at presentation boundaries.

Test success, validation, no-op, missing target, persistence failure, retry identity, draft preservation, feature independence and cancellation. Real-driver tests separately verify storage.
