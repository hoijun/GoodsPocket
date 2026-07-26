# Screen Alignment Rules

Read [Product Direction](product-direction.md), [Foundations](foundations.md), [Shared Components](components.md), and [Visual Verification](verification.md) with this file.

## Shared Migration Rules

- Preserve domain models, state, repositories, state transitions, and event callbacks.
- Create presentation models only when a real visual mapping is required.
- Reuse verified colors, surface behavior, typography weights, media fallback, and shared navigation.
- Measure each screen's card width, height, padding, type size, and row rhythm from its own reference.
- Do not apply Home's destination-specific top inset or glyph transforms to other screens.
- Keep primary workflows operational while changing visual composition.

## Collection

References:

- `/Users/Hoijun/Downloads/ChatGPT Image 2026년 4월 19일 오후 11_46_22.png`
- Reserved state: `/Users/Hoijun/Downloads/ChatGPT Image 2026년 4월 19일 오후 11_55_02.png`

Rules:

- Preserve Collection filtering and item click handlers.
- Keep reserved or arrival-planned goods inside Collection as a status or segment.
- Reuse Home's goods-card surface, border, low shadow, typography weight, and gray fallback treatment where the reference supports them.
- Measure grid column count, thumbnail ratio, filter height, search height, and bottom summary independently from the Collection reference.
- Do not revive a separate preorder destination solely because an older visual reference includes one.

## Events

Reference: `/Users/Hoijun/Downloads/ChatGPT Image 2026년 4월 26일 오후 08_56_28.png`

Rules:

- Reuse the schedule badge colors, muted date typography, low-contrast dividers, and gray media fallback.
- Measure calendar cell size, month controls, segmented filter, event-card geometry, and list rhythm from the Events reference.
- Do not stretch Home schedule rows into calendar cells.
- Keep calendar selection and event handlers operational.

## My

My now has a normalized, image-locked contract. Read [My Image-Locked Contract](my.md) instead of older exploratory My references. Do not restore interested-goods or activity sections because the current My state and event contract does not expose them.

## Settings

Reference: `/Users/Hoijun/Downloads/ChatGPT Image 2026년 4월 26일 오후 09_02_42.png`

Rules:

- Keep Settings as an operational list reached through My.
- Do not turn setting groups into promotional cards.
- Reuse the warm background, `16dp` card language, low shadow, subtle outline, title weight, and dividers where supported by the reference.
- Preserve every existing setting, toggle, navigation action, and accessibility state.
- Measure row height, group spacing, icon box, switch geometry, and app-bar inset from the Settings reference.

## Detail And Editor Sheets

References:

- Detail: `/Users/Hoijun/Downloads/ChatGPT Image 2026년 4월 26일 오후 09_04_37.png`
- Edit: `/Users/Hoijun/Downloads/ChatGPT Image 2026년 4월 26일 오후 09_06_24.png`

Use pure white only when a sheet needs clear separation from the warm app background. Keep the border and muted typography family, but measure sheet corner radius, media height, form geometry, and action spacing from each sheet reference.

Read [Sheets And Forms](sheets-forms.md) for interaction structure.
