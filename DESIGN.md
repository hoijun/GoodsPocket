# DESIGN.md

## Overview
GoodsPocket is a mobile app for tracking, organizing, and reviewing a personal goods collection.
It is built for users who collect animation, game, character, VTuber, and collaboration merchandise over time.
The product should feel like a collector's journal and schedule companion, not a shopping app, resale market, or social feed.

The visual style should feel cute, warm, tidy, and dependable.
Favor rounded shapes, gentle contrast, soft pastel accents, and compact information blocks.
The interface should remain practical and text-first, because users need to scan spending, preorder status, dates, and storage information quickly.

Design for mobile-first use.
Support Korean-first UI copy and short, dense metadata rows without the layout feeling cramped.
Prioritize clarity over decoration.
Light mode is the default presentation.
Dark mode may be supported, but it should preserve the same warm, soft, collector-focused mood rather than shifting to a cold or neon aesthetic.

Home and dashboard screens should prioritize information in this order:
1. upcoming events and deadlines
2. current month spending
3. active preorder count
4. owned collection count
5. recent activity
6. quick add actions

Favor utility-first layouts with a warm collector mood.
Users should feel that the app helps them remember, manage, and care for their collection.

## Colors
- Primary: `#F26CA7`
  Use for primary actions, selected states, key highlights, and friendly emphasis.
- Secondary: `#87DCCB`
  Use for supportive accents, completion states, active filters, and calm secondary emphasis.
- Tertiary: `#FFD46F`
  Use for upcoming schedule badges, deadline highlights, and cheerful accents.
- Neutral:
  - `#2B2530` for strong text
  - `#665D6D` for secondary text
  - `#AFA6B5` for disabled or muted text
  - `#EDE6EE` for borders and dividers
  - `#F8F2F7` for surface backgrounds
  - `#FFF9FC` for app background

Derived color roles:
- `surface`: `#FFF9FC`
  Default screen and sheet background.
- `surfaceVariant`: `#F8F2F7`
  Use for cards, grouped list sections, and soft containers.
- `surfaceRaised`: `#FFFFFF`
  Use for elevated cards and modal surfaces.
- `outline`: `#E2D5E3`
  Use for borders, dividers, and text field strokes.
- `onPrimary`: `#FFFFFF`
  Use for text and icons on primary fills.
- `onSecondary`: `#173B35`
  Use for text and icons on secondary fills.
- `onSurface`: `#2B2530`
  Main text color.
- `onSurfaceVariant`: `#665D6D`
  Secondary text color.
- `success`: `#73C7A5`
  Use for completed, received, or settled states.
- `warning`: `#F3B85A`
  Use for upcoming deadlines and due soon states.
- `error`: `#E07A94`
  Use for cancelled, destructive, or error states.

Use warm light backgrounds instead of stark white.
Keep most data surfaces neutral and let pastel colors act as accents.
Status colors should feel soft and readable, never loud or alarming unless the state is genuinely critical.
Never place long paragraphs of text on tinted backgrounds.
Accent colors should usually appear in buttons, badges, tabs, key numbers, and selected filters rather than full-screen blocks.

State mapping:
- `owned` or `in collection`: neutral or soft secondary accent, calm and stable
- `active preorder`: primary or secondary tint, should feel in progress but not urgent
- `payment due` or `release soon`: warning accent, noticeable but still soft
- `received` or `completed`: success accent
- `cancelled`, `failed`, or destructive actions: error accent
- `transferred planned`, `inactive`, or archived states: muted neutral styling

Status chips should use filled or softly tinted containers with high-contrast text.
Do not encode status through color alone; combine color with text labels.

## Typography
- Heading font: `Gowun Dodum`
- Body font: `Noto Sans KR`

Headings should feel rounded, friendly, and lightly expressive.
Body text must remain highly legible and compact enough for metadata-heavy screens.
Dates, prices, counts, and status labels should feel crisp and easy to compare at a glance.

Favor clear hierarchy:
- page title
- section title
- main metric
- supporting metadata

Avoid decorative handwriting, luxury serif styling, or overly playful display treatments.

Preferred scale:
- `display`: 32-36px, bold, for main monthly totals or rare hero numbers
- `headline`: 24-28px, semibold or bold, for screen titles and large section anchors
- `title`: 18-20px, semibold, for card headers and detail section headings
- `body`: 14-16px, regular, for standard content and form text
- `label`: 11-13px, medium or semibold, for chips, badges, captions, and helper text

Use compact but breathable line heights.
Avoid oversized headline treatments on normal screens.
Numbers for spending, counts, and dates should feel especially crisp and high-contrast.

## Elevation
Use light elevation and soft shadows.
Prefer separation through spacing, subtle border lines, and tonal surface changes rather than strong floating layers.
Cards should feel tactile and cozy, like planner blocks or collection note cards.

The app should never feel glossy, glassy, or overly dimensional.

Roundedness and spacing rules:
- small chip radius: 999px pill
- input radius: 16-18px
- card radius: 20-24px
- bottom sheet radius: 24-28px top corners
- primary button radius: 18-20px

Spacing rules:
- screen horizontal padding: 16-20px
- section gap: 12-16px
- card internal padding: 14-18px
- compact metadata row gap: 6-8px
- list item vertical spacing: 10-12px

Prefer one clear layer of emphasis per section.
If a card is tinted, keep the content layout simple and avoid stacking additional tinted pills inside it unless they communicate status.
Dark surfaces should remain muted plum-gray rather than pure black.

## Components
### Dashboard Cards
Home cards should present one main piece of information with a few supporting details.
Monthly spending, active preorders, owned item count, and upcoming events should be easy to read within a few seconds.
Use compact card layouts with rounded corners and restrained accent color.
Each card should have one strong metric or heading, one short support line, and optional badges or pills.
Avoid crowded dashboards with more than three emphasis colors on the same screen.

### Collection Lists and Cards
Collection UI should feel organized and collectible.
Prioritize item name, series or character context, category, quantity, and storage location.
Layouts must still work well without images, because the MVP is text-first.
If thumbnails are present, they should remain secondary to the text structure.
Collection rows should preserve scannability even with long Korean item names.

### Thumbnail and Fallback Media
Images are optional, not foundational.
When thumbnails exist, use small rounded rectangles or soft squares.
If no image exists, use a pastel placeholder with a simple icon, category marker, or short initial-based fallback.
Fallback media should never dominate the row height or push metadata below the fold.
Text-first layouts must remain complete and attractive without any image.

### Preorder and Event Blocks
Preorder and event rows should make date and state obvious.
Use chips, badges, and date labels to communicate waiting, due soon, received, cancelled, or completed states.
Time-sensitive information should stand out through hierarchy, not through aggressive warning colors.
Use D-day labels, due dates, and state chips near the top of the information cluster.
Do not hide important schedule signals in low-contrast captions.

### Transaction Surfaces
Spending history should feel structured and calm.
Amounts, transaction type, and date should be immediately scannable.
Use simple grouping and clear dividers instead of chart-heavy or finance-dashboard styling.
Spending totals may be highlighted, but detailed rows should remain compact and ledger-like.

### Inputs and Quick Entry
Forms should feel lightweight and low-friction.
Quick entry matters, so inputs must feel welcoming and easy to complete with minimal required fields.
Bottom sheets and editors should look clean, soft, and efficient rather than formal or dense.
Primary save actions should remain obvious.
Support partial entry without making the form feel incomplete or broken.

### List, Loading, and Empty States
List-heavy screens should clearly distinguish between loading, empty, no results, and error states.
Loading states should prefer soft skeleton rows or subdued placeholders over spinners dominating the screen.
Empty states should feel encouraging and lightweight.
No-result states should suggest filtering or search adjustment rather than looking identical to true empty states.
Error states should be calm, readable, and action-oriented, with one clear retry or recovery action.
Delete confirmation and destructive prompts should use stronger contrast and spacing, but remain visually consistent with the pastel system.

### Search Bars
Search bars should be full-width, softly outlined, and visually calm.
Leading search icons are appropriate.
Do not style search like a store search or promotional discovery surface.

### Chips and Filters
Chips should be pill-shaped, compact, and easy to tap.
Selected chips may use tinted fills from the primary or secondary palette.
Filters should feel playful but still utilitarian.
Status chips should be more important than decorative chips.
Keep chip labels short and easy to scan in Korean.

### Segmented Tabs and Filter Bars
Segmented controls for preorder status, transaction type, and event type are core patterns.
They should feel compact, stable, and utility-first.
Use a single emphasized active state and keep inactive states quiet but legible.
Avoid oversized tab bars, underlines with harsh contrast, or overly decorative category navigation.
Horizontal filter bars may scroll, but the default visible set should show the most common filters first.

### Section Headers
Section headers should pair a clear title with a short support line when needed.
They should feel friendly and informative, not editorial.
Use gentle contrast and avoid oversized all-caps treatments.

### Navigation
Bottom navigation should feel stable, simple, and obvious.
Icons and labels should be highly legible.
Do not over-style navigation with large floating controls or commerce-style emphasis.
The active tab may use soft tinting or stronger icon contrast, but the bar should still feel grounded and utility-first.

### Floating Action Button
Use a single prominent quick-add action.
It may feel slightly more playful than the rest of the UI, but it should still match the rounded pastel system.
Avoid oversized floating actions or multiple competing FABs.

### Bottom Sheets
Bottom sheets are a core interaction pattern for quick add, detail inspection, and editing.
Sheets should feel soft, tidy, and high-trust.
Use clear section grouping, strong titles, and one primary action.

### Detail Sheets and Editor Structure
Detail and editor sheets should follow a predictable structure:
1. title and status
2. key metadata
3. related entities or linked records
4. primary action
5. secondary actions
6. destructive action last

Important metadata should appear near the top.
Destructive actions should be clearly separated from edit or confirm actions.
Do not bury key state, due date, or financial information below long descriptive content.

### Empty States
Empty states may use soft iconography or light characterful illustrations, but they must remain minimal.
The mood should be encouraging and gentle, not promotional.
Illustrations should be simple, rounded, and low-detail.
Prefer small icons, stickers, or line illustrations over full-scene artwork.

### Iconography
Icons should be rounded, simple, and easy to read at small sizes.
Favor friendly system-style icons over sharp enterprise glyphs.
Icons should support the data, not become the visual focal point.

## Light and Dark Behavior
Prefer light mode for primary mocks and generated screens.
If dark mode is shown, preserve warm neutrals, soft tints, and strong readability.
Do not use pure black backgrounds, electric highlights, or cyber-style contrast.
Dark mode should still feel like the same collector planner product, not a different app theme.

## Do's and Don'ts
### Do
- Make the product feel like a personal collection tracker and planner
- Use rounded corners across cards, chips, sheets, and inputs
- Keep dashboards tidy, compact, and easy to scan
- Emphasize dates, status, counts, and spending totals clearly
- Let pastel accents add warmth without overpowering the data
- Keep layouts functional even when there are no images
- Support Korean text density gracefully

### Don't
- Don't make the app look like an online store or resale marketplace
- Don't use sale banners, promo hero sections, or product spotlight layouts
- Don't rely on large imagery or image-first cards
- Don't use harsh black-and-white contrast as the default mood
- Don't use oversized shadows, glassmorphism, or flashy gradients
- Don't make the interface childish, noisy, or toy-like
- Don't let decorative elements compete with schedules, records, and collection data
- Don't turn list-heavy screens into gallery-style feeds
- Don't bury metrics, deadlines, or status under decorative framing
