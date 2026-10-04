package goods.pocket.app.presentation.collection

import java.io.File
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CollectionVisualContractTest {
    @Test
    fun `controls align without moving the title result count or grid`() {
        val metricsSource = collectionModuleDirectory()
            .resolve(
                "src/commonMain/kotlin/goods/pocket/app/presentation/collection/CollectionReferenceMetrics.kt",
            )
            .readText()
        assertTrue(metricsSource.contains("val TitleToSegmentSpacing = 18.dp"))
        assertTrue(metricsSource.contains("val SearchToCountSpacing = 17.dp"))
        assertTrue(metricsSource.contains("val TitleTopPadding = 24.dp"))
        assertTrue(metricsSource.contains("val GridTop = 201.dp"))
    }

    @Test
    fun `collection keeps accessible native controls and supported actions only`() {
        val moduleDirectory = collectionModuleDirectory()
        val screenSource = moduleDirectory
            .resolve(
                "src/commonMain/kotlin/goods/pocket/app/presentation/collection/CollectionScreen.kt",
            )
            .readText()
        val componentsSource = moduleDirectory
            .resolve(
                "src/commonMain/kotlin/goods/pocket/app/presentation/collection/CollectionScreenComponents.kt",
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
