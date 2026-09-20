package goods.pocket.app.data

import java.io.File
import kotlin.test.Test
import kotlin.test.assertTrue

class CollectionEditorPersistenceContractTest {
    @Test
    fun `related link is persisted for owned and reserved entries`() {
        val moduleDirectory = collectionEditorModuleDirectory()
        val domainDirectory = moduleDirectory.resolve(
            "src/commonMain/kotlin/goods/pocket/app/domain/model",
        )
        val records = moduleDirectory.resolve(
            "src/commonMain/kotlin/goods/pocket/app/data/local/model/GoodsPocketLocalRecords.kt",
        ).readText()
        val schema = moduleDirectory.resolve(
            "src/commonMain/sqldelight/goods/pocket/app/db/GoodsPocketDatabase.sq",
        ).readText()
        val mappers = moduleDirectory.resolve(
            "src/commonMain/kotlin/goods/pocket/app/data/repository/GoodsPocketLocalMappers.kt",
        ).readText()

        listOf("CollectionEntry.kt", "Item.kt", "Preorder.kt").forEach { fileName ->
            assertTrue(domainDirectory.resolve(fileName).readText().contains("relatedLink"))
        }
        assertTrue(records.countOccurrences("relatedLink") >= 2)
        assertTrue(schema.countOccurrences("related_link") >= 4)
        assertTrue(mappers.countOccurrences("relatedLink") >= 4)
    }
}

private fun collectionEditorModuleDirectory(): File {
    val workingDirectory = File(checkNotNull(System.getProperty("user.dir")))
    return listOf(workingDirectory, workingDirectory.resolve("composeApp"))
        .first { candidate -> candidate.resolve("src/commonMain").isDirectory }
}

private fun String.countOccurrences(value: String): Int = windowed(value.length).count { it == value }
