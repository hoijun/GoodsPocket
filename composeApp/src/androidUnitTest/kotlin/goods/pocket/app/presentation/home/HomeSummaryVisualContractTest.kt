package goods.pocket.app.presentation.home

import java.io.File
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class HomeSummaryVisualContractTest {
    @Test
    fun `header does not add extra space before the hero`() {
        val screenSource = homeModuleDirectory()
            .resolve("src/commonMain/kotlin/goods/pocket/app/presentation/home/HomeScreen.kt")
            .readText()
        assertFalse(screenSource.contains(".padding(top = 0.dp, bottom = 3.dp)"))
        assertTrue(screenSource.contains("PaddingValues(top = 2.dp, bottom = 16.dp)"))
        assertTrue(screenSource.contains(".size(38.dp)"))
    }

    @Test
    fun `home major sections use the locked sixteen dp spacing`() {
        val moduleDirectory = homeModuleDirectory()
        val screenSource = moduleDirectory
            .resolve("src/commonMain/kotlin/goods/pocket/app/presentation/home/HomeScreen.kt")
            .readText()
        val metricsSource = moduleDirectory
            .resolve(
                "src/commonMain/kotlin/goods/pocket/app/presentation/home/HomeReferenceMetrics.kt",
            )
            .readText()

        assertTrue(metricsSource.contains("val SectionSpacing = 16.dp"))
        assertTrue(
            screenSource.contains(
                "verticalArrangement = Arrangement.spacedBy(HomeReferenceMetrics.SectionSpacing)",
            ),
        )
    }

    @Test
    fun `home summary omits the unsupported sale metric`() {
        val moduleDirectory = homeModuleDirectory()
        val screenSource = moduleDirectory
            .resolve("src/commonMain/kotlin/goods/pocket/app/presentation/home/HomeScreen.kt")
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
        assertTrue(
            englishResources.contains("<string name=\"home_summary_reserved\">Reserved</string>"),
        )
        assertFalse(englishResources.contains("home_summary_wishlist"))
    }
}

private fun homeModuleDirectory(): File {
    val workingDirectory = File(checkNotNull(System.getProperty("user.dir")))
    return listOf(workingDirectory, workingDirectory.resolve("composeApp"))
        .first { candidate -> candidate.resolve("src/commonMain").isDirectory }
}
