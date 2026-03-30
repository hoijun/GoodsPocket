package goods.pocket.app.presentation

import java.io.File
import kotlin.test.Test
import kotlin.test.assertTrue

class QuickAddItemParityRegressionTest {

    @Test
    fun `quick add item flow captures the same editable fields needed by item edit`() {
        val quickAddSource = source("composeApp/src/commonMain/kotlin/goods/pocket/app/presentation/component/QuickAddSheet.kt")
        val stateHolderSource = source("composeApp/src/commonMain/kotlin/goods/pocket/app/presentation/state/GoodsPocketAppStateHolder.kt")

        assertTrue(quickAddSource.contains("onSubmitItem: (String, String, ItemStatus, String, String, String) -> Unit"))
        assertTrue(quickAddSource.contains("listOf(ItemStatus.OWNED, ItemStatus.PLANNED_CLEANUP)"))
        assertTrue(quickAddSource.contains("field_status"))
        assertTrue(quickAddSource.contains("field_series"))
        assertTrue(quickAddSource.contains("field_character"))
        assertTrue(quickAddSource.contains("field_store"))

        assertTrue(stateHolderSource.contains("fun submitItem("))
        assertTrue(stateHolderSource.contains("status: ItemStatus"))
        assertTrue(stateHolderSource.contains("seriesName: String"))
        assertTrue(stateHolderSource.contains("characterName: String"))
        assertTrue(stateHolderSource.contains("purchaseStore: String"))
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
