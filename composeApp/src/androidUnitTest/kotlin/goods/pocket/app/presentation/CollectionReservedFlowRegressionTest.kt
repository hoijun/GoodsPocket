package goods.pocket.app.presentation

import java.io.File
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CollectionReservedFlowRegressionTest {

    @Test
    fun `detail and editor sheets are unified around collection entries`() {
        val detailSource = source("composeApp/src/commonMain/kotlin/goods/pocket/app/presentation/component/DetailSheet.kt")
        val editorSource = source("composeApp/src/commonMain/kotlin/goods/pocket/app/presentation/component/EditorSheet.kt")

        assertTrue(detailSource.contains("CollectionEntryDetailSheet"))
        assertTrue(editorSource.contains("CollectionEntryEditorSheet"))

        assertFalse(detailSource.contains("fun PreorderDetailSheet("))
        assertFalse(detailSource.contains("fun ItemDetailSheet("))
        assertFalse(editorSource.contains("fun PreorderEditorSheet("))
        assertFalse(editorSource.contains("fun ItemEditorSheet("))
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
