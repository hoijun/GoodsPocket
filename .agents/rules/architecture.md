# Architecture Rules

## Target Structure

- Keep the project as a single `:composeApp` Kotlin Multiplatform module unless modularization is explicitly requested.
- Organize shared code by package-based Clean Architecture inside `commonMain`.
- Keep dependency direction as `presentation -> domain` and `data -> domain`.
- Domain code must not depend on data, presentation, Compose, SQLDelight, Koin, Android, or iOS APIs.
- Data implementations may depend on domain interfaces and models.
- Presentation may depend on domain interfaces, domain models, and business use cases, but not on datasource or SQLDelight types.

```text
Compose UI
    -> GoodsPocketAppStateHolder
        -> Domain Repository for simple operations
        -> UseCase for business operations
            -> Domain Repository
                -> Repository implementation
                    -> Persistence datasource
                        -> SQLDelight
                            -> Platform database driver
```

## Repository Map

- `composeApp/src/commonMain/kotlin/goods/pocket/app/domain`: domain models, repository contracts, services, and use cases
- `composeApp/src/commonMain/kotlin/goods/pocket/app/data`: repository implementations, local persistence, runtime values, and DI
- `composeApp/src/commonMain/kotlin/goods/pocket/app/presentation`: Compose UI, state holders, navigation, and design system
- `composeApp/src/commonMain/sqldelight`: SQLDelight schema and queries
- `composeApp/src/commonTest` and `composeApp/src/androidUnitTest`: shared and Android-focused tests
- `iosApp`: native iOS host, signing, assets, and Xcode configuration

## Source Sets and Platform Boundaries

- Put shared UI, domain logic, data logic, and shared DI declarations in `composeApp/src/commonMain`.
- Put Android-only Kotlin integration in `androidMain` and iOS-only Kotlin integration in `iosMain`.
- Use `expect/actual` only for real platform boundaries.
- Keep `iosApp` as the native host for app lifecycle, signing, assets, plist configuration, and Compose framework integration.
- Do not move shared business or presentation logic into `iosApp`.

## Dependency Injection

- Use Koin Annotations with KSP.
- Do not add classic Koin definition DSL when annotations can express the binding.
- Classic DSL is allowed only for application bootstrap or a binding that Koin Annotations cannot represent; document the reason nearby.
- Do not reintroduce `AppContainer` or another manual service locator.
- Keep platform-specific providers in `PlatformDataModule`.
- Keep `DatabaseDriverFactory` in the existing common `expect` and Android/iOS `actual` structure.
- Keep `KOIN_CONFIG_CHECK` passing when DI definitions or constructor dependencies change.

## Migration Direction

- Treat pass-through use cases, duplicated state transitions, and hardcoded production time values as migration debt.
- When modifying an affected flow, migrate the relevant code to these target rules.
- Do not turn a focused task into a repository-wide architecture migration unless explicitly requested.
- Report relevant out-of-scope violations instead of silently expanding the task.
