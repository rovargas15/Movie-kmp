package detail

import LoaderImage
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import model.Movie
import model.MovieDetail
import model.MovieImage
import moe.tlaster.precompose.lifecycle.Lifecycle
import moe.tlaster.precompose.lifecycle.LifecycleObserver
import moe.tlaster.precompose.lifecycle.LifecycleRegistry
import toDateFormat
import toHour
import toVote
import kotlin.math.min

@Composable
fun ScreenDetailMovie(
    detailViewmodel: DetailViewmodel,
    onBackPress: () -> Unit,
) {
    ManagerState(detailViewmodel, onBackPress)

    ContentMovieDetail(
        action = {
            detailViewmodel.managerAction(it)
        },
        movie = detailViewmodel.movie,
        movieDetail = detailViewmodel.movieDetail,
        movieImage = detailViewmodel.movieImage,
    )
}

@Composable
private fun ManagerState(
    viewmodel: DetailViewmodel,
    onBackPress: () -> Unit,
) {
    LifecycleRegistry().addObserver(
        observer =
            object : LifecycleObserver {
                override fun onStateChanged(state: Lifecycle.State) {
                    if (state == Lifecycle.State.Initialized) {
                        viewmodel.getData()
                    }
                }
            },
    )

    DisposableEffect(Unit) {
        onDispose {
            viewmodel.managerAction(DetailMovieAction.CleanStatus)
        }
    }

    val state by viewmodel.uiState.collectAsState()
    when (state) {
        DetailMovieUiState.Init -> {
        }
        DetailMovieUiState.OnBack -> {
            LaunchedEffect(Unit) {
                onBackPress()
            }
        }
        is DetailMovieUiState.Success -> {
        }
    }
}

@Composable
private fun ContentMovieDetail(
    action: (DetailMovieAction) -> Unit,
    movie: Movie?,
    movieDetail: MovieDetail?,
    movieImage: MovieImage?,
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier.verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Box {
            LoaderImage(
                url = movie?.backdropPath ?: "",
                modifier =
                    Modifier.fillMaxWidth().height(250.dp)
                        .graphicsLayer {
                            alpha = min(1f, 1 - (scrollState.value / 600f))
                            translationY = -scrollState.value * 0.1f
                        },
            )

            IconButton(
                modifier =
                    Modifier.padding(10.dp).align(Alignment.TopStart)
                        .graphicsLayer {
                            alpha = min(1f, 1 - (scrollState.value / 600f))
                            translationY = -scrollState.value * 0.1f
                        },
                onClick = {
                    action(DetailMovieAction.OnBackPress)
                },
            ) {
                Icon(
                    modifier =
                        Modifier.size(35.dp)
                            .background(Color.LightGray.copy(0.2f), shape = CircleShape),
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Volver",
                )
            }

            IconButton(
                modifier =
                    Modifier.padding(10.dp).align(Alignment.TopEnd),
                onClick = {
                    action(DetailMovieAction.AddFavorite(movie = movie!!))
                },
            ) {
                Icon(
                    modifier =
                        Modifier.size(35.dp),
                    imageVector = Icons.Filled.FavoriteBorder,
                    contentDescription = "favorite",
                )
            }

            Row(
                modifier =
                    Modifier.align(Alignment.CenterStart)
                        .padding(top = 140.dp, start = 12.dp),
            ) {
                Box {
                    ElevatedCard(modifier = Modifier.width(150.dp).height(220.dp)) {
                        LoaderImage(movie?.posterPath ?: "", modifier = Modifier.fillMaxSize())
                    }
                }
                Column(
                    modifier =
                        Modifier.fillMaxWidth().padding(
                            top = 120.dp,
                            start = 8.dp,
                            end = 12.dp,
                        ),
                ) {
                    Text(
                        text = movie?.originalTitle ?: "",
                        style = MaterialTheme.typography.titleLarge,
                    )
                    Row {
                        Icon(
                            modifier =
                                Modifier.size(20.dp),
                            imageVector = Icons.Filled.Star,
                            contentDescription = "vote",
                            tint = Color(0xFFFEB800),
                        )
                        Text(
                            text = movie?.voteAverage?.toVote() ?: "",
                            modifier = Modifier.align(Alignment.CenterVertically),
                            style = MaterialTheme.typography.labelSmall,
                        )
                    }
                    movieDetail?.genres?.let { genres ->
                        LazyRow(
                            modifier = Modifier.fillMaxWidth().height(40.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            items(genres) {
                                itemGenre(it)
                            }
                        }
                    }
                }
            }
        }
        movieDetail?.let {
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    itemLabelRow("Duración")
                    itemRow(movieDetail.runtime.toHour())
                }

                Column(modifier = Modifier.weight(1f)) {
                    itemLabelRow("Estreno")
                    itemRow(movieDetail.releaseDate.toDateFormat())
                }

                Column(modifier = Modifier.weight(1f)) {
                    itemLabelRow("Lenguaje original")
                    itemRow(movieDetail.originalLanguage)
                }
            }
        }

        Text(
            "Descripción",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 12.dp),
        )

        Text(
            text = movie?.overview ?: "",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(horizontal = 12.dp),
        )

        movieImage?.backdrops?.let {
            Text(
                "Imagenes",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 12.dp),
            )

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(it) { image ->
                    ElevatedCard(modifier = Modifier.width(250.dp)) {
                        LoaderImage(
                            url = image.filePath,
                            modifier =
                                Modifier.fillMaxWidth(),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun itemLabelRow(text: String) {
    Text(
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth(),
        text = text,
        style = MaterialTheme.typography.labelSmall,
    )
}

@Composable
private fun itemRow(text: String) {
    Text(
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth(),
        text = text,
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.bodyLarge,
    )
}

@Composable
private fun itemGenre(genre: MovieDetail.Genre) {
    Box(
        modifier =
            Modifier
                .background(Color.Blue.copy(alpha = .1f), shape = CircleShape),
    ) {
        Text(
            text = genre.name,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(8.dp),
        )
    }
}
