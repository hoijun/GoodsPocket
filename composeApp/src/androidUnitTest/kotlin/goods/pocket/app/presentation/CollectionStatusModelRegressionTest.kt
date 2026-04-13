package goods.pocket.app.presentation

import java.io.File
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CollectionStatusModelRegressionTest {

    @Test
    fun `collection item status model keeps only owned and planned cleanup`() {
        val itemSource = source("composeApp/src/commonMain/kotlin/goods/pocket/app/domain/model/Item.kt")
        val i18nSource = source("composeApp/src/commonMain/kotlin/goods/pocket/app/presentation/i18n/GoodsPocketStrings.kt")
        val koreanStrings = source("composeApp/src/commonMain/composeResources/values/strings.xml")
        val englishStrings = source("composeApp/src/commonMain/composeResources/values-en/strings.xml")

        assertTrue(itemSource.contains("OWNED"))
        assertTrue(itemSource.contains("PLANNED_CLEANUP"))
        assertFalse(itemSource.contains("WAITING_DELIVERY"))
        assertFalse(itemSource.contains("PLANNED_TRANSFER"))
        assertFalse(itemSource.contains("LOST"))

        assertTrue(i18nSource.contains("ItemStatus.PLANNED_CLEANUP"))
        assertFalse(i18nSource.contains("ItemStatus.WAITING_DELIVERY"))
        assertFalse(i18nSource.contains("ItemStatus.PLANNED_TRANSFER"))
        assertFalse(i18nSource.contains("ItemStatus.LOST"))

        assertTrue(koreanStrings.contains("<string name=\"item_status_planned_cleanup\">정리 예정</string>"))
        assertFalse(koreanStrings.contains("item_status_waiting_delivery"))
        assertFalse(koreanStrings.contains("item_status_planned_transfer"))
        assertFalse(koreanStrings.contains("item_status_lost"))

        assertTrue(englishStrings.contains("<string name=\"item_status_planned_cleanup\">Planned cleanup</string>"))
        assertFalse(englishStrings.contains("item_status_waiting_delivery"))
        assertFalse(englishStrings.contains("item_status_planned_transfer"))
        assertFalse(englishStrings.contains("item_status_lost"))
    }

    @Test
    fun `collection screen uses unified entries with owned reserved and all segments`() {
        val collectionSource = source("composeApp/src/commonMain/kotlin/goods/pocket/app/presentation/screen/CollectionScreen.kt")
        val i18nSource = source("composeApp/src/commonMain/kotlin/goods/pocket/app/presentation/i18n/GoodsPocketStrings.kt")
        val koreanStrings = source("composeApp/src/commonMain/composeResources/values/strings.xml")
        val englishStrings = source("composeApp/src/commonMain/composeResources/values-en/strings.xml")

        assertTrue(collectionSource.contains("entries: List<CollectionEntry>"))
        assertTrue(collectionSource.contains("selectedSegment: CollectionSegment"))
        assertTrue(collectionSource.contains("onSegmentChange: (CollectionSegment) -> Unit"))
        assertTrue(collectionSource.contains("CollectionSegment.OWNED"))
        assertTrue(collectionSource.contains("CollectionSegment.RESERVED"))
        assertTrue(collectionSource.contains("CollectionSegment.ALL"))
        assertTrue(collectionSource.contains("CollectionEntryStatus.RESERVED"))
        assertTrue(collectionSource.contains("preorders_store_release"))
        assertTrue(i18nSource.contains("CollectionSegment.OWNED"))
        assertTrue(i18nSource.contains("collection_segment_reserved"))
        assertTrue(i18nSource.contains("collection_segment_all"))
        assertFalse(collectionSource.contains("selectedStatus: ItemStatus"))
        assertFalse(collectionSource.contains("visibleItems = items.filter { it.status == selectedStatus }"))
        assertFalse(koreanStrings.contains("collection_quantity_badge"))
        assertFalse(englishStrings.contains("collection_quantity_badge"))
    }

    @Test
    fun `legacy collection statuses collapse into the simplified model`() {
        val sqlDelightSource = source("composeApp/src/commonMain/kotlin/goods/pocket/app/data/local/SqlDelightGoodsPocketLocalDataSource.kt")
        val databaseSource = source("composeApp/src/commonMain/sqldelight/goods/pocket/app/db/GoodsPocketDatabase.sq")

        assertTrue(sqlDelightSource.contains("\"PLANNED_TRANSFER\" -> ItemStatus.PLANNED_CLEANUP"))
        assertTrue(sqlDelightSource.contains("\"WAITING_DELIVERY\" -> ItemStatus.OWNED"))
        assertTrue(sqlDelightSource.contains("\"LOST\" -> ItemStatus.OWNED"))
        assertTrue(databaseSource.contains("WHERE status = 'OWNED';"))
    }

    private fun source(path: String): String {
        val startingDirectory = File(checkNotNull(System.getProperty("user.dir")))
        val sourceFile = generateSequence(startingDirectory) { it.parentFile }
            .map { File(it, path) }
            .firstOrNull(File::exists)
            ?: error("Could not locate $path from ${startingDirectory.absolutePath}")

        return sourceFile.readText()
    }
}
