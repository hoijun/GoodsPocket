package goods.pocket.app.presentation.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import goods.pocket.app.presentation.designsystem.GoodsPocketVisualTokens

internal data class MySummaryItem(
    val label: String,
    val value: String,
    val tone: MySummaryTone,
    val icon: MyReferenceIconKind,
)

internal enum class MySummaryTone {
    Owned,
    Reserved,
    Spending,
    Upcoming,
}

internal data class MyManagementItem(
    val action: MyHubAction,
    val title: String,
    val subtitle: String,
    val icon: MyReferenceIconKind,
)

@Composable
internal fun MyPageHeader(title: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(MyReferenceMetrics.PageTitleTopPadding))
        Text(
            text = title,
            modifier = Modifier.fillMaxWidth(),
            color = MyColors.Ink,
            fontSize = MyReferenceMetrics.PageTitleFontSize,
            lineHeight = MyReferenceMetrics.PageTitleLineHeight,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(MyReferenceMetrics.TitleToProfileSpacing))
    }
}

@Composable
internal fun MyProfileCard(
    displayName: String,
    syncStatus: String,
    hint: String,
) {
    MyReferenceSurface(
        modifier = Modifier.height(MyReferenceMetrics.ProfileCardHeight),
        radius = MyReferenceMetrics.ProfileCardRadius,
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = MyReferenceMetrics.ProfileHorizontalPadding),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(MyReferenceMetrics.AvatarSize)
                    .clip(CircleShape)
                    .background(MyColors.Avatar),
            )
            Spacer(modifier = Modifier.width(MyReferenceMetrics.AvatarToTextSpacing))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = displayName,
                    color = MyColors.Ink,
                    fontSize = MyReferenceMetrics.ProfileNameFontSize,
                    lineHeight = MyReferenceMetrics.ProfileNameLineHeight,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.height(MyReferenceMetrics.ProfileNameToStatusSpacing))
                Surface(
                    modifier = Modifier.height(MyReferenceMetrics.ProfileStatusHeight),
                    color = MyColors.NeutralContainer,
                    shape = RoundedCornerShape(8.dp),
                ) {
                    Box(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = syncStatus,
                            color = MyColors.Muted,
                            fontSize = MyReferenceMetrics.ProfileStatusFontSize,
                            lineHeight = MyReferenceMetrics.ProfileStatusLineHeight,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1,
                        )
                    }
                }
                Spacer(modifier = Modifier.height(MyReferenceMetrics.ProfileStatusToHintSpacing))
                Text(
                    text = hint,
                    color = MyColors.Muted,
                    fontSize = MyReferenceMetrics.ProfileHintFontSize,
                    lineHeight = MyReferenceMetrics.ProfileHintLineHeight,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
internal fun MySectionHeader(
    title: String,
    topSpacing: Dp,
    bottomSpacing: Dp,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(topSpacing))
        Text(
            text = title,
            modifier = Modifier.padding(horizontal = 20.dp),
            color = MyColors.Ink,
            fontSize = MyReferenceMetrics.SectionTitleFontSize,
            lineHeight = MyReferenceMetrics.SectionTitleLineHeight,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(bottomSpacing))
    }
}

@Composable
internal fun MySummaryCard(items: List<MySummaryItem>) {
    require(items.size == 4)

    MyReferenceSurface(
        modifier = Modifier.height(MyReferenceMetrics.SummaryCardHeight),
        radius = MyReferenceMetrics.SummaryCardRadius,
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(modifier = Modifier.height(MyReferenceMetrics.SummaryCellHeight)) {
                    MySummaryCell(item = items[0], modifier = Modifier.weight(1f))
                    MySummaryCell(item = items[1], modifier = Modifier.weight(1f))
                }
                Row(modifier = Modifier.height(MyReferenceMetrics.SummaryCellHeight)) {
                    MySummaryCell(item = items[2], modifier = Modifier.weight(1f))
                    MySummaryCell(item = items[3], modifier = Modifier.weight(1f))
                }
            }
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .width(1.dp)
                    .height(166.dp)
                    .background(MyColors.Outline),
            )
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth()
                    .padding(horizontal = MyReferenceMetrics.DividerInset)
                    .height(1.dp)
                    .background(MyColors.Outline),
            )
        }
    }
}

@Composable
private fun MySummaryCell(
    item: MySummaryItem,
    modifier: Modifier = Modifier,
) {
    val colors = item.tone.colors()
    Row(
        modifier = modifier
            .fillMaxHeight()
            .padding(start = 19.dp, end = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Surface(
            modifier = Modifier.size(MyReferenceMetrics.SummaryIconSize),
            shape = CircleShape,
            color = colors.container,
        ) {
            Box(contentAlignment = Alignment.Center) {
                MyReferenceIcon(
                    kind = item.icon,
                    color = colors.content,
                    modifier = Modifier.size(22.dp),
                )
            }
        }
        Spacer(modifier = Modifier.width(MyReferenceMetrics.SummaryIconToTextSpacing))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.label,
                color = MyColors.Muted,
                fontSize = MyReferenceMetrics.SummaryLabelFontSize,
                lineHeight = MyReferenceMetrics.SummaryLabelLineHeight,
                maxLines = 1,
            )
            Spacer(modifier = Modifier.height(MyReferenceMetrics.SummaryLabelToValueSpacing))
            Text(
                text = item.value,
                color = colors.content,
                fontSize = MyReferenceMetrics.SummaryValueFontSize,
                lineHeight = MyReferenceMetrics.SummaryValueLineHeight,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
internal fun MyManagementCard(
    items: List<MyManagementItem>,
    onOpenSettings: () -> Unit,
) {
    require(items.size == 3)

    MyReferenceSurface(
        modifier = Modifier.height(MyReferenceMetrics.ManagementCardHeight),
        radius = MyReferenceMetrics.ManagementCardRadius,
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            items.forEachIndexed { index, item ->
                MyManagementRow(
                    item = item,
                    showDivider = index < items.lastIndex,
                    onClick = if (item.action == MyHubAction.SETTINGS) onOpenSettings else null,
                )
            }
        }
    }
}

@Composable
private fun MyManagementRow(
    item: MyManagementItem,
    showDivider: Boolean,
    onClick: (() -> Unit)?,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(MyReferenceMetrics.ManagementRowHeight)
            .then(if (onClick == null) Modifier else Modifier.clickable(onClick = onClick)),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = MyReferenceMetrics.ManagementHorizontalPadding),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(
                modifier = Modifier.size(MyReferenceMetrics.ManagementIconSize),
                color = MyColors.IconContainer,
                shape = RoundedCornerShape(10.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    MyReferenceIcon(
                        kind = item.icon,
                        color = MyColors.Ink,
                        modifier = Modifier.size(24.dp),
                    )
                }
            }
            Spacer(modifier = Modifier.width(MyReferenceMetrics.ManagementIconToTextSpacing))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    color = MyColors.Ink,
                    fontSize = MyReferenceMetrics.ManagementTitleFontSize,
                    lineHeight = MyReferenceMetrics.ManagementTitleLineHeight,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                )
                Spacer(modifier = Modifier.height(MyReferenceMetrics.ManagementTextSpacing))
                Text(
                    text = item.subtitle,
                    color = MyColors.Muted,
                    fontSize = MyReferenceMetrics.ManagementSubtitleFontSize,
                    lineHeight = MyReferenceMetrics.ManagementSubtitleLineHeight,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            if (onClick != null) {
                MyReferenceIcon(
                    kind = MyReferenceIconKind.Chevron,
                    color = MyColors.Muted,
                    modifier = Modifier
                        .size(18.dp)
                        .offset(x = 1.dp),
                )
            }
        }
        if (showDivider) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = MyReferenceMetrics.DividerInset)
                    .height(1.dp)
                    .background(MyColors.Outline),
            )
        }
    }
}

@Composable
private fun MyReferenceSurface(
    modifier: Modifier,
    radius: Dp,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = MyReferenceMetrics.ScreenHorizontalPadding),
        shape = RoundedCornerShape(radius),
        color = MyColors.Card,
        contentColor = MyColors.Ink,
        border = BorderStroke(1.dp, MyColors.Outline),
        shadowElevation = MyReferenceMetrics.CardShadowElevation,
        content = content,
    )
}

@Composable
private fun MySummaryTone.colors(): MyToneColors {
    return when (this) {
        MySummaryTone.Owned -> MyToneColors(MyColors.OwnedContainer, MyColors.Owned)
        MySummaryTone.Reserved -> MyToneColors(MyColors.ReservedContainer, MyColors.Reserved)
        MySummaryTone.Spending -> MyToneColors(MyColors.SpendingContainer, MyColors.Primary)
        MySummaryTone.Upcoming -> MyToneColors(MyColors.UpcomingContainer, MyColors.Warning)
    }
}

private object MyColors {
    val Primary = Color(GoodsPocketVisualTokens.Primary)
    val Owned = Color(GoodsPocketVisualTokens.Secondary)
    val Reserved = Color(GoodsPocketVisualTokens.Tertiary)
    val Warning = Color(GoodsPocketVisualTokens.Warning)
    val Ink = Color(GoodsPocketVisualTokens.Ink)
    val Muted = Color(GoodsPocketVisualTokens.MutedInk)
    val Card = Color(0xFFFEFBF8)
    val Outline = Color(0xFFEFEDEC)
    val Avatar = Color(0xFFD7D2CC)
    val NeutralContainer = Color(0xFFF3F0ED)
    val IconContainer = Color(0xFFF4F2F0)
    val OwnedContainer = Color(0xFFE5F8ED)
    val ReservedContainer = Color(0xFFEDE8FF)
    val SpendingContainer = Color(0xFFFFECE3)
    val UpcomingContainer = Color(0xFFFFF4DE)
}

private data class MyToneColors(
    val container: Color,
    val content: Color,
)
