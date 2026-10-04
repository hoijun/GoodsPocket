# iOS Visual Audit

2026-10-04. 승인된 edge-to-edge 수정 이후 기본 화면 5개의 저장된 캡처를 검사했다. **일부 기하 표본은 일치하지만 전체 1px 시각 검증은 완료되지 않았다.** 이 보고서는 새 참조 승인이나 UI 변경 요청을 대신하지 않는다.

아래 입력 캡처는 후속 확대 글꼴/상세 시트 수정 이전의 edge-to-edge 빌드다. 최종 빌드 전체의 픽셀 검증으로 재사용하지 않는다. 후속 런타임 검증 범위는 [adaptive-layout.md](adaptive-layout.md)에 구분했다.

## Inputs and Method

- 참조: `design/references/home/default.png`, `collection/owned.png`, `events/default.png`, `my/default.png`, `settings/default.png`.
- 현재: `/tmp/gp-edge-ios-home.png`는 `1179 x 2556`, 나머지 `/tmp/goodspocket-final-{collection,events,my,settings}.png`는 `1178 x 2556`이다.
- Pillow Lanczos로 양쪽을 `393 x 852` RGB로 정규화했다. Home 참조는 `853 x 1844`, 나머지 저장소 참조는 이미 정규화된 이미지다.
- 재현 산출물: 무시된 `build/visual-comparison/remaining/`의 `measure.py`, `measurements.json`, 화면별 `*-reference.png`, `*-current.png`, `*-pair.png`. JSON에 입력 경로, 원본 크기와 SHA-256을 기록했다. `python3 build/visual-comparison/remaining/measure.py`로 재실행하며 Pillow와 NumPy가 필요하다. 원본 임시 캡처도 필요하다.
- 표의 bounds는 양 끝을 포함한다. 주황 영역은 RGB `R>=245, 90<=G<=140, B<=100`의 4방향 연결요소로 측정했다. 긴 outline 행은 `#EFEDEC`와 채널별 차이 5 이하인 픽셀이 `x=20..374`에서 220개를 초과한 행이다. 단색 마스크 경계는 Compose layout bounds와 같다고 가정하지 않는다.

## Inset Interpretation

[공유 inset 정책](../../.agents/design/components.md#current-edge-to-edge-policy)에 따라 실제 safeDrawing과 하단 systemBars/displayCutout을 보존한다. Collection/Events/My의 상단 콘텐츠는 이전 보정 제거로 약 `+32px`, Home은 `+25px` 이동하는 것을 허용한다. Settings는 같은 상단 위치다. 하단 navigation은 별도의 실제 inset에 고정되므로 상단과 같은 translation을 적용하지 않는다. 기존 `y=770`, 고정 81dp 또는 음수 offset을 맞추기 위해 복원하지 않는다.

## Measured Samples

| 표본 | 참조 | 현재 | 판정 |
| --- | --- | --- | --- |
| Events featured card 수평 outline 행 | top 223, bottom 388..389 | top 255, bottom 420..421 | **통과: 이 표본만** +32 보정 후 일치 |
| My profile 수평 outline 행 | 90, 209 | 122, 241 | **통과: 이 표본만** +32 보정 후 일치, 외곽 높이 120px |
| Settings language/format 카드 top | 142, 272 | 142, 272 | **통과: 이 표본만** 같은 행 |
| Settings 카드 bottom | 212, 392 | 213, 391 | **통과: 이 표본만** 최대 1px 차이 |
| Settings 선택 주황 마스크 | (195,164)..(277,191) | (195,164)..(276,191) | **통과: 이 마스크만** 오른쪽 1px 차이 |
| Collection 선택 주황 마스크 | (20,93)..(134,126) | (19,125)..(136,156) | **미통과: 마스크 표본** +32 보정 후 오른쪽/하단 2px 차이. layout와 raster 경계를 추가 구분해야 함 |
| Events 전체 필터 주황 마스크 | (19,179)..(77,206) | (19,211)..(78,240) | **미통과: 마스크 표본** +32 보정 후 하단 2px 차이. 전체 필터 기하 실패로 확대하지 않음 |
| Home hero outline 행 | 84, 204 | 113, 232 | **미통과: 위치 표본** +25 보정 뒤 top 4px/bottom 3px 잔여. 크기는 약 120px; 해당 위치 차이의 별도 승인 근거는 확인하지 못함 |
| Collection 첫 미디어 단색 영역 | 참조 gradient로 동일 마스크 불가 | (17,261)..(127,394) | **미검증** 구조상 3열 첫 카드 유지. 참조 gradient와 native flat gray 차이는 승인된 제외 |
| My summary/management 전체 bounds | 참조 border가 단색 임계값에서 연속 추출되지 않음 | 일부 border만 추출 | **미검증** 육안으로 그룹 구조는 유지되나 모든 경계 1px 판정 불가 |

Home 후속 섹션에는 기존에 승인된 16dp 주간격이 적용되어 있어 raw 생성 이미지와 전체 y 이동량이 일정하지 않다. 이를 새 inset 회귀나 추가 허용 오차로 임의 분류하지 않았다. Home hero 위치 잔여와 선택 필터 마스크 잔여는 확인 가능한 차이로 남기며, 전체 경계 재측정 또는 명시적 처리 없이 일치로 표시하지 않는다.

## Exclusions and Remaining Work

- 실제 데이터는 컬렉션 1건·이벤트 1건이다. 이름·금액·월·개수·차트 높이·빈 timeline과 참조의 추가 카드 수는 동적 데이터 차이다. 없는 행의 geometry가 통과한 것은 아니다.
- 사진/아바타/hero artwork 대신 native flat gray를 사용하는 차이는 현행 미디어 계약의 제외다. 참조 screenshot을 실제 UI로 사용하지 않았다. 시간·상태바·Dynamic Island와 비기하적인 글꼴 raster 차이도 해당 화면 계약에 따라 구분한다.
- 기본 화면의 구성, 카드 내부 배치, 글꼴 크기, 색상은 이전 소스 21개 파일 비교에서 보존된 것으로 확인했다. 이는 캡처의 모든 픽셀·glyph·icon이 일치한다는 증거가 아니다.
- 공통 navigation은 실제 safe area 안에 위치하고 Settings에는 표시되지 않는다. 아이콘/label/quick-add 각각의 전면적인 픽셀 비교는 미검증이다.
- 시트·다이얼로그, reserved 상태 전체, 다중 행 timeline, 긴 번역·글자 확대·다른 화면 크기의 모든 시각 상태는 이번 5개 캡처에 포함되지 않았다.
- 실제 장치 조작 없이 기존 PNG를 분석했다. UI 소스·기준 PNG·승인 기록은 변경하지 않았다. 전체 1px 통과나 신규 시각 승인을 선언하지 않는다.
