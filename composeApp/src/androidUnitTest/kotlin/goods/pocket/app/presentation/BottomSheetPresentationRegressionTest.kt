package goods.pocket.app.presentation

import java.io.File
import kotlin.test.Test
import kotlin.test.assertTrue

class BottomSheetPresentationRegressionTest {

    @Test
    fun `all modal bottom sheets open fully and render a static inline header handle`() {
        val quickAddSource = source("composeApp/src/commonMain/kotlin/goods/pocket/app/presentation/component/QuickAddSheet.kt")
        val detailSource = source("composeApp/src/commonMain/kotlin/goods/pocket/app/presentation/component/DetailSheet.kt")
        val editorSource = source("composeApp/src/commonMain/kotlin/goods/pocket/app/presentation/component/EditorSheet.kt")
        val surfaceSource = source("composeApp/src/commonMain/kotlin/goods/pocket/app/presentation/designsystem/GoodsPocketSurface.kt")

        listOf(quickAddSource, detailSource, editorSource).forEach { source ->
            assertTrue(source.contains("rememberModalBottomSheetState(skipPartiallyExpanded = true)"))
            assertTrue(source.contains("dragHandle = null"))
            assertTrue(source.contains("GoodsPocketBottomSheetHandle()"))
        }

        assertTrue(surfaceSource.contains("fun GoodsPocketBottomSheetHandle("))
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
