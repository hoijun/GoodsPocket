# Home Image-Locked Contract

This document records the values verified against the Home reference. It overrides looser ranges in shared design documents for the Home screen.

Reference: `/Users/Hoijun/Downloads/ChatGPT Image 2026년 4월 19일 오후 11_37_28.png`

Read [Visual Verification](verification.md) before comparing or declaring Home complete.

## Locked Colors

- App background: `#FFFCF8`.
- Image-locked card surface: `#FEFBF8`.
- Hero surface: `#FEF9F5`.
- Home/card outline: `#EFEDEC` at `1dp`.
- Bottom navigation outline: `#F3F0EE` at `1dp`.
- Primary orange: `#FF7445`.
- Owned green: `#36C781`.
- Wishlist/event purple: `#8F6EF2`.
- Sale/warning orange: `#FFB13B`.
- Strong text: `#202838`.
- Muted text and inactive navigation: `#8A8F9B`.
- Gray media placeholder fill: `#D7D2CC`.
- Gray media placeholder detail: `#C7C1BA`.

Do not use pure white as the default image-locked card fill. Reserve `#FFFFFF` for modal or deliberately raised surfaces. Do not use the legacy `#EAE2DC` outline on Home cards because it renders too dark against the warm background.

## Locked Surfaces And Shadows

- Standard dashboard/list card: `16dp` radius, `#FEFBF8`, `1dp #EFEDEC` border, `0.25dp` shadow.
- Hero card: `18dp` radius, `#FEF9F5`, `1dp #EFEDEC` border, `0dp` shadow.
- Compact goods card: `8dp` radius, `#FEFBF8`, `1dp #EFEDEC` border, `0.5dp` shadow.
- Bottom navigation: flat `#FFFCF8`, `0dp` shadow, `1dp #F3F0EE` top border.
- Center quick-add: `38dp` orange circle with `0.5dp` shadow.
- Badges and D-day treatments: filled pastel containers without borders or extra shadows.

Never use the old `3dp` or `4dp` card elevation on Home. Do not stack a border, tonal elevation, and visible drop shadow on one surface.

## Locked Geometry

| Element | Locked value |
| --- | --- |
| Screen horizontal padding | `16dp` |
| Main vertical item spacing | `12dp` |
| Hero | `120dp` high, `18dp` radius |
| Hero text start | `18dp` from card start |
| Today summary | `98dp` high, `16dp` radius |
| Summary title | start `13dp`, top `10dp` |
| Summary metrics | four equal columns across the full card width |
| Summary divider | `1 x 40dp` |
| Recent section header | stable `19dp` high |
| Recent goods card | `84 x 140dp` |
| Recent image area | `84 x 96dp` |
| Recent card spacing | `8dp` |
| Spending card | `102dp` high |
| Spending card padding | horizontal `13dp`, vertical `7dp` |
| Spending chart | `120 x 56dp` |
| Spending bar | `7dp` wide, `7dp` gap |
| Schedule thumbnail | `40dp`, `10dp` radius |
| Schedule content | `7 + 40 + 14 + 40 + 7dp` vertical rhythm |
| Schedule horizontal padding | `11dp` |
| Bottom navigation | `81dp` high |
| Bottom nav icon canvas | `24dp` |
| Center quick-add | `38dp` diameter |

The Home Scaffold keeps its bottom inset unchanged and reduces only the Home top inset by `25dp`. This is destination-specific. Collection, Events, My, and Settings retain their own insets until measured against their references.

Do not move an entire scroll viewport with `Modifier.offset` to compensate for one device. Adjust Scaffold content padding so top and bottom clip boundaries remain correct.

## Locked Typography

- Brand logo: base `22sp`, `26sp` line height, extra bold. The Home implementation applies a small reference-only glyph transform; do not copy it to other titles.
- Hero title: `15sp / 20sp`, bold.
- Hero body: `12sp / 19sp`, medium.
- Summary and section titles: `13sp / 17sp`, bold.
- Summary metric label: `10sp / 14sp`, semibold.
- Summary metric number: `20sp / 24sp`, bold.
- Recent goods series: `9sp / 11sp`.
- Recent goods title: `10sp / 13sp`, bold.
- Spending amount: `18sp / 20sp`, bold.
- Spending metadata and change: `10sp / 12-13sp`.
- Schedule title: `11sp / 14sp`, bold.
- Schedule date: `10sp / 13sp`, medium.
- Badge: `9sp / 11sp`, bold.
- D-day: `10sp / 13sp`, bold.
- Bottom navigation label: `9sp / 11sp`.

Use `FontWeight.Bold` for normal hierarchy. `ExtraBold` is reserved for the brand. Do not inherit generic Material typography inside compact image-locked cards. Letter spacing is `0`.

## Behavior

- `View all` is text plus a separate thin chevron glyph, never a typed `>` character.
- Bell, navigation, chart, placeholder, and chevron visuals are native Compose components or drawing, never cropped screenshot assets.
- Recent goods cards have no favorite affordance because the current domain and event contract does not support favorites.
- Missing media uses a gray placeholder while preserving the exact image box. Do not use initials as fake artwork.
- Recent goods remain horizontally scrollable with stable item keys and click IDs.
- Summary, spending, recent activity, and schedule values come from domain state. Do not hardcode reference-only values into production UI.
- Existing event handlers and navigation callbacks stay attached to the visible action component.
- A screenshot is a visual comparison source only, never an app `Image` replacing the UI.
