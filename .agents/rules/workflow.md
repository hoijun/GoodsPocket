# Workflow Rules

## Source of Truth

- Follow explicit user requirements and the root `AGENTS.md` first.
- Follow the approved task-specific specification or design document for the feature being changed.
- Treat current code and tests as evidence of existing behavior, not automatically as the desired architecture.
- When documentation, tests, and implementation disagree, report the conflict before making a broad change.
- Update relevant architecture or behavior documentation when a change makes it inaccurate.
- Do not revive routes, screens, or architecture described only in stale documentation without confirming current usage.

## Instruction Maintenance

- Keep the root `AGENTS.md` as a concise router and repository-wide guardrail summary.
- Add rules only for recurring mistakes or durable project constraints.
- Put specialized guidance in the closest applicable rule document or nested `AGENTS.md`.
- Keep one-off task details, temporary plans, generated prompts, and personal preferences out of repository instructions.

## Worktree Safety and Scope

- Treat existing modified and untracked files as user-owned work.
- Do not revert, overwrite, delete, or reformat unrelated user changes.
- Modify only files related to the requested task.
- If requested work overlaps existing changes, preserve them and report conflicts that cannot be resolved safely.
- Do not expand a focused task into unrelated cleanup or repository-wide migration.
- Do not edit generated KSP, SQLDelight, Compose resource, or other generated output directly.

## Subagents

- Use subagents when work splits into at least two independent scopes and parallel execution is likely to save time.
- Prefer subagents for large read-heavy exploration, independent reviews, and test-failure triage.
- Do not delegate small tasks when coordination overhead is greater than the expected benefit.
- Avoid parallel write-heavy work unless file ownership is clearly separated.
- Keep final synthesis, shared-file integration, and repository-finalizing Git actions in the main thread.

## Git Actions

- Do not stage, commit, push, create a branch, or open a pull request unless the user explicitly requests it.
- Keep repository-finalizing Git actions in the main thread.
- When a commit is requested, write the commit message in Korean.
- Prefer `type(scope if useful): summary`, using a concrete scope such as `home`, `navigation`, `settings`, or `i18n` when helpful.
- For a commit containing multiple grouped changes, add one contiguous block of flat bullet lines after the title with no blank lines between bullets.
- Do not require bullet lines for a genuinely small single-purpose commit.

Example:

```text
feat(home): 홈 화면 상태와 데이터 흐름 정리
- 홈 집계와 최근 활동 로딩 책임을 정리
- 관련 회귀 테스트와 문서를 갱신
```
