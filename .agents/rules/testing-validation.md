# Testing and Validation Rules

## Test Design

- Use focused fake implementations of domain repository contracts for common behavior tests; use a real driver for SQLDelight behavior.
- Name tests by observable behavior rather than implementation details.
- Structure tests clearly as arrange, act, and assert.
- Keep test data deterministic.
- Use fixed clocks and ID generators in tests instead of production current-time or random providers.
- Prefer fakes and in-memory repositories for behavior tests.
- Use mocks only when interaction verification is the behavior under test or a fake would be disproportionately expensive.
- Add a focused regression test for every bug fix.
- State-transition tests must assert every affected record and final status.
- Repository contract tests should verify behavior through domain repository interfaces.
- Use SQLDelight-specific tests for queries, persistence mapping, migrations, and transaction behavior.
- Verify nullable fields, links, money and dates through real save/read round trips, reopening persistence, observation emissions, and rollback. Source-text field checks do not replace these tests.
- Verify same-ID receipt, preserved reservation metadata, hidden canceled entries, retained event rows after collection deletion, and month/year aggregation boundaries.
- Test feature independence, draft retention, retry ID stability, cancellation, and command-success/observation-failure separation.

## Validation Selection

- Documentation-only changes do not require Gradle validation.
- During Kotlin or Compose work, run the smallest relevant test or compile task first.
- For production Kotlin or Compose changes, run `bash ./gradlew :composeApp:compileDebugKotlinAndroid`.
- For targeted Android and common tests, run `bash ./gradlew :composeApp:testDebugUnitTest`.
- For domain, data, DI, SQLDelight, platform, or broad cross-feature changes, run `bash ./gradlew :composeApp:allTests` before completion.
- Do not run `compileDebugKotlinAndroid` immediately before `allTests` when `allTests` already performs the required compilation.
- For `iosApp`, iOS framework integration, or Apple platform configuration changes, run the relevant Xcode or iOS build validation.
- If DI definitions change, ensure the KSP configuration check passes.
- If validation cannot run or fails for an unrelated existing reason, report the skipped command or failure explicitly.
- Never claim a build, test, formatter, or static-analysis check passed unless the corresponding command was run successfully.
