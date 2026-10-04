# Collection load feedback verification — 2026-10-04

Status: implemented and tested; user authorized commit/push after the comparison on 2026-10-04. This is not a full-screen pixel-equivalence pass.

## Behavior

Collection owns its loading/failure card inside the scrollable content below search. The initial read shows neither a zero summary nor a no-results message until a successful read. Retry preserves query/segment and the last successful data. Existing normal Collection styling and shared navigation remain in place. Shell, persistence, and command handlers are unchanged.

The reference candidate is `design/references/collection/load-failure.png`; its GPT Web provenance and exact prompt are in `design/prompts/collection-load-failure.md`. Loading uses the same card with localized progress text and an indeterminate indicator.

## Executed verification

- Native Compose regression first failed on the previous overlay placement and premature empty-state text, then passed after the correction.
- `bash ./gradlew :composeApp:allTests :composeApp:ktlintCheck :composeApp:checkArchitecture`: passed after the final spacing correction; includes Android production compilation. The explicitly requested `compileDebugKotlinAndroid` task also passed during this change.
- Isolated `connectedDebugAndroidTest`, selecting `CollectionFeedbackTest`: 4 passed, 0 skipped/failed. Covers initial failure placement, retry/loading/success, English at font scale 2, and search keyboard/query retention.
- Common recovery tests cover query/segment retention, successful-load state, retained previous data, and absence of writes.
- iOS simulator framework and Xcode fixture host built successfully. iPhone 16 / iOS 18.6 ran the real CollectionRoute and shared bottom navigation with a controlled in-memory repository. Native button interaction verified failure → loading → successful empty result. Error, loading, and success captures were saved.
- Separate application IDs (`goods.pocket.app.feedbacktest` on Android, `goods.pocket.app.feedbackvalidation` on iOS) isolate testing from the user's installed application. No production database or demo seed was used.

## Visual evidence and limits

Local artifacts are under `build/visual-comparison/collection-feedback/`: `reference.png`, `current-raw.png`, `current.png`, `pair.png`, `overlay.png`, `loading.png`, `retry-success.png`, logs, fixture hosts, and `measure.py` / `measurements.json`. These are ignored build artifacts, not production resources.

Reference (851 × 1847) and iOS capture (1179 × 2556) were normalized to 393 × 852 using Lanczos. The current safe-area layout translates the new card approximately 32 px down relative to the generated reference. The normalized iPhone safe area starts at y=61; the search-to-card gap remains 16 dp. Navigation uses the real bottom inset and begins around y=763, rather than the generated y=770.

Measured inclusive ink/paint bounds:

| Element | Reference | Current | Difference after subtracting 32 px from current y |
| --- | --- | --- | --- |
| Heading | 110,236–283,249 | 110,267–282,280 | ≤1 px |
| Supporting text | 127,264–265,274 | 126,296–265,306 | ≤1 px |
| Orange button | 31,298–361,336 | 31,330–361,368 | 0 px |

The card retains a 1 dp outline, 10 dp radius, no shadow, and flexible height for wrapped text. The generated gradient is represented by the existing flat theme orange. The generated filled Collection navigation glyph differs from the existing approved outline glyph; the shared navigation was not redesigned. System glyphs, Dynamic Island, font rasterization, and existing chrome differences prevent a full-screen equality claim. Partial bounds above only establish the new content's measured alignment.

Not verified by this fixture: full application navigation away/back, iOS keyboard with enlarged text, other screens' feedback UI, or save/cancel/delete flows. Those remain separate checks; Android native tests cover the keyboard/enlarged-text scenarios for this change. Empty collection copy is unchanged and remains a separate support-state review.
