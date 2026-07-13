# Shared Component Rules

Use these rules with [Foundations](foundations.md). A screen-specific image contract overrides general ranges in this file.

## Dashboard Cards

Dashboard cards present one main piece of information with a few supporting details. Monthly spending, collection counts, active preorders, and upcoming events should be understandable within a few seconds.

Use warm off-white cards, low-contrast dividers, and restrained accent colors. Each card should have one strong metric or heading, one short support line, and optional status badges. Avoid more than three emphasis colors in one screen region.

## Collection Cards

Collection UI should feel organized and collectible. Prioritize item name, series or character context, category, quantity, and storage location.

The visual target uses a 3-column goods grid with large thumbnails, status chips, and favorite affordances. Reserved or arrival-planned goods belong inside Collection as a status or segment, not as a separate top-level tab.

A list fallback may exist for accessibility or dense data, but it does not replace the reference grid when implementing the image-locked screen.

## Thumbnail And Fallback Media

Product images are a primary part of the image-locked direction. Use large rounded rectangles that preserve the reference media ratio.

If no image exists, use the locked gray placeholder treatment while preserving the exact image box. Do not substitute initials or fake text artwork. Fallback media must preserve card proportions so the layout does not collapse.

## Preorder And Event Blocks

Make date and state immediately visible. Use compact chips, badges, and date labels for waiting, due soon, received, cancelled, or completed states.

Place D-day, due date, and state labels near the top of the information cluster. Use hierarchy instead of aggressive warning colors. Preorder cards belong in Collection when they represent future collection items. Event cards remain in Events when they represent popups, fairs, applications, visits, or calendar commitments.

## Transaction Surfaces

Spending history should feel structured and calm. Amount, transaction type, and date must be immediately scannable.

Use simple grouping and clear dividers instead of chart-heavy finance-dashboard styling. Spending totals may be highlighted; detailed rows remain compact and ledger-like.

## List, Loading, Empty, And Error States

- Loading: subdued rounded skeletons or placeholders using the same component geometry as loaded content.
- Empty: encouraging, lightweight, and clear about the next useful action.
- No results: suggest changing search or filters; do not make it indistinguishable from a truly empty collection.
- Error: calm, readable, action-oriented, with one clear retry or recovery action.
- Destructive confirmation: stronger contrast and spacing while remaining inside the pastel system.

Empty-state illustration may use minimal pastel stickers, item placeholders, or line drawings. Do not use full-scene promotional artwork.

## Search Bars

Search bars are full-width, softly outlined, rounded, and visually calm. A leading search icon is appropriate when it follows the rounded line-icon language. Do not style search like store discovery or promotion.

## Chips And Filters

Chips are compact pills with adequate touch targets. Selected chips may use primary or secondary tinted fills. Keep labels short and scannable in Korean.

Status chips carry meaning; decorative chips do not. Use one emphasized active state and keep inactive states quiet but legible. Horizontal filter bars may scroll, but common filters should remain visible first.

## Segmented Controls

Segmented controls are compact, stable, and utility-first. Use one emphasized active state. Avoid oversized tab bars, harsh underlines, or decorative category navigation.

## Section Headers

Pair a clear title with a short support line only when useful. Keep section headers friendly and informative, not editorial. Avoid oversized all-caps treatments.

For a `View all` action, use text plus a separate thin chevron icon rather than a typed `>` character.

## Navigation

Bottom navigation must feel stable, simple, and obvious. Use legible rounded line icons or simple filled symbols with labels.

Keep one centered orange quick-add action. It may be slightly more playful than surrounding controls, but must retain the same visual system. Avoid oversized FABs, competing floating actions, or commerce-style promotional treatment.

The active tab uses the screen contract's accent treatment. Navigation remains grounded and utility-first.

## Iconography

Icons are simple, rounded, friendly, and readable at small sizes. Prefer a project icon library when available. Use native Compose drawing for reference-specific symbols that cannot be matched otherwise.

Icons support data and actions; they do not become the visual focal point. Provide accessibility labels for meaningful icons and mark decorative visuals appropriately.
