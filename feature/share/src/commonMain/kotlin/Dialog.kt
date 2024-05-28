import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.movie.kmp.Res
import com.movie.kmp.cancel
import com.movie.kmp.remove
import com.movie.kmp.remove_movie_from_favorites
import com.movie.kmp.remove_movie_from_favorites_confirmation
import org.jetbrains.compose.resources.stringResource

@Composable
fun ConfirmRemoveFavoriteDialog(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = stringResource(Res.string.remove))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(Res.string.cancel))
            }
        },
        title = {
            Text(text = stringResource(Res.string.remove_movie_from_favorites))
        },
        text = {
            Text(text = stringResource(Res.string.remove_movie_from_favorites_confirmation))
        },
    )
}
