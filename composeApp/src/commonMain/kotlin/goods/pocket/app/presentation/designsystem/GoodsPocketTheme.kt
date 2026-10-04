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
    extraSmall = RoundedCornerShape(10.dp),
    small = RoundedCornerShape(14.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(30.dp),
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
fun GoodsPocketTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        shapes = GoodsPocketShapes,
        typography = GoodsPocketTypography,
        content = content,
    )
}

private fun buildGoodsPocketLightScheme(): ColorScheme = lightColorScheme(
    primary = colorOf(GoodsPocketVisualTokens.PRIMARY),
    onPrimary = colorOf(0xFFFFFFFFL),
    primaryContainer = colorOf(GoodsPocketVisualTokens.PRIMARY_CONTAINER),
    onPrimaryContainer = colorOf(GoodsPocketVisualTokens.INK),
    background = colorOf(GoodsPocketVisualTokens.BACKGROUND),
    onBackground = colorOf(GoodsPocketVisualTokens.INK),
    surface = colorOf(GoodsPocketVisualTokens.SURFACE),
    onSurface = colorOf(GoodsPocketVisualTokens.INK),
    surfaceContainer = colorOf(GoodsPocketVisualTokens.SURFACE_LOW),
    surfaceContainerLow = colorOf(GoodsPocketVisualTokens.SURFACE_LOW),
    surfaceContainerHigh = colorOf(GoodsPocketVisualTokens.SURFACE_HIGH),
    surfaceContainerHighest = colorOf(GoodsPocketVisualTokens.SURFACE_TINT),
    surfaceVariant = colorOf(GoodsPocketVisualTokens.SURFACE_TINT),
    onSurfaceVariant = colorOf(GoodsPocketVisualTokens.MUTED_INK),
    outline = colorOf(GoodsPocketVisualTokens.OUTLINE),
    outlineVariant = colorOf(GoodsPocketVisualTokens.OUTLINE),
    secondary = colorOf(GoodsPocketVisualTokens.SECONDARY),
    onSecondary = colorOf(0xFF0F3B2AL),
    secondaryContainer = colorOf(GoodsPocketVisualTokens.SECONDARY_CONTAINER),
    onSecondaryContainer = colorOf(0xFF0F3B2AL),
    tertiary = colorOf(GoodsPocketVisualTokens.TERTIARY),
    onTertiary = colorOf(0xFFFFFFFFL),
    tertiaryContainer = colorOf(GoodsPocketVisualTokens.TERTIARY_CONTAINER),
    onTertiaryContainer = colorOf(0xFF352260L),
    error = colorOf(GoodsPocketVisualTokens.DANGER),
    onError = colorOf(0xFFFFFFFFL),
    errorContainer = colorOf(GoodsPocketVisualTokens.PRIMARY_CONTAINER),
    onErrorContainer = colorOf(GoodsPocketVisualTokens.INK),
)

private fun colorOf(value: Long): Color = Color(value)
