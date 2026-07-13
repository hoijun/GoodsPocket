# Product Direction

## Product Mood

GoodsPocket is a mobile app for tracking, organizing, and reviewing a personal goods collection. It is built for users who collect animation, game, character, VTuber, and collaboration merchandise over time.

The product should feel like a collector's journal and schedule companion, not a shopping app, resale market, or social feed. The interface should feel like a finished collector app screenshot: warm, practical, image-friendly, and information-rich without becoming dense or finance-like.

Design for mobile-first use. Support Korean-first UI copy and short, dense metadata rows without the layout feeling cramped. Prioritize clarity over decoration. Light mode is the default presentation.

The visual style is locked to the generated GoodsPocket reference images. Those images are the source of truth for UI direction, not loose inspiration. Favor the generated image composition over abstract rules when there is a conflict.

Users should feel that the app helps them remember, manage, and care for their collection.

## Primary Composition

Home and dashboard screens follow this sequence:

1. branded GoodsPocket header with notification affordance
2. warm hero banner
3. today's collection summary
4. recently added goods carousel
5. current month spending summary
6. upcoming schedule list
7. bottom navigation with centered quick-add action

## Reference Screens

The current redesign target is the saved generated image set:

- Design system: common palette, cards, buttons, badges, tab bar, and sheet language.
- Home: `GoodsPocket` header, hero banner, collection summary, recent goods, spending, upcoming schedule.
- Collection: title-centered app bar, segmented status filter, search bar, 3-column goods grid, bottom status summary, bottom navigation.
- Events: title-centered app bar, upcoming/past segmented filter, monthly calendar, upcoming schedule cards, bottom navigation.
- My: profile hero, collection statistics, interested goods carousel, recent activity, settings list, bottom navigation.
- Settings: settings list in the same rounded-card system, reached from My.
- Collection Detail Sheet: large goods image, metadata, status, price, notes, action buttons.
- Collection Edit Sheet: image change, form fields, status selection, price/date fields, primary save button.

Reference image files:

- Design system: `/Users/Hoijun/Downloads/ChatGPT Image 2026년 4월 19일 오후 11_29_55.png`
- Home: `/Users/Hoijun/Downloads/ChatGPT Image 2026년 4월 19일 오후 11_37_28.png`
- Collection: `/Users/Hoijun/Downloads/ChatGPT Image 2026년 4월 19일 오후 11_46_22.png`
- Preorder/Reserved collection state: `/Users/Hoijun/Downloads/ChatGPT Image 2026년 4월 19일 오후 11_55_02.png`
- Events: `/Users/Hoijun/Downloads/ChatGPT Image 2026년 4월 26일 오후 08_56_28.png`
- My: `/Users/Hoijun/Downloads/ChatGPT Image 2026년 4월 26일 오후 08_58_58.png`
- Settings: `/Users/Hoijun/Downloads/ChatGPT Image 2026년 4월 26일 오후 09_02_42.png`
- Collection Detail Sheet: `/Users/Hoijun/Downloads/ChatGPT Image 2026년 4월 26일 오후 09_04_37.png`
- Collection Edit Sheet: `/Users/Hoijun/Downloads/ChatGPT Image 2026년 4월 26일 오후 09_06_24.png`

Match these images as closely as Compose allows before introducing alternate layouts.

## Scope Guardrails

- Preserve existing domain models, state, repositories, and event callbacks during visual work.
- Keep the app focused on personal collection tracking and planning.
- Do not make the app look like an online store, resale marketplace, social feed, or finance dashboard.
- Do not revive stale screens or routes solely because an older reference mentions them; confirm current product structure first.
