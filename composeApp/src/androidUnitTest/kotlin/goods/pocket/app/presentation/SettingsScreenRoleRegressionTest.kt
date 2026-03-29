package goods.pocket.app.presentation

import java.io.File
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SettingsScreenRoleRegressionTest {

    @Test
    fun `settings screen focuses on language and display format only`() {
        val settingsSource = source("composeApp/src/commonMain/kotlin/goods/pocket/app/presentation/screen/SettingsScreen.kt")
        val appSource = source("composeApp/src/commonMain/kotlin/goods/pocket/app/presentation/GoodsPocketApp.kt")
        val appPreferenceSource = source("composeApp/src/commonMain/kotlin/goods/pocket/app/domain/model/AppPreference.kt")
        val stateHolderSource = source("composeApp/src/commonMain/kotlin/goods/pocket/app/presentation/state/GoodsPocketAppStateHolder.kt")
        val supportSource = source("composeApp/src/commonMain/kotlin/goods/pocket/app/presentation/state/GoodsPocketAppStateHolderSupport.kt")
        val localDataSource = source("composeApp/src/commonMain/kotlin/goods/pocket/app/data/local/SqlDelightGoodsPocketLocalDataSource.kt")

        assertFalse(settingsSource.contains("settings_default_start_tab"))
        assertFalse(settingsSource.contains("settings_storage_presets"))
        assertFalse(settingsSource.contains("settings_storage_preset_fallback"))
        assertFalse(settingsSource.contains("storageLocations: List<StorageLocation>"))
        assertFalse(settingsSource.contains("onStartTabChange"))
        assertFalse(settingsSource.contains("AppDestination.primaryDestinations"))
        assertTrue(settingsSource.contains("settings_quick_preferences"))
        assertTrue(settingsSource.contains("settings_display_format"))

        assertFalse(appSource.contains("storageLocations = uiState.storageLocations"))
        assertFalse(appSource.contains("onStartTabChange = appStateHolder::updateStartTab"))

        assertFalse(appPreferenceSource.contains("startTabRoute"))
        assertFalse(stateHolderSource.contains("fun updateStartTab("))
        assertFalse(supportSource.contains("primaryDestinationForRoute("))
        assertFalse(supportSource.contains("migrateLegacyStartTabPreference"))
        assertFalse(localDataSource.contains("startTabRoute = row.start_tab_route"))
        assertFalse(localDataSource.contains("start_tab_route = preferences.startTabRoute"))
    }

    @Test
    fun `settings resources no longer mention default start tab or storage presets`() {
        val koreanStrings = source("composeApp/src/commonMain/composeResources/values/strings.xml")
        val englishStrings = source("composeApp/src/commonMain/composeResources/values-en/strings.xml")

        assertFalse(koreanStrings.contains("name=\"settings_default_start_tab\""))
        assertFalse(koreanStrings.contains("name=\"settings_storage_presets\""))
        assertFalse(koreanStrings.contains("name=\"settings_storage_preset_fallback\""))
        assertFalse(koreanStrings.contains("앱 설정과 기본 시작 탭을 관리해요."))
        assertTrue(koreanStrings.contains("name=\"settings_quick_preferences\""))
        assertTrue(koreanStrings.contains("name=\"settings_display_format\""))

        assertFalse(englishStrings.contains("name=\"settings_default_start_tab\""))
        assertFalse(englishStrings.contains("name=\"settings_storage_presets\""))
        assertFalse(englishStrings.contains("name=\"settings_storage_preset_fallback\""))
        assertFalse(englishStrings.contains("Manage app settings and the default start tab."))
        assertTrue(englishStrings.contains("name=\"settings_quick_preferences\""))
        assertTrue(englishStrings.contains("name=\"settings_display_format\""))
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
