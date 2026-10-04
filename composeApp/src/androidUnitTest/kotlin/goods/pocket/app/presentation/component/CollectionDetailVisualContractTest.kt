package goods.pocket.app.presentation.component

import java.io.File
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CollectionDetailVisualContractTest {
    @Test
    fun `owned detail keeps the approved large media and compact actions`() {
        val moduleDirectory = collectionDetailModuleDirectory()
        val detailSource = moduleDirectory
            .resolve(
                "src/commonMain/kotlin/goods/pocket/app/presentation/component/" +
                    "CollectionEntryDetailSheet.kt",
            )
            .readText()
        val metricsSource = moduleDirectory
            .resolve(
                "src/commonMain/kotlin/goods/pocket/app/presentation/component/" +
                    "CollectionDetailReferenceMetrics.kt",
            )
            .readText()

        assertTrue(metricsSource.contains("SheetHeight = 602.dp"))
        assertTrue(metricsSource.contains("HorizontalPadding = 29.dp"))
        assertTrue(metricsSource.contains("MediaHeight = 257.dp"))
        assertTrue(metricsSource.contains("MediaContentGap = 8.dp"))
        assertTrue(metricsSource.contains("CloseIconSize = 16.dp"))
        assertTrue(metricsSource.contains("BadgeTitleGap = 10.dp"))
        assertTrue(metricsSource.contains("TitleSubtitleGap = 3.dp"))
        assertTrue(metricsSource.contains("TitleMetadataGap = 15.dp"))
        assertTrue(metricsSource.contains("TitleFontSize = 14.sp"))
        assertTrue(metricsSource.contains("MetadataFontSize = 10.sp"))
        assertTrue(metricsSource.contains("MetadataRowHeight = 25.dp"))
        assertTrue(metricsSource.contains("NoteTopPadding = 9.dp"))
        assertTrue(metricsSource.contains("NoteTextGap = 3.dp"))
        assertTrue(metricsSource.contains("ActionTopGap = 5.dp"))
        assertTrue(metricsSource.contains("ActionHeight = 38.dp"))

        assertTrue(detailSource.contains("MediaPlaceholder = Color(0xFFD7D2CC)"))
        assertTrue(detailSource.contains("scrimColor = Color.Black.copy(alpha = 0.32f)"))
        assertTrue(detailSource.contains("contentWindowInsets = { WindowInsets(0, 0, 0, 0) }"))
        assertTrue(detailSource.contains("CollectionDetailReferenceMetrics.MediaHeight"))
        assertTrue(detailSource.contains("CollectionDetailReferenceMetrics.MediaContentGap"))
        assertTrue(detailSource.contains("CollectionDetailReferenceMetrics.BadgeTitleGap"))
        assertTrue(detailSource.contains("CollectionDetailReferenceMetrics.TitleSubtitleGap"))
        assertTrue(detailSource.contains("CollectionDetailReferenceMetrics.NoteTopPadding"))
        assertTrue(detailSource.contains("CollectionDetailReferenceMetrics.NoteTextGap"))
        assertTrue(detailSource.contains("CollectionDetailReferenceMetrics.ActionTopGap"))
        assertTrue(detailSource.contains("CollectionDetailReferenceMetrics.ActionHeight"))
        assertTrue(detailSource.contains("Box(modifier = Modifier.weight(1f))"))
        assertTrue(detailSource.contains(".fillMaxSize()"))
        assertTrue(detailSource.contains("onClick = onEdit"))
        assertTrue(detailSource.contains("onClick = onDelete"))

        assertFalse(detailSource.contains("take(1)"))
        assertFalse(detailSource.contains("shadowElevation"))
        assertFalse(detailSource.contains("favorite", ignoreCase = true))
        assertFalse(detailSource.contains("wishlist", ignoreCase = true))
        assertFalse(detailSource.contains("sale", ignoreCase = true))
    }

    @Test
    fun `reserved detail reuses owned geometry and preserves the receive transition`() {
        val detailSource = collectionDetailModuleDirectory()
            .resolve(
                "src/commonMain/kotlin/goods/pocket/app/presentation/component/" +
                    "CollectionEntryDetailSheet.kt",
            )
            .readText()

        assertTrue(detailSource.contains("CollectionEntryStatus.RESERVED"))
        assertTrue(detailSource.contains("Res.string.detail_expected_release_date"))
        assertTrue(detailSource.contains("Res.string.detail_reservation_store"))
        assertTrue(detailSource.contains("Res.string.detail_reservation_amount"))
        assertTrue(detailSource.contains("Res.string.detail_related_link"))
        assertTrue(detailSource.contains("Res.string.action_mark_received"))
        assertTrue(detailSource.contains("onClick = onMarkReceived"))
        assertFalse(detailSource.contains("ReservedCollectionEntryDetailSheet"))
    }
}

private fun collectionDetailModuleDirectory(): File {
    val workingDirectory = File(checkNotNull(System.getProperty("user.dir")))
    return listOf(workingDirectory, workingDirectory.resolve("composeApp"))
        .first { candidate -> candidate.resolve("src/commonMain").isDirectory }
}
