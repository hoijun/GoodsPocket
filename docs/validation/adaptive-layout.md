# Adaptive Layout Validation

2026-10-04. Android 16/API 36, Medium_Phone_API_36.0 emulator.

## Fixed and Rechecked

| Scenario | Evidence and result |
| --- | --- |
| Landscape, 2400 x 1080, density 420 | Collection detail previously started at y=0; bounded height now leaves the 63px status area clear. Close/action controls remain visible and the body scrolls. `/tmp/gp-landscape-detail-after.png`, `/tmp/gp-landscape-detail-scroll.png`. |
| Landscape collection grid | The grid viewport ends above the fixed summary instead of drawing cards behind it. `/tmp/gp-landscape-collection-after.png`. |
| Small portrait, 840 x 1600 at density 420 (320dp width) | Maestro created an event, entered its detail, and found the edit action. `/tmp/gp-small-event.yaml`, `/tmp/gp-small-event.log`, `/tmp/gp-small-event-after.png`. |
| Font scale 2.0, Settings | Korean and English headings and language options are visible. Maestro switched both languages successfully. `/tmp/gp-font-validation.log`, `/tmp/gp-font-settings-en-after.png`. |
| Font scale 2.0, Home | Summary values, hero copy, and spending total no longer clip vertically. Content below the viewport remains scrollable. Recent cards retain intentional title ellipsis. `/tmp/gp-font-home-final.png`, `/tmp/gp-font-spending-final.png`. |
| Font scale 2.0, collection detail | Edit/delete labels now fit the expanded action heights. Metadata can wrap and notes are scrollable. `/tmp/gp-font-detail-final.png`. |
| Font scale 2.0, collection editor with real IME | Header, focused input, scrollable body, and save action remain above the keyboard. `/tmp/gp-font-editor-ime.png`. |

The captures above are inspected runtime evidence, not a complete automated geometry assertion. Source-contract regression tests only guard implementation boundaries and do not prove pixel correctness. Temporary captures and Maestro flows are not committed artifacts.

## Automated Checks

`bash ./gradlew :composeApp:allTests :composeApp:ktlintCheck :composeApp:checkArchitecture :composeApp:assembleDebug` passed after the fixes.

- Android debug: 96 tests, zero failures/errors/skips.
- Android release: 96 tests, zero failures/errors/skips.
- iOS simulator common tests: 66 tests, zero failures/errors/skips.
- New contracts: adaptive detail 3, font scaling 2, Home large font 4.
- Actual SQLite recovery is recorded separately in [storage-recovery.md](storage-recovery.md).

The iPhone 16 / iOS 18.6 simulator Xcode Debug build also passed (`/tmp/gp-adaptive-xcode.log`). After installing that build, Maestro navigated Home, Collection, Events, My, and Settings and verified the previously saved collection/event records (`/tmp/gp-adaptive-ios-smoke.log`). This is a launch/navigation/persistence smoke test, not an iOS large-font or full visual comparison.

## Remaining Issues and Limits

- Rotation recreates the Android Activity and returns navigation to Home. Current session and editor drafts are composition-owned, so unsaved state is not guaranteed across recreation. This is a separate state-restoration issue, not fixed by sizing changes.
- All combinations of landscape, font scale 2.0, IME, long translations, and every screen/overlay have not been exhausted. The short-window body can be small because header and actions remain fixed outside the scroll area.
- No physical Android device or iOS storage-failure scenario was exercised. The 320dp test used an emulator display-size override.
- The [visual audit](visual-audit.md) retains measured differences and unverified areas. This work does not certify full 1px reference alignment.

After validation, the Android display-size override was reset; font scale returned to 1.0, user rotation to 0, automatic rotation to its original enabled setting, and temporary IME settings were restored. The emulator remains open with the updated app.
