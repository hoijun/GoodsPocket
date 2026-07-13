# Design Foundations

## Color Roles

- Primary: `#FF7445` for primary actions, selected tabs, centered quick-add, active segmented controls, and key highlights.
- Secondary: `#36C781` for owned, complete, success, and calm positive emphasis.
- Tertiary: `#8F6EF2` for wishlist, event badges, and secondary highlights.
- Warning: `#FFB13B` for preorder, arrival, deadline, and waiting states.
- Strong text: `#202838`.
- Muted text and inactive navigation: `#8A8F9B`.
- Disabled or subdued text: `#C8CDD6`.
- Legacy general outline: `#EAE2DC`; image-locked cards use `#EFEDEC`.
- Warm grouped surface: `#FFF5EE`.
- App background: `#FFFCF8`.

Derived roles:

- `background`: `#FFFCF8`.
- `surface`: `#FFFFFF`, reserved for modal and deliberately raised surfaces.
- `surfaceVariant`: `#FFF5EE`.
- `surfaceRaised`: `#FFFFFF`.
- `outline`: `#EAE2DC`, except where a screen-specific image-locked contract overrides it.
- `onPrimary`: `#FFFFFF`.
- `onSecondary`: `#0F3B2A`.
- `onSurface`: `#202838`.
- `onSurfaceVariant`: `#8A8F9B`.
- `success`: `#36C781`.
- `warning`: `#FFB13B`.
- `error`: `#FF7445`.

Use warm light backgrounds instead of stark white. Keep most data surfaces neutral and let pastel colors act as accents. Accent colors should usually appear in buttons, badges, tabs, key numbers, and selected filters rather than full-screen blocks.

Never place long paragraphs on tinted backgrounds. Status colors should feel soft and readable, never loud unless the state is genuinely critical.

## State Colors

- `owned` or `in collection`: green filled or tinted badge.
- `wishlist`: purple filled or tinted badge.
- `reserved` or `arrival planned`: orange or peach badge.
- `sale`: warm brown or neutral badge.
- `event`: purple badge.
- `payment due`, `release soon`, or `D-day`: orange or peach emphasis paired with text.
- `completed`: green or quiet neutral according to importance.
- `cancelled`, `failed`, or destructive actions: orange/error treatment with clear wording.

Do not encode state through color alone. Pair color with a text label, icon, selected state, or state description.

## Typography

- Heading font: `Noto Sans KR` or an equivalent rounded Korean sans.
- Body font: `Noto Sans KR` or the platform Korean sans.
- Letter spacing: `0`.

Headings should be bold, rounded, and app-like. Body text must remain legible and compact enough for metadata-heavy screens. Dates, prices, counts, and status labels should be crisp and easy to compare at a glance.

Default hierarchy:

1. page title
2. section title
3. main metric
4. supporting metadata

General scale, used only when a screen-specific contract does not provide an exact value:

- `display`: `32-36sp`, bold, for rare hero totals.
- `headline`: `24-28sp`, semibold or bold, for screen titles.
- `title`: `18-20sp`, semibold, for card headers and detail sections.
- `body`: `14-16sp`, regular, for standard content and forms.
- `label`: `11-13sp`, medium or semibold, for chips, badges, captions, and helper text.

Use compact but breathable line heights. Avoid oversized headlines on operational screens. Avoid decorative handwriting, luxury serif styling, pixel fonts, or retro game treatments.

## Surfaces And Elevation

Use subtle shadows, rounded borders, and warm surface separation. The app should never feel glossy, glassy, or overly dimensional.

General rules, used only when a screen-specific contract does not provide an exact value:

- Standard card radius: `16dp`.
- Home hero radius: `18dp`.
- Compact goods card radius: `8dp`.
- Chips: soft pill shape.
- Inputs: large rounded rectangle.
- Bottom sheets: large rounded top corners.
- Primary buttons: pill or large rounded rectangle.
- Shadows: soft and low elevation.

Avoid hard pixel-offset shadows, heavy outlines, harsh black shadows, and multiple simultaneous elevation treatments. Prefer one clear layer of emphasis per section.

General spacing fallback:

- Screen horizontal padding: `16-20dp`.
- Section gap: `12-16dp`.
- Card internal padding: `14-18dp`.
- Compact metadata row gap: `6-8dp`.
- List item vertical spacing: `10-12dp`.

Use a verified screen-specific value whenever one exists.

## Light And Dark Behavior

Prefer light mode for primary implementations and reference matching. Dark mode may be supported, but it must preserve warm neutrals, soft tints, strong readability, and the same collector-focused mood.

Do not use pure black backgrounds, electric highlights, neon contrast, or a cold cyber aesthetic. Dark mode should still feel like the same collector planner product.

## Direction Checklist

Do:

- Make the product feel like a personal collection tracker and planner.
- Match reference-image rounded corners across cards, chips, sheets, and inputs.
- Keep dashboards tidy, compact, and scannable.
- Emphasize dates, status, counts, and spending totals with bold Korean sans typography.
- Let pastel accents add warmth without overpowering the data.
- Keep layouts functional when media is missing.
- Support Korean text density and system font scaling gracefully.

Do not:

- Add sale banners, promotional hero sections, or product spotlight layouts.
- Invent image treatments that diverge from the generated goods cards.
- Use harsh black-and-white contrast, heavy 8-bit styling, glassmorphism, or flashy gradients.
- Make the interface chaotic or overly toy-like.
- Let decoration compete with schedules, records, collection data, metrics, deadlines, or status.
- Turn data-heavy screens into marketplace feeds.
