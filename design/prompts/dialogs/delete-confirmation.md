# Delete Confirmation

- Conversation: https://chatgpt.com/c/6aaf477b-f1b8-83ee-ba2a-aff776f868fc
- Reference: `design/references/dialogs/delete-confirmation.png`
- Generated under delegated screen work, not separately approved by the user.

## Generation Request

공통 삭제 확인 다이얼로그 기준 이미지 1장. 기존 Events 기본 화면 위 scrim,
상세 시트는 닫힌 상태. 세로 393:852, 폰 프레임 없음. 중앙의 흰 dialog,
radius 16, 은은한 shadow. 제목 "이벤트를 삭제할까요?".
본문 "이 작업은 이벤트를 영구적으로 삭제합니다." 두 줄.
동일 너비의 취소 outline 버튼과 orange 삭제 버튼. 추가 기능 없음.

## Interpretation

Normalize the returned image to 393 x 852. The generated dialog is approximately
288 wide and 162 high, with 16 corner radius, 16 horizontal inset, and 40-high
buttons. Use the returned image rather than the originally requested 320 width.
The body uses a constrained width and two lines. Keep localized action-specific
copy and existing callbacks. Cancelling restores the originating detail.
Background domain values and system status indicators are dynamic exclusions.
The reference is not a production image asset.
