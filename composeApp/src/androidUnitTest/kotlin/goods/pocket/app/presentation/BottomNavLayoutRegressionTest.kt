package goods.pocket.app.presentation

import java.io.File
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class BottomNavLayoutRegressionTest {

    @Test
    fun `bottom nav glyph does not request full available height`() {
        val source = goodsPocketAppSource()
        val glyphBlock = source.substringAfter("private fun BottomNavGlyph(")
            .substringBefore("@Composable\nprivate fun GoodsPocketNavHost(")

        assertFalse(
            glyphBlock.contains("fillMaxHeight()"),
            "BottomNavGlyph must not use fillMaxHeight because it can expand the bottom navigation item vertically.",
        )
    }

    @Test
    fun `design system surfaces do not use gradients or glossy alpha overlays`() {
        val source = goodsPocketSurfaceSource()

        assertFalse(source.contains("Brush.linearGradient"))
        assertFalse(source.contains("Brush.radialGradient"))
        assertFalse(source.contains("alpha = 0.16f"))
    }

    @Test
    fun `design system cards use borders instead of elevated shadows`() {
        val source = goodsPocketSurfaceSource()

        assertFalse(source.contains("cardElevation(defaultElevation = 1.dp"))
        assertTrue(source.contains("BorderStroke"))
        assertTrue(source.contains("border ="))
    }

    @Test
    fun `bottom bar container does not use drop shadow`() {
        val source = goodsPocketAppSource()
        val bottomBarBlock = source.substringAfter("private fun GoodsPocketBottomBar(")
            .substringBefore("@Composable\nprivate fun BottomNavGlyph(")

        assertFalse(bottomBarBlock.contains("shadowElevation ="))
    }

    @Test
    fun `bottom bar items disable click indication instead of using NavigationBarItem ripple`() {
        val source = goodsPocketAppSource()
        val bottomBarBlock = source.substringAfter("private fun GoodsPocketBottomBar(")
            .substringBefore("@Composable\nprivate fun BottomNavGlyph(")

        assertFalse(bottomBarBlock.contains("NavigationBarItem("))
        assertTrue(bottomBarBlock.contains("selectable("))
        assertTrue(bottomBarBlock.contains("indication = null"))
        assertTrue(bottomBarBlock.contains("role = Role.Tab"))
    }

    @Test
    fun `top app bar uses compact custom height`() {
        val source = goodsPocketAppSource()

        assertTrue(source.contains("TopAppBar("))
        assertTrue(source.contains("expandedHeight = 56.dp"))
    }

    @Test
    fun `top app bar includes one dp divider before content`() {
        val source = goodsPocketAppSource()

        assertTrue(source.contains("HorizontalDivider("))
        assertTrue(source.contains("thickness = 1.dp"))
    }

    @Test
    fun `android preview is not kept for the bottom navigation`() {
        assertFalse(bottomNavPreviewFile().exists())
        assertFalse(goodsPocketAppSource().contains("BottomNavPreviewContent("))
    }

    @Test
    fun `screen lists keep horizontal outer padding and move top spacing into scroll content`() {
        val layoutSource = screenLayoutSource()
        val screenModifierBlock = layoutSource.substringAfter("fun goodsPocketScreenModifier(): Modifier {")
            .substringBefore("fun goodsPocketPrimaryScrollContentPadding()")
        val primaryPaddingBlock = layoutSource.substringAfter("fun goodsPocketPrimaryScrollContentPadding(): PaddingValues {")
            .substringBefore("fun goodsPocketSecondaryScrollContentPadding()")
        val secondaryPaddingBlock = layoutSource.substringAfter("fun goodsPocketSecondaryScrollContentPadding(): PaddingValues {")

        assertTrue(layoutSource.contains("fun goodsPocketScreenModifier()"))
        assertTrue(screenModifierBlock.contains("start = 16.dp"))
        assertTrue(screenModifierBlock.contains("end = 16.dp"))
        assertFalse(screenModifierBlock.contains("top = 12.dp"))
        assertFalse(screenModifierBlock.contains("bottom = 12.dp"))
        assertTrue(layoutSource.contains("fun goodsPocketPrimaryScrollContentPadding()"))
        assertTrue(primaryPaddingBlock.contains("PaddingValues(top = 12.dp, bottom = 28.dp)"))
        assertTrue(layoutSource.contains("fun goodsPocketSecondaryScrollContentPadding()"))
        assertTrue(secondaryPaddingBlock.contains("PaddingValues(top = 12.dp)"))

        primaryScreenSources().forEach { source ->
            assertTrue(source.contains("goodsPocketScreenModifier()"))
            assertTrue(source.contains("contentPadding = goodsPocketPrimaryScrollContentPadding()"))
            assertFalse(source.contains("padding(horizontal = 16.dp, vertical = 12.dp)"))
        }

        secondaryScreenSources().forEach { source ->
            assertTrue(source.contains("goodsPocketScreenModifier()"))
            assertTrue(source.contains("contentPadding = goodsPocketSecondaryScrollContentPadding()"))
        }
    }

    private fun goodsPocketAppSource(): String {
        val startingDirectory = File(checkNotNull(System.getProperty("user.dir")))
        val sourceFile = generateSequence(startingDirectory) { it.parentFile }
            .map { File(it, "composeApp/src/commonMain/kotlin/goods/pocket/app/presentation/GoodsPocketApp.kt") }
            .firstOrNull(File::exists)
            ?: error("Could not locate GoodsPocketApp.kt from ${startingDirectory.absolutePath}")

        return sourceFile.readText()
    }

    private fun goodsPocketSurfaceSource(): String {
        val startingDirectory = File(checkNotNull(System.getProperty("user.dir")))
        val sourceFile = generateSequence(startingDirectory) { it.parentFile }
            .map { File(it, "composeApp/src/commonMain/kotlin/goods/pocket/app/presentation/designsystem/GoodsPocketSurface.kt") }
            .firstOrNull(File::exists)
            ?: error("Could not locate GoodsPocketSurface.kt from ${startingDirectory.absolutePath}")

        return sourceFile.readText()
    }

    private fun bottomNavPreviewFile(): File {
        val startingDirectory = File(checkNotNull(System.getProperty("user.dir")))
        return generateSequence(startingDirectory) { it.parentFile }
            .map { File(it, "composeApp/src/androidMain/kotlin/goods/pocket/app/BottomNavPreview.kt") }
            .firstOrNull(File::exists)
            ?: File(startingDirectory, "composeApp/src/androidMain/kotlin/goods/pocket/app/BottomNavPreview.kt")
    }

    private fun screenLayoutSource(): String {
        val startingDirectory = File(checkNotNull(System.getProperty("user.dir")))
        val sourceFile = generateSequence(startingDirectory) { it.parentFile }
            .map { File(it, "composeApp/src/commonMain/kotlin/goods/pocket/app/presentation/designsystem/GoodsPocketLayout.kt") }
            .firstOrNull(File::exists)
            ?: error("Could not locate GoodsPocketLayout.kt from ${startingDirectory.absolutePath}")

        return sourceFile.readText()
    }

    private fun primaryScreenSources(): List<String> {
        val startingDirectory = File(checkNotNull(System.getProperty("user.dir")))
        val rootDirectory = generateSequence(startingDirectory) { it.parentFile }
            .firstOrNull { File(it, "composeApp/src/commonMain/kotlin/goods/pocket/app/presentation/screen").exists() }
            ?: error("Could not locate presentation/screen directory from ${startingDirectory.absolutePath}")

        return listOf(
            "HomeScreen.kt",
            "CollectionScreen.kt",
            "PreordersScreen.kt",
            "MyScreen.kt",
        ).map { name ->
            File(rootDirectory, "composeApp/src/commonMain/kotlin/goods/pocket/app/presentation/screen/$name").readText()
        }
    }

    private fun secondaryScreenSources(): List<String> {
        val startingDirectory = File(checkNotNull(System.getProperty("user.dir")))
        val rootDirectory = generateSequence(startingDirectory) { it.parentFile }
            .firstOrNull { File(it, "composeApp/src/commonMain/kotlin/goods/pocket/app/presentation/screen").exists() }
            ?: error("Could not locate presentation/screen directory from ${startingDirectory.absolutePath}")

        return listOf(
            "EventsScreen.kt",
            "SettingsScreen.kt",
        ).map { name ->
            File(rootDirectory, "composeApp/src/commonMain/kotlin/goods/pocket/app/presentation/screen/$name").readText()
        }
    }
}
