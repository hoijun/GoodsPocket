# Owned Collection Editor

- GPT Web: https://chatgpt.com/c/6aaf486a-4088-83ee-bbe1-ccc322610468
- Reference: `design/references/sheets/collection-editor-owned.png`
- Generated under delegated authority; separate visual approval is pending.
- The older April editor candidate was rejected for unsupported image upload,
  scale, material, wishlist, sale, and price-editing controls.

## Prompt Contract

Generate one 393:852 GoodsPocket owned editor over a dimmed Collection screen.
Match the existing Quick Add style. Sheet top near 204, radius 24, handle,
left title `컬렉션 편집`, right gray X. Labels above populated fields:
`굿즈명` / `블루 아카이브 아트북`, `카테고리` / `도서`, `상태` /
`보유` selected and `정리 예정`, `시리즈` / `블루 아카이브`, empty
`캐릭터`, `판매처` / `교보문고`, empty multiline `메모`.
Fixed orange `변경사항 저장` button. Cream #FFFCF8, ink #202838,
muted #8A8F9B, primary #FF7445; minimal shadow and thin gray outlines.
No new fields, photo controls, dates, prices, or unsupported statuses.

## Native Contract

- Owned/planned-cleanup and reserved editors share this layout.
- Reserved reuses the same geometry without a new generated reference: status
  stays reserved, reservation store replaces purchase store, and release date
  appears before memo. The extra field scrolls within the fixed header/footer.
- Event editor visuals remain unchanged.
- Initialize UI-local drafts from the existing entry; retain the existing save callback.
- Required name/category validation is unchanged. Hidden price/date/location/link
  values stay in the existing persistence path.
- Reserved additionally requires reservation store and release date, as before.
- Closing a collection editor restores the same entry detail without saving.
- Header and footer stay fixed; only the fields scroll, with keyboard-aware height.
- Compare at 393 x 852. Reference top is approximately 204, header 72 high,
  labels 16 high, label/input gap 4, fields 34 minimum, group gap 13,
  memo 54 minimum, and save button approximately y762 with height44.
- Host domain values, gray thumbnails and system status indicators are dynamic.
- Reference image is never used as a production UI asset.
