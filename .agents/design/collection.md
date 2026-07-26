# Collection Image-Locked Contract

Read [Product Direction](product-direction.md), [Foundations](foundations.md), [Shared Components](components.md), and [Visual Verification](verification.md) with this file.

This document locks the approved Collection references. The owned reference defines shared geometry; the reserved reference defines only state-specific visual content.

## Approved Reference

- Owned state: `design/references/collection/owned.png`
- Reserved state: `design/references/collection/reserved.png`
- Normalized viewport: `393 x 852`
- Owned generation prompt: `design/prompts/collection-owned.md`
- Reserved generation prompt: `design/prompts/collection-reserved.md`

Generated state images can vary slightly in coordinates. Do not create new images solely to eliminate that drift. Collection uses the owned-state measurements below as its single shared geometry contract; reserved uses its generated image for selection, copy, badge, and metadata treatment only.

## Component Hierarchy

1. system status bar
2. centered `컬렉션` app-bar title
3. `보유품 / 예약중 / 전체` segmented control
4. full-width search field
5. result count
6. vertically scrolling three-column goods grid
7. fixed collection summary band
8. shared bottom navigation with centered quick add

Do not add sort, filter-menu, view-mode, favorite, wishlist, sale, or overflow actions. Those capabilities are not part of the current Collection domain or event contract.

## Locked Geometry

All values below are measured in the normalized `393 x 852` reference. A `1px` measurement maps to `1dp` in the fixed comparison viewport.

| Element | Bounds or value |
| --- | --- |
| Screen horizontal padding | `16px` |
| Page title | centered, visual bounds `176, 54` to `219, 70` |
| Segmented control | `16, 92, 361 x 38` |
| Search field | `16, 146, 361 x 39` |
| Result count baseline region | starts at `17, 203` |
| Grid | starts at `16, 229` |
| Grid column width | `113px` |
| Grid horizontal gap | `10-11px` |
| First-row card height | `222px` |
| First-row media height | `135px` |
| Grid row gap | `11px` |
| Summary band | `16, 694, 361 x 66` |
| Bottom navigation top border | `770px` |
| Bottom navigation | `82px` high including home-indicator region |
| Center quick-add | approximately `40px` diameter at `176, 776` |

The grid occupies the flexible content region between the result count and summary band. Dynamic content scrolls behind neither the summary band nor bottom navigation.

## Locked Styling

- Background: `#FFFCF8`.
- Card and summary surface: `#FEFBF8`.
- Outline: `1px #EFEDEC`.
- Goods-card radius: `8px`.
- Standard shadow: at most `0.5dp`; keep the reference's visual separation without a visible halo.
- Selected segment and selected navigation: `#FF7445` with white content.
- Owned badge: pale green container with `#36C781` content.
- Strong text: `#202838`; metadata and inactive chrome: `#8A8F9B`.
- Missing media: flat `#D7D2CC` placeholder; do not reproduce the generated raster gradient in Compose.

## Typography

- Page title: `18sp / 22sp`, bold.
- Segment label: `13sp / 16sp`, bold.
- Search placeholder: `13sp / 17sp`, regular.
- Result count: `12sp / 15sp`, semibold.
- Goods name: `11sp / 14sp`, bold, maximum two lines.
- Series and category: `9sp / 12sp`, regular, maximum one line.
- Status badge: `9sp / 11sp`, bold.
- Summary label: `9sp / 11sp`, medium.
- Summary value: `16sp / 19sp`, bold.
- Bottom-navigation label: use the shared `9sp / 11sp` contract.

Letter spacing is `0` for every Collection text style.

## State And Behavior

- `CollectionSegment.OWNED` maps to the selected `보유품` segment.
- `CollectionSegment.RESERVED` maps to the selected `예약중` segment without changing shared geometry.
- Segment taps keep calling the existing `onSegmentChange` callback.
- Search text keeps calling `onQueryChange` and filters name, series, or character.
- Goods cards keep stable entry IDs and call `onEntryClick(entry.id)`.
- Owned cards show localized series and category metadata.
- Reserved cards show localized reservation store and release-date metadata.
- Reserved badges use the pale peach container and primary-orange content shown in the reserved reference.
- Counts, prices, names, categories, status labels, and metadata remain domain-derived.
- The fixed summary presents owned count, reserved count, and total purchase amount.
- Missing media contains no initials, text, generated artwork, or screenshot crop.
- Empty and no-result states preserve the same header, controls, summary, and navigation geometry.

## Chrome

- Collection is a primary destination with no back button or notification bell.
- The shared bottom navigation remains visible and highlights Collection.
- The centered orange quick-add button keeps its existing callback.
- Do not add a second FAB or screen-local add button.

## Comparison Exclusions

- System status-bar glyph rasterization.
- Dynamic domain values and localized currency formatting.
- Non-geometric font rasterization.
- The generated reference's gray gradient is intentionally replaced by the locked flat placeholder color.

Card bounds, grid tracks, search and segment geometry, summary position, type size, line height, borders, shadows, and bottom-navigation position are not excluded.

## Verification Evidence

The owned and reserved implementations were captured from the fixed iPhone 16 simulator and normalized to `393 x 852`.

- Shared title, control, count, grid, summary, and navigation bounds are produced by the same Compose hierarchy in both states.
- The reserved capture's first-row flat media region occupies `y=229..362`; the owned geometry reference occupies `y=228..362` after thresholding. This is within the `1px` geometry tolerance.
- The summary band remains at `16, 694, 361 x 66`, and the bottom-navigation top border remains at `y=770`.
- The reserved generated image's independent row and summary drift is not used as geometry input. Its selected segment, reservation metadata, and peach/orange badge treatment are used as the state reference.
- The deterministic seed currently renders two reserved entries; missing generated sample cards and dynamic counts are permitted content differences.
- GPT Web secondary review confirmed the selected reserved segment, domain result count, store/release metadata, reserved badges, shared structure, and absence of unsupported controls.
