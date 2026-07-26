package goods.pocket.app.presentation.screen

import java.io.File
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class HomeInteractionContractTest {
    @Test
    fun `home exposes one explicit action contract without empty callback defaults`() {
        val moduleDirectory = interactionModuleDirectory()
        val actionSource = moduleDirectory
            .resolve("src/commonMain/kotlin/goods/pocket/app/presentation/screen/HomeAction.kt")
        val screenSource = moduleDirectory
            .resolve("src/commonMain/kotlin/goods/pocket/app/presentation/screen/HomeScreen.kt")
            .readText()
        val appSource = moduleDirectory
            .resolve("src/commonMain/kotlin/goods/pocket/app/presentation/GoodsPocketApp.kt")
            .readText()

        assertTrue(actionSource.isFile)
        assertTrue(screenSource.contains("onAction: (HomeAction) -> Unit"))
        assertFalse(screenSource.contains("onUpcomingEventsClick: () -> Unit = {}"))
        assertFalse(screenSource.contains("onRecentActivitiesClick: () -> Unit = {}"))
        assertTrue(appSource.contains("HomeAction.OpenRecentCollection"))
        assertTrue(appSource.contains("HomeAction.OpenReservedCollection"))
        assertTrue(appSource.contains("is HomeAction.OpenRecentEntry"))
        assertTrue(appSource.contains("is HomeAction.OpenEvent"))
    }
}

private fun interactionModuleDirectory(): File {
    val workingDirectory = File(checkNotNull(System.getProperty("user.dir")))
    return listOf(workingDirectory, workingDirectory.resolve("composeApp"))
        .first { candidate -> candidate.resolve("src/commonMain").isDirectory }
}
