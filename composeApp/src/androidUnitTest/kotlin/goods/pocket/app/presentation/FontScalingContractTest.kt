package goods.pocket.app.presentation

import java.io.File
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FontScalingContractTest {
    private val moduleDirectory = File(checkNotNull(System.getProperty("user.dir"))).let { root ->
        listOf(root, root.resolve("composeApp")).first { it.resolve("src/commonMain").isDirectory }
    }

    @Test
    fun settingsTextContainersCanGrowWithFontScale() {
        val source = source("settings/SettingsScreenComponents.kt")

        assertTrue(source.contains(".heightIn(min = SettingsReferenceMetrics.HeaderHeight)"))
        assertTrue(source.contains(".heightIn(min = SettingsReferenceMetrics.FormatRowHeight)"))
        assertFalse(source.contains(".height(SettingsReferenceMetrics.LanguageCardHeight)"))
        assertFalse(source.contains(".height(SettingsReferenceMetrics.FormatCardHeight)"))
        assertTrue(source.contains("LocalDensity.current.fontScale"))
    }

    @Test
    fun formActionsAndHeadersDoNotClipScaledText() {
        listOf(
            "QuickAddReferenceComponents.kt" to 46,
            "CollectionEntryEditorSheet.kt" to 44,
            "EventEditorSheet.kt" to 44,
        ).forEach { (file, height) ->
            val source = source("component/$file")
            assertTrue(source.contains(".heightIn(min = $height.dp)"), file)
            assertFalse(source.contains("Box(Modifier.fillMaxWidth().height("), file)
        }
    }

    private fun source(path: String): String = moduleDirectory
        .resolve("src/commonMain/kotlin/goods/pocket/app/presentation/$path")
        .readText()
}
