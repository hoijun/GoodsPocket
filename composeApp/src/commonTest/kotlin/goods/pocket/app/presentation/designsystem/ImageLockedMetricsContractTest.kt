package goods.pocket.app.presentation.designsystem

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        assertEquals(0.5.dp, ImageLockedNavigationMetrics.QuickAddShadowElevation)
        assertEquals((-13).dp, ImageLockedNavigationMetrics.ItemVerticalOffset)
        assertEquals(9.sp, ImageLockedNavigationMetrics.LabelFontSize)
        assertEquals(24.dp, ImageLockedNavigationMetrics.IconSize)
    }
}
