package goods.pocket.app.presentation

import java.io.File
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class NavigationIconVisualContractTest {
    @Test
    fun selectedMyUsesFilledClosedSilhouetteWhileUnselectedKeepsOutline() {
        val source = iconSource().substringAfter("private fun GoodsPocketBottomNavIcon(")
        val myIcon = source.substringAfter("AppDestination.My -> {")
            .substringBefore("AppDestination.Settings -> Unit")
        assertTrue(myIcon.contains("if (selected) Fill else stroke"))
        assertTrue(myIcon.contains("if (selected) close()"))
        assertTrue(myIcon.contains("if (selected) Offset(0.44f, 0.39f)"))
        assertTrue(myIcon.contains("if (selected) 0.175f else 0.16f"))
        assertTrue(myIcon.contains("moveTo(size.width * 0.125f, size.height * 0.96f)"))
        assertTrue(myIcon.contains("size.width * 0.75f"))
        assertTrue(myIcon.contains("size.height * 0.52f"))
        assertTrue(source.contains(".size(ImageLockedNavigationMetrics.IconSize)"))
    }

    @Test
    fun navigationDrawingConvertsLogicalStrokeAndCornerTokensToPixels() {
        val source = iconSource().substringAfter("private fun GoodsPocketBottomNavIcon(")
        assertTrue(source.contains("NavigationIconDrawingMetrics.StrokeWidth.toPx()"))
        assertTrue(source.contains("NavigationIconDrawingMetrics.CornerRadius.toPx()"))
        assertFalse(source.contains("strokeWidth = 3.2f"))
        assertFalse(source.contains("Stroke(width = 3.2f)"))
        assertFalse(source.contains("CornerRadius(5f, 5f)"))
    }
}

private fun iconSource(): String {
    val cwd = File(checkNotNull(System.getProperty("user.dir")))
    val module = listOf(cwd, cwd.resolve("composeApp"))
        .first { it.resolve("src/commonMain").isDirectory }
    return module.resolve(
        "src/commonMain/kotlin/goods/pocket/app/presentation/designsystem/" +
            "GoodsPocketImageLockedComponents.kt",
    ).readText()
}
