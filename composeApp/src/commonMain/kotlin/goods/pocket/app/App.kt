package goods.pocket.app

import androidx.compose.runtime.Composable
import goods.pocket.app.di.GoodsPocketAppModule
import goods.pocket.app.presentation.GoodsPocketApp
import goods.pocket.app.presentation.designsystem.GoodsPocketTheme
import org.koin.compose.KoinApplication
import org.koin.dsl.koinConfiguration
import org.koin.ksp.generated.module

@Composable
fun App() {
    GoodsPocketTheme {
        KoinApplication(
            configuration = koinConfiguration {
                modules(GoodsPocketAppModule().module)
            },
        ) {
            GoodsPocketApp()
        }
    }
}
