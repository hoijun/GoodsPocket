# My Image-Locked Contract

Read [Product Direction](product-direction.md), [Foundations](foundations.md), [Shared Components](components.md), and [Visual Verification](verification.md) with this file.

## Approved Reference

- Default local-profile state: `design/references/my/default.png`
- Normalized viewport: `393 x 852`
- Final prompt: `design/prompts/my-default.md`

## Hierarchy

1. system status bar
2. centered `마이` title with no trailing action
3. one profile card with a flat gray avatar, profile name, status pill, and hint
4. `내 요약` title
5. one `2 x 2` summary card with four domain values and inset dividers
6. `계정 관리` title
7. one three-row management card with internal dividers
8. shared bottom navigation and centered quick add

Do not add profile editing, avatar upload, login or account-connect CTA, interested-goods, recent activity, social statistics, logout, toggles, promotional content, or a screen-local FAB.

## Locked Geometry

| Element | Normalized measurement |
| --- | --- |
| Screen horizontal padding | `15px` |
| Page title | `18sp/22sp`, visual top near `56px` |
| Title-to-profile spacing | `13dp` |
| Profile card | `363 x 120px`, visible top near `90px` |
| Avatar | `76px`, starts near `30, 112` |
| Profile-to-summary-title spacing | `29dp` |
| Summary title | visual top near `242px` |
| Summary title-to-card spacing | `10dp` |
| Summary card | `363 x 190px`, visible top near `269px` |
| Summary cell | `181.5 x 95px` |
| Summary-to-management-title spacing | `30dp` |
| Management title | visual top near `491px` |
| Management title-to-card spacing | `8dp` |
| Management card | `363 x 210px`, visible top near `515px` |
| Management row | `70px` high |
| Bottom-navigation border | shared locked position at `y=770` |

Content can scroll above the fixed bottom navigation. The Scaffold keeps the bottom inset and reduces only the My top inset by `32dp`.

## Styling And Type

- Background `#FFFCF8`, card `#FEFBF8`, outline `1px #EFEDEC`.
- Card radius `10px`; shadow at most `0.5dp`.
- Flat avatar placeholder `#D7D2CC`, with no gradient, text, initial, symbol, or illustration.
- Strong `#202838`, muted `#8A8F9B`, primary `#FF7445`, green `#36C781`, purple `#8F6EF2`, warning `#FFB13B`.
- Page title and profile name `18sp`; section title `15sp`; summary value `20sp`; management title `13sp`.
- Summary icons use circular pastel containers. Management icons use compact rounded neutral containers.
- Internal dividers are inset and low contrast. Do not replace the two grouped cards with independent tiles or rows.
- Letter spacing is `0`. Keep hierarchy compact and operational rather than promotional.

## State And Behavior

- Profile name, sync status, owned count, active preorder count, monthly spend, and upcoming event count remain state-derived.
- Currency uses the existing localized formatter; do not hardcode the generated sample values.
- Sync and backup and Notifications are informational with the current contract. They have no click affordance or chevron.
- Only Settings shows a chevron and calls the existing `onOpenSettings` callback.
- The center quick add and all destination changes remain owned by shared app chrome.
- Missing media remains a native flat gray Compose placeholder; the reference image is never embedded in the app.

## Exclusions

- Status-bar time, glyph rasterization, and Dynamic Island.
- Dynamic profile and summary values, including localized currency punctuation.
- Non-geometric font rasterization.
- The generated avatar gradient; the approved implementation uses the required flat `#D7D2CC` placeholder.
- The generated image places navigation slightly above the shared lock; `y=770` remains authoritative.

Card grouping, heights, widths, dividers, icon-container types, type hierarchy, shadows, and action affordances are not excluded.

## Verification Evidence

- Final iPhone 16 capture normalized to `393 x 852`: `build/visual-comparison/my-final-393x852.png`.
- Side-by-side reference comparison: `build/visual-comparison/my-final-side-by-side.png`.
- Half-opacity overlay: `build/visual-comparison/my-final-overlay-50.png`.
- The implementation keeps one `120px` profile card, one `190px` summary card, and one `210px` management card with three `70px` rows.
- The summary remains a single `2 x 2` surface; the management section remains one grouped three-row surface.
- GPT Web secondary review confirmed the hierarchy and absence of unsupported actions. Candidate size and spacing suggestions that contradicted the normalized pixel measurements were not applied.
- `:composeApp:compileDebugKotlinAndroid` and `:composeApp:allTests` pass with the locked metrics contract.
