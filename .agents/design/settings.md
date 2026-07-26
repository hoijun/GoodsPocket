# Settings Image-Locked Contract

Read [Product Direction](product-direction.md), [Foundations](foundations.md), [Shared Components](components.md), and [Visual Verification](verification.md) with this file.

## Approved Reference

- Default Korean-preference state: `design/references/settings/default.png`
- Normalized viewport: `393 x 852`
- Final prompt: `design/prompts/settings-default.md`

## Hierarchy

1. system status bar
2. compact app bar with a left back chevron, centered `설정`, and empty right side
3. `앱 바로 설정` section title
4. one language card with `언어` and a two-option segmented control
5. `표시 형식` section title
6. one grouped card with two read-only rows and one inset divider
7. open warm background with no bottom navigation

Do not add notifications, marketing consent, backup, restore, reset, theme, font size, account, password, security, logout, app information, help, terms, privacy, profile, destructive actions, a hero card, a top-right action, or a center quick-add action.

## Locked Geometry

| Element | Normalized measurement |
| --- | --- |
| Screen horizontal padding | `16px` |
| Page title | visual top near `60px`, `18sp/22sp` |
| Back chevron | visual bounds near `22, 59` to `30, 74` |
| Header-to-first-section spacing | `31dp` |
| First section title | visual start near `22, 114` |
| Section-title-to-card spacing | `10dp` |
| Language card | `361 x 72px`, visible top near `142px` |
| Language segmented control | visible horizontal bounds `x=194...360`, `31px` high |
| Language-card-to-format-title spacing | `29dp` |
| Format section title | visual start near `22, 244` |
| Format card | `361 x 120px`, visible top near `272px` |
| Format row | `60px` high |
| Format icon container | `31px`, starts near `32px` layout x |
| Format row text | visual x near `81px` |

Settings uses the normal Scaffold top inset and has no bottom bar inset. Content can scroll when localization or font scaling requires it.

## Styling And Type

- Background `#FFFCF8`, card `#FEFBF8`, card outline `1px #EFEDEC`.
- Card radius `10px`; card shadow `0.5dp`.
- Secondary control and icon-container outline `#E5E3E2`.
- Primary selected segment `#FF7445` with white content.
- Strong `#202838`, muted `#8A8F9B`, letter spacing `0`.
- Page title `18sp/22sp` bold; section title `14sp/19sp` bold.
- Language label `14sp/20sp` semibold; segment label `13sp/16sp` semibold.
- Read-only row text `12sp/17sp` semibold.
- Back, currency, and calendar symbols are native Compose line drawings.

## State And Behavior

- The back affordance calls the existing `onBack` callback and returns to the selected primary destination.
- `한국어` and `영어` are a real two-option selectable group and call the existing `onLanguageChange` callback with the existing language codes.
- The visible selected option follows `AppPreference.languageCode`.
- Currency and date-format rows remain dynamic, localized, and read-only.
- Read-only rows have no chevrons, switches, click handlers, or menus.
- Settings remains a secondary screen reached from My and never displays shared bottom navigation.

## Exclusions

- Status-bar time, glyph rasterization, and Dynamic Island.
- Dynamic preference values and translated copy after changing language.
- Non-geometric font rasterization and glyph-only drift within `1-2px`.

App-bar structure, grouped-card bounds, segmented-control bounds, row heights, divider placement, icon-container type, card shadow, outlines, and action affordances are not excluded.

## Verification Evidence

- Final iPhone 16 capture normalized to `393 x 852`: `build/visual-comparison/settings-final-393x852.png`.
- Side-by-side comparison: `build/visual-comparison/settings-final-side-by-side.png`.
- Half-opacity overlay: `build/visual-comparison/settings-final-overlay-50.png`.
- The segmented-control occupied bounds match the reference at `x=194...360`.
- The secondary icon outline differs from the generated reference by at most two RGB levels at the measured border point.
- Maestro verifies the real `마이 -> 설정` path and all required Korean labels.
- GPT Web secondary review confirmed the app-bar, hierarchy, segmented control, grouped rows, inset divider, absent bottom navigation, and absence of unsupported settings.
