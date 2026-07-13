package goods.pocket.app.presentation.designsystem

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import kotlin.math.absoluteValue

@Composable
internal fun GoodsPocketCardArtworkSeed(
    title: String,
    modifier: Modifier = Modifier,
) {
    val seed = title.hashCode().absoluteValue
    val bodyColors = listOf(
        Color(0xFF2D6A4F),
        Color(0xFF202838),
        Color(0xFF8F6EF2),
        Color(0xFFFF7445),
        Color(0xFFFFB13B),
    )
    val bodyColor = bodyColors[seed % bodyColors.size]
    val accentColor = bodyColors[(seed + 2) % bodyColors.size]
    val baseColor = listOf(
        Color(0xFFE8DDD4),
        Color(0xFFEDE8FF),
        Color(0xFFFFE8DC),
    )[seed % 3]

    Canvas(modifier = modifier.size(96.dp)) {
        drawCircle(
            color = baseColor,
            radius = size.minDimension * 0.44f,
            center = Offset(size.width * 0.50f, size.height * 0.56f),
        )
        drawLine(
            color = accentColor,
            start = Offset(size.width * 0.50f, size.height * 0.06f),
            end = Offset(size.width * 0.50f, size.height * 0.22f),
            strokeWidth = 4.5f,
        )
        drawCircle(
            color = accentColor,
            radius = size.minDimension * 0.08f,
            center = Offset(size.width * 0.50f, size.height * 0.08f),
            style = Stroke(width = 3f),
        )
        drawCircle(
            color = Color(0xFFFFD6B8),
            radius = size.minDimension * 0.17f,
            center = Offset(size.width * 0.50f, size.height * 0.34f),
        )
        drawCircle(
            color = bodyColor,
            radius = size.minDimension * 0.20f,
            center = Offset(size.width * 0.50f, size.height * 0.62f),
        )
        drawLine(
            color = bodyColor,
            start = Offset(size.width * 0.32f, size.height * 0.56f),
            end = Offset(size.width * 0.18f, size.height * 0.72f),
            strokeWidth = 7f,
        )
        drawLine(
            color = bodyColor,
            start = Offset(size.width * 0.68f, size.height * 0.56f),
            end = Offset(size.width * 0.82f, size.height * 0.72f),
            strokeWidth = 7f,
        )
        drawLine(
            color = Color(0xFF202838),
            start = Offset(size.width * 0.42f, size.height * 0.79f),
            end = Offset(size.width * 0.36f, size.height * 0.94f),
            strokeWidth = 6f,
        )
        drawLine(
            color = Color(0xFF202838),
            start = Offset(size.width * 0.58f, size.height * 0.79f),
            end = Offset(size.width * 0.64f, size.height * 0.94f),
            strokeWidth = 6f,
        )
        drawCircle(
            color = Color(0xFF202838),
            radius = 2.4f,
            center = Offset(size.width * 0.43f, size.height * 0.33f),
        )
        drawCircle(
            color = Color(0xFF202838),
            radius = 2.4f,
            center = Offset(size.width * 0.57f, size.height * 0.33f),
        )
    }
}

@Composable
internal fun GoodsPocketFavoriteMark(
    favorite: Boolean,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.size(30.dp),
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 2.dp,
    ) {
        GoodsPocketFavoriteHeart(favorite = favorite)
    }
}

@Composable
internal fun GoodsPocketFavoriteHeart(
    favorite: Boolean,
) {
    val heartColor = if (favorite) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }
    val cutoutColor = MaterialTheme.colorScheme.surface
    Canvas(modifier = Modifier.padding(7.dp).fillMaxSize()) {
        val heart = Path().apply {
            moveTo(size.width * 0.50f, size.height * 0.88f)
            cubicTo(
                size.width * 0.06f,
                size.height * 0.58f,
                size.width * 0.08f,
                size.height * 0.18f,
                size.width * 0.34f,
                size.height * 0.14f,
            )
            cubicTo(
                size.width * 0.45f,
                size.height * 0.12f,
                size.width * 0.50f,
                size.height * 0.22f,
                size.width * 0.50f,
                size.height * 0.28f,
            )
            cubicTo(
                size.width * 0.50f,
                size.height * 0.22f,
                size.width * 0.55f,
                size.height * 0.12f,
                size.width * 0.66f,
                size.height * 0.14f,
            )
            cubicTo(
                size.width * 0.92f,
                size.height * 0.18f,
                size.width * 0.94f,
                size.height * 0.58f,
                size.width * 0.50f,
                size.height * 0.88f,
            )
            close()
        }
        if (favorite) {
            drawPath(path = heart, color = heartColor)
        } else {
            drawPath(
                path = heart,
                color = heartColor,
                style = Stroke(width = 2.4f),
            )
        }
        if (!favorite) {
            drawLine(
                color = cutoutColor,
                start = Offset(size.width * 0.44f, size.height * 0.30f),
                end = Offset(size.width * 0.56f, size.height * 0.30f),
                strokeWidth = 2.4f,
            )
        }
    }
}
