package goods.pocket.app.presentation.designsystem

import java.io.File
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SelectionPaintContractTest {
    @Test
    fun `collection selected paint is inset independently from equal clickable slots`() {
        val source = source("collection/CollectionScreenComponents.kt")

        assertTrue(source.contains("horizontal = CollectionReferenceMetrics.SegmentInnerPadding"))
        assertTrue(
            source.contains(
                "horizontal = CollectionReferenceMetrics.SelectionPaintHorizontalInset",
            ),
        )
        assertTrue(
            source.contains("vertical = CollectionReferenceMetrics.SelectionPaintVerticalInset"),
        )
        assertTrue(source.contains(".weight(1f)"))
        assertTrue(source.contains("role = Role.Tab"))
        assertFalse(source.contains(".padding(CollectionReferenceMetrics.SegmentInnerPadding)"))
    }

    @Test
    fun `events selected paint is separate from full height click and label bounds`() {
        val source = source("events/EventsScreenComponents.kt")

        assertTrue(
            source.contains("horizontal = EventsReferenceMetrics.SelectionPaintHorizontalInset"),
        )
        assertTrue(source.contains("vertical = EventsReferenceMetrics.SelectionPaintVerticalInset"))
        assertTrue(
            source.contains("RoundedCornerShape(EventsReferenceMetrics.SelectionPaintRadius)"),
        )
        assertTrue(source.contains(".height(EventsReferenceMetrics.FilterHeight)"))
        assertTrue(source.contains("onClick = { onSelected(index) }"))
    }
}

private fun source(name: String): String {
    val workingDirectory = File(checkNotNull(System.getProperty("user.dir")))
    val module = listOf(workingDirectory, workingDirectory.resolve("composeApp"))
        .first { candidate -> candidate.resolve("src/commonMain").isDirectory }
    return module.resolve("src/commonMain/kotlin/goods/pocket/app/presentation/$name")
        .readText()
        .replace(Regex("\\s+"), " ")
}
