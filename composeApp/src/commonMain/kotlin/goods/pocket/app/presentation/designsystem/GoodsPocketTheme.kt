package goods.pocket.app.presentation.designsystem

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val LightColors = buildGoodsPocketLightScheme()
private val GoodsPocketShapes = Shapes(
    extraSmall = RoundedCornerShape(12.dp),
    small = RoundedCornerShape(16.dp),
    medium = RoundedCornerShape(22.dp),
    large = RoundedCornerShape(26.dp),
    extraLarge = RoundedCornerShape(28.dp),
)

private val GoodsPocketTypography = Typography(
    displayLarge = TextStyle(
        fontSize = 34.sp,
        lineHeight = 38.sp,
        fontWeight = FontWeight.Bold,
    ),
    headlineLarge = TextStyle(
        fontSize = 28.sp,
        lineHeight = 34.sp,
        fontWeight = FontWeight.SemiBold,
    ),
    headlineMedium = TextStyle(
        fontSize = 24.sp,
        lineHeight = 30.sp,
        fontWeight = FontWeight.SemiBold,
    ),
    titleLarge = TextStyle(
        fontSize = 20.sp,
        lineHeight = 26.sp,
        fontWeight = FontWeight.SemiBold,
    ),
    titleMedium = TextStyle(
        fontSize = 18.sp,
        lineHeight = 24.sp,
        fontWeight = FontWeight.SemiBold,
    ),
    titleSmall = TextStyle(
        fontSize = 16.sp,
        lineHeight = 22.sp,
        fontWeight = FontWeight.SemiBold,
    ),
    bodyLarge = TextStyle(
        fontSize = 16.sp,
        lineHeight = 22.sp,
        fontWeight = FontWeight.Normal,
    ),
    bodyMedium = TextStyle(
        fontSize = 14.sp,
        lineHeight = 20.sp,
        fontWeight = FontWeight.Normal,
    ),
    bodySmall = TextStyle(
        fontSize = 12.sp,
        lineHeight = 17.sp,
        fontWeight = FontWeight.Normal,
    ),
    labelLarge = TextStyle(
        fontSize = 13.sp,
        lineHeight = 18.sp,
        fontWeight = FontWeight.SemiBold,
    ),
    labelMedium = TextStyle(
        fontSize = 12.sp,
        lineHeight = 16.sp,
        fontWeight = FontWeight.Medium,
    ),
    labelSmall = TextStyle(
        fontSize = 11.sp,
        lineHeight = 14.sp,
        fontWeight = FontWeight.Medium,
    ),
)

@Composable
fun GoodsPocketTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = LightColors,
        shapes = GoodsPocketShapes,
        typography = GoodsPocketTypography,
        content = content,
    )
}

private fun buildGoodsPocketLightScheme(): ColorScheme {
    return lightColorScheme(
        primary = colorOf(GoodsPocketVisualTokens.Primary),
        onPrimary = colorOf(0xFFFFFFFFL),
        primaryContainer = colorOf(GoodsPocketVisualTokens.PrimaryContainer),
        onPrimaryContainer = colorOf(GoodsPocketVisualTokens.Ink),
        background = colorOf(GoodsPocketVisualTokens.Background),
        onBackground = colorOf(GoodsPocketVisualTokens.Ink),
        surface = colorOf(GoodsPocketVisualTokens.Surface),
        onSurface = colorOf(GoodsPocketVisualTokens.Ink),
        surfaceContainer = colorOf(0xFFF5EDF4L),
        surfaceContainerLow = colorOf(GoodsPocketVisualTokens.SurfaceLow),
        surfaceContainerHigh = colorOf(GoodsPocketVisualTokens.SurfaceHigh),
        surfaceContainerHighest = colorOf(GoodsPocketVisualTokens.SurfaceTint),
        surfaceVariant = colorOf(GoodsPocketVisualTokens.SurfaceTint),
        onSurfaceVariant = colorOf(GoodsPocketVisualTokens.MutedInk),
        outline = colorOf(GoodsPocketVisualTokens.Outline),
        outlineVariant = colorOf(GoodsPocketVisualTokens.Outline),
        secondary = colorOf(GoodsPocketVisualTokens.Secondary),
        onSecondary = colorOf(0xFF173B35L),
        secondaryContainer = colorOf(GoodsPocketVisualTokens.SecondaryContainer),
        onSecondaryContainer = colorOf(0xFF173B35L),
        tertiary = colorOf(GoodsPocketVisualTokens.Tertiary),
        onTertiary = colorOf(0xFF4F3A00L),
        tertiaryContainer = colorOf(GoodsPocketVisualTokens.TertiaryContainer),
        onTertiaryContainer = colorOf(0xFF5E4600L),
        error = colorOf(GoodsPocketVisualTokens.Danger),
        onError = colorOf(0xFFFFFFFFL),
        errorContainer = colorOf(0xFFFBE7EDL),
        onErrorContainer = colorOf(0xFF6A3245L),
    )
}

private fun colorOf(value: Long): Color = Color(value)
