package goods.pocket.app.presentation

import java.io.File
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class EdgeToEdgeContractTest {
    private val moduleDirectory = File(checkNotNull(System.getProperty("user.dir"))).let { root ->
        listOf(root, root.resolve("composeApp")).first { it.resolve("src/commonMain").isDirectory }
    }

    @Test
    fun scaffoldPreservesAndConsumesSystemPaddingOnEveryDestination() {
        val source = productionSource("GoodsPocketApp.kt")

        assertTrue(source.contains("contentWindowInsets = WindowInsets.safeDrawing"))
        assertTrue(source.contains(".padding(innerPadding)"))
        assertTrue(source.contains(".consumeWindowInsets(innerPadding)"))
        assertFalse(source.contains("calculateTopPadding() -"))
        assertFalse(source.contains("TopInsetReduction"))
    }

    @Test
    fun customNavigationSeparatesSystemInsetsFromInteractiveContent() {
        val source = productionSource("designsystem/GoodsPocketImageLockedComponents.kt")

        assertTrue(source.contains(".windowInsetsPadding("))
        assertTrue(source.contains("WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom"))
        assertTrue(
            source.contains(".heightIn(min = ImageLockedNavigationMetrics.ContentMinHeight)"),
        )
        assertFalse(source.contains(".height(81.dp)"))
        assertFalse(source.contains("ImageLockedNavigationMetrics.ItemVerticalOffset"))
    }

    private fun productionSource(path: String): String = moduleDirectory
        .resolve("src/commonMain/kotlin/goods/pocket/app/presentation/$path")
        .readText()
}
