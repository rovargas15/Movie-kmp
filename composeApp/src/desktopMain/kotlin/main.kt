import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import di.koinModules
import org.koin.core.context.startKoin

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "Movie") {
        startKoin {
            modules(koinModules)
        }
        App()
    }
}