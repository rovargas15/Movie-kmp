import androidx.compose.runtime.Composable
import org.koin.compose.KoinContext

@Composable
fun App() {
    KoinContext {
        NavigatorApp()
    }
}
