package goods.pocket.app.presentation

import java.io.File
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TemporaryPreorderRouteRegressionTest {

    @Test
    fun `goods pocket app no longer renders a dedicated preorders destination`() {
        val appSource = source("composeApp/src/commonMain/kotlin/goods/pocket/app/presentation/GoodsPocketApp.kt")
        val destinationSource = source("composeApp/src/commonMain/kotlin/goods/pocket/app/presentation/navigation/AppDestination.kt")

        assertFalse(appSource.contains("AppDestination.Preorders ->"))
        assertFalse(destinationSource.contains("data object Preorders"))
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
