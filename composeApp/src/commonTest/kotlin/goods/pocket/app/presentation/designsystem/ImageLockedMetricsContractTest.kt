package goods.pocket.app.presentation.designsystem

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import goods.pocket.app.presentation.screen.CollectionReferenceMetrics
import goods.pocket.app.presentation.screen.EventsReferenceMetrics
import goods.pocket.app.presentation.screen.HomeReferenceMetrics
import goods.pocket.app.presentation.screen.MyReferenceMetrics
import goods.pocket.app.presentation.screen.SettingsReferenceMetrics
import goods.pocket.app.presentation.screen.SettingsReferenceColors
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

    @Test
    fun `my reference metrics keep the approved profile and summary geometry`() {
        assertEquals(15.dp, MyReferenceMetrics.ScreenHorizontalPadding)
        assertEquals(32.dp, MyReferenceMetrics.TopInsetReduction)
        assertEquals(27.dp, MyReferenceMetrics.PageTitleTopPadding)
        assertEquals(13.dp, MyReferenceMetrics.TitleToProfileSpacing)
        assertEquals(120.dp, MyReferenceMetrics.ProfileCardHeight)
        assertEquals(76.dp, MyReferenceMetrics.AvatarSize)
        assertEquals(29.dp, MyReferenceMetrics.ProfileToSummaryTitleSpacing)
        assertEquals(10.dp, MyReferenceMetrics.SectionTitleToCardSpacing)
        assertEquals(190.dp, MyReferenceMetrics.SummaryCardHeight)
        assertEquals(95.dp, MyReferenceMetrics.SummaryCellHeight)
        assertEquals(30.dp, MyReferenceMetrics.SummaryToManagementTitleSpacing)
        assertEquals(8.dp, MyReferenceMetrics.ManagementTitleToCardSpacing)
        assertEquals(70.dp, MyReferenceMetrics.ManagementRowHeight)
        assertEquals(210.dp, MyReferenceMetrics.ManagementCardHeight)
        assertEquals(0.5.dp, MyReferenceMetrics.CardShadowElevation)
        assertEquals(18.sp, MyReferenceMetrics.PageTitleFontSize)
        assertEquals(15.sp, MyReferenceMetrics.SectionTitleFontSize)
        assertEquals(18.sp, MyReferenceMetrics.ProfileNameFontSize)
        assertEquals(20.sp, MyReferenceMetrics.SummaryValueFontSize)
        assertEquals(13.sp, MyReferenceMetrics.ManagementTitleFontSize)
    }

    @Test
    fun `settings reference metrics keep the approved grouped row geometry`() {
        assertEquals(16.dp, SettingsReferenceMetrics.ScreenHorizontalPadding)
        assertEquals(31.dp, SettingsReferenceMetrics.HeaderToFirstSectionSpacing)
        assertEquals(10.dp, SettingsReferenceMetrics.SectionTitleToCardSpacing)
        assertEquals(72.dp, SettingsReferenceMetrics.LanguageCardHeight)
        assertEquals(167.dp, SettingsReferenceMetrics.LanguageSegmentWidth)
        assertEquals(31.dp, SettingsReferenceMetrics.LanguageSegmentHeight)
        assertEquals(29.dp, SettingsReferenceMetrics.LanguageCardToFormatTitleSpacing)
        assertEquals(120.dp, SettingsReferenceMetrics.FormatCardHeight)
        assertEquals(60.dp, SettingsReferenceMetrics.FormatRowHeight)
        assertEquals(31.dp, SettingsReferenceMetrics.FormatIconSize)
        assertEquals(17.dp, SettingsReferenceMetrics.FormatIconToTextSpacing)
        assertEquals(21.dp, SettingsReferenceMetrics.SectionTitleHorizontalPadding)
        assertEquals(0.5.dp, SettingsReferenceMetrics.CardShadowElevation)
        assertEquals(18.sp, SettingsReferenceMetrics.PageTitleFontSize)
        assertEquals(14.sp, SettingsReferenceMetrics.SectionTitleFontSize)
        assertEquals(14.sp, SettingsReferenceMetrics.LanguageLabelFontSize)
        assertEquals(12.sp, SettingsReferenceMetrics.RowTextFontSize)
        assertEquals(Color(0xFFE5E3E2), SettingsReferenceColors.ControlOutline)
    }
}
