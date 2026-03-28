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
    fun `collection summary metrics use wider padding and reduced rounding`() {
        val collectionScreen = source("composeApp/src/commonMain/kotlin/goods/pocket/app/presentation/screen/CollectionScreen.kt")
        val surfaceSource = source("composeApp/src/commonMain/kotlin/goods/pocket/app/presentation/designsystem/GoodsPocketSurface.kt")

        assertTrue(surfaceSource.contains("shape: Shape = CircleShape"))
        assertTrue(surfaceSource.contains("contentPadding: PaddingValues = PaddingValues(horizontal = 12.dp, vertical = 10.dp)"))
        assertTrue(collectionScreen.contains("val summaryMetricShape = RoundedCornerShape(14.dp)"))
        assertTrue(collectionScreen.contains("val summaryMetricPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)"))
        assertTrue(collectionScreen.contains("shape = summaryMetricShape"))
        assertTrue(collectionScreen.contains("contentPadding = summaryMetricPadding"))
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
