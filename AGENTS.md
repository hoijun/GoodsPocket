# GoodsPocket

## DI
- Use Koin Annotations + KSP.
- Do not add classic Koin DSL unless necessary.
- Do not reintroduce `AppContainer`.

## Data
- Datasource is persistence only.
- Do not put business orchestration in datasource.

## Use Cases
- For simple pass-through read, save, update, and delete operations, call the repository directly.
- Do not create a use case when it only forwards data to a single repository method.
- Use a use case only for business rules, validation, multi-repository orchestration, state transitions, or transaction boundaries.
- Keep dashboard aggregation, recent activity building, and preorder receiving as use cases.

## Presentation
- For simple pass-through operations, use the repository directly in `GoodsPocketAppStateHolder`.

## Subagents
- Use subagents for read-heavy exploration, reviews, and test-failure triage when the work can be split cleanly.
- Avoid parallel write-heavy edits unless file ownership is clearly separated.
- Keep final synthesis and repository-finalizing git actions in the main thread.

## Platform DI
- Keep platform-specific DI providers in `PlatformDataModule`.
- Keep `DatabaseDriverFactory` in the current `expect/actual` + platform module structure.

## Generated Code
- Do not edit generated KSP or SQLDelight files directly.

## Time
- Do not keep hardcoded date or month values in production logic.

## Tests
- Use `InMemoryGoodsPocketRepository` for common tests unless the test explicitly targets SQLDelight behavior.
- If DI definitions change, keep KSP configuration check passing.

## Validation
- After meaningful changes, run `bash ./gradlew :composeApp:compileDebugKotlinAndroid`.
- After meaningful changes, run `bash ./gradlew :composeApp:allTests`.
