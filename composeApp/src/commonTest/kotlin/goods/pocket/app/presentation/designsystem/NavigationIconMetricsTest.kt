package goods.pocket.app.presentation.designsystem

import androidx.compose.ui.unit.Density
import kotlin.test.Test
import kotlin.test.assertEquals

class NavigationIconMetricsTest {
    @Test
    fun referenceDensityKeepsOriginalStrokeAndCornerRadiusPixels() {
        with(Density(3f)) {
            assertEquals(3.2f, NavigationIconDrawingMetrics.StrokeWidth.toPx(), 0.0001f)
            assertEquals(5f, NavigationIconDrawingMetrics.CornerRadius.toPx(), 0.0001f)
        }
    }

    @Test
    fun strokeAndCornerRadiusKeepSameLogicalSizeAcrossDensities() {
        listOf(1f, 2f, 2.625f, 3f, 4f).forEach { density ->
            with(Density(density)) {
                assertEquals(
                    3.2f / 3f,
                    NavigationIconDrawingMetrics.StrokeWidth.toPx() / density,
                    0.0001f,
                )
                assertEquals(
                    5f / 3f,
                    NavigationIconDrawingMetrics.CornerRadius.toPx() / density,
                    0.0001f,
                )
            }
        }
    }
}
