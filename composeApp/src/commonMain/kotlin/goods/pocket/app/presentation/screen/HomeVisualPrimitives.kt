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
import androidx.compose.ui.geometry.Offset
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
internal fun HomeHeroMediaPlaceholder(
    modifier: Modifier = Modifier,
) {
    Canvas(modifier = modifier) {
        drawRect(
            color = Color(0xFFD7D2CC),
            size = size,
        )
    }
}

@Composable
internal fun HomeRecentGoodsMediaPlaceholder(
    modifier: Modifier = Modifier,
) {
    Canvas(modifier = modifier) {
        drawRoundRect(
            color = Color(0xFFD7D2CC),
            size = size,
        )
    }
}

@Composable
internal fun HomeScheduleMediaPlaceholder(
    modifier: Modifier = Modifier,
) {
    Canvas(modifier = modifier) {
        drawRoundRect(
            color = Color(0xFFD7D2CC),
            size = size,
        )
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
    fractions: List<Float>,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .width(HomeReferenceMetrics.SpendingChartWidth)
            .height(HomeReferenceMetrics.SpendingChartHeight),
        horizontalArrangement = Arrangement.spacedBy(7.dp),
        verticalAlignment = androidx.compose.ui.Alignment.Bottom,
    ) {
        val colors = listOf(
            Color(0xFFFFE7CE),
            Color(0xFFFFA071),
            Color(0xFFFFB579),
            Color(0xFFFFEBDD),
            MaterialTheme.colorScheme.tertiary,
            Color(0xFFFFD39A),
            Color(0xFFFFEBDD),
            Color(0xFFFFDDB7),
            MaterialTheme.colorScheme.primary,
        )
        colors.zip(fractions).forEach { (color, heightFraction) ->
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
