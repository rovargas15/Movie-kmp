import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import di.koinModules
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin

fun main() =
    application {
        initKoin()
        Window(onCloseRequest = ::exitApplication, title = "Movie KMP") {
            App()
        }
    }

fun initKoin(): KoinApplication {
    return startKoin {
        modules(koinModules + dataBase)
    }
}
