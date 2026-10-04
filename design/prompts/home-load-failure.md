# Home Initial Load Failure

- Source: GPT Web, https://chatgpt.com/c/6ac24c6a-c218-83ee-8ef0-990773c1ab8c
- Reference: `design/references/home/load-failure.png`, normalized to 393×852 with Lanczos resampling.
- Status: generated under the user's delegated correction request; the user accepted the Home result and authorized continuing to Collection on 2026-10-04.
- Anchor: existing Home reference attached to that conversation. Normal Home is not redesigned.

## Final Generation Contract

Generate one Home initial-load-failure screen at 393:852, without device frame, collage, annotations or extra screens. Preserve the GoodsPocket brand and bell, the existing 120px hero and Home-selected shared navigation. Use a flat gray #D7D2CC hero placeholder, preserving “오늘도 즐거운 수집 생활! 🎁” and “새로운 굿즈와 추억을 기록해보세요.” over it.

Below the hero, replace unavailable summary, recent-goods, spending and schedule content with one error card. Do not show invented 0 counts, 0원 or empty-result messages. The card has 16px screen margins, #FEFBF8 fill, #EFEDEC outline, soft rounded corners and low elevation. It contains “홈 정보를 불러오지 못했어요”, “잠시 후 다시 시도해 주세요.” and one orange “다시 시도” button. Preserve the compact typography/button rhythm of the existing Collection error state. Leave the remainder of the page blank.

Use #FFFCF8 background, #FF7445 orange, #202838 strong text, #8A8F9B muted text and compact Korean sans. No new features, hearts, wishlist, sale, raw exceptions, new illustration or initial-letter placeholders.

## Implementation Interpretation

- Existing Home safe-area policy, normal-card geometry and user-selected 1dp center action take precedence over generated chrome drift.
- Loading uses the same feedback region with localized loading text and a progress indicator; it does not expose unknown summary values.
- After any successful observation, a later failure/retry retains the last successful content below the feedback card.
- Reference texture/gradient is not embedded as an image. All app content is native Compose.
