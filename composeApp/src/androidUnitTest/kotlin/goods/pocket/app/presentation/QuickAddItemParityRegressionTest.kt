package goods.pocket.app.presentation

import java.io.File
import kotlin.test.Test
import kotlin.test.assertTrue

class QuickAddItemParityRegressionTest {

    @Test
    fun `quick add uses one unified collection entry form instead of separate item and preorder flows`() {
        val quickAddSource = source("composeApp/src/commonMain/kotlin/goods/pocket/app/presentation/component/QuickAddSheet.kt")
        val stateHolderSource = source("composeApp/src/commonMain/kotlin/goods/pocket/app/presentation/state/GoodsPocketAppStateHolder.kt")

        assertTrue(quickAddSource.contains("onSubmitCollectionEntry"))
        assertTrue(quickAddSource.contains("CollectionEntryStatus.RESERVED"))
        assertTrue(quickAddSource.contains("CollectionEntryStatus.OWNED"))
        assertTrue(quickAddSource.contains("CollectionEntryStatus.PLANNED_CLEANUP"))
        assertTrue(quickAddSource.contains("field_status"))
        assertTrue(quickAddSource.contains("field_series"))
        assertTrue(quickAddSource.contains("field_character"))
        assertTrue(quickAddSource.contains("field_store"))
        assertTrue(quickAddSource.contains("field_release_date"))
        assertTrue(quickAddSource.contains("QuickAddTarget.COLLECTION_ENTRY"))

        kotlin.test.assertFalse(quickAddSource.contains("QuickAddTarget.PREORDER"))
        kotlin.test.assertFalse(quickAddSource.contains("onSubmitPreorder"))

        assertTrue(stateHolderSource.contains("fun submitCollectionEntry("))
        assertTrue(stateHolderSource.contains("CollectionEntryStatus"))
        assertTrue(stateHolderSource.contains("seriesName: String"))
        assertTrue(stateHolderSource.contains("characterName: String"))
        assertTrue(stateHolderSource.contains("purchaseStore: String"))
        assertTrue(stateHolderSource.contains("releaseDate: String"))
        assertTrue(stateHolderSource.contains("reservationStore: String"))
        assertTrue(stateHolderSource.contains("note: String"))
        assertTrue(stateHolderSource.contains("seriesName = seriesName.trim().ifBlank { null }"))
        assertTrue(stateHolderSource.contains("characterName = characterName.trim().ifBlank { null }"))
        assertTrue(stateHolderSource.contains("purchaseStore = purchaseStore.trim().ifBlank { null }"))
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
