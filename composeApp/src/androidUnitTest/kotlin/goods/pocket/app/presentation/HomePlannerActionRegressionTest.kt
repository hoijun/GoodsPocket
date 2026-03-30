package goods.pocket.app.presentation

import java.io.File
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class HomePlannerActionRegressionTest {

    @Test
    fun `home planner cards use localized copy and real callbacks`() {
        val homeSource = source("composeApp/src/commonMain/kotlin/goods/pocket/app/presentation/screen/HomeScreen.kt")
        val appSource = source("composeApp/src/commonMain/kotlin/goods/pocket/app/presentation/GoodsPocketApp.kt")

        assertFalse(homeSource.contains("\"수령 대기\""))
        assertFalse(homeSource.contains("\"지출 보기\""))
        assertFalse(homeSource.contains("\"일정 보기\""))
        assertFalse(homeSource.contains("\"빠른 추가\""))
        assertTrue(homeSource.contains("home_planner_preorders_count"))
        assertFalse(homeSource.contains("home_planner_transactions"))
        assertTrue(homeSource.contains("home_planner_events_count"))

        assertTrue(appSource.contains("onPreordersClick = { appStateHolder.selectDestination(AppDestination.Preorders) }"))
        assertTrue(appSource.contains("onQuickAddClick = appStateHolder::openQuickAdd"))
        assertFalse(appSource.contains("onMonthlySummaryClick = appStateHolder::openTransactionsOverview"))
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
