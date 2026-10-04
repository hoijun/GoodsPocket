package goods.pocket.app.presentation.home

import java.io.File
import kotlin.test.Test
import kotlin.test.assertTrue

class HomeLargeFontLayoutContractTest {
    @Test
    fun `hero expanded text drives card height without a filling background`() {
        val screen = homeSource("HomeScreen.kt")

        assertTrue(screen.contains("Modifier.heightIn(min = 120.dp)"))
        assertTrue(screen.contains("modifier = Modifier.matchParentSize()"))
        assertTrue(screen.contains("maxLines = if (isExpandedText) Int.MAX_VALUE else 2"))
    }

    @Test
    fun `expanded spending text reserves a separate chart area`() {
        val sections = homeSource("HomeScreenSections.kt")

        assertTrue(sections.contains("Modifier.heightIn(min = 102.dp)"))
        assertTrue(sections.contains("HomeReferenceMetrics.SpendingChartHeight + 12.dp"))
        assertTrue(
            sections.contains(
                "if (isExpandedText) Modifier.fillMaxWidth() else Modifier.fillMaxSize()",
            ),
        )
    }

    @Test
    fun `summary and section headers allow scaled text to exceed reference height`() {
        val screen = homeSource("HomeScreen.kt")
        val sections = homeSource("HomeScreenSections.kt")

        assertTrue(screen.contains(".heightIn(min = 98.dp)"))
        assertTrue(sections.contains(".heightIn(min = HomeReferenceMetrics.SectionHeaderHeight)"))
    }

    @Test
    fun `recent cards preserve reference height but allow scaled text to grow`() {
        val sections = homeSource("HomeScreenSections.kt")

        assertTrue(sections.contains("LocalDensity.current.fontScale > 1f"))
        assertTrue(sections.contains("Modifier.heightIn(min = 140.dp)"))
        assertTrue(sections.contains("Modifier.height(140.dp)"))
    }
}

private fun homeSource(name: String): String {
    val workingDirectory = File(checkNotNull(System.getProperty("user.dir")))
    val module = listOf(workingDirectory, workingDirectory.resolve("composeApp"))
        .first { candidate -> candidate.resolve("src/commonMain").isDirectory }
    return module.resolve("src/commonMain/kotlin/goods/pocket/app/presentation/home/$name")
        .readText()
}
