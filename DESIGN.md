# GoodsPocket Design Rules

## Rule Loading

Before changing visual UI, read the design documents that apply to the target path or component.

Always read:

- [Product Direction](.agents/design/product-direction.md)
- [Foundations](.agents/design/foundations.md)

Read when the task touches the corresponding area:

- Home screen or Home-specific shared chrome: [Home Image-Locked Contract](.agents/design/home.md) and [Visual Verification](.agents/design/verification.md)
- Collection screen: [Collection Image-Locked Contract](.agents/design/collection.md), [Shared Components](.agents/design/components.md), and [Visual Verification](.agents/design/verification.md)
- Events screen: [Events Image-Locked Contract](.agents/design/events.md), [Shared Components](.agents/design/components.md), and [Visual Verification](.agents/design/verification.md)
- My screen: [My Image-Locked Contract](.agents/design/my.md), [Shared Components](.agents/design/components.md), and [Visual Verification](.agents/design/verification.md)
- Settings screen: [Settings Image-Locked Contract](.agents/design/settings.md), [Shared Components](.agents/design/components.md), and [Visual Verification](.agents/design/verification.md)
- Other screen-to-reference alignment: [Screen Alignment](.agents/design/screen-alignment.md), [Shared Components](.agents/design/components.md), and [Visual Verification](.agents/design/verification.md)
- Cards, lists, search, filters, navigation, badges, empty/loading states, or iconography: [Shared Components](.agents/design/components.md)
- Inputs, quick entry, detail/edit sheets, or bottom sheets: [Sheets and Forms](.agents/design/sheets-forms.md)
- Screenshot capture, normalized comparison, pixel tolerance, or visual completion: [Visual Verification](.agents/design/verification.md)

If a task spans multiple areas, read every relevant document. These linked files are mandatory design instructions, not optional background material.

## Precedence

1. Explicit user requirements and the target screen's reference image
2. The screen-specific document in `.agents/design/`
3. Shared foundations and component rules
4. Existing implementation where it does not conflict with the sources above

When a screen has its own reference, measure that screen instead of copying Home geometry blindly. Preserve domain state, event handlers, navigation behavior, localization, and accessibility while applying the visual contract.
