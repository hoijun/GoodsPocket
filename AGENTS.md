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

## File Size
- Keep source files at 600 lines or fewer.
- When a file grows beyond 600 lines, split it by responsibility.

## Git Commits
- Write git commit messages in Korean.
- Prefer the format `type(scope if useful): summary`.
- If a scope is helpful, use a concrete area such as `home`, `navigation`, `settings`, or `i18n`.
- After the title, add flat bullet lines that summarize the grouped changes.
- Keep the bullet lines in one contiguous block with no blank lines between bullets.
- Use this style:
```text
feat(가능하면 어떤 부분인지): 저널형 UI와 한국어 기본 카피 정비
- Home/Collection/Preorders/My 화면을 저널형 레이아웃으로 재구성하고 공통 UI 토큰을 정리
- Transactions/Events/Settings 화면, 바텀 내비게이션, 시트 스타일, 상태 재로딩 흐름을 정리
- 기본 화면 문구와 시드 데이터를 한국어로 정리하고 회귀 테스트와 작업 규칙을 추가
```

## Tests
- Use `InMemoryGoodsPocketRepository` for common tests unless the test explicitly targets SQLDelight behavior.
- If DI definitions change, keep KSP configuration check passing.

## Validation
- After meaningful changes, run `bash ./gradlew :composeApp:compileDebugKotlinAndroid`.
- After meaningful changes, run `bash ./gradlew :composeApp:allTests`.
