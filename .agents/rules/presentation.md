# Presentation Rules

## State Ownership

- Keep application state in `GoodsPocketAppStateHolder` and expose immutable `StateFlow`.
- Keep mutable flows private, using `_state` for the mutable property and `state` for the public immutable view.
- Use a single immutable UI state model for related screen state.
- Keep persistent domain state separate from temporary UI state such as search input, selected filters, sheets, dialogs, details, and editors.
- Avoid reloading unrelated data after a focused mutation when a targeted refresh is sufficient.
- Represent loading, failure, and retry state when an asynchronous operation can fail.

## Compose Boundaries

- Keep screen composables stateless where practical: pass state down and events up.
- Screens and reusable components must not access repositories, use cases, datasources, databases, or Koin directly.
- Resolve application dependencies at the app or state-holder boundary.
- Do not perform database access, business logic, blocking work, or heavy collection transformations during composition.
- Use `remember` only for UI-local state or cached calculations with explicit inputs.
- Use a sealed event type only when a screen accumulates enough related callbacks that its API becomes difficult to understand.

## Composable APIs and Performance

- Name composable functions with `PascalCase`.
- Put required parameters first, then `modifier: Modifier = Modifier`, optional parameters, event callbacks, and trailing content slots.
- Accept and apply the provided `Modifier`; do not replace or ignore it.
- Treat modifier order as behavior and review layout, drawing, clipping, and interaction order intentionally.
- Give lazy-list items stable keys when stable IDs exist.
- Avoid allocating filtered collections or expensive derived values repeatedly during recomposition.
- Keep previews deterministic and independent from production persistence and DI.

## Navigation and Overlays

- Keep destinations and stable route identifiers centralized in `AppDestination`.
- Do not pass a navigation controller or mutable navigation state into reusable screen components.
- Represent dialogs, sheets, details, and editors through explicit UI state.
- Define back behavior for secondary destinations and overlays.
- Do not add or replace the navigation framework unless the requested feature requires it.

## Design System

- Reuse design-system colors, typography, shapes, spacing, layouts, and components instead of duplicating values.
- Keep reusable UI primitives in `presentation/designsystem` or `presentation/component` according to responsibility.
- Do not mix feature-specific business behavior into design-system components.

## Localization

- Put user-facing text in Compose resources.
- Keep Korean and English resource sets synchronized when adding or changing user-facing copy.
- Do not hardcode user-facing strings in composables, state holders, use cases, or persistence mappings.
- Keep currency, date, quantity, and status formatting locale-aware.
- Never use translated display text as a persisted identifier, category key, route, or status value.

## Accessibility

- Provide accessibility labels for meaningful icons and images; mark purely decorative content appropriately.
- Preserve minimum interactive touch-target sizes.
- Do not rely only on color to communicate status or selection.
- Support system font scaling and avoid fixed layouts that clip localized text.
- Add semantic roles, selected state, and state descriptions when custom components require them.
