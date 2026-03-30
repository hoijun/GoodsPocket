package goods.pocket.app.presentation

import java.io.File
import kotlin.test.Test
import kotlin.test.assertTrue

class BottomSheetPresentationRegressionTest {

    @Test
    fun `all modal bottom sheets use a bounded bordered container with animated header drag dismissal affordances`() {
        val quickAddSource = source("composeApp/src/commonMain/kotlin/goods/pocket/app/presentation/component/QuickAddSheet.kt")
        val detailSource = source("composeApp/src/commonMain/kotlin/goods/pocket/app/presentation/component/DetailSheet.kt")
        val editorSource = source("composeApp/src/commonMain/kotlin/goods/pocket/app/presentation/component/EditorSheet.kt")
        val bottomSheetSource = source("composeApp/src/commonMain/kotlin/goods/pocket/app/presentation/designsystem/GoodsPocketBottomSheet.kt")
        val surfaceSource = source("composeApp/src/commonMain/kotlin/goods/pocket/app/presentation/designsystem/GoodsPocketSurface.kt")

        listOf(quickAddSource, detailSource, editorSource).forEach { source ->
            assertTrue(source.contains("GoodsPocketModalBottomSheet("))
        }

        assertTrue(bottomSheetSource.contains("rememberModalBottomSheetState(skipPartiallyExpanded = true)"))
        assertTrue(bottomSheetSource.contains("sheetGesturesEnabled = false"))
        assertTrue(bottomSheetSource.contains("ModalBottomSheetProperties("))
        assertTrue(bottomSheetSource.contains("shouldDismissOnBackPress = false"))
        assertTrue(bottomSheetSource.contains("shouldDismissOnClickOutside = false"))
        assertTrue(bottomSheetSource.contains("heightIn(max = maxHeight - 88.dp)"))
        assertTrue(bottomSheetSource.contains("border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)"))
        assertTrue(bottomSheetSource.contains("verticalScroll(rememberScrollState())"))
        assertTrue(bottomSheetSource.contains("rememberDraggableState"))
        assertTrue(bottomSheetSource.contains(".offset { IntOffset("))
        assertTrue(bottomSheetSource.contains("animate("))
        assertTrue(surfaceSource.contains("fun GoodsPocketBottomSheetHandle("))
        assertTrue(surfaceSource.contains("fun GoodsPocketBottomSheetHeader("))
        assertTrue(surfaceSource.contains("GoodsPocketBottomSheetHandle()"))
        assertTrue(surfaceSource.contains("TextButton("))
        assertTrue(surfaceSource.contains("padding(start = 20.dp)"))
        assertTrue(surfaceSource.contains("padding(vertical = 20.dp)"))
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
