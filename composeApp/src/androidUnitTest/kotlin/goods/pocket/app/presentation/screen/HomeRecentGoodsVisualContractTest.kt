package goods.pocket.app.presentation.screen

import java.io.File
import kotlin.test.Test
import kotlin.test.assertFalse

class HomeRecentGoodsVisualContractTest {
    @Test
    fun `recent goods cards do not show unsupported favorite affordances`() {
        val module = moduleDirectory()
        val source = module
            .resolve("src/commonMain/kotlin/goods/pocket/app/presentation/screen/HomeScreenSections.kt")
            .readText()
        val artworkFile = module.resolve(
            "src/commonMain/kotlin/goods/pocket/app/presentation/designsystem/" +
                "GoodsPocketGoodsCardArtwork.kt",
        )
        val componentSource = module
            .resolve(
                "src/commonMain/kotlin/goods/pocket/app/presentation/designsystem/" +
                    "GoodsPocketImageLockedComponents.kt",
            )
            .readText()
        val tokenSource = module
            .resolve(
                "src/commonMain/kotlin/goods/pocket/app/presentation/designsystem/" +
                    "GoodsPocketVisualTokens.kt",
            )
            .readText()

        assertFalse(source.contains("HomeFavoriteHeartGlyph("))
        assertFalse(artworkFile.exists())
        assertFalse(componentSource.contains("favorite:"))
        assertFalse(componentSource.contains("Wishlist"))
        assertFalse(tokenSource.contains("Wishlist"))
    }
}

private fun moduleDirectory(): File {
    val workingDirectory = File(checkNotNull(System.getProperty("user.dir")))
    return listOf(workingDirectory, workingDirectory.resolve("composeApp"))
        .first { candidate -> candidate.resolve("src/commonMain").isDirectory }
}
