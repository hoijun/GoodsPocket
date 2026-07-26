package goods.pocket.app.presentation.screen

import java.io.File
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class HomeSummaryVisualContractTest {
    @Test
    fun `home summary omits the unsupported sale metric`() {
        val moduleDirectory = homeModuleDirectory()
        val screenSource = moduleDirectory
            .resolve("src/commonMain/kotlin/goods/pocket/app/presentation/screen/HomeScreen.kt")
            .readText()
        val koreanResources = moduleDirectory
            .resolve("src/commonMain/composeResources/values/strings.xml")
            .readText()
        val englishResources = moduleDirectory
            .resolve("src/commonMain/composeResources/values-en/strings.xml")
            .readText()

        assertFalse(screenSource.contains("home_summary_sale"))
        assertFalse(koreanResources.contains("home_summary_sale"))
        assertFalse(englishResources.contains("home_summary_sale"))
        assertTrue(screenSource.contains("home_summary_reserved"))
        assertFalse(screenSource.contains("home_summary_wishlist"))
        assertTrue(koreanResources.contains("<string name=\"home_summary_reserved\">예약</string>"))
        assertFalse(koreanResources.contains("home_summary_wishlist"))
        assertTrue(englishResources.contains("<string name=\"home_summary_reserved\">Reserved</string>"))
        assertFalse(englishResources.contains("home_summary_wishlist"))
    }
}

private fun homeModuleDirectory(): File {
    val workingDirectory = File(checkNotNull(System.getProperty("user.dir")))
    return listOf(workingDirectory, workingDirectory.resolve("composeApp"))
        .first { candidate -> candidate.resolve("src/commonMain").isDirectory }
}
