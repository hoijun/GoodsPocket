# GoodsPocket

A Kotlin Multiplatform goods collection and schedule app for Android and iOS.

## Product

Primary tabs are Home, Collection, Events and My. Owned and reserved goods share Collection. Settings opens from My without bottom navigation. Collection/event registration, detail and editing use sheets.

The approved rebuild preserves the image-locked design while using feature-owned state, pure domain contracts, unified local collection storage and an empty production database. Development and validation status must be reported separately from this target contract.

Payment-ledger features, automatic reservation events, photos, authentication and Firebase/Supabase synchronization are deferred. Their original product intent remains documented.

## Structure

- composeApp: one KMP module containing common UI/domain/data and Android/iOS platform integration.
- iosApp: native iOS host and configuration.
- [Documentation](docs/README.md): product and architecture contracts.
- [Development rules](AGENTS.md): rule router.
- [Design rules](DESIGN.md): approved screen references and visual verification.

Koin Annotations + KSP, SQLDelight and Coroutines/Flow remain the core implementation stack.

## Build

```sh
bash ./gradlew :composeApp:assembleDebug
bash ./gradlew :composeApp:allTests
bash ./gradlew :composeApp:ktlintCheck :composeApp:checkArchitecture
```

Open iosApp in Xcode to select an available simulator and build/run the native host. Follow the repository validation rules for the actual scope of each change; documentation edits alone do not require Gradle.
