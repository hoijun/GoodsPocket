package goods.pocket.app.presentation.screen

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

internal enum class SettingsLineIconKind {
    Back,
    Currency,
    Calendar,
}

@Composable
internal fun SettingsLineIcon(
    kind: SettingsLineIconKind,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Canvas(modifier = modifier) {
        val stroke = Stroke(
            width = 1.6.dp.toPx(),
            cap = StrokeCap.Round,
            join = StrokeJoin.Round,
        )
        when (kind) {
            SettingsLineIconKind.Back -> {
                val path = Path().apply {
                    moveTo(size.width * 0.68f, size.height * 0.10f)
                    lineTo(size.width * 0.28f, size.height * 0.50f)
                    lineTo(size.width * 0.68f, size.height * 0.90f)
                }
                drawPath(path, color, style = stroke)
            }
            SettingsLineIconKind.Currency -> {
                val yTop = size.height * 0.28f
                val yBottom = size.height * 0.75f
                drawLine(
                    color,
                    Offset(size.width * 0.26f, yTop),
                    Offset(size.width * 0.39f, yBottom),
                    strokeWidth = stroke.width,
                    cap = StrokeCap.Round,
                )
                drawLine(
                    color,
                    Offset(size.width * 0.39f, yBottom),
                    Offset(size.width * 0.50f, yTop),
                    strokeWidth = stroke.width,
                    cap = StrokeCap.Round,
                )
                drawLine(
                    color,
                    Offset(size.width * 0.50f, yTop),
                    Offset(size.width * 0.61f, yBottom),
                    strokeWidth = stroke.width,
                    cap = StrokeCap.Round,
                )
                drawLine(
                    color,
                    Offset(size.width * 0.61f, yBottom),
                    Offset(size.width * 0.74f, yTop),
                    strokeWidth = stroke.width,
                    cap = StrokeCap.Round,
                )
                drawLine(
                    color,
                    Offset(size.width * 0.19f, size.height * 0.45f),
                    Offset(size.width * 0.81f, size.height * 0.45f),
                    strokeWidth = stroke.width,
                    cap = StrokeCap.Round,
                )
                drawLine(
                    color,
                    Offset(size.width * 0.21f, size.height * 0.59f),
                    Offset(size.width * 0.79f, size.height * 0.59f),
                    strokeWidth = stroke.width,
                    cap = StrokeCap.Round,
                )
            }
            SettingsLineIconKind.Calendar -> {
                drawRoundRect(
                    color = color,
                    topLeft = Offset(size.width * 0.20f, size.height * 0.25f),
                    size = Size(size.width * 0.60f, size.height * 0.57f),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(2.dp.toPx()),
                    style = stroke,
                )
                drawLine(
                    color,
                    Offset(size.width * 0.20f, size.height * 0.43f),
                    Offset(size.width * 0.80f, size.height * 0.43f),
                    strokeWidth = stroke.width,
                )
                drawLine(
                    color,
                    Offset(size.width * 0.35f, size.height * 0.18f),
                    Offset(size.width * 0.35f, size.height * 0.33f),
                    strokeWidth = stroke.width,
                    cap = StrokeCap.Round,
                )
                drawLine(
                    color,
                    Offset(size.width * 0.65f, size.height * 0.18f),
                    Offset(size.width * 0.65f, size.height * 0.33f),
                    strokeWidth = stroke.width,
                    cap = StrokeCap.Round,
                )
            }
        }
    }
}
