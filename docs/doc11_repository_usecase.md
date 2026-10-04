# Repository and Use Cases

Repository interfaces live in pure domain feature packages; implementations live in data. Collection, Event and Settings contracts expose observable reads and suspend commands. SQL rows, SDK types and platform errors never cross that boundary.

Simple CRUD calls repositories directly from feature StateHolders. Use cases own actual validation, state transitions and aggregation. Do not create SaveX/GetX forwarding classes for every operation.

## Business Operations

MarkPreorderReceivedUseCase validates receipt and delegates its atomic persistence primitive to the repository. The same entry ID and reservation metadata survive. Already received is a no-op; absent is not-found; canceled cannot be received. Datasources do not decide business rules.

Cancel archives the reservation outside active collection UI. Permanent deletion removes collection data and clears links on retained events atomically. No transaction-ledger entry or automatic event is created; those original product goals are deferred.

## Preserved Amount Semantics

The pre-rebuild GetDashboardSummaryUseCase uses these rules, which the unified model must reproduce:

- Non-reservation purchases contribute purchasePrice (null = zero) on purchaseDate; missing dates do not contribute.
- Every reservation contributes totalPrice (null = zero) on orderDate; missing dates do not contribute. Status, including cancellation or receipt, does not suppress this record.
- A received purchase linked to an existing reservation is excluded from purchase contributions, preventing double counting. The unified row therefore contributes its reservation total once, including after receipt.
- Deposit, remaining balance and shipping do not add separate amounts. Quantity does not multiply the stored amount.
- Month totals select the record's calendar month. Current month is derived through the injected clock and explicit time-zone conversion; stored purchase/order dates are date-only.
- Previous month uses calendar arithmetic, including year boundaries.
- The chart has nine buckets: days 1-3, 4-6, 7-9, 10-12, 13-15, 16-18, 19-21, 22-24, and 25 through month end.

| Example in October | Contribution |
| --- | --- |
| Purchase 20,000 on October 2 | 20,000 in bucket 1 |
| Reservation total 50,000 ordered October 5, deposit 10,000 | 50,000 in bucket 2, not 10,000 |
| That reservation received in November | October remains 50,000; no November duplicate |
| That reservation canceled | Archived outside active UI; October total still includes 50,000 |
| Missing purchase/order date | No monthly contribution |
| Purchase on October 31 | Bucket 9 |
| Previous month of January 2027 | December 2026 |

These totals are purchase/reservation amounts, not actual payments or refunds. Changing that meaning requires a separate product decision. Do not silently change it while refactoring.

Owned count is the number of visible OWNED entries, not summed quantity. Active preorder count is the number of RESERVED entries with canceledAt == null. ReservationDetails.receivedAt records receipt; canceledAt records archival cancellation. The new storage has no ACTIVE/PAYMENT_PENDING/RECEIVED/CANCELED enum. Recent collection display uses stable entry IDs and update time; the unified row must not produce duplicate cards for receipt. UI localization belongs in presentation, not aggregation.

## Errors and Future Remote Data

Expected validation/absence/no-op returns focused results. Infrastructure errors are translated at data boundaries with causes preserved. Cancellation propagates. Retrying a draft reuses its ID; successful writes are not replayed because observation later failed.

Firebase/Supabase SDKs, empty remote adapters and generic CloudClient wrappers are not introduced now. Later remote work separately defines authentication, ownership, account isolation, pending writes, revision conflicts, deletion propagation and file lifetime. Local-first boundaries alone are not completed sync.
