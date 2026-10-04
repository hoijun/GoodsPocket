package goods.pocket.app.presentation.component

import java.io.File
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AdaptiveDetailLayoutContractTest {
    @Test
    fun `detail actions and metadata grow with scaled or wrapped text`() {
        val collection = presentationSource("component/CollectionEntryDetailSheet.kt")
        assertTrue(
            collection.contains(".heightIn(min = CollectionDetailReferenceMetrics.ActionHeight)"),
        )
        assertFalse(collection.contains(".height(CollectionDetailReferenceMetrics.ActionHeight)"))
        assertTrue(
            collection.contains(
                ".heightIn(min = CollectionDetailReferenceMetrics.MetadataRowHeight)",
            ),
        )
        assertTrue(
            collection.contains(".heightIn(min = CollectionDetailReferenceMetrics.NoteHeight)"),
        )
        val metadataAndNote = collection.substringAfter("private fun CollectionMetadataRow(")
            .substringBefore("private fun CollectionDetailActions(")
        assertFalse(metadataAndNote.contains("maxLines = 1"))

        val event = presentationSource("component/EventDetailSheet.kt")
        assertTrue(event.contains("Modifier.weight(1f).heightIn(min = 46.dp)"))
        assertFalse(event.contains("Modifier.weight(1f).height(46.dp)"))
    }

    @Test
    fun `both detail sheets bound their height and protect horizontal and bottom safe areas`() {
        listOf("CollectionEntryDetailSheet.kt", "EventDetailSheet.kt").forEach { name ->
            val source = presentationSource("component/$name")
            assertTrue(source.contains("boundedDetailSheetHeight("), name)
            assertTrue(source.contains("WindowInsets.safeDrawing.only("), name)
            assertTrue(
                source.contains("WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom"),
                name,
            )
        }
    }

    @Test
    fun `collection grid reserves footer space in its viewport not scroll content`() {
        val source = presentationSource("collection/CollectionScreen.kt")
        assertTrue(source.contains("bottom = CollectionReferenceMetrics.GridBottomClearance"))
        assertFalse(source.contains("contentPadding = PaddingValues(bottom ="))
        assertTrue(source.contains(".clipToBounds()"))
    }
}

private fun presentationSource(path: String): String {
    val workingDirectory = File(checkNotNull(System.getProperty("user.dir")))
    val module = listOf(workingDirectory, workingDirectory.resolve("composeApp"))
        .first { it.resolve("src/commonMain").isDirectory }
    return module.resolve("src/commonMain/kotlin/goods/pocket/app/presentation/$path").readText()
}
