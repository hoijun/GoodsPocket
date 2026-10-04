package goods.pocket.app

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import goods.pocket.app.di.GoodsPocketAppModule
import goods.pocket.app.presentation.GoodsPocketApp
import goods.pocket.app.presentation.PresentationSessionViewModel
import goods.pocket.app.presentation.designsystem.GoodsPocketTheme
import org.koin.dsl.koinApplication
import org.koin.ksp.generated.module

@Composable
fun App() {
    val owner = viewModel<PresentationSessionViewModel> {
        val graph = koinApplication {
            modules(GoodsPocketAppModule().module)
        }
        try {
            PresentationSessionViewModel(graph.koin.get(), graph::close)
        } catch (error: Exception) {
            graph.close()
            throw error
        }
    }
    GoodsPocketTheme {
        GoodsPocketApp(owner.session)
    }
}
