# Android 저장 실패 및 복구 검증

검증일: 2026-10-04, Asia/Seoul

## 실행 환경과 결과

- 대상: `Medium_Phone_API_36.0(AVD)`, Android 16 / API 36, `emulator-5554`.
- 실행 방식: `AndroidJUnitRunner`의 실제 기기 instrumentation.
- SQLite 구현: `AndroidSqliteDriver`와 Android SQLite. JDBC 또는 in-memory fake가 아니다.
- 결과: 테스트 3개 통과, 실패 0개, 오류 0개, 건너뜀 0개.
- XML 보고서의 테스트 스위트 시간: 1.361초. 설치 및 빌드를 포함한 Gradle 실행은 46초.
- 프로덕션 코드에 실패 주입용 분기나 테스트 훅을 추가하지 않았다.

## 확인한 동작

| 테스트 | 실패 주입과 검증 |
| --- | --- |
| `failedSaveRetriesOriginalDraftAndIdentityThenSurvivesReopening` | 실제 `CollectionStateHolder`가 원래 초안을 저장하는 동안 SQLite INSERT trigger가 `RAISE(ABORT)`를 발생시킨다. 실패 상태, 미호출 성공 콜백, 빈 DB를 확인한다. trigger 제거 후 같은 command를 재시도하여 동일 ID, 이름·카테고리·시리즈·캐릭터·구입처·메모와 단일 레코드를 확인한다. driver를 닫고 다시 열어 저장값이 복원되는지 확인한다. |
| `failedDeleteRollsBackEventUnlinkAndSuccessfulRetryPersistsAfterReopen` | 컬렉션과 연결 이벤트를 실제 DB에 저장한다. DELETE trigger가 이벤트 연결 해제 이후 삭제를 실패시키면 컬렉션과 이벤트 연결 모두 원래 값으로 rollback되는지 확인한다. trigger 제거 후 재시도하고 driver 재연결 뒤 컬렉션 삭제 및 이벤트 보존·연결 해제를 확인한다. |
| `androidSQLiteRejectsMissingCollectionRelationship` | 없는 컬렉션 ID를 참조하는 이벤트를 저장하면 원인을 보존한 `RepositoryFailure(WRITE)`가 발생하고 이벤트가 남지 않는지 확인한다. |

테스트 코드 자체는 별도의 `instrumentation-*.db`를 사용하고 종료 시 해당 파일을 정리하며, `GoodsPocket-v2.db`를 직접 열거나 삭제하지 않는다. coroutine scope의 작업과 관찰자를 종료한 뒤 driver를 닫는다.

단, Gradle connected runner의 설치·정리 수명주기는 테스트 코드의 DB 접근 범위와 별개다. 이번 실행 후 대상 앱이 제거되어 `adb am start`가 Activity 부재를 보고했고, 이후 앱을 다시 설치했다. 앱 제거는 해당 앱의 데이터도 지울 수 있으므로 운영 DB 보존을 보장하는 실행으로 취급하면 안 된다. 재실행에는 보존할 앱 데이터가 없는 전용 에뮬레이터를 사용한다. 이번 에뮬레이터의 데이터는 재생성 가능한 smoke-test 데이터였다.

## 검증 범위의 한계

- 저장 실패는 SQLite trigger의 `RAISE(ABORT)`로 주입했다. 물리적 저장 공간 부족, 파일시스템 손상, 운영체제 강제 종료를 재현한 결과는 아니다.
- 저장 초안과 동일 ID 재시도는 실제 StateHolder의 보류된 command와 저장 결과로 확인했다. Compose 편집 폼의 표시 유지, 실패 대화상자의 모양, 버튼 클릭 흐름을 계측한 UI 테스트는 아니다.
- 재시작 복원은 driver를 닫고 같은 DB 파일을 다시 여는 방식으로 검증했다. 앱 프로세스 종료·재실행 검증과는 구분한다.
- FK 테스트는 별도의 DB 이름을 사용하는 테스트 driver에 FK callback을 명시한다. 따라서 실제 Android SQLite에서의 제약 동작을 확인하지만, 운영 `DatabaseDriverFactory` 배선 자체를 통과하는 테스트는 아니다.
- 이번 instrumentation 결과는 Android 에뮬레이터에 해당한다. 실물 기기와 iOS 저장 실패 복구를 확인한 것으로 확대하지 않는다.

## 재실행 명령

```sh
bash ./gradlew :composeApp:assembleDebugAndroidTest
bash ./gradlew :composeApp:ktlintAndroidInstrumentedTestSourceSetCheck
bash ./gradlew :composeApp:connectedDebugAndroidTest \
  -Pandroid.testInstrumentationRunnerArguments.class=goods.pocket.app.data.AndroidStorageRecoveryTest
```

위 세 작업을 성공적으로 실행했다. connected 테스트는 보존할 앱 데이터가 없는 전용 에뮬레이터에서 실행한다. 실행 중에는 같은 에뮬레이터에서 수동 UI 조작이나 다른 instrumentation을 동시에 실행하지 않는다. 종료 후 대상 앱의 설치 상태를 확인하고, 필요한 경우 앱과 테스트 데이터를 다시 준비한다.

## 소스와 실행 산출물

- 테스트 소스: [`AndroidStorageRecoveryTest.kt`](../../composeApp/src/androidInstrumentedTest/kotlin/goods/pocket/app/data/AndroidStorageRecoveryTest.kt)
- HTML 보고서: `composeApp/build/reports/androidTests/connected/debug/index.html`
- XML 보고서: `composeApp/build/outputs/androidTest-results/connected/debug/TEST-Medium_Phone_API_36.0(AVD) - 16-_composeApp-.xml`
- 기기별 logcat 및 UTP 결과: `composeApp/build/outputs/androidTest-results/connected/debug/Medium_Phone_API_36.0(AVD) - 16/`

`build/` 산출물은 다음 빌드나 정리 작업으로 바뀔 수 있다. 이 문서의 통과 기록은 위 날짜의 실행에 한정한다.
