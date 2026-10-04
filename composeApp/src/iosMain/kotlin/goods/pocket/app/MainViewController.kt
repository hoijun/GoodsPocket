package goods.pocket.app

import androidx.compose.ui.window.ComposeUIViewController

// Keep the established exported factory name used by the Swift host.
@Suppress("ktlint:standard:function-naming")
fun MainViewController() = ComposeUIViewController { App() }
