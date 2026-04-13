package goods.pocket.app.presentation

import java.io.File
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CollectionMetricLabelRegressionTest {

    @Test
    fun `collection total label matches cumulative purchase meaning`() {
        val koreanStrings = source("composeApp/src/commonMain/composeResources/values/strings.xml")
        val englishStrings = source("composeApp/src/commonMain/composeResources/values-en/strings.xml")

        assertFalse(koreanStrings.contains("<string name=\"collection_metric_monthly_spend\">이번 달 지출</string>"))
        assertTrue(koreanStrings.contains("<string name=\"collection_metric_monthly_spend\">총 구매액</string>"))

        assertFalse(englishStrings.contains("<string name=\"collection_metric_monthly_spend\">Monthly spend</string>"))
        assertTrue(englishStrings.contains("<string name=\"collection_metric_monthly_spend\">Total purchase amount</string>"))
    }

    @Test
    fun `collection total purchase summary matches the selected status card footprint`() {
        val collectionScreen = source("composeApp/src/commonMain/kotlin/goods/pocket/app/presentation/screen/CollectionScreen.kt")

        assertTrue(collectionScreen.contains("CollectionSummaryCard("))
        assertTrue(collectionScreen.contains("widthIn(min = 136.dp)"))
        assertTrue(collectionScreen.contains("padding(horizontal = 16.dp, vertical = 14.dp)"))
        assertTrue(collectionScreen.contains("pixelShadow("))
        assertTrue(collectionScreen.contains("style = MaterialTheme.typography.titleLarge"))
        assertTrue(collectionScreen.contains("style = MaterialTheme.typography.labelMedium"))
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
