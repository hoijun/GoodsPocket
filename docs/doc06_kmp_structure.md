
KMP Project Structure
Architecture Overview
프로젝트는 Kotlin Multiplatform + Compose Multiplatform + SQLDelight 기반으로 구성한다.
비즈니스 로직과 데이터 계층은 shared에 두고, 플랫폼 앱은 진입점과 플랫폼 특화 기능만 가진다.
Modules
shared
- commonMain 중심의 도메인, 데이터, 디자인 시스템, 네비게이션 정의
- Android/iOS가 공통으로 사용하는 핵심 코드 위치
androidApp
- Android Activity, 앱 아이콘, 권한, 알림 채널, 플랫폼 DI 진입점
iosApp
- iOS App entry, SwiftUI host 또는 Compose launcher 래핑
- 향후 iOS 대응을 위한 최소 진입 구조 유지
Shared Internal Structure
core:common
- Result, dispatcher, date formatter interface, logging abstraction
core:model
- Item, Preorder, Transaction, Event, StorageLocation 등 도메인 모델
core:database
- SQLDelight database, driver factory, query bindings, migrations
core:designsystem
- theme, typography, colors, shared components
core:navigation
- route constants, navigator abstraction, back stack helpers
domain
- repository interfaces
- use case classes
- validation rules
data
- repository implementations
- local data source
- entity-to-domain mapper
feature:home
- dashboard UI와 요약 로직
feature:collection
- 보유 굿즈 등록/조회/수정
feature:preorder
- 예약 관리와 수령 전환
feature:transaction
- 지출 기록과 월별 집계
feature:event
- 일정 관리
feature:settings
- 환경설정과 사전 데이터 관리
platform
- image picker placeholder interface
- notification scheduler interface
- file export/import interface
Dependency Direction
- feature -> domain, core
- data -> domain, core
- domain -> core
- app modules -> shared
- feature 간 직접 의존은 금지하고 필요 데이터는 repository/use case를 통해 전달
Recommended Tooling
- DI: Koin 또는 수동 주입 중 프로젝트 규모에 맞게 선택, MVP는 수동 조립도 가능
- Concurrency: Kotlin Coroutines + StateFlow
- Serialization: kotlinx.serialization
- Date/Time: kotlinx-datetime
Build Strategy
- 공통 로직은 commonMain 우선 구현
- 플랫폼별 차이는 expect/actual 또는 interface + platform implementation으로 분리
- 데이터베이스 스키마 변경은 SQLDelight migration과 문서 갱신을 동시에 수행
