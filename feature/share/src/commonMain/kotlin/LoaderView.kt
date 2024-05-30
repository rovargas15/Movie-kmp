import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.movie.kmp.Res
import com.movie.kmp.ic_movie
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun Loading(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize(),
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun iconLogo(modifier: Modifier) {
    Icon(
        modifier = modifier,
        tint = Color.Red,
        painter =
        painterResource(
            resource = Res.drawable.ic_movie,
        ),
        contentDescription = "Logo",
    )
}

@Composable
fun ErrorDialog(
    title: String,
    message: String,
    showErrorDialog: Boolean,
    onCloseDialog: () -> Unit,
) {
    if (showErrorDialog) {
        Dialog(
            onDismissRequest = { onCloseDialog() },
            properties = DialogProperties(dismissOnClickOutside = false),
        ) {
            OutlinedCard(
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(16.dp),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.Black,
                    )
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Button(
                        onClick = { onCloseDialog() },
                        modifier = Modifier.align(Alignment.End),
                    ) {
                        Text(text = "Aceptar", style = MaterialTheme.typography.labelMedium)
                    }
                }
            }
        }
    }
}
