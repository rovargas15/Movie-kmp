package detail

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenDetailMovie(onBackPress: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Detail") })
        },
        content = {},
    )
}
