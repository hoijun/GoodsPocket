# Code Conventions

## Kotlin Style

- Follow the official Kotlin coding conventions and the established project style.
- Use 4 spaces for indentation and do not use tabs.
- Use trailing commas in multiline declarations, calls, collections, and argument lists.
- Use `PascalCase` for types, `camelCase` for functions and properties, and `UPPER_SNAKE_CASE` for constants.
- Name Boolean properties and functions with `is`, `has`, `can`, or `should` when appropriate.
- Prefer immutable `val`, read-only collections, and immutable state models.
- Use exhaustive `when` expressions for closed state sets.
- Avoid `!!`; use null-safe operations or an explicit contract check.
- Use `require` for invalid caller input and `check` for invalid internal state.
- Keep visibility as narrow as possible.
- Do not use wildcard imports.
- Do not suppress compiler or lint warnings without a nearby explanation.
- Comments should explain why a decision exists, not restate what the code does.

## Naming and Responsibility

- Use descriptive domain names.
- Avoid vague names such as `Manager`, `Helper`, `Utils`, `Data`, or `Info` without a focused responsibility.
- Keep one primary public type or responsibility per Kotlin file.
- Keep small private types and extensions with the code they support.
- Do not create catch-all `Utils`, `Extensions`, or `Common` files.
- Keep production Kotlin files at 600 lines or fewer.
- Prefer extracting a cohesive type over moving arbitrary functions only to satisfy the line limit.

## Functions

- Keep functions focused on one responsibility.
- Prefer guard clauses over deeply nested conditionals.
- Prefer expression bodies for short, readable functions.
- Use named arguments when a call has multiple parameters of the same type or when meaning is not obvious.
- Avoid Boolean parameters that substantially change behavior; use separate functions or a descriptive type.
- Do not perform hidden writes or unrelated state changes in functions named as reads.
- When a function requires many related primitive parameters, introduce a focused parameter model.

## API and Type Design

- Avoid `Pair`, `Triple`, `Map<String, Any?>`, and primitive-heavy return values in production APIs; introduce a focused named type.
- Expose read-only collection interfaces and never leak an internally mutable collection.
- Declare explicit return and property types for public or cross-package APIs when inference could leak an implementation or platform type.
- Use a property only when the value is cheap, deterministic, side-effect-free, and does not throw; otherwise use a function.
- Prefer an enum or sealed hierarchy over strings or multiple Booleans for a closed state.

## Exceptions and Cancellation

- Catch the narrowest meaningful exception type.
- Catch `Exception` or `Throwable` only at an application or infrastructure boundary.
- A broad catch must rethrow `CancellationException` before translating or reporting the failure.
- Do not use exceptions for expected absence, validation failure, or no-op outcomes; use a nullable value or a focused result type.
- Do not silently discard an exception or replace it without preserving the original cause.

## Abstractions and Scope Functions

- Prefer an existing project pattern before introducing a new wrapper or abstraction.
- Add an abstraction only when it owns an invariant or boundary, removes meaningful duplication, or clarifies multiple call sites.
- Do not create a pass-through wrapper or helper for a single call site unless it owns a real contract.
- Keep extension functions close to their owner or consumer and restrict their visibility.
- Avoid nested scope functions or ambiguous `it` receivers; use a named local value or lambda parameter when meaning is not immediate.
- Use `let`, `run`, `apply`, `also`, and `with` according to their semantic purpose, not merely to shorten code.

## File Formatting

- Follow the existing file-local style and use one blank line between top-level declarations and independent logical blocks.
- Do not add a blank line before `else`, `catch`, `finally`, chained calls, or closely related statements.
- Do not use multiple consecutive blank lines.
- Remove trailing whitespace and end every text file with a newline.
- Do not reformat unrelated code or unrelated sections.
- Use configured formatting and static-analysis tasks when they exist; do not claim `ktlint` or `detekt` validation when the project has no such task.

## Dependencies and Sensitive Data

- Add and version dependencies through `gradle/libs.versions.toml`.
- Reuse the standard library or existing dependencies when sufficient.
- Do not add a dependency without a concrete use case.
- Never commit API keys, signing credentials, access tokens, personal data, or machine-specific configuration.
- Do not log personal collection data, prices, reservation identifiers, or other user-sensitive values.
