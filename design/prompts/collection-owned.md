# Collection Owned Reference Prompt

The attached Home image is the approved GoodsPocket visual style anchor.

Create exactly one full-screen mobile UI reference image.

- Screen: Collection
- State: Owned selected
- Target aspect ratio: 393:852

Required component hierarchy:

1. iOS status bar
2. compact centered app bar with the title `컬렉션`
3. one compact three-option segmented control: `보유품`, `예약중`, `전체`; `보유품` is selected in orange
4. one full-width rounded search field with a gray search icon and placeholder `이름 또는 시리즈`
5. a compact result row showing `총 98개`
6. a vertically scrollable 3-column goods grid
7. a fixed compact collection summary band immediately above bottom navigation
8. shared bottom navigation: `홈`, `컬렉션`, centered orange circular plus button, `이벤트`, `마이`

Required visible state and exact Korean copy:

- App bar: `컬렉션`
- Segments: `보유품`, `예약중`, `전체`
- Search placeholder: `이름 또는 시리즈`
- Result count: `총 98개`
- Sample card contents:
  - `호시마치 스이세이 아크릴 스탠드` / `홀로라이브 · 아크릴 스탠드` / `보유 중`
  - `블루 아카이브 아트북` / `블루 아카이브 · 도서` / `보유 중`
  - `탄지로 피규어` / `귀멸의 칼날 · 피규어` / `보유 중`
  - `카게야마 키링` / `하이큐!! · 키링` / `보유 중`
  - `인형 키링` / `쿠로미 · 키링` / `보유 중`
  - `아냐 피규어` / `스파이 패밀리 · 피규어` / `보유 중`
- Summary band: `보유품 98`, `예약중 11`, `총 구매액 ₩243,600`
- Bottom navigation: `홈`, `컬렉션`, `이벤트`, `마이`; `컬렉션` is selected in orange

Existing behaviors the layout must support:

- tapping a segment changes among owned, reserved, and all collection entries
- typing in search filters by goods name, series, or character
- tapping a goods card opens its detail sheet
- the grid scrolls vertically with stable 3-column card geometry
- the center plus button opens quick add
- bottom navigation changes primary destinations
- counts and amounts are dynamic domain values
- do not add favorite, wishlist, sale, sort, view-mode, or filter actions because those behaviors do not exist in the current Collection screen

Screen chrome:

- Collection is a primary destination
- shared bottom navigation is visible
- centered orange quick-add button is visible
- no back button
- no global floating action button
- no notification bell
- keep the bottom navigation at the same height and vertical position as the attached Home image

Keep the GoodsPocket visual system from the attached Home image:

- warm `#FFFCF8` background
- `#FF7445` primary orange
- `#36C781` owned/success green
- `#8F6EF2` secondary/event purple
- `#202838` strong text and `#8A8F9B` muted text
- compact Korean sans typography with zero letter spacing
- warm `#FEFBF8` cards, subtle `#EFEDEC` 1px outlines, 8px card radius, and almost flat shadows
- use the same bottom-navigation icons, proportions, spacing, and centered plus button as the attached Home image

Goods card contract:

- 3 equal columns with narrow 8px gaps and 16px screen side padding
- each card is a compact vertical card; media occupies roughly the top 62% and text/status the bottom 38%
- use uniform neutral gray media placeholders in every image area because real goods images are unavailable
- placeholders must contain no initials, words, letters, fake artwork, or pixel art
- below media: goods name first, series/category second, small green `보유 중` badge last
- no heart, star, favorite, overflow, or decorative icons on cards

Constraints:

- output one screen only
- no phone frame, presentation board, collage, annotations, or outer background
- no landing page, marketplace, resale, or social-feed composition
- no primary feature outside the behavior contract
- preserve status-bar space and do not overlap system UI
- all visible content must fit within one 393:852 composition
- render all supplied Korean copy without replacing or inventing labels
- output only the final UI image, without explanatory text inside the image
