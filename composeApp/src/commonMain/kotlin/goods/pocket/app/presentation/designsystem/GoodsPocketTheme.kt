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
    small = RoundedCornerShape(18.dp),
    medium = RoundedCornerShape(24.dp),
    large = RoundedCornerShape(28.dp),
    extraLarge = RoundedCornerShape(32.dp),
)

private val GoodsPocketTypography = Typography(
    headlineLarge = TextStyle(
        fontSize = 30.sp,
        lineHeight = 36.sp,
        fontWeight = FontWeight.Bold,
    ),
    headlineMedium = TextStyle(
        fontSize = 26.sp,
        lineHeight = 32.sp,
        fontWeight = FontWeight.Bold,
    ),
    titleLarge = TextStyle(
        fontSize = 22.sp,
        lineHeight = 28.sp,
        fontWeight = FontWeight.Bold,
    ),
    titleMedium = TextStyle(
        fontSize = 18.sp,
        lineHeight = 24.sp,
        fontWeight = FontWeight.SemiBold,
    ),
    bodyLarge = TextStyle(
        fontSize = 16.sp,
        lineHeight = 24.sp,
        fontWeight = FontWeight.Normal,
    ),
    bodyMedium = TextStyle(
        fontSize = 14.sp,
        lineHeight = 21.sp,
        fontWeight = FontWeight.Normal,
    ),
    bodySmall = TextStyle(
        fontSize = 12.sp,
        lineHeight = 18.sp,
        fontWeight = FontWeight.Normal,
    ),
    labelLarge = TextStyle(
        fontSize = 14.sp,
        lineHeight = 20.sp,
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
        onPrimary = colorOf(0xFFEEFBFFL),
        primaryContainer = colorOf(GoodsPocketVisualTokens.PrimaryContainer),
        onPrimaryContainer = colorOf(0xFF005664L),
        background = colorOf(GoodsPocketVisualTokens.Background),
        onBackground = colorOf(GoodsPocketVisualTokens.Ink),
        surface = colorOf(GoodsPocketVisualTokens.Surface),
        onSurface = colorOf(GoodsPocketVisualTokens.Ink),
        surfaceContainer = colorOf(0xFFE8EFF1L),
        surfaceContainerLow = colorOf(GoodsPocketVisualTokens.SurfaceLow),
        surfaceContainerHigh = colorOf(GoodsPocketVisualTokens.SurfaceHigh),
        surfaceContainerHighest = colorOf(GoodsPocketVisualTokens.SurfaceTint),
        surfaceVariant = colorOf(GoodsPocketVisualTokens.SurfaceTint),
        onSurfaceVariant = colorOf(GoodsPocketVisualTokens.MutedInk),
        outline = colorOf(0xFF727C7FL),
        outlineVariant = colorOf(GoodsPocketVisualTokens.Outline),
        secondary = colorOf(0xFF4B6369L),
        onSecondary = colorOf(0xFFEEFBFFL),
        secondaryContainer = colorOf(0xFFCDE7EEL),
        onSecondaryContainer = colorOf(0xFF3E555BL),
        tertiary = colorOf(0xFF456182L),
        onTertiary = colorOf(0xFFF7F9FFL),
        tertiaryContainer = colorOf(0xFFB5D1F8L),
        onTertiaryContainer = colorOf(0xFF2B4767L),
        error = colorOf(GoodsPocketVisualTokens.Danger),
        onError = colorOf(0xFFFFF7F6L),
        errorContainer = colorOf(0xFFFA746FL),
        onErrorContainer = colorOf(0xFF6E0A12L),
    )
}

private fun colorOf(value: Long): Color = Color(value)
