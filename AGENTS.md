# GoodsPocket

## Rule Loading

Before changing files, read the detailed rule documents that apply to the task.

Always read:

- [Workflow](.agents/rules/workflow.md)

Read when the task touches the corresponding area:

- Production code, module structure, source sets, dependency direction, or DI boundaries: [Architecture](.agents/rules/architecture.md)
- Kotlin production or test code, Gradle, SQLDelight source, Compose resources, or dependency declarations: [Code Conventions](.agents/rules/code-conventions.md)
- Domain, repository, datasource, SQLDelight, schema, seed, time, ID, or business state transitions: [Domain and Data](.agents/rules/domain-data.md)
- Compose UI, state holder, navigation, design system, localization, or accessibility: [Presentation](.agents/rules/presentation.md)
- Visual UI, screen layout, styling, design-system components, or reference-image alignment: [Design Rules](DESIGN.md), then every task-specific document selected by that router
- Tests, DI checks, builds, or completion of a code change: [Testing and Validation](.agents/rules/testing-validation.md)

If a task spans multiple areas, read every relevant rule file. These linked files are mandatory project instructions, not optional reference material.

## Project Guardrails

- Keep the target architecture as a single `:composeApp` KMP module with package-based Clean Architecture.
- Keep dependency direction as `presentation -> domain` and `data -> domain`.
- Use Koin Annotations with KSP and do not reintroduce `AppContainer`.
- Keep `DatabaseDriverFactory` and platform DI in the existing `expect/actual` structure.
- Keep datasources persistence-only and keep business orchestration in use cases or repositories according to the detailed rules.
- Do not create pass-through use cases for simple repository operations.
- Do not edit generated KSP or SQLDelight files directly.
- Do not hardcode current production dates or months.
- Keep production Kotlin files at 600 lines or fewer and split them by responsibility.
- Preserve existing user changes and do not perform Git finalization actions unless explicitly requested.
