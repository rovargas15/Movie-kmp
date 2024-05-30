import androidx.compose.runtime.Composable
import org.koin.compose.KoinContext
import ui.theme.MyApplicationTheme

@Composable
fun App() {
    MyApplicationTheme {
        KoinContext {
            NavigatorApp()
        }
    }
}
