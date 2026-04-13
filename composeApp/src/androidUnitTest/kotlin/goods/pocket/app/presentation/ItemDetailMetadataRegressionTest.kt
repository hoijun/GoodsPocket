package goods.pocket.app.presentation

import java.io.File
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ItemDetailMetadataRegressionTest {

    @Test
    fun `collection entry detail keeps purchase metadata for owned entries`() {
        val detailSource = source("composeApp/src/commonMain/kotlin/goods/pocket/app/presentation/component/DetailSheet.kt")
        val koreanStrings = source("composeApp/src/commonMain/composeResources/values/strings.xml")
        val englishStrings = source("composeApp/src/commonMain/composeResources/values-en/strings.xml")

        assertTrue(detailSource.contains("CollectionEntryDetailSheet"))
        assertTrue(detailSource.contains("tr(Res.string.detail_purchase_date)"))
        assertTrue(detailSource.contains("tr(Res.string.detail_purchase_price)"))
        assertFalse(detailSource.contains("detail_linked_transactions"))
        assertFalse(detailSource.contains("detail_no_linked_transactions"))
        assertFalse(detailSource.contains("LinkedTransactionRow("))
        assertFalse(koreanStrings.contains("detail_linked_transactions"))
        assertFalse(koreanStrings.contains("detail_no_linked_transactions"))
        assertTrue(koreanStrings.contains("<string name=\"detail_purchase_date\">구매 날짜</string>"))
        assertTrue(koreanStrings.contains("<string name=\"detail_purchase_price\">구매 가격</string>"))
        assertFalse(englishStrings.contains("detail_linked_transactions"))
        assertFalse(englishStrings.contains("detail_no_linked_transactions"))
        assertTrue(englishStrings.contains("<string name=\"detail_purchase_date\">Purchase date</string>"))
        assertTrue(englishStrings.contains("<string name=\"detail_purchase_price\">Purchase price</string>"))
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
