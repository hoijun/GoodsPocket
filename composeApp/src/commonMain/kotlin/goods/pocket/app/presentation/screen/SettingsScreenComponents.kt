package goods.pocket.app.presentation.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
internal fun SettingsPageHeader(
    title: String,
    backLabel: String,
    onBack: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(SettingsReferenceMetrics.HeaderHeight),
    ) {
        Text(
            text = title,
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (-2).dp),
            color = SettingsReferenceColors.Ink,
            fontSize = SettingsReferenceMetrics.PageTitleFontSize,
            lineHeight = SettingsReferenceMetrics.PageTitleLineHeight,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .offset(x = 3.dp, y = (-3).dp)
                .size(SettingsReferenceMetrics.BackTouchSize)
                .semantics { contentDescription = backLabel }
                .clickable(role = Role.Button, onClick = onBack),
            contentAlignment = Alignment.Center,
        ) {
            SettingsLineIcon(
                kind = SettingsLineIconKind.Back,
                color = SettingsReferenceColors.Ink,
                modifier = Modifier.size(SettingsReferenceMetrics.BackIconSize),
            )
        }
    }
}

@Composable
internal fun SettingsSectionHeader(
    title: String,
    topSpacing: Dp,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(topSpacing))
        Text(
            text = title,
            modifier = Modifier
                .padding(horizontal = SettingsReferenceMetrics.SectionTitleHorizontalPadding)
                .offset(y = (-1).dp),
            color = SettingsReferenceColors.Ink,
            fontSize = SettingsReferenceMetrics.SectionTitleFontSize,
            lineHeight = SettingsReferenceMetrics.SectionTitleLineHeight,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(SettingsReferenceMetrics.SectionTitleToCardSpacing))
    }
}

@Composable
internal fun SettingsLanguageCard(
    label: String,
    koreanLabel: String,
    englishLabel: String,
    selectedLanguageCode: String,
    koreanCode: String,
    englishCode: String,
    onLanguageChange: (String) -> Unit,
) {
    SettingsReferenceSurface(
        modifier = Modifier.height(SettingsReferenceMetrics.LanguageCardHeight),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = SettingsReferenceMetrics.LanguageHorizontalPadding,
                    end = SettingsReferenceMetrics.ScreenHorizontalPadding,
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = label,
                modifier = Modifier.offset(x = (-1).dp, y = 1.dp),
                color = SettingsReferenceColors.Ink,
                fontSize = SettingsReferenceMetrics.LanguageLabelFontSize,
                lineHeight = SettingsReferenceMetrics.LanguageLabelLineHeight,
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(modifier = Modifier.weight(1f))
            SettingsLanguageSegment(
                koreanLabel = koreanLabel,
                englishLabel = englishLabel,
                selectedLanguageCode = selectedLanguageCode,
                koreanCode = koreanCode,
                englishCode = englishCode,
                onLanguageChange = onLanguageChange,
            )
        }
    }
}

@Composable
private fun SettingsLanguageSegment(
    koreanLabel: String,
    englishLabel: String,
    selectedLanguageCode: String,
    koreanCode: String,
    englishCode: String,
    onLanguageChange: (String) -> Unit,
) {
    val shape = RoundedCornerShape(SettingsReferenceMetrics.LanguageSegmentRadius)
    Box(
        modifier = Modifier
            .width(SettingsReferenceMetrics.LanguageSegmentWidth)
            .height(SettingsReferenceMetrics.LanguageSegmentTouchHeight)
            .selectableGroup(),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(SettingsReferenceMetrics.LanguageSegmentHeight)
                .clip(shape)
                .background(SettingsReferenceColors.Card)
                .border(1.dp, SettingsReferenceColors.ControlOutline, shape),
        ) {
            SettingsLanguageOptionVisual(
                label = koreanLabel,
                selected = selectedLanguageCode == koreanCode,
                modifier = Modifier.weight(1f),
            )
            SettingsLanguageOptionVisual(
                label = englishLabel,
                selected = selectedLanguageCode == englishCode,
                modifier = Modifier.weight(1f),
            )
        }
        Row(modifier = Modifier.fillMaxSize()) {
            SettingsLanguageOptionTouchTarget(
                label = koreanLabel,
                selected = selectedLanguageCode == koreanCode,
                onClick = { onLanguageChange(koreanCode) },
                modifier = Modifier.weight(1f),
            )
            SettingsLanguageOptionTouchTarget(
                label = englishLabel,
                selected = selectedLanguageCode == englishCode,
                onClick = { onLanguageChange(englishCode) },
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun SettingsLanguageOptionVisual(
    label: String,
    selected: Boolean,
    modifier: Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .background(if (selected) SettingsReferenceColors.Primary else Color.Transparent),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            color = if (selected) SettingsReferenceColors.SelectedContent else SettingsReferenceColors.Muted,
            fontSize = SettingsReferenceMetrics.LanguageSegmentFontSize,
            lineHeight = SettingsReferenceMetrics.LanguageSegmentLineHeight,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
        )
    }
}

@Composable
private fun SettingsLanguageOptionTouchTarget(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .selectable(
                selected = selected,
                role = Role.RadioButton,
                onClick = onClick,
            )
            .semantics { contentDescription = label },
    )
}

@Composable
internal fun SettingsFormatCard(
    currency: String,
    dateFormat: String,
) {
    SettingsReferenceSurface(
        modifier = Modifier.height(SettingsReferenceMetrics.FormatCardHeight),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            SettingsFormatRow(
                text = currency,
                icon = SettingsLineIconKind.Currency,
                showDivider = true,
            )
            SettingsFormatRow(
                text = dateFormat,
                icon = SettingsLineIconKind.Calendar,
                showDivider = false,
            )
        }
    }
}

@Composable
private fun SettingsFormatRow(
    text: String,
    icon: SettingsLineIconKind,
    showDivider: Boolean,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(SettingsReferenceMetrics.FormatRowHeight),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = SettingsReferenceMetrics.FormatHorizontalPadding)
                .offset(y = 2.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(SettingsReferenceMetrics.FormatIconSize)
                    .clip(RoundedCornerShape(SettingsReferenceMetrics.FormatIconRadius))
                    .border(
                        width = 1.dp,
                        color = SettingsReferenceColors.ControlOutline,
                        shape = RoundedCornerShape(SettingsReferenceMetrics.FormatIconRadius),
                    ),
                contentAlignment = Alignment.Center,
            ) {
                SettingsLineIcon(
                    kind = icon,
                    color = SettingsReferenceColors.Muted,
                    modifier = Modifier.size(20.dp),
                )
            }
            Spacer(modifier = Modifier.width(SettingsReferenceMetrics.FormatIconToTextSpacing))
            Text(
                text = text,
                color = SettingsReferenceColors.Ink,
                fontSize = SettingsReferenceMetrics.RowTextFontSize,
                lineHeight = SettingsReferenceMetrics.RowTextLineHeight,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
            )
        }
        if (showDivider) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = SettingsReferenceMetrics.DividerInset)
                    .height(1.dp)
                    .background(SettingsReferenceColors.Outline),
            )
        }
    }
}

@Composable
private fun SettingsReferenceSurface(
    modifier: Modifier,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = SettingsReferenceMetrics.ScreenHorizontalPadding),
        shape = RoundedCornerShape(SettingsReferenceMetrics.CardRadius),
        color = SettingsReferenceColors.Card,
        contentColor = SettingsReferenceColors.Ink,
        border = BorderStroke(1.dp, SettingsReferenceColors.Outline),
        shadowElevation = SettingsReferenceMetrics.CardShadowElevation,
        content = content,
    )
}

internal object SettingsReferenceColors {
    val Primary = Color(0xFFFF7445)
    val Ink = Color(0xFF202838)
    val Muted = Color(0xFF8A8F9B)
    val Card = Color(0xFFFEFBF8)
    val Outline = Color(0xFFEFEDEC)
    val ControlOutline = Color(0xFFE5E3E2)
    val SelectedContent = Color(0xFFFFFFFF)
}
