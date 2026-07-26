package goods.pocket.app.presentation.designsystem

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import goods.pocket.app.presentation.screen.CollectionReferenceMetrics
import goods.pocket.app.presentation.screen.EventsReferenceMetrics
import goods.pocket.app.presentation.screen.HomeReferenceMetrics
import kotlin.test.Test
import kotlin.test.assertEquals

class ImageLockedMetricsContractTest {

    @Test
    fun `home reference metrics keep the approved proportions`() {
        assertEquals(22.sp, HomeReferenceMetrics.BrandFontSize)
        assertEquals(13.sp, HomeReferenceMetrics.SummaryTitleFontSize)
        assertEquals(0.25.dp, HomeReferenceMetrics.CardShadowElevation)
        assertEquals(0.5.dp, HomeReferenceMetrics.RecentCardShadowElevation)
        assertEquals(96.dp, HomeReferenceMetrics.RecentArtworkHeight)
        assertEquals(9.sp, HomeReferenceMetrics.RecentSeriesFontSize)
        assertEquals(10.sp, HomeReferenceMetrics.RecentTitleFontSize)
        assertEquals(19.dp, HomeReferenceMetrics.SectionHeaderHeight)
        assertEquals(13.sp, HomeReferenceMetrics.SectionHeaderFontSize)
        assertEquals(40.dp, HomeReferenceMetrics.ScheduleThumbnailSize)
        assertEquals(120.dp, HomeReferenceMetrics.SpendingChartWidth)
        assertEquals(56.dp, HomeReferenceMetrics.SpendingChartHeight)
    }

    @Test
    fun `bottom navigation metrics keep the approved footprint`() {
        assertEquals(38.dp, ImageLockedNavigationMetrics.QuickAddDiameter)
        assertEquals(4.dp, ImageLockedNavigationMetrics.QuickAddTopPadding)
        assertEquals(0.5.dp, ImageLockedNavigationMetrics.QuickAddShadowElevation)
        assertEquals((-13).dp, ImageLockedNavigationMetrics.ItemVerticalOffset)
        assertEquals(9.sp, ImageLockedNavigationMetrics.LabelFontSize)
        assertEquals(24.dp, ImageLockedNavigationMetrics.IconSize)
    }

    @Test
    fun `collection owned reference metrics keep the approved grid geometry`() {
        assertEquals(16.dp, CollectionReferenceMetrics.ScreenHorizontalPadding)
        assertEquals(32.dp, CollectionReferenceMetrics.TopInsetReduction)
        assertEquals(16.dp, CollectionReferenceMetrics.TitleToSegmentSpacing)
        assertEquals(38.dp, CollectionReferenceMetrics.SegmentHeight)
        assertEquals(39.dp, CollectionReferenceMetrics.SearchHeight)
        assertEquals(19.dp, CollectionReferenceMetrics.SearchToCountSpacing)
        assertEquals(10.dp, CollectionReferenceMetrics.GridSpacing)
        assertEquals(201.dp, CollectionReferenceMetrics.GridTop)
        assertEquals(222.dp, CollectionReferenceMetrics.GoodsCardHeight)
        assertEquals(135.dp, CollectionReferenceMetrics.GoodsMediaHeight)
        assertEquals(66.dp, CollectionReferenceMetrics.SummaryHeight)
        assertEquals(0.5.dp, CollectionReferenceMetrics.CardShadowElevation)
        assertEquals(18.sp, CollectionReferenceMetrics.TitleFontSize)
        assertEquals(11.sp, CollectionReferenceMetrics.GoodsNameFontSize)
        assertEquals(9.sp, CollectionReferenceMetrics.GoodsMetadataFontSize)
    }

    @Test
    fun `events reference metrics keep the approved timeline geometry`() {
        assertEquals(19.dp, EventsReferenceMetrics.ScreenHorizontalPadding)
        assertEquals(31.dp, EventsReferenceMetrics.FilterHeight)
        assertEquals(25.dp, EventsReferenceMetrics.CountPillWidth)
        assertEquals(2.dp, EventsReferenceMetrics.CountPillOffsetY)
        assertEquals((-1).dp, EventsReferenceMetrics.CountTextOffsetY)
        assertEquals(166.dp, EventsReferenceMetrics.FeaturedCardHeight)
        assertEquals(26.dp, EventsReferenceMetrics.FeaturedTopPadding)
        assertEquals(10.dp, EventsReferenceMetrics.FeaturedBadgeToDateSpacing)
        assertEquals(10.dp, EventsReferenceMetrics.FeaturedDateToTitleSpacing)
        assertEquals(8.dp, EventsReferenceMetrics.FeaturedTitleToLocationSpacing)
        assertEquals(132.dp, EventsReferenceMetrics.TimelineCardHeight)
        assertEquals(66.dp, EventsReferenceMetrics.TimelineRowHeight)
        assertEquals(0.5.dp, EventsReferenceMetrics.CardShadowElevation)
        assertEquals(18.sp, EventsReferenceMetrics.PageTitleFontSize)
        assertEquals(20.sp, EventsReferenceMetrics.OverviewTitleFontSize)
        assertEquals(20.sp, EventsReferenceMetrics.FeaturedTitleFontSize)
    }
}
