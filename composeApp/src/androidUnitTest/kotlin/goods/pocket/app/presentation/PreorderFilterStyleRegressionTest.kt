package goods.pocket.app.presentation

import java.io.File
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PreorderFilterStyleRegressionTest {

    @Test
    fun `preorder filter uses block cards instead of compact chips`() {
        val source = preorderScreenSource()

        assertTrue(source.contains("PreorderStatusFilterCard("))
        assertTrue(source.contains(".widthIn(min = 132.dp)"))
        assertFalse(source.contains("GoodsPocketFilterChip("))
    }

    private fun preorderScreenSource(): String {
        val startingDirectory = File(checkNotNull(System.getProperty("user.dir")))
        val sourceFile = generateSequence(startingDirectory) { it.parentFile }
            .map { File(it, "composeApp/src/commonMain/kotlin/goods/pocket/app/presentation/screen/PreordersScreen.kt") }
            .firstOrNull(File::exists)
            ?: error("Could not locate PreordersScreen.kt from ${startingDirectory.absolutePath}")

        return sourceFile.readText()
    }
}
