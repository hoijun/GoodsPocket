# Sheet Visual Audit

## Capture And Method

The 2026-10-04 iOS simulator captures were compared with the four saved references
in `design/references/sheets`. The current captures are
`/tmp/gp-visual-{collection-detail,collection-editor,event-detail,event-editor}.png`.
Each image was normalized to 393 x 852 using Lanczos resampling.

The reproducible measurement script and generated side-by-side, overlay, and
difference images are local ignored artifacts under
`build/visual-comparison/sheets/`. Run `measure.py` with a Python environment
containing Pillow and NumPy. `measurements.json` records threshold-based paint
runs. These runs are not text-layout boxes or a pixel-parity certificate.

## Measured Results

Coordinates are normalized inclusive pixel ranges. Surface top is detected at
x=30..34 to avoid the corner and central drag handle. Orange ranges describe the
wide primary-action paint, excluding most antialiased edge pixels.

| Sheet | Reference Top | Current Top | Reference Action Y | Current Action Y |
| --- | --- | --- | --- | --- |
| Collection detail | 251 | 250 | 780..816 | 767..804 |
| Collection editor | 205 | 204 | 762..803 | 762..803 |
| Event detail | 424 | 424 | 762..806 | 761..806 |
| Event editor | 414 | 414 | 752..793 | 752..793 |

Collection detail was corrected with coupled changes to `SheetHeight`
606dp -> 602dp, `MediaContentGap` 12dp -> 8dp, and `HorizontalPadding`
28dp -> 29dp. The 257dp media height and 40dp header remain unchanged. Fresh
captures after the rebuilt iOS app confirm the following before/after result:

| Collection Detail Paint | Reference | Before | After |
| --- | --- | --- | --- |
| Sheet top | 251 | 246 | 250 |
| Media horizontal bounds | 28..362 | 28..363 | 29..362 |
| Media vertical bounds | 290..545 | 286..542 | 290..546 |
| Green badge vertical bounds | 555..573 | 555..572 | 555..572 |
| Dark title vertical bounds | 584..595 | 585..595 | 585..595 |

Every listed final collection-detail edge is within 1px of the reference. Moving
the media down while reducing the following gap retained the already aligned
badge and title. The source media width is now 335dp; thresholded paint differs
slightly at its antialiased edges.

The current collection detail action row is higher than the historical image.
The current 34px bottom safe area, 8dp footer bottom padding, and the 5dp inset
around 38dp action paint inside a minimum 48dp interactive layout explain its
paint bottom: `852 - 34 - 8 - 5 = 805` (exclusive). The reference paint ends at
approximately 818, or `852 - 34`. Thus the 13px gap is footer spacing and
interactive-layout geometry, not evidence of a historical 21px system inset.
Moving the painted action without losing its 48dp touch target is a contract
decision; removing real safe-area padding or shrinking hit targets is not an
acceptable visual-only correction.

The two editors have closely aligned top, handle, title, field, and segmented
control structure. Their former 3px action-paint offset was corrected by changing
the button minimum paint height from 44dp to 42dp and bottom padding from 13dp
to 11dp for collection, and 23dp to 21dp for event. `heightIn` still permits
growth, and Material's minimum 48dp interactive layout was not overridden.
Fresh captures confirm collection action paint y=762..803 and event action
paint y=752..793, exactly matching the reference threshold runs. Their previous
bounds were 759..802 and 749..792 respectively.

## Exclusions And Findings

- Existing smoke-record names, dates, categories, status, optional metadata,
  counts, and notes differ from the references. The event detail legitimately
  omits absent related-entry, location, and memo rows. Collection detail has an
  additional related-link row and no note for this record.
- Background screens use the current real-device inset policy. Their absolute
  y positions and dynamic content are not sheet geometry defects.
- System status text, Dynamic Island, and home-indicator raster differences are
  excluded. Gray media is the intentional missing-image fallback; historical
  gradient/grain does not represent loaded product media.
- No overlapping title, controls, or primary action was observed in these four
  portrait captures. Both detail actions remain distinct and equal-width;
  gradient color thresholds alone can misleadingly suggest unequal widths.
- This audit does not prove landscape, keyboard, large-font, or exact reference
  data parity. Those interaction/adaptive checks are documented separately.

## Validation And Verdict

The coupled collection-detail and editor-action corrections have regression
RED/GREEN coverage. Final validation reported by the coordinating agent passed
Android debug 112/112, Android release 112/112, iOS simulator 74/74, ktlint,
architecture checks, debug APK assembly, and the Xcode build. Native Android
Activity recreation tests passed 7/7 again, including real editor input/save and
recreation, recorded in `/tmp/gp-visual-refinement-native.log`. The final iOS
Maestro flow passed five screens, four sheets, and filter/control interactions;
its execution log is `/tmp/gp-visual-final-ios.log`. This audit independently
reran normalization and paint measurements and inspected the fresh editor
side-by-side images after the final correction.

The collection-detail sheet/media/badge/title bounds above now satisfy the 1px
target, event-detail sheet/action bounds satisfy that target, and both editor
action vertical paint bounds match exactly. Full-sheet 1px parity is not claimed:
the preserved accessible collection-detail footer remains an explicit deviation,
and unrelated text/raster/internal-form pixels are not comprehensively certified.
No safe-area subtraction or reduced touch target was introduced.
