# Sheets And Forms

Use these rules with [Foundations](foundations.md), [Shared Components](components.md), and the target screen's reference.

## Inputs And Quick Entry

Forms should feel lightweight and low-friction. Quick entry matters, so inputs must be welcoming and complete with minimal required fields.

- Keep primary save actions obvious.
- Support partial entry without making the form look broken.
- Use appropriate input controls for each value type.
- Preserve validation, formatting, focus, keyboard, and event behavior during visual changes.
- Keep user-facing copy in Compose resources and synchronize Korean and English resources.

## Bottom Sheets

Bottom sheets are the primary pattern for quick add, detail inspection, and editing. They should feel soft, tidy, efficient, and high-trust.

- Use clear section grouping and strong titles.
- Keep one obvious primary action.
- Preserve back, dismiss, confirm, and destructive-action behavior.
- Use pure white only when the sheet needs visual separation from the warm app background.
- Measure radius, drag-handle spacing, media height, content padding, and action spacing from the relevant reference.

## Detail And Editor Structure

Use this information order unless the target reference and current product behavior require a more specific arrangement:

1. title and status
2. key metadata
3. related entities or linked records
4. primary action
5. secondary actions
6. destructive action last

Keep important state, due dates, and financial information near the top. Clearly separate destructive actions from edit or confirm actions. Do not bury important information below long descriptive content.

## Visual States

Sheets and forms must distinguish loading, validation failure, save failure, disabled action, and successful completion without changing the underlying state flow.

Use calm, readable inline feedback and one clear recovery action. Do not rely on color alone. Ensure error text, labels, and controls remain legible at system font scales.
