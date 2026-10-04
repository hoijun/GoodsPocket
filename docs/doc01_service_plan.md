# GoodsPocket Product Scope

GoodsPocket is a local-first collection journal and schedule companion for animation, game, character, VTuber, and collaboration merchandise collectors. It connects owned goods, reservations, dates, and purchase context. It is not a marketplace or social network.

## Approved Rebuild

The rebuild preserves the current image-locked design and supported interactions while replacing inconsistent internal structures. The unreleased development schema may change. This is the target contract, not an implementation-completion claim.

| Area | Included behavior |
| --- | --- |
| Home | Brand/banner, collection counts, recent entries, monthly amount summary, upcoming events, navigation to real targets |
| Collection | Owned/reserved/all segments, name/series/character search, three-column grid, summary, registration, detail, edit, receipt, cancel/delete |
| Events | Month overview, type filters, featured event, timeline, registration, detail, edit/delete |
| My | Local profile, summary, informational sync/notification rows, Settings entry |
| Settings | Persisted Korean/English selection, read-only currency/date format, return to My |
| Overlays | Collection/event quick add, detail/editor sheets, confirmation, validation and recoverable failure |

Primary tabs are Home, Collection, Events, and My. Reservations are a collection segment. Settings is secondary without bottom navigation. Production starts with an empty database; deterministic samples require an explicit demo/test/preview environment.

Minimum entry remains important: collection name, category and ownership status; reserved goods also require store and release date. Events require title, date and type. An editor preserves fields it does not expose. Receipt retains the entry ID and reservation metadata.

## Original Goals Deferred

These goals remain product ideas; being unimplemented does not make them obsolete. They are outside the basic rebuild and do not imply new UI, tables, or automatic behavior.

| Goal | Later decisions |
| --- | --- |
| Payment ledger | Deposits, balances, shipping, refunds, resale, transaction CRUD, reconciliation |
| Automatic reservation events | Create/update/cancel policy and user-edit preservation |
| Broader inputs | Price, quantity, dates, location and inputs absent from approved sheets |
| Photos | Picker, file lifetime, permissions, persistence and upload |
| Cloud/authentication | Firebase or Supabase, ownership, authorization, offline synchronization and conflicts |
| Notifications/backup/export | Platform integration and explicit UI contracts |
| Calendar/favorites/wishlist | Product approval and screen design |

Monthly amounts are derived purchase/reservation totals, not a payment ledger. Exact semantics are in [Repository and Use Cases](doc11_repository_usecase.md).

## Acceptance

Registration, reopening, editing, receipt and deletion must work against real storage. Failures retain drafts and retries do not duplicate writes. Relevant observers and summaries update consistently. Korean/English, keyboard, accessibility, Android/iOS and reference comparisons are validated separately. Future cloud readiness is not implemented synchronization.

[DESIGN.md](../DESIGN.md) and its screen contracts govern visuals. Historical exploratory images do not override them.
