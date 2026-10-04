# Navigation Design

| Kind | Destination | Route |
| --- | --- | --- |
| Primary | Home | home |
| Primary | Collection | collection/list |
| Primary | Events | event/list |
| Primary | My | my |
| Secondary | Settings | settings |

Home is the initial tab. Reservations are a Collection segment. There is no preorder/transaction route or start-tab setting.

Shell owns navigation and overlay kind/target ID only. Features own reads, drafts and writes. Quick add, collection detail/editor, event detail/editor and confirmations are overlays, not independent routes.

Home opens a requested collection segment, an entry or events. Settings opens from My and returns to My without bottom navigation or quick add. Reselecting a primary tab returns to its root while preserving unrelated feature state.

Editor cancellation returns to its original detail. Successful deletion closes the removed detail; failures retain it. Back dismisses confirmation before its parent sheet and protects unsaved drafts. Root exit/background behavior follows the platform. External deep links and notification routing are deferred.
