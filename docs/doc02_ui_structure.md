# UI Structure

This is the approved rebuild surface. [DESIGN.md](../DESIGN.md) governs screen geometry and visual exceptions.

| Destination | Contents and actions |
| --- | --- |
| Home | Brand/banner, counts, recent goods, spending, schedule; open collection segments, entry details, events and quick add |
| Collection | Owned/reserved/all, search, three-column grid, summary; detail, edit, receive, cancel/delete |
| Events | Current-month overview, type filters, featured event and timeline; detail, edit/delete |
| My | Local profile, four summary values and management information; open Settings |
| Settings | Language selection, read-only formats, back to My |

Home, Collection, Events and My share bottom navigation and centered quick add. Settings has neither. Do not recreate separate Preorders or Transactions destinations.

## Sheets

- Quick add offers collection and event registration.
- Owned/reserved collection details share geometry; metadata and receipt/cancel actions vary.
- Editors own drafts and save state, retaining unexposed persisted fields.
- Event detail shows linked goods when present and tolerates a cleared link.
- Canceling a reservation archives it outside active collection UI. Permanent collection deletion keeps events and clears their links.
- Shell overlays store kind and target ID, not copied entity snapshots.
- Header/footer remain fixed while form content scrolls above the keyboard. Validation and failure retain the draft.

## States and Exclusions

Distinguish loading, empty, no results, content and recoverable failure. A failed observer after successful save must not replay the write.

Do not add calendar, maps, RSVP, photos, favorites, wishlist, profile editing, login, ledger, backup, reset, theme controls or click affordances on read-only settings. Deferred original goals remain in [Product Scope](doc01_service_plan.md).
