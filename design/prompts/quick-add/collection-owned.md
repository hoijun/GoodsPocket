# Quick Add Reference

- GPT Web: https://chatgpt.com/c/6aaf486a-4088-83ee-bbe1-ccc322610468
- Generated 2026-09-20 under delegated implementation authority; not separately user-approved.
- Anchor: original Home reference. The generated image is a visual reference only.
- Asset: `design/references/quick-add/collection-owned.png`.

Generate one portrait GoodsPocket quick-add bottom sheet over the Home anchor, aspect ratio 393:852, no phone frame or collage. Warm cream #FFFCF8, dark text #202838, muted #8A8F9B, orange #FF7445, almost no shadow. Rounded top corners and centered handle. Header: 빠른 추가 and a gray close icon. Target segmented control: 컬렉션 / 이벤트. Fields: 굿즈명, 카테고리, 상태 (보유 / 예약 / 정리 예정), 시리즈, 캐릭터, 판매처, 메모. Thin outlined inputs, roughly 42px high and 10px gaps. Fixed disabled pale-orange 저장 button above the bottom safe area. Do not add image upload, price, favorites, sale or wishlist. No new illustration.

The returned image, not requested coordinates, defines shared geometry. Reserved and Event variants retain that geometry even when their generated variants drift. Existing localized labels and domain validation are preserved. Only the input body scrolls; the save action stays outside it.

## Normalized Comparison

Compare at 393 x 852, not the original generated image's resolution. Approximate
reference top edges: sheet 204, target selector 276, first field 332, category
382, status selector 457, series 510, character 562, store 614, memo 668,
save 754. These are comparison landmarks, not absolute-position layout rules.

The shared implementation uses a 648dp maximum sheet height with keyboard-aware
available height; the body scrolls independently of the footer. First input is
40dp minimum; ordinary inputs 42dp; store 44dp; memo 64dp. Status label-to-control
gap is 7dp; the next field has 14dp clearance. Adjacent inactive segments have
a short separator; separators beside the active segment are hidden.

The GPT Web review initially compared images at different resolutions and
incorrectly described overall compression. After uploading the normalized
reference it retracted that conclusion and identified smaller text/icon sizing.
Geometry decisions use the normalized local captures rather than that initial
review. Header 22sp, segment labels 14sp, input text 15sp, save label 18sp are the
subsequent typography pass. Global pixel-perfect approval remains outstanding.
