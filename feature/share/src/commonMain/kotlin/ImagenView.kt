import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.seiko.imageloader.rememberImagePainter

@Composable
fun LoaderImage(
    url: String,
    modifier: Modifier,
) {
    val painter = rememberImagePainter("https://image.tmdb.org/t/p/original/$url")
    Image(
        modifier = modifier,
        painter = painter,
        contentDescription = "image",
        contentScale = ContentScale.Crop,
    )
}

@Composable
fun LoaderImagePoster(
    url: String,
    modifier: Modifier = Modifier,
) {
    val painter = rememberImagePainter("https://image.tmdb.org/t/p/w500/$url")
    Image(
        modifier = modifier,
        painter = painter,
        contentDescription = "poster",
        contentScale = ContentScale.Inside,
    )
}
