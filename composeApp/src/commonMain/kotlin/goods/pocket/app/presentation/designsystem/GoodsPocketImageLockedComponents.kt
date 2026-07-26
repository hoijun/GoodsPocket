package goods.pocket.app.presentation.designsystem

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import goods.pocket.app.presentation.i18n.localizedLabel
import goods.pocket.app.presentation.navigation.AppDestination

@Composable
fun GoodsPocketImageLockedCard(
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val cardContent: @Composable () -> Unit = {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            content = content,
        )
    }
    if (onClick != null) {
        Surface(
            onClick = onClick,
            modifier = modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            color = containerColor,
            contentColor = MaterialTheme.colorScheme.onSurface,
            shadowElevation = 4.dp,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            content = cardContent,
        )
        return
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        color = containerColor,
        contentColor = MaterialTheme.colorScheme.onSurface,
        shadowElevation = 4.dp,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        content = cardContent,
    )
}

@Composable
fun GoodsPocketImageLockedBadge(
    text: String,
    modifier: Modifier = Modifier,
    tone: GoodsPocketBadgeTone = GoodsPocketBadgeTone.Neutral,
) {
    val colors = tone.colors()
    Surface(
        modifier = modifier,
        shape = CircleShape,
        color = colors.container,
        contentColor = colors.content,
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

enum class GoodsPocketBadgeTone {
    Neutral,
    Owned,
    Reserved,
    Event,
    Danger,
}

@Composable
fun GoodsPocketImageLockedSegmentedControl(
    options: List<String>,
    selectedIndex: Int,
    onSelectedIndexChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
    ) {
        Row(
            modifier = Modifier.padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            options.forEachIndexed { index, option ->
                val selected = index == selectedIndex
                Surface(
                    onClick = { onSelectedIndexChange(index) },
                    modifier = Modifier.weight(1f),
                    shape = CircleShape,
                    color = if (selected) MaterialTheme.colorScheme.primary else Color.Transparent,
                    contentColor = if (selected) {
                        MaterialTheme.colorScheme.onPrimary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                ) {
                    Text(
                        text = option,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 9.dp),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}

@Composable
fun GoodsPocketImageLockedSearchField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        placeholder = {
            Text(
                text = placeholder,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        },
        singleLine = true,
        shape = MaterialTheme.shapes.large,
        colors = goodsPocketOutlinedFieldColors(),
    )
}

@Composable
fun GoodsPocketImageLockedBottomBar(
    selectedPrimaryDestination: AppDestination,
    onSelectDestination: (AppDestination) -> Unit,
    onQuickAdd: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(81.dp),
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(81.dp)
                .align(Alignment.BottomCenter),
            shape = RoundedCornerShape(0.dp),
            color = Color(GoodsPocketVisualTokens.Background),
            tonalElevation = 0.dp,
            shadowElevation = 0.dp,
            border = BorderStroke(1.dp, Color(0xFFF3F0EE)),
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AppDestination.primaryDestinations.forEachIndexed { index, destination ->
                    if (index == 2) {
                        Spacer(modifier = Modifier.width(68.dp))
                    }
                    val label = destination.localizedLabel()
                    val selected = destination.route == selectedPrimaryDestination.route
                    GoodsPocketImageLockedBottomBarItem(
                        destination = destination,
                        label = label,
                        selected = selected,
                        onClick = { onSelectDestination(destination) },
                    )
                }
            }
        }
        GoodsPocketCenteredQuickAddButton(
            onClick = onQuickAdd,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = ImageLockedNavigationMetrics.QuickAddTopPadding),
        )
    }
}

@Composable
fun GoodsPocketCenteredQuickAddButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        onClick = onClick,
        modifier = modifier
            .offset(x = (-1).dp)
            .size(ImageLockedNavigationMetrics.QuickAddDiameter),
        shape = CircleShape,
        color = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        shadowElevation = ImageLockedNavigationMetrics.QuickAddShadowElevation,
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = "+",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 18.sp,
                    lineHeight = 22.sp,
                ),
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
private fun RowScope.GoodsPocketImageLockedBottomBarItem(
    destination: AppDestination,
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = Modifier
            .weight(1f)
            .offset(
                x = destination.referenceBottomNavOffset(),
                y = ImageLockedNavigationMetrics.ItemVerticalOffset,
            )
            .selectable(
                selected = selected,
                onClick = onClick,
                role = Role.Tab,
                interactionSource = interactionSource,
                indication = null,
            )
            .padding(top = 8.dp, bottom = 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(5.dp),
    ) {
        GoodsPocketBottomNavIcon(
            destination = destination,
            selected = selected,
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium.copy(
                fontSize = ImageLockedNavigationMetrics.LabelFontSize,
                lineHeight = 11.sp,
            ),
            color = if (selected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            },
        )
    }
}

private fun AppDestination.referenceBottomNavOffset() = when (this) {
    AppDestination.Home -> 2.dp
    AppDestination.Collection -> (-4).dp
    AppDestination.Events -> 2.dp
    AppDestination.My -> (-4).dp
    AppDestination.Settings -> 0.dp
}

private fun AppDestination.referenceBottomNavIconOffset() = when (this) {
    AppDestination.Home -> 0.dp
    AppDestination.Collection -> 2.dp
    AppDestination.Events -> 2.dp
    AppDestination.My -> 3.dp
    AppDestination.Settings -> 0.dp
}

@Composable
private fun GoodsPocketBottomNavIcon(
    destination: AppDestination,
    selected: Boolean,
) {
    val color = if (selected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }
    val stroke = Stroke(width = 3.2f)
    Canvas(
        modifier = Modifier
            .offset(y = destination.referenceBottomNavIconOffset())
            .size(ImageLockedNavigationMetrics.IconSize),
    ) {
        when (destination) {
            AppDestination.Home -> {
                val roof = Path().apply {
                    moveTo(size.width * 0.16f, size.height * 0.46f)
                    lineTo(size.width * 0.50f, size.height * 0.18f)
                    lineTo(size.width * 0.84f, size.height * 0.46f)
                    lineTo(size.width * 0.78f, size.height * 0.46f)
                    lineTo(size.width * 0.78f, size.height * 0.82f)
                    lineTo(size.width * 0.60f, size.height * 0.82f)
                    lineTo(size.width * 0.60f, size.height * 0.60f)
                    lineTo(size.width * 0.40f, size.height * 0.60f)
                    lineTo(size.width * 0.40f, size.height * 0.82f)
                    lineTo(size.width * 0.22f, size.height * 0.82f)
                    lineTo(size.width * 0.22f, size.height * 0.46f)
                    close()
                }
                if (selected) {
                    drawPath(path = roof, color = color)
                } else {
                    drawPath(path = roof, color = color, style = stroke)
                }
            }
            AppDestination.Collection -> {
                val cube = Path().apply {
                    moveTo(size.width * 0.50f, size.height * 0.14f)
                    lineTo(size.width * 0.82f, size.height * 0.30f)
                    lineTo(size.width * 0.82f, size.height * 0.68f)
                    lineTo(size.width * 0.50f, size.height * 0.86f)
                    lineTo(size.width * 0.18f, size.height * 0.68f)
                    lineTo(size.width * 0.18f, size.height * 0.30f)
                    close()
                }
                drawPath(path = cube, color = color, style = stroke)
                drawLine(
                    color = color,
                    start = Offset(size.width * 0.18f, size.height * 0.30f),
                    end = Offset(size.width * 0.50f, size.height * 0.47f),
                    strokeWidth = 3.2f,
                )
                drawLine(
                    color = color,
                    start = Offset(size.width * 0.82f, size.height * 0.30f),
                    end = Offset(size.width * 0.50f, size.height * 0.47f),
                    strokeWidth = 3.2f,
                )
                drawLine(
                    color = color,
                    start = Offset(size.width * 0.50f, size.height * 0.47f),
                    end = Offset(size.width * 0.50f, size.height * 0.86f),
                    strokeWidth = 3.2f,
                )
            }
            AppDestination.Events -> {
                drawRoundRect(
                    color = color,
                    topLeft = Offset(size.width * 0.18f, size.height * 0.24f),
                    size = Size(size.width * 0.64f, size.height * 0.58f),
                    cornerRadius = CornerRadius(5f, 5f),
                    style = stroke,
                )
                drawLine(
                    color = color,
                    start = Offset(size.width * 0.18f, size.height * 0.42f),
                    end = Offset(size.width * 0.82f, size.height * 0.42f),
                    strokeWidth = 3.2f,
                )
                drawLine(
                    color = color,
                    start = Offset(size.width * 0.34f, size.height * 0.14f),
                    end = Offset(size.width * 0.34f, size.height * 0.30f),
                    strokeWidth = 3.2f,
                )
                drawLine(
                    color = color,
                    start = Offset(size.width * 0.66f, size.height * 0.14f),
                    end = Offset(size.width * 0.66f, size.height * 0.30f),
                    strokeWidth = 3.2f,
                )
            }
            AppDestination.My -> {
                drawCircle(
                    color = color,
                    radius = size.minDimension * 0.16f,
                    center = Offset(size.width * 0.50f, size.height * 0.28f),
                    style = stroke,
                )
                val shoulders = Path().apply {
                    moveTo(size.width * 0.22f, size.height * 0.84f)
                    cubicTo(
                        size.width * 0.28f,
                        size.height * 0.60f,
                        size.width * 0.72f,
                        size.height * 0.60f,
                        size.width * 0.78f,
                        size.height * 0.84f,
                    )
                }
                drawPath(path = shoulders, color = color, style = stroke)
            }
            AppDestination.Settings -> Unit
        }
    }
}

@Composable
private fun GoodsPocketBadgeTone.colors(): BadgeColors {
    return when (this) {
        GoodsPocketBadgeTone.Neutral -> BadgeColors(
            container = MaterialTheme.colorScheme.surfaceContainerLow,
            content = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        GoodsPocketBadgeTone.Owned -> BadgeColors(
            container = MaterialTheme.colorScheme.secondaryContainer,
            content = MaterialTheme.colorScheme.onSecondaryContainer,
        )
        GoodsPocketBadgeTone.Reserved -> BadgeColors(
            container = MaterialTheme.colorScheme.primaryContainer,
            content = MaterialTheme.colorScheme.primary,
        )
        GoodsPocketBadgeTone.Event -> BadgeColors(
            container = MaterialTheme.colorScheme.tertiaryContainer,
            content = MaterialTheme.colorScheme.onTertiaryContainer,
        )
        GoodsPocketBadgeTone.Danger -> BadgeColors(
            container = MaterialTheme.colorScheme.errorContainer,
            content = MaterialTheme.colorScheme.error,
        )
    }
}

private data class BadgeColors(
    val container: Color,
    val content: Color,
)
