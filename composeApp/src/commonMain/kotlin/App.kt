import androidx.compose.runtime.Composable
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.navigation.rememberNavigator
import org.koin.compose.KoinContext

@Composable
fun App() {
    PreComposeApp {
        KoinContext {
            NavigatorApp(rememberNavigator())
        }
    }
}
