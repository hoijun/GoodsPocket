# Home spending and central quick-add — 2026-10-04

Status: the user selected standard 1 dp elevation and authorized commit/push on 2026-10-04. This is a focused Home/navigation correction, not a full-screen pixel-equivalence pass. Previous Collection work was pushed as `5507a92`.

## Cause and correction

- Spending used uniform 1 dp text gaps, crowding its title, month, amount, and change toward the top. Fresh reference measurement required different gaps and typography. Adjusted only this card's text padding, row gaps, and type sizes. Card size, chart placement, theme, live aggregation, currency preference, current month, and enlarged-font layout remain intact.
- The central action used a literal `+` at 18 sp, which rendered about 9 × 9 px at the comparison size and grew with system text. Replaced it with rounded native lines. The existing 38 dp circle, inset policy, accessibility label, and callback remain intact. The final user-selected shadow uses standard 1 dp elevation.

## Evidence

Existing reference: `design/references/home/default.png`. No image was generated. Fresh before/after captures use an isolated iPhone 16 / iOS 18.6 fixture with actual HomeScreen and shared bottom navigation; no database is opened. Values and sample records are fixture-only.

Local ignored artifacts: `build/visual-comparison/home-spend-nav/` contains normalized `reference.png`, `before.png`, `final.png`, `final-pair.png`, `spend-three.png`, raw captures and build/test logs. Both image sources were normalized to 393 × 852 with Lanczos.

The spending card begins around reference y=510 and current y=548. This difference includes preserved safe areas and previously approved section spacing. The following measurements are relative to each card, using non-background ink rows in x=25..149:

| Text | Reference y | Before y | After y |
| --- | --- | --- | --- |
| Title | 13..23 | 8..20 | 13..23 |
| Month | 38..45 | 27..33 | 38..45 |
| Amount including currency | 57..68 | 41..56 | 55..68 |
| Change | 79..86 | 64..69 | 79..86 |

Amount strings differ (`₩243,600` in the image versus the current Korean preference `243,600원`). The Korean suffix adds taller ink; the shared numeric subregion is within 1 px vertically. Currency formatting was not replaced to match an illustration.

Quick-add white glyph threshold bounds: reference x=188..202 / y=789..802, before x=191..199 / y=782..790, after x=188..202 / y=778..791. The new visible size is 15 × 14 px, equal to the reference; absolute y follows the real bottom inset.

This is a focused comparison, not a full-screen pixel-equivalence claim. The reference's illustrative graph heights/overlapping pale bar are not reproduced as fabricated spending data. The production nine-bucket chart and its placement are unchanged. Real data, locale formatting, gray placeholders, removed unsupported features, system glyphs, safe areas, and previously approved spacing account for other visible differences. No whole-Home visual approval is implied.

## Validation

- Added native Compose tests for spending row separation, captured plus bounds, the action callback, and plus size at font scale 2.
- Before the correction, default plus size and spending spacing failed. The initial isolated Android run passed all 3 tests; the centering follow-up expanded the passing class to 5 tests.
- The simulator framework and isolated Xcode host built and ran after the last adjustment; the final capture is from that build.
- `bash ./gradlew :composeApp:allTests :composeApp:ktlintCheck :composeApp:checkArchitecture`: passed after the final Kotlin adjustment, including Android production compilation. Full output is in `validation-final.log`.
- This fixture does not validate the complete quick-add save/cancel flow or real navigation. The unchanged callback is exercised by native tests; no user storage was touched.

## Final central-action placement and shadow

Removed the top-pinned 4 dp placement and centered the shared action inside the inset-free navigation control region. This follows the actual row height when text is enlarged and preserves the system home-indicator inset. At 393 × 852, the circle moved from y=766..803 to y=771..808; its size and the surrounding navigation did not change.

The user compared the original warm shadow with custom warm drawing and standard elevation variants, then selected standard **1 dp elevation**. The final implementation contains no custom shadow drawing. The original image remains a reference, and the explicit user selection governs this shadow treatment.

Final artifacts under the ignored comparison directory:

- `elevation1-raw.png` / `elevation1.png`: final iOS capture and normalized screen.
- `elevation1-comparison.png`: original / 2 dp / 1 dp, with circle centers aligned for inspection.
- `navigation-reference-before-after.png`: reference / old top-pinned placement / centered placement.
- `elevation1-ios.log`, `elevation1-xcode.log`: successful final framework and isolated host builds.
- `elevation1-validation.log`, `elevation1-precommit.log`: successful full Gradle validation, including Android compilation, allTests, ktlintCheck, and checkArchitecture.

The normal and 2× text alignment regressions failed before the centering fix and passed afterward; the native test class passed all 5 cases (`center-green.log`). The later shadow-only changes were rebuilt and captured on iOS. Tests and captures use explicit isolated fixtures; no sample data or fixed dates were added to production, and no production database was opened.
