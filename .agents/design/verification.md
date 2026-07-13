# Visual Verification

Use this process for every screen that has a saved reference image.

## Comparison Coordinate System

- Normalize every reference and app capture to `393 x 852` before comparing.
- The current iPhone 16 simulator capture is `1179 x 2556`; resize it to `393 x 852` with Lanczos resampling.
- Compare the app viewport, not the raw source-image resolution.
- System status-bar differences, Dynamic Island, dynamic domain values, and intentionally gray media placeholders may be excluded only when explicitly documented.
- All other geometry has a target tolerance of `1px` after normalization.

## Iteration Process

1. Capture the current screen in the same simulator and normalize it to `393 x 852`.
2. Measure the reference bounding boxes for the app bar, first content block, repeated cards or rows, and bottom navigation.
3. Reuse locked colors, surface rules, typography weights, placeholder treatment, and shared navigation where applicable.
4. Measure screen-specific width, height, internal padding, typography, and row rhythm from that screen's reference.
5. Add a focused regression contract before changing production Compose code when the geometry is stable enough to encode.
6. Rebuild, recapture, and compare after each independent geometry change.
7. Stop only when all non-excluded differences are within `1px` in the normalized comparison.

Do not redesign from memory. Do not copy Home dimensions blindly to another screen.

## Completion Checklist

- No production source file exceeds `600` lines.
- No reference screenshot is imported by production UI code.
- No temporary initial-letter media placeholder remains.
- State, event callbacks, navigation behavior, localization, and accessibility remain intact.
- Card surface, border, corner radius, and shadow match the applicable screen contract.
- Major section and repeated-item bounding boxes match the reference within `1px` after normalization.
- Bottom content remains visible above shared navigation and system insets.
- Deliberately excluded differences are listed with the comparison result.
- `bash ./gradlew :composeApp:compileDebugKotlinAndroid` passes after meaningful changes.
- `bash ./gradlew :composeApp:allTests` passes after meaningful changes.

## Home-Specific Checks

- Home cards use `#FEFBF8`; the hero uses `#FEF9F5`.
- Standard Home card shadow is no higher than `0.25dp`; compact goods card shadow is no higher than `0.5dp`.
- Summary columns span the full card width.
- Recent image area is `96dp` high.
- Spending chart normalized bounding box matches the reference within `1px`.
- Schedule second row and bottom border are visible above bottom navigation.
- Shared bottom navigation starts at the same normalized y coordinate as the reference.
