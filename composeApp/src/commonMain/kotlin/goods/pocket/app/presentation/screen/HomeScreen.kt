package goods.pocket.app.presentation.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import goods.pocket.app.domain.model.Event
import goods.pocket.app.domain.model.HomeSummary
import goods.pocket.app.presentation.designsystem.GoodsPocketVisualTokens
import goods.pocket.app.presentation.designsystem.goodsPocketScreenModifier
import goods.pocket.app.presentation.i18n.tr
import goodspocket.composeapp.generated.resources.Res
import goodspocket.composeapp.generated.resources.home_brand_title
import goodspocket.composeapp.generated.resources.home_hero_body
import goodspocket.composeapp.generated.resources.home_hero_title
import goodspocket.composeapp.generated.resources.home_summary_owned
import goodspocket.composeapp.generated.resources.home_summary_reserved
import goodspocket.composeapp.generated.resources.home_summary_total_goods
import goodspocket.composeapp.generated.resources.home_today_summary_title

internal val HomeOrange = Color(GoodsPocketVisualTokens.Primary)
internal val HomeGreen = Color(GoodsPocketVisualTokens.Secondary)
internal val HomePurple = Color(GoodsPocketVisualTokens.Tertiary)
internal val HomeInk = Color(GoodsPocketVisualTokens.Ink)
internal val HomeMuted = Color(GoodsPocketVisualTokens.MutedInk)
internal val HomeCardSurface = Color(0xFFFEFBF8)
internal val HomeHeroSurface = Color(0xFFFEF9F5)
internal val HomeCardBorder = Color(0xFFEFEDEC)

@Composable
fun HomeScreen(
    dashboardSummary: HomeSummary,
    upcomingEvents: List<Event>,
    currentDate: String,
    onAction: (HomeAction) -> Unit,
) {
    LazyColumn(
        modifier = goodsPocketScreenModifier(),
        contentPadding = PaddingValues(top = 2.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(HomeReferenceMetrics.SectionSpacing),
    ) {
        item {
            HomeBrandHeader(
                showNotificationDot = upcomingEvents.isNotEmpty(),
                onNotificationsClick = { onAction(HomeAction.OpenScheduleOverview) },
            )
        }
        item { HomeHeroCard(onClick = { onAction(HomeAction.OpenQuickAdd) }) }
        item {
            HomeTodaySummaryCard(
                dashboardSummary = dashboardSummary,
                onAction = onAction,
            )
        }
        item {
            HomeRecentGoodsCarousel(
                activities = dashboardSummary.recentActivities,
                onEntryClick = { onAction(HomeAction.OpenRecentEntry(it)) },
                onViewAll = { onAction(HomeAction.OpenRecentCollection) },
            )
        }
        item {
            HomeMonthlySpendCard(
                dashboardSummary = dashboardSummary,
                currentDate = currentDate,
            )
        }
        item {
            HomeUpcomingScheduleCard(
                events = upcomingEvents,
                currentDate = currentDate,
                onCardClick = { onAction(HomeAction.OpenScheduleOverview) },
                onEventClick = { onAction(HomeAction.OpenEvent(it)) },
            )
        }
    }
}

@Composable
private fun HomeBrandHeader(
    showNotificationDot: Boolean,
    onNotificationsClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 0.dp, bottom = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = tr(Res.string.home_brand_title),
            modifier = Modifier
                .graphicsLayer(
                    scaleX = 0.923f,
                    scaleY = 1.125f,
                    transformOrigin = TransformOrigin(0f, 1f),
                )
                .offset(x = (-1).dp, y = 4.dp),
            style = MaterialTheme.typography.displayLarge.copy(
                fontSize = HomeReferenceMetrics.BrandFontSize,
                lineHeight = 26.sp,
            ),
            color = HomeOrange,
            fontWeight = FontWeight.ExtraBold,
        )
        HomeBellButton(
            showNotificationDot = showNotificationDot,
            onClick = onNotificationsClick,
        )
    }
}

@Composable
private fun HomeBellButton(
    showNotificationDot: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(38.dp)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        HomeBellGlyph(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .graphicsLayer(
                    scaleX = 1.125f,
                    scaleY = 0.955f,
                )
                .offset(y = 1.5.dp),
        )
        if (showNotificationDot) {
            Surface(
                modifier = Modifier
                    .size(6.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = (-3).dp, y = 10.dp),
                shape = CircleShape,
                color = HomeOrange,
            ) {}
        }
    }
}

@Composable
private fun HomeHeroCard(
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = (-5).dp)
            .height(120.dp),
        shape = RoundedCornerShape(18.dp),
        color = HomeHeroSurface,
        contentColor = HomeInk,
        shadowElevation = 0.dp,
        border = BorderStroke(1.dp, HomeCardBorder),
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            HomeHeroMediaPlaceholder(
                modifier = Modifier.fillMaxSize(),
            )
            Column(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 18.dp, end = 126.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = tr(Res.string.home_hero_title),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 15.sp,
                        lineHeight = 20.sp,
                    ),
                    color = HomeInk,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = tr(Res.string.home_hero_body),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 12.sp,
                        lineHeight = 19.sp,
                    ),
                    color = HomeMuted,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
private fun HomeTodaySummaryCard(
    dashboardSummary: HomeSummary,
    onAction: (HomeAction) -> Unit,
) {
    HomeWhiteCard(
        modifier = Modifier
            .offset(y = (-5).dp)
            .height(98.dp),
        contentPadding = 0.dp,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Text(
                text = tr(Res.string.home_today_summary_title),
                modifier = Modifier.padding(start = 13.dp, top = 10.dp),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = HomeReferenceMetrics.SummaryTitleFontSize,
                    lineHeight = 17.sp,
                ),
                color = HomeInk,
                fontWeight = FontWeight.Bold,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                val totalCount = dashboardSummary.ownedItemCount + dashboardSummary.activePreorderCount
                HomeCollectionMetric(
                    label = tr(Res.string.home_summary_total_goods),
                    value = totalCount.toString(),
                    color = HomeInk,
                    modifier = Modifier.weight(1f),
                    onClick = { onAction(HomeAction.OpenAllCollection) },
                )
                HomeMetricDivider()
                HomeCollectionMetric(
                    label = tr(Res.string.home_summary_owned),
                    value = dashboardSummary.ownedItemCount.toString(),
                    color = HomeGreen,
                    modifier = Modifier.weight(1f),
                    onClick = { onAction(HomeAction.OpenOwnedCollection) },
                )
                HomeMetricDivider()
                HomeCollectionMetric(
                    label = tr(Res.string.home_summary_reserved),
                    value = dashboardSummary.activePreorderCount.toString(),
                    color = HomeOrange,
                    modifier = Modifier.weight(1f),
                    onClick = { onAction(HomeAction.OpenReservedCollection) },
                )
            }
        }
    }
}

@Composable
internal fun HomeWhiteCard(
    modifier: Modifier = Modifier,
    contentPadding: androidx.compose.ui.unit.Dp = 16.dp,
    containerColor: Color = HomeCardSurface,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val cardContent: @Composable () -> Unit = {
        Column(
            modifier = Modifier.padding(contentPadding),
            verticalArrangement = Arrangement.spacedBy(0.dp),
            content = content,
        )
    }
    if (onClick != null) {
        Surface(
            onClick = onClick,
            modifier = modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = containerColor,
            contentColor = HomeInk,
            shadowElevation = HomeReferenceMetrics.CardShadowElevation,
            border = BorderStroke(1.dp, HomeCardBorder),
            content = cardContent,
        )
    } else {
        Surface(
            modifier = modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = containerColor,
            contentColor = HomeInk,
            shadowElevation = HomeReferenceMetrics.CardShadowElevation,
            border = BorderStroke(1.dp, HomeCardBorder),
            content = cardContent,
        )
    }
}

@Composable
internal fun HomeCollectionMetric(
    label: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(vertical = 0.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(5.dp),
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge.copy(
                fontSize = 10.sp,
                lineHeight = 14.sp,
            ),
            color = HomeMuted,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontSize = 20.sp,
                lineHeight = 24.sp,
            ),
            color = color,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
internal fun HomeMetricDivider() {
    Spacer(
        modifier = Modifier
            .width(1.dp)
            .height(40.dp)
            .background(HomeCardBorder),
    )
}
