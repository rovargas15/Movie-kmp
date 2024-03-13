import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kottieComposition.KottieCompositionSpec
import kottieComposition.animateKottieCompositionAsState
import kottieComposition.rememberKottieComposition

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
                        Text(text = "Aceptar", color = Color.White)
                    }
                }
            }
        }
    }
}
