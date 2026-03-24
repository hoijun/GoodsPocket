package goods.pocket.app.presentation

import java.io.File
import kotlin.test.Test
import kotlin.test.assertFalse

class KoreanCopyRegressionTest {

    @Test
    fun `screens do not keep english hardcoded user facing copy`() {
        val homeSource = source("composeApp/src/commonMain/kotlin/goods/pocket/app/presentation/screen/HomeScreen.kt")
        val collectionSource = source("composeApp/src/commonMain/kotlin/goods/pocket/app/presentation/screen/CollectionScreen.kt")
        val mySource = source("composeApp/src/commonMain/kotlin/goods/pocket/app/presentation/screen/MyScreen.kt")
        val appSource = source("composeApp/src/commonMain/kotlin/goods/pocket/app/presentation/GoodsPocketApp.kt")

        assertFalse(homeSource.contains("\"items\""))
        assertFalse(homeSource.contains("slots active"))

        assertFalse(collectionSource.contains("\"Total Items\""))
        assertFalse(collectionSource.contains("\"Monthly Spend\""))
        assertFalse(collectionSource.contains("\"Waiting\""))
        assertFalse(collectionSource.contains("formatCollectionCurrency"))

        assertFalse(mySource.contains("\"Local Profile\""))
        assertFalse(mySource.contains("\"Not connected\""))
        assertFalse(mySource.contains("-> \"R\""))
        assertFalse(mySource.contains("-> \"E\""))
        assertFalse(mySource.contains("-> \"W\""))
        assertFalse(mySource.contains("-> \"S\""))
        assertFalse(mySource.contains("-> \"N\""))

        assertFalse(appSource.contains("AppDestination.Home -> \"H\""))
        assertFalse(appSource.contains("AppDestination.Collection -> \"C\""))
        assertFalse(appSource.contains("AppDestination.Preorders -> \"P\""))
        assertFalse(appSource.contains("AppDestination.My -> \"M\""))
    }

    @Test
    fun `default seed data does not keep english demo content for the korean default app`() {
        val seedDataSource = source("composeApp/src/commonMain/kotlin/goods/pocket/app/data/local/GoodsPocketSeedData.kt")

        assertFalse(seedDataSource.contains("Hoshimachi Suisei Acrylic Stand"))
        assertFalse(seedDataSource.contains("Blue Archive Art Book"))
        assertFalse(seedDataSource.contains("Nijisanji Anniversary Badge Set"))
        assertFalse(seedDataSource.contains("Project Sekai Limited Tapestry"))
        assertFalse(seedDataSource.contains("Animate"))
        assertFalse(seedDataSource.contains("Kyobo"))
        assertFalse(seedDataSource.contains("Melonbooks"))
        assertFalse(seedDataSource.contains("Aniplus Shop"))
        assertFalse(seedDataSource.contains("Glass Cabinet"))
        assertFalse(seedDataSource.contains("Book Shelf"))
    }

    @Test
    fun `navigation model no longer keeps unused english label fields`() {
        val destinationSource = source("composeApp/src/commonMain/kotlin/goods/pocket/app/presentation/navigation/AppDestination.kt")

        assertFalse(destinationSource.contains("val label: String"))
        assertFalse(destinationSource.contains("allDestinations"))
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
