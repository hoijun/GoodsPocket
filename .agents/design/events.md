# Events Image-Locked Contract

Read [Product Direction](product-direction.md), [Foundations](foundations.md), [Shared Components](components.md), and [Visual Verification](verification.md) with this file.

## Approved Reference

- Default state: `design/references/events/default.png`
- Normalized viewport: `393 x 852`
- Final prompt: `design/prompts/events-default.md`

## Hierarchy

1. system status bar
2. centered `이벤트` title
3. unframed month overview and count pill
4. one-row event-type filters
5. featured event card
6. `타임라인` title
7. timeline surface with rows and dividers
8. shared bottom navigation and centered quick add

Do not add calendar navigation or grid, search, sort, favorite, RSVP, notification, map, or screen-local FAB actions.

## Locked Geometry

| Element | Normalized measurement |
| --- | --- |
| Events content padding | `19px` horizontal |
| Page title | visual top near `59px` |
| Month label | starts at `19, 114` |
| Overview headline | starts at `19, 136` |
| Count pill | `349, 130`, fixed `25 x 27` surface |
| Filter row | top `178px`, height `31px`, gap `7px` |
| Featured card | `19, 223, 355 x 166` |
| Timeline title | starts at `19, 416` |
| Timeline card | `19, 441, 355 x 132` |
| Timeline row | `66px` high |
| Bottom-navigation border | shared locked position at `y=770` |
| Center quick add | shared `38px` action at about `176, 776` |

Additional domain rows scroll above the fixed bottom navigation.

## Styling And Type

- Background `#FFFCF8`, card `#FEFBF8`, outline `1px #EFEDEC`.
- Shadow at most `0.5dp`; card radius `10px`.
- Selected filter/navigation: `#FF7445` with white content.
- Payment due: pale peach and orange. Release: pale purple and `#8F6EF2`.
- Strong `#202838`, muted `#8A8F9B`, letter spacing `0`.
- Page title `18sp/22sp` bold; month `14sp/18sp` semibold; overview `20sp/25sp` bold.
- Filter `11sp/14sp` semibold.
- Featured badge `11sp/13sp` bold; date `15sp/19sp` bold; title `20sp/23sp` bold; location `14sp/17sp`.
- Section title `15sp/19sp` bold; row title `12sp/15sp` semibold; metadata/date `10sp/13sp`.

## State And Behavior

- `null` selects `전체`; existing `EventType` values keep `onTypeChange` callbacks.
- The nearest visible event is featured; every remaining event appears in date order in the timeline.
- Featured card and rows call `onEventClick(event.id)`.
- Month, count, labels, titles, locations, and dates remain domain-derived and localized.
- Empty states keep the title, overview, filters, and navigation geometry.
- Events remains a primary destination with Events selected in bottom navigation.

## Exclusions

- Status-bar glyph rasterization and Dynamic Island.
- Dynamic domain copy, count, month, and date formatting.
- Non-geometric font rasterization.
- The generated Events image placed shared navigation `10px` too high; the approved shared Home/Collection navigation geometry is authoritative.

Card bounds, filter geometry, type scale, padding, dividers, borders, shadows, and navigation position are not excluded.

## Verification Evidence

The final implementation was captured from the fixed iPhone 16 simulator and normalized to `393 x 852`.

- Final capture: `build/visual-comparison/events-final-393x852.png`.
- The selected filter surface starts at the locked `y=178` layout position.
- The featured card is `19, 223, 355 x 166`; its badge, date, title, and location visual tops are `256`, `288`, `316`, and `348`, matching the reference.
- The count surface uses a fixed `25 x 27` footprint at `x=349`; its number glyph visual top matches the reference at `y=139`.
- The timeline title starts at `y=416`; the timeline surface is `19, 441, 355 x 132` with two `66px` rows.
- Timeline title visual tops match the reference at `y=463` and `y=527`. The divider is drawn inside the first row so it does not consume row height.
- GPT Web secondary review was used to identify candidate spacing differences. Pixel-boundary measurement retained only reproducible geometry corrections; subjective suggestions that contradicted the normalized measurements were not applied.
- The current date separator follows the existing user date-format preference. The generated reference uses dots, so punctuation remains an explicitly excluded dynamic formatting difference.
- The generated reference navigation begins above the shared locked position. The implementation keeps the approved Home/Collection border at `y=770`.
