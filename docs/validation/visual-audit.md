# iOS Visual Audit

2026-10-04. 승인된 edge-to-edge 수정 이후 기본 화면 5개의 저장된 캡처를 검사했다. **일부 기하 표본은 일치하지만 전체 1px 시각 검증은 완료되지 않았다.** 이 보고서는 새 참조 승인이나 UI 변경 요청을 대신하지 않는다.

이번 최종 재측정은 Home header 하단3dp 제거, Collection 간격18/17dp, Settings 기본 header22dp 복원 및 Collection/Events 선택 배경 inset 분리 이후 새 Xcode 빌드와 Maestro 확인으로 전달된 최신 기본 배율 캡처를 사용했다. 확대 글꼴/상세 시트의 기능 검증은 [adaptive-layout.md](adaptive-layout.md)에 구분했다. PNG 자체에는 빌드 식별자가 없으므로 아래 입력 해시가 재현 기준이다.

## Inputs and Method

- 참조: `design/references/home/default.png`, `collection/owned.png`, `events/default.png`, `my/default.png`, `settings/default.png`.
- 현재: `/tmp/goodspocket-final-{home,collection,events,my,settings}.png`, 모두 `1178 x 2556`이다.
- Pillow Lanczos로 양쪽을 `393 x 852` RGB로 정규화했다. Home 참조는 `853 x 1844`, 나머지 저장소 참조는 이미 정규화된 이미지다.
- 재현 산출물: 무시된 `build/visual-comparison/remaining/`의 `measure.py`, `measurements.json`, 화면별 `*-reference.png`, `*-current.png`, `*-pair.png`. JSON에 입력 경로, 원본 크기와 SHA-256을 기록했다. `python3 build/visual-comparison/remaining/measure.py`로 재실행하며 Pillow와 NumPy가 필요하다. 원본 임시 캡처도 필요하다.
- 표의 bounds는 양 끝을 포함한다. 주황 영역은 RGB `R>=245, 90<=G<=140, B<=100`의 4방향 연결요소로 측정했다. 긴 outline 행은 `#EFEDEC`와 채널별 차이 5 이하인 픽셀이 `x=20..374`에서 220개를 초과한 행이다. 단색 마스크 경계는 Compose layout bounds와 같다고 가정하지 않는다.
- 흐린 테두리는 지정한 좁은 y 구간에서 `x=30..362`의 `#EFEDEC` 최대 채널 차이 중앙값이 가장 낮은 행을 별도 추출했다. `edgeProbes`에 상위 3행과 차이 8 이하 픽셀 수를 남긴다. 이 행은 그림자/안티앨리어싱을 포함할 수 있어 문서의 layout 좌표와 별도로 표시한다.
- `paint-audit.py/json`은 선택 영역의 행별 좌우 경계, RGB 중앙값, 근사 round-rect fit을 기록한다. 글자 구멍을 행 내부 채움으로 제거하고 8방향 침식으로 외곽을 추출한 뒤, 참조 좌표로 y를32px 보정하여 양방향 최근접 Chebyshev 거리의 최댓값을 계산했다. 이는 지정한 색 마스크 윤곽의 측정이며 전체 화면의 픽셀 오차가 아니다.

## Inset Interpretation

[공유 inset 정책](../../.agents/design/components.md#current-edge-to-edge-policy)에 따라 실제 safeDrawing과 하단 systemBars/displayCutout을 보존한다. Collection/Events/My의 상단 콘텐츠는 이전 보정 제거로 약 `+32px`, Home은 `+25px` 이동하는 것을 허용한다. Settings는 같은 상단 위치다. 하단 navigation은 별도의 실제 inset에 고정되므로 상단과 같은 translation을 적용하지 않는다. 기존 `y=770`, 고정 81dp 또는 음수 offset을 맞추기 위해 복원하지 않는다.

## Measured Samples

| 표본 | 참조 | 현재 | 판정 |
| --- | --- | --- | --- |
| Events featured card 수평 outline 행 | top 223, bottom 388..389 | top 255, bottom 420..421 | **통과: 이 표본만** +32 보정 후 일치 |
| My profile 수평 outline 행 | 90, 209 | 122, 241 | **통과: 이 표본만** +32 보정 후 일치, 외곽 높이 120px |
| Settings language/format 카드 top | 142, 272 | 142, 272 | **통과: 이 표본만** 각각 일치 |
| Settings 카드 bottom | 212, 392 | 213, 391 | **통과: 이 표본만** 각각 +1/-1px |
| Settings 선택 주황 마스크 | (195,164)..(277,191) | (195,164)..(276,191) | **통과: 이 마스크만** 오른쪽 -1px, y 일치 |
| Collection 선택 주황 마스크 | (20,93)..(134,126) | (21,126)..(134,159) | **통과: 마스크 경계/윤곽 표본** +32 보정 후 left/top/bottom +1px, right 0px. 외곽 윤곽 최대 거리1px |
| Events 전체 필터 주황 마스크 | (19,179)..(77,206) | (20,212)..(77,239) | **통과: 마스크 경계/윤곽 표본** +32 보정 후 left/top/bottom +1px, right 0px. 외곽 윤곽 최대 거리1px |
| Home hero outline 행 | 84, 204 | 110, 229 | **통과: 이 표본만** +25 보정 뒤 top +1px/bottom 0px |
| Collection 첫 미디어 단색 영역 | 참조 gradient로 동일 마스크 불가 | (17,261)..(127,394) | **미검증** 구조상 3열 첫 카드 유지. 참조 gradient와 native flat gray 차이는 승인된 제외 |
| My summary top/bottom, 흐린 outline probe | 268 / 458..459 | 300..301 / 490..491 | **통과: 행 표본만** +32 보정 후 최대 1px. 문서 layout top269와 현재301은 정확히 +32 |
| My management top/bottom, 흐린 outline probe | 514 / 724..725 | 548 / 757 또는759 | **미검증: raw 외곽 전체** top은 +32 보정 뒤 +2px지만 문서 layout top515 기준 +1px. bottom759는 그림자 후보이므로 확정 경계로 사용하지 않음 |
| Collection segment 외곽 top/bottom | 흐린 outline91 / 129, 문서 layout top92 | 124 / 161 | **통과: 행 표본** +32 보정 후 raw top +1px/bottom 0px. 문서 layout 원점과 일치 |
| Collection search top/bottom | 흐린 outline145..146 / 184..185 | 178 / 216 | **통과: 행 표본** +32 보정 후 top 0..+1px, bottom -1..0px. 문서 layout top146과 일치 |
| Collection summary top/bottom | 693 / 758 | 685 / 750 | **부분 확인** 모두 -8px 이동, 외곽 길이66px 유지. 하단 navigation에 고정되므로 상단 +32 보정을 적용하지 않음 |

Home 후속 섹션에는 기존에 승인된 16dp 주간격이 적용되어 있어 raw 생성 이미지와 전체 y 이동량이 일정하지 않다. Summary 외곽은 참조215..312, 현재246..343으로 높이98px는 같지만 +31px 이동한다. 최근 미디어는 현재96dp 계약을 유지하고, Spending 외곽은 현재548..650 부근이다. 원본 Home에는 네 개 summary 지표와 더 짧은 섹션 간격이 있어 모든 후속 y 차이를 하나의 inset 이동으로 제거할 수 없다. 현재 세 지표는 승인된 제품 계약이고 원본의 희망/판매 지표 복원은 수정 대상이 아니다.

## Applied Corrections and Evidence

- **Settings 기본 배율 복원 확인:** `SettingsPageHeader`를 기본 배율에서 고정22dp, 확대 배율에서는 최소22dp로 분리한 후 language/format top은143/273에서142/272로 이동했다. Language bottom214에서213으로 돌아와 참조 대비+1px다. 카드72dp 자체는 줄이지 않았다.
- **Home header 수정 확인:** Header bottom padding3dp 제거 후 Hero113..232가110..229로 정확히3px 이동했다. 참조+25인109..229 대비+1/0px다. Brand bounds는 수정 전후 모두 y74..91로 유지되어 참조50..65+25 대비-1/+1px다. top padding2dp, bell38dp, 주간격16dp, Hero/summary offset -5dp를 유지했다. 해당 위치 표본은 통과하며 전체 Home 픽셀 통과로 확대하지 않는다.
- **선택 배경 분리 확인:** Collection 외곽38dp/equal slots/horizontal row padding3dp를 유지하고, 배경만 대칭 inset 가로1.5dp/세로2dp의 radius7dp Surface로 분리했다. 슬롯 높이는32dp에서38dp로 확장되며 텍스트 중심/Role.Tab/콜백은 유지한다. Events 외곽 클릭 슬롯31dp는 유지하고, 배경을 가로0.5dp/세로1dp inset/radius11.5dp Surface로 분리했다. 음수 offset/clip escape 없이 배경과 클릭·텍스트 영역을 분리했다. 최신 마스크 사각 경계와 양방향 외곽 윤곽 거리는 두 화면 모두1px 이내다.
- **Events 모서리 재검증 완료:** radius12dp 첫 캡처에는 왼쪽 위 윤곽의 단일2px 차이가 있었다. radius11.5dp로 미세 조정한 새 빌드 캡처에서 동일 threshold/동일 비교법으로 최대 거리1px를 확인했다. 사각 경계 `(20,212)..(77,239)`와 카드/필터 외곽 원점은 그대로 유지되었다. 이 결과는 측정한 선택 상태의 윤곽만 통과한 것이며 모든 필터 상태나 전체 화면의 픽셀 통과로 확대하지 않는다.
- **색상 계약 유지:** 생성 참조 선택 마스크 RGB 중앙값은 Collection `(248,119,64)`, Events `(253,114,53)`이고 현재는 두 화면 모두 `(255,116,69)`다. 명시된 기본색 `#FF7445`를 유지했으며 생성 이미지의 색상 흔들림을 복제하지 않았다. 색상 전체 픽셀 일치로 주장하지 않는다.
- **My 누적 오차는 실패 확정 아님:** summary/management 문서 원점269/515와 흐린 원본 테두리268/514가 이미1px 다르다. 현재301/548은 문서 원점에 +32를 적용하면0/+1px다. 관리 카드 height210dp를 줄이는 근거는 없다.
- **Collection 간격 상쇄 수정 확인:** `TitleToSegmentSpacing=16 -> 18dp`, `SearchToCountSpacing=19 -> 17dp` 적용 후 segment 외곽122..159가124..161, search176..214가178..216으로 정확히2px 이동했다. 문서 원점92/146+32와 일치한다. Count glyph는236..246, title glyph는86..101로 수정 전후 동일하며 각각 참조+32 대비0/+1px, -1/-1px다. 독립 grid 원점과38dp/39dp 높이는 유지했다. 이번 paint 분리 후에도 이 외곽 좌표는 동일하다.

## Meaning of the 1px Target

문서에 지정된 layout box는 고정 viewport에서 1px 수준으로 비교할 수 있다. 그러나 생성 참조의 anti-aliased edge, 그림자, 비균일한 필터 paint와 Compose 도형의 모든 픽셀을 동시에1px로 일치시키는 것은 현재 증거로 보장할 수 없다. 예를 들어 My 문서 top515와 원본의 가장 진한 edge514가 다르다. 이것은 임의 오차 확대 승인이 아니라 **layout 계약과 raster 측정의 구분이 필요하다는 증거**다. 안정적으로 정의한 bounds만 항목별 통과로 표시하고 나머지는 미통과/미검증으로 유지한다.

## Exclusions and Remaining Work

- 실제 데이터는 컬렉션 1건·이벤트 1건이다. 이름·금액·월·개수·차트 높이·빈 timeline과 참조의 추가 카드 수는 동적 데이터 차이다. 없는 행의 geometry가 통과한 것은 아니다.
- 사진/아바타/hero artwork 대신 native flat gray를 사용하는 차이는 현행 미디어 계약의 제외다. 참조 screenshot을 실제 UI로 사용하지 않았다. 시간·상태바·Dynamic Island와 비기하적인 글꼴 raster 차이도 해당 화면 계약에 따라 구분한다.
- 기본 화면의 구성, 카드 내부 배치, 글꼴 크기, 색상은 이전 소스 21개 파일 비교에서 보존된 것으로 확인했다. 이는 캡처의 모든 픽셀·glyph·icon이 일치한다는 증거가 아니다.
- 공통 navigation은 실제 safe area 안에 위치하고 Settings에는 표시되지 않는다. 선택 My 아이콘은 채움 실루엣으로 보정했고 선 두께와 모서리를 dp에서 px로 변환하여 밀도별 논리 크기를 유지한다. 최종 My 마스크 bounds는 참조 `[340,779,355,797)`, 현재 `[340,780,354,797)`로 각 경계 차이가 최대1px다. 다만 몸통 곡선의 양방향 최근접 유클리드 거리는 최대1.414px로 전체 실루엣1px 통과는 아니다. 전체 아이콘/label/quick-add의 전면적인 픽셀 비교도 미검증이다.
- 시트·다이얼로그, reserved 상태 전체, 다중 행 timeline, 긴 번역·글자 확대·다른 화면 크기의 모든 시각 상태는 이번 5개 캡처에 포함되지 않았다.
- 측정 담당자는 통합 담당자가 새 빌드에서 생성한 PNG를 분석했다. 기준 PNG·승인 기록은 변경하지 않았다. 전체 1px 통과나 신규 시각 승인을 선언하지 않는다.

## Final Verification

- 이 감사에서 `measure.py`를 최신 5개 PNG로 재실행하고 모든 side-by-side 산출물을 직접 확인했다. `git diff --check -- docs/validation/visual-audit.md`는 통과했다.
- 통합 담당자의 최종 실행 결과: `bash ./gradlew :composeApp:allTests`는 Android debug 112개, release 112개, iOS 74개 통과. `:composeApp:ktlintCheck`, `:composeApp:checkArchitecture`, `:composeApp:assembleDebug`도 통과했다. Android connected ActivityRecreation 테스트7개도 통과했다(`/tmp/gp-visual-refinement-native.log`).
- 통합 담당자의 새 Xcode 빌드 및 iOS Maestro Home/Collection/Events/My/Settings 5개 화면 smoke가 통과했고, 이 실행에서 전달된 캡처가 본문의 최종 측정 입력이다. 회전 검증 작업은 `afaa0d4`에 기록되었다. 빌드/기능 테스트 성공을 픽셀 일치의 대체 근거로 사용하지 않았다.
- iOS 최종 flow는 5개 기본 화면, 4개 시트, reserved/all/delivery 전환까지 성공했다(`/tmp/gp-visual-final-ios.log`). Android 필터/탭 조작도 성공했다(`/tmp/gp-visual-controls-android.log`). 이 Android 조작 캡처는 최종 아이콘 비율·편집 버튼·radius11.5dp 보정 전이며, 최종 Android 빌드의 검증은 위 재생성 테스트7개와 APK 빌드다. 시트 기능 성공은 시트 전체 기하 비교 통과를 의미하지 않는다.
- 결론: Home Hero 원점, Collection segment/search 외곽, Settings 카드 원점과 두 선택 배경의 사각 경계/외곽 윤곽은 최신 측정 표본에서1px 목표를 만족한다. My 흐린 외곽 판정, 전체 icon/glyph/시트/다중 데이터 상태는 별도 미완료 범위로 남는다. Navigation 아이콘의 수치 검증은 별도 담당 범위이며 본 보고서의 선택 마스크 측정과 합산하지 않았다.
