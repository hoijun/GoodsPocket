package goods.pocket.app.presentation.screen

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

internal enum class MyReferenceIconKind {
    Collection,
    Calendar,
    Spending,
    Clock,
    Sync,
    Bell,
    Settings,
    Chevron,
}

@Composable
internal fun MyReferenceIcon(
    kind: MyReferenceIconKind,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Canvas(modifier = modifier) {
        val stroke = Stroke(
            width = 1.8.dp.toPx(),
            cap = StrokeCap.Round,
            join = StrokeJoin.Round,
        )
        when (kind) {
            MyReferenceIconKind.Collection -> drawCollectionIcon(color, stroke)
            MyReferenceIconKind.Calendar -> drawCalendarIcon(color, stroke)
            MyReferenceIconKind.Spending -> drawSpendingIcon(color, stroke)
            MyReferenceIconKind.Clock -> drawClockIcon(color, stroke)
            MyReferenceIconKind.Sync -> drawSyncIcon(color, stroke)
            MyReferenceIconKind.Bell -> drawBellIcon(color, stroke)
            MyReferenceIconKind.Settings -> drawSettingsIcon(color, stroke)
            MyReferenceIconKind.Chevron -> drawChevronIcon(color, stroke)
        }
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawCollectionIcon(
    color: Color,
    stroke: Stroke,
) {
    val cube = Path().apply {
        moveTo(size.width * 0.50f, size.height * 0.12f)
        lineTo(size.width * 0.84f, size.height * 0.30f)
        lineTo(size.width * 0.84f, size.height * 0.70f)
        lineTo(size.width * 0.50f, size.height * 0.88f)
        lineTo(size.width * 0.16f, size.height * 0.70f)
        lineTo(size.width * 0.16f, size.height * 0.30f)
        close()
    }
    drawPath(cube, color, style = stroke)
    drawLine(color, Offset(size.width * 0.16f, size.height * 0.30f), Offset(size.width * 0.50f, size.height * 0.48f), stroke.width)
    drawLine(color, Offset(size.width * 0.84f, size.height * 0.30f), Offset(size.width * 0.50f, size.height * 0.48f), stroke.width)
    drawLine(color, Offset(size.width * 0.50f, size.height * 0.48f), Offset(size.width * 0.50f, size.height * 0.88f), stroke.width)
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawCalendarIcon(
    color: Color,
    stroke: Stroke,
) {
    drawRoundRect(
        color = color,
        topLeft = Offset(size.width * 0.18f, size.height * 0.22f),
        size = Size(size.width * 0.64f, size.height * 0.62f),
        cornerRadius = CornerRadius(size.width * 0.08f),
        style = stroke,
    )
    drawLine(color, Offset(size.width * 0.18f, size.height * 0.40f), Offset(size.width * 0.82f, size.height * 0.40f), stroke.width)
    drawLine(color, Offset(size.width * 0.34f, size.height * 0.12f), Offset(size.width * 0.34f, size.height * 0.30f), stroke.width)
    drawLine(color, Offset(size.width * 0.66f, size.height * 0.12f), Offset(size.width * 0.66f, size.height * 0.30f), stroke.width)
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawSpendingIcon(
    color: Color,
    stroke: Stroke,
) {
    val widths = listOf(0.18f, 0.42f, 0.66f)
    val tops = listOf(0.48f, 0.20f, 0.35f)
    widths.forEachIndexed { index, left ->
        drawRoundRect(
            color = color,
            topLeft = Offset(size.width * left, size.height * tops[index]),
            size = Size(size.width * 0.13f, size.height * (0.78f - tops[index])),
            cornerRadius = CornerRadius(size.width * 0.05f),
            style = stroke,
        )
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawClockIcon(
    color: Color,
    stroke: Stroke,
) {
    drawCircle(color, radius = size.minDimension * 0.34f, center = center, style = stroke)
    drawLine(color, center, Offset(size.width * 0.50f, size.height * 0.29f), stroke.width)
    drawLine(color, center, Offset(size.width * 0.66f, size.height * 0.60f), stroke.width)
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawSyncIcon(
    color: Color,
    stroke: Stroke,
) {
    val cloud = Path().apply {
        moveTo(size.width * 0.27f, size.height * 0.66f)
        cubicTo(size.width * 0.08f, size.height * 0.65f, size.width * 0.08f, size.height * 0.39f, size.width * 0.29f, size.height * 0.36f)
        cubicTo(size.width * 0.37f, size.height * 0.13f, size.width * 0.70f, size.height * 0.17f, size.width * 0.74f, size.height * 0.39f)
        cubicTo(size.width * 0.94f, size.height * 0.42f, size.width * 0.91f, size.height * 0.66f, size.width * 0.74f, size.height * 0.66f)
    }
    drawPath(cloud, color, style = stroke)
    drawLine(color, Offset(size.width * 0.50f, size.height * 0.82f), Offset(size.width * 0.50f, size.height * 0.43f), stroke.width)
    drawLine(color, Offset(size.width * 0.50f, size.height * 0.43f), Offset(size.width * 0.38f, size.height * 0.55f), stroke.width)
    drawLine(color, Offset(size.width * 0.50f, size.height * 0.43f), Offset(size.width * 0.62f, size.height * 0.55f), stroke.width)
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawBellIcon(
    color: Color,
    stroke: Stroke,
) {
    val bell = Path().apply {
        moveTo(size.width * 0.24f, size.height * 0.68f)
        cubicTo(size.width * 0.34f, size.height * 0.57f, size.width * 0.30f, size.height * 0.42f, size.width * 0.34f, size.height * 0.30f)
        cubicTo(size.width * 0.41f, size.height * 0.10f, size.width * 0.59f, size.height * 0.10f, size.width * 0.66f, size.height * 0.30f)
        cubicTo(size.width * 0.70f, size.height * 0.42f, size.width * 0.66f, size.height * 0.57f, size.width * 0.76f, size.height * 0.68f)
        close()
    }
    drawPath(bell, color, style = stroke)
    drawLine(color, Offset(size.width * 0.43f, size.height * 0.78f), Offset(size.width * 0.57f, size.height * 0.78f), stroke.width)
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawSettingsIcon(
    color: Color,
    stroke: Stroke,
) {
    val centerPoint = center
    drawCircle(color, radius = size.minDimension * 0.16f, center = centerPoint, style = stroke)
    drawCircle(color, radius = size.minDimension * 0.32f, center = centerPoint, style = stroke)
    repeat(8) { index ->
        val angle = index * PI.toFloat() / 4f
        val inner = size.minDimension * 0.34f
        val outer = size.minDimension * 0.43f
        drawLine(
            color = color,
            start = Offset(centerPoint.x + cos(angle) * inner, centerPoint.y + sin(angle) * inner),
            end = Offset(centerPoint.x + cos(angle) * outer, centerPoint.y + sin(angle) * outer),
            strokeWidth = stroke.width,
            cap = StrokeCap.Round,
        )
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawChevronIcon(
    color: Color,
    stroke: Stroke,
) {
    val path = Path().apply {
        moveTo(size.width * 0.36f, size.height * 0.20f)
        lineTo(size.width * 0.64f, size.height * 0.50f)
        lineTo(size.width * 0.36f, size.height * 0.80f)
    }
    drawPath(path, color, style = stroke)
}
