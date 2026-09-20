# Event Editor

- Reference: `design/references/sheets/event-editor.png`
- GPT Web: https://chatgpt.com/c/6aaf486a-4088-83ee-bbe1-ccc322610468
- User approved the displayed reference before implementation.

## Generation Contract

Generate one 393:852 GoodsPocket event editor over the dimmed Events host.
Match the collection editor colors, typography, thin borders and low shadow.
No phone frame. Bottom sheet near y420, top radius24, centered handle.
Header `이벤트 편집` and gray X. Horizontal padding20.
Label `제목`, value `아크릴 스탠드 발매`; label `예정일`, value `2026-10-15`.
Label `이벤트 유형`, four equal options `발매`, `결제 마감`, `배송`, `오프라인`.
Select only release in orange. Fixed `변경사항 저장` action at bottom.
Cream #FFFCF8, ink #202838, muted #8A8F9B, primary #FF7445.
No photos, memo, location, delete action, tabs or calendar popup.

## Native Contract

- Measured normalized reference: sheet top414, handle425, header454,
  first field513, second582, type selector652, save751 with height44.
- Reuse existing EventDraft validation and save callback; expose all EventType entries.
- Preserve domain-linked IDs, memo and location through the existing save path.
- Cancel restores the same event detail and leaves host filters unchanged.
- Header and save action remain fixed while form content can scroll above the keyboard.
- Dynamic host content, entered values and system indicators are excluded from comparison.
- Reference is documentation only, never a production image asset.
- Implementation visual approval is pending.

## Device Verification

- iPhone 16: blank title disables save; a populated title with blank date also
  disables save; populating both enables save.
- Software keyboard: header, both inputs, all four type options and save action
  remain visible. Offline selection works with the keyboard open.
- Cancelling modified title/date/type restores the original event detail and
  original values. No persistent record was changed during these checks.
- Evidence: `build/visual-comparison/event-editor-keyboard-393x852.png`.
- Follow-up correction: event-only field outline changed to #DFDFE5, type labels
  use SemiBold, and form start moved down 1dp. Collection defaults are unchanged.
- Rebuilt and recaptured as `event-editor-refined-393x852.png`; Android compile,
  allTests and iOS build passed after correction. Full pixel-tolerance compliance
  has not been established.
- Device save persistence and failure/retry presentation were not exercised.

## Follow-up Evidence

- Repository-fake regression passes: failed UPDATE retains editor and every
  original record; retry saves the same ID, trims title/date, updates the type,
  preserves memo/location/linked IDs/created date, reopens detail and retains
  the host filter. A fresh state holder reads the saved result.
- All multiplatform tests passed after adding the regression.
- Pixel masks at 393x852 (inclusive bounds, reference -> current):
  handle (175,425,217,429) -> same;
  first/second input top 513/582 -> same;
  selected orange (22,654,108,683) -> (22,654,107,683);
  save orange (20,752,372,793) -> (20,751,372,794).
- These sampled geometry bounds are within 1px; they do not certify every
  glyph, antialiased edge or surface pixel. Device-visible storage failure
  injection remains outside this pass; existing device records were untouched.
