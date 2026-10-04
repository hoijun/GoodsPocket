package goods.pocket.app.presentation.designsystem

import androidx.compose.ui.unit.dp
import goods.pocket.app.presentation.collection.CollectionReferenceMetrics
import goods.pocket.app.presentation.events.EventsReferenceMetrics
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SelectionPaintMetricsTest {
    @Test
    fun collectionPaintPreservesEqualSlotsAndCenteredLabel() {
        val controlWidth = 393.dp - CollectionReferenceMetrics.ScreenHorizontalPadding * 2
        val slotWidth = (controlWidth - CollectionReferenceMetrics.SegmentInnerPadding * 2) / 3
        val horizontalInset = CollectionReferenceMetrics.SelectionPaintHorizontalInset
        val verticalInset = CollectionReferenceMetrics.SelectionPaintVerticalInset
        val paintWidth = slotWidth - horizontalInset * 2
        val paintHeight = CollectionReferenceMetrics.SegmentHeight - verticalInset * 2

        assertEquals(38.dp, CollectionReferenceMetrics.SegmentHeight)
        assertEquals(3.dp, CollectionReferenceMetrics.SegmentInnerPadding)
        assertEquals(34.dp, paintHeight)
        assertEquals(slotWidth / 2, horizontalInset + paintWidth / 2)
        assertEquals(CollectionReferenceMetrics.SegmentHeight / 2, verticalInset + paintHeight / 2)
        assertTrue(paintWidth.value in 115f..116f)
        assertEquals(7.dp, CollectionReferenceMetrics.SelectedSegmentRadius)
    }

    @Test
    fun eventsPaintFitsInsideUnchangedClickSlot() {
        val slotWidth = 60.dp
        val horizontalInset = EventsReferenceMetrics.SelectionPaintHorizontalInset
        val verticalInset = EventsReferenceMetrics.SelectionPaintVerticalInset
        val paintWidth = slotWidth - horizontalInset * 2
        val paintHeight = EventsReferenceMetrics.FilterHeight - verticalInset * 2

        assertEquals(31.dp, EventsReferenceMetrics.FilterHeight)
        assertEquals(59.dp, paintWidth)
        assertEquals(29.dp, paintHeight)
        assertEquals(slotWidth / 2, horizontalInset + paintWidth / 2)
        assertEquals(EventsReferenceMetrics.FilterHeight / 2, verticalInset + paintHeight / 2)
        assertEquals(11.5.dp, EventsReferenceMetrics.SelectionPaintRadius)
    }
}
