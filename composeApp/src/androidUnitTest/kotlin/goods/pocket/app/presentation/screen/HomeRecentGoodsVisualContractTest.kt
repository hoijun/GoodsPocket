package goods.pocket.app.presentation.screen

import java.io.File
import kotlin.test.Test
import kotlin.test.assertFalse

class HomeRecentGoodsVisualContractTest {
    @Test
    fun `recent goods cards do not show unsupported favorite affordances`() {
        val source = moduleDirectory()
            .resolve("src/commonMain/kotlin/goods/pocket/app/presentation/screen/HomeScreenSections.kt")
            .readText()

        assertFalse(source.contains("HomeFavoriteHeartGlyph("))
    }
}

private fun moduleDirectory(): File {
    val workingDirectory = File(checkNotNull(System.getProperty("user.dir")))
    return listOf(workingDirectory, workingDirectory.resolve("composeApp"))
        .first { candidate -> candidate.resolve("src/commonMain").isDirectory }
}
