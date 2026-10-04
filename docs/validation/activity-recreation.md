# Activity Recreation Validation

2026-10-04. Android 16/API 36, Medium_Phone_API_36.0 emulator.

## Reproduction and Fix

Before the fix, rotating with an unsaved quick-add draft returned to Home and discarded the draft. The native regression run also failed to find the quick-add input and Collection search after `ActivityScenario.recreate()`. That baseline run was interrupted after those failures; it was not a complete suite run.

The host ViewModel now retains the presentation session across configuration recreation. Explicit draft savers restore Collection/Event quick-add and editor fields. Final owner clearing cancels the session and closes its local Koin graph.

## Native Regression Evidence

`bash ./gradlew :composeApp:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=goods.pocket.app.presentation.ActivityRecreationTest` passed all 7 tests on the real emulator. Tests use Compose input/actions, real MainActivity recreation and SQLite repository reads, not source-text assertions.

| Scenario | Assertions |
| --- | --- |
| Collection navigation/search | Route and typed search remain after recreation. |
| Collection quick-add | Name, category and series remain; saving and recreating leaves exactly one record. |
| Collection editor | Unsaved name/note remain; original ID and hidden price/link survive the save. |
| Event quick-add | Name, date and type remain; saving and recreating leaves exactly one record. |
| Event editor | Unsaved title/date remain; original ID, hidden store and memo survive. |
| Host lifetime | Same ViewModel and active Job after recreation; Job canceled after final close; new launch gets a different owner. |
| Final close | Unsaved overlay is discarded and a fresh launch starts on Home with an empty new draft. |

Generated report: `composeApp/build/reports/androidTests/connected/debug/index.html`. Test logs are under `composeApp/build/outputs/androidTest-results/connected/debug/`. Generated artifacts are not committed.

Common tests separately verify a retained in-flight command writes once with one generated ID, final owner cancellation, and full draft saver round trips including incomplete input. They do not replace the native recreation tests.

The final combined validation command, `bash ./gradlew :composeApp:allTests :composeApp:ktlintCheck :composeApp:checkArchitecture :composeApp:assembleDebug`, passed: Android debug 103, Android release 103 and iOS simulator 70 tests, all with zero failures/errors/skips. This run also includes the accompanying visual-contract corrections. The Xcode iPhone 16 / iOS 18.6 Debug build passed (`/tmp/gp-session-xcode.log`).

After installing that build, the iOS Maestro smoke flow navigated Home, Collection, Events, My and Settings, verified saved records and captured all five screens (`/tmp/gp-session-ios-smoke.log`). This confirms launch/navigation/persistence, not native host deallocation.

A separate physical-orientation command rotated the running Android emulator to landscape with an unsaved name/category. Both fields and the save action remained visible (`/tmp/gp-rotation-fixed-landscape.png`). Automatic rotation was restored to its original enabled setting, portrait rotation to 0 and font scale to 1.0; the updated app remains open.

## Limits

- This is configuration recreation, not process-death recovery. Shell navigation and overlay IDs are not persisted across a new process.
- Android lifecycle assertions do not prove iOS native host removal/deallocation. iOS uses the Compose host's ViewModelStore owner.
- Koin graph closure is not a claim that the existing lazy SQLDelight driver has an explicit close hook.
- These tests do not exhaust every scroll position, IME state, font scale or orientation combination.
- Use a dedicated emulator: the connected-test runner may uninstall the target application afterward. Test fixtures remove only their own named records and restore the previous language preference; reinstalling an APK does not recover data removed by uninstall.
