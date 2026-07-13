package goods.pocket.app.presentation.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
internal fun HomeBellGlyph(
    modifier: Modifier = Modifier,
) {
    val color = MaterialTheme.colorScheme.onSurfaceVariant
    Canvas(modifier = modifier.size(30.dp)) {
        val stroke = Stroke(width = 3.1f)
        val bell = Path().apply {
            moveTo(size.width * 0.25f, size.height * 0.68f)
            cubicTo(
                size.width * 0.31f,
                size.height * 0.57f,
                size.width * 0.30f,
                size.height * 0.37f,
                size.width * 0.50f,
                size.height * 0.29f,
            )
            cubicTo(
                size.width * 0.70f,
                size.height * 0.37f,
                size.width * 0.69f,
                size.height * 0.57f,
                size.width * 0.75f,
                size.height * 0.68f,
            )
            lineTo(size.width * 0.25f, size.height * 0.68f)
        }
        drawPath(path = bell, color = color, style = stroke)
        drawLine(
            color = color,
            start = Offset(size.width * 0.38f, size.height * 0.79f),
            end = Offset(size.width * 0.62f, size.height * 0.79f),
            strokeWidth = 3.1f,
        )
        drawCircle(
            color = color,
            radius = 2.9f,
            center = Offset(size.width * 0.50f, size.height * 0.90f),
        )
        drawCircle(
            color = color,
            radius = 2.5f,
            center = Offset(size.width * 0.50f, size.height * 0.22f),
        )
    }
}

@Composable
internal fun HomeDeskShelfIllustration(
    modifier: Modifier = Modifier,
) {
    Canvas(modifier = modifier) {
        drawRoundRect(
            color = Color(0xFFE6E2DE),
            topLeft = Offset(size.width * 0.12f, size.height * 0.20f),
            size = Size(size.width * 0.78f, size.height * 0.58f),
            cornerRadius = CornerRadius(18f, 18f),
        )
        drawRoundRect(
            color = Color(0xFFD5D0CA),
            topLeft = Offset(size.width * 0.22f, size.height * 0.32f),
            size = Size(size.width * 0.58f, size.height * 0.10f),
            cornerRadius = CornerRadius(7f, 7f),
        )
        drawRoundRect(
            color = Color(0xFFCAC5BE),
            topLeft = Offset(size.width * 0.30f, size.height * 0.52f),
            size = Size(size.width * 0.42f, size.height * 0.08f),
            cornerRadius = CornerRadius(7f, 7f),
        )
    }
}

@Composable
internal fun HomeFigureKeyringArtwork(
    title: String,
    modifier: Modifier = Modifier,
) {
    Canvas(modifier = modifier) {
        drawRoundRect(
            color = Color(0xFFD7D2CC),
            size = size,
            cornerRadius = CornerRadius(20f, 20f),
        )
        drawRoundRect(
            color = Color(0xFFC7C1BA),
            topLeft = Offset(size.width * 0.24f, size.height * 0.42f),
            size = Size(size.width * 0.52f, size.height * 0.14f),
            cornerRadius = CornerRadius(7f, 7f),
        )
    }
}

@Composable
internal fun HomeScheduleArtwork(
    title: String,
    modifier: Modifier = Modifier,
) {
    Canvas(modifier = modifier) {
        drawRoundRect(
            color = Color(0xFFD7D2CC),
            size = size,
            cornerRadius = CornerRadius(14f, 14f),
        )
        drawRoundRect(
            color = Color(0xFFC7C1BA),
            topLeft = Offset(size.width * 0.22f, size.height * 0.43f),
            size = Size(size.width * 0.56f, size.height * 0.14f),
            cornerRadius = CornerRadius(6f, 6f),
        )
    }
}

@Composable
internal fun HomeFavoriteHeartGlyph(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
) {
    Canvas(modifier = modifier) {
        val heart = Path().apply {
            moveTo(size.width * 0.50f, size.height * 0.86f)
            cubicTo(
                size.width * 0.10f,
                size.height * 0.58f,
                size.width * 0.08f,
                size.height * 0.20f,
                size.width * 0.34f,
                size.height * 0.16f,
            )
            cubicTo(
                size.width * 0.44f,
                size.height * 0.14f,
                size.width * 0.50f,
                size.height * 0.24f,
                size.width * 0.50f,
                size.height * 0.30f,
            )
            cubicTo(
                size.width * 0.50f,
                size.height * 0.24f,
                size.width * 0.56f,
                size.height * 0.14f,
                size.width * 0.66f,
                size.height * 0.16f,
            )
            cubicTo(
                size.width * 0.92f,
                size.height * 0.20f,
                size.width * 0.90f,
                size.height * 0.58f,
                size.width * 0.50f,
                size.height * 0.86f,
            )
            close()
        }
        drawPath(path = heart, color = color)
    }
}

@Composable
internal fun HomeViewAllChevron(
    modifier: Modifier = Modifier,
) {
    Canvas(modifier = modifier) {
        val strokeWidth = 1.3f
        drawLine(
            color = HomeMuted,
            start = Offset(size.width * 0.18f, size.height * 0.10f),
            end = Offset(size.width * 0.78f, size.height * 0.50f),
            strokeWidth = strokeWidth,
        )
        drawLine(
            color = HomeMuted,
            start = Offset(size.width * 0.78f, size.height * 0.50f),
            end = Offset(size.width * 0.18f, size.height * 0.90f),
            strokeWidth = strokeWidth,
        )
    }
}

@Composable
internal fun HomeSpendingBars(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .width(HomeReferenceMetrics.SpendingChartWidth)
            .height(HomeReferenceMetrics.SpendingChartHeight),
        horizontalArrangement = Arrangement.spacedBy(7.dp),
        verticalAlignment = androidx.compose.ui.Alignment.Bottom,
    ) {
        listOf(
            0.26f to Color(0xFFFFE7CE),
            0.40f to Color(0xFFFFA071),
            0.52f to Color(0xFFFFB579),
            0.72f to Color(0xFFFFEBDD),
            0.82f to MaterialTheme.colorScheme.tertiary,
            0.50f to Color(0xFFFFD39A),
            1.0f to Color(0xFFFFEBDD),
            0.62f to Color(0xFFFFDDB7),
            0.82f to MaterialTheme.colorScheme.primary,
        ).forEach { (heightFraction, color) ->
            Surface(
                modifier = Modifier
                    .width(7.dp)
                    .fillMaxHeight(heightFraction),
                shape = CircleShape,
                color = color,
            ) {}
        }
    }
}
