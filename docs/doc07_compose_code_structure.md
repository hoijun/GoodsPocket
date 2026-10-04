# Compose Code Structure

Each feature uses the same pattern under `presentation/<feature>/`:

```text
CollectionRoute.kt
CollectionScreen.kt
CollectionStateHolder.kt
CollectionUiState.kt
detail/
editor/
component/
```

- Route collects supplied state and connects Screen callbacks to feature operations/navigation.
- Screen renders immutable state and callbacks; no Koin, Repository, datasource or SQL access.
- StateHolder owns feature observation, search/filter and operation state, exposing private _state as immutable state.
- UiState coherently describes related state without contradictory loading/error flags.
- Editor owns an explicit draft initialized once; observation never overwrites edits.
- Shell owns destination and overlay kind/ID, not feature data snapshots or mutations.

Keep simple callbacks simple. Use sealed actions when they clarify a large related event set; do not mandate UiAction/UiEffect or base classes everywhere. Simple CRUD calls domain repositories; business transitions use focused use cases.

Shared visual primitives belong in presentation/designsystem; feature pieces remain local. Reuse approved metrics, placeholders and localization. Do not render screenshots as UI.

Use stable list IDs, 4-space indentation, trailing commas, explicit public boundary types, no wildcard imports, and files of at most 600 lines split by responsibility.

Test observable feature behavior, drafts, failures and cancellation. Verify geometry against each screen contract separately.
