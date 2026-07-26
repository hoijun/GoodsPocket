# Settings Default Reference Prompt

Use the approved Home image as the sole full-screen visual anchor and create one `393:852` Settings screen.

Required hierarchy:

1. iOS status bar.
2. Compact app bar with a thin rounded back chevron on the left, centered `설정`, and an empty right side.
3. `앱 바로 설정` section title.
4. One warm off-white grouped card with a single `언어` row and a compact `한국어` / `영어` segmented control. `한국어` is selected in orange.
5. `표시 형식` section title.
6. One warm off-white grouped card with exactly two read-only rows separated by an inset divider: `통화: KRW` and `날짜 형식: yyyy-MM-dd`.
7. Open warm background below the cards. Do not show bottom navigation or a center add button.

The back icon uses the existing back callback. The two language options use the existing language-change callback. Currency and date format remain dynamic, read-only preferences.

Use `#FFFCF8` background, `#FEFBF8` cards, `#EFEDEC` outlines, at most `0.5dp` shadow, `#FF7445` orange, `#202838` strong text, `#8A8F9B` muted text, compact Korean sans typography, zero letter spacing, and Compose-reproducible line icons.

Do not add notifications, marketing consent, backup, restore, reset, theme, font size, account, password, security, logout, app information, help, terms, privacy, profile, destructive actions, a hero card, a top-right action, or any other setting. Output one screen only with no phone frame, presentation board, collage, annotations, or outer background.
