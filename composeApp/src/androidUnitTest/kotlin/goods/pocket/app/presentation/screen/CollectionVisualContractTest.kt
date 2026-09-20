package goods.pocket.app.presentation.screen

import java.io.File
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CollectionVisualContractTest {
    @Test
    fun `collection keeps accessible native controls and supported actions only`() {
        val moduleDirectory = collectionModuleDirectory()
        val screenSource = moduleDirectory
            .resolve("src/commonMain/kotlin/goods/pocket/app/presentation/screen/CollectionScreen.kt")
            .readText()
        val componentsSource = moduleDirectory
            .resolve(
                "src/commonMain/kotlin/goods/pocket/app/presentation/screen/CollectionScreenComponents.kt",
            )
            .readText()

        assertTrue(componentsSource.contains("role = Role.Tab"))
        assertTrue(componentsSource.contains("BasicTextField("))
        assertTrue(componentsSource.contains("Surface(\n        onClick = onClick"))
        assertTrue(componentsSource.contains("MediaPlaceholder = Color(0xFFD7D2CC)"))
        assertTrue(screenSource.contains("key = CollectionEntry::id"))

        val unsupportedTerms = listOf(
            "favorite",
            "wishlist",
            "sortMenu",
            "viewMode",
            "sale",
        )
        unsupportedTerms.forEach { term ->
            assertFalse(screenSource.contains(term, ignoreCase = true))
            assertFalse(componentsSource.contains(term, ignoreCase = true))
        }
    }
}

private fun collectionModuleDirectory(): File {
    val workingDirectory = File(checkNotNull(System.getProperty("user.dir")))
    return listOf(workingDirectory, workingDirectory.resolve("composeApp"))
        .first { candidate -> candidate.resolve("src/commonMain").isDirectory }
}
