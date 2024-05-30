package detail

import ComposableLifecycle
import ConfirmRemoveFavoriteDialog
import LoaderImage
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import model.Movie
import model.MovieDetail
import model.MovieImage
import observeLifecycleEvents
import org.koin.compose.koinInject
import toDateFormat
import toHour
import toVote

@Composable
fun ScreenDetailMovie(
    detailViewmodel: DetailViewmodel = koinInject(),
    movieId: Int,
    onBackPress: () -> Unit,
) {
    HandleState(detailViewmodel, movieId, onBackPress)
    Scaffold(
        content = { paddingValuest: PaddingValues ->
            detailViewmodel.movie?.let { movie ->
                ContentMovieDetail(
                    modifier = Modifier.padding(paddingValuest),
                    action = {
                        detailViewmodel.handleAction(it)
                    },
                    movie = movie,
                    movieDetail = detailViewmodel.movieDetail,
                    movieImage = detailViewmodel.movieImage,
                )
            }
        },
    )
}

@Composable
private fun HandleState(
    viewmodel: DetailViewmodel,
    movieId: Int,
    onBackPress: () -> Unit,
) {
    ComposableLifecycle { _: LifecycleOwner, event: Lifecycle.Event ->
        if (event == Lifecycle.Event.ON_CREATE) {
            viewmodel.movieId = movieId
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            viewmodel.handleAction(DetailMovieAction.CleanStatus)
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

        is DetailMovieUiState.OnConfirmRemoveFavorite -> {
            ConfirmRemoveFavoriteDialog(
                onDismissRequest = {
                    viewmodel.handleAction(DetailMovieAction.CleanStatus)
                },
                onConfirm = {
                    viewmodel.handleAction(
                        DetailMovieAction.RemoveMovieFavorite(
                            (state as DetailMovieUiState.OnConfirmRemoveFavorite).movie,
                        ),
                    )
                },
                onDismiss = {
                    viewmodel.handleAction(DetailMovieAction.CleanStatus)
                },
            )
        }
    }
    viewmodel.observeLifecycleEvents(lifecycle = LocalLifecycleOwner.current.lifecycle)
}

@Composable
private fun ContentMovieDetail(
    modifier: Modifier,
    action: (DetailMovieAction) -> Unit,
    movie: Movie,
    movieDetail: MovieDetail?,
    movieImage: MovieImage?,
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Box {
            LoaderImage(
                url = movie.backdropPath,
                modifier = Modifier.fillMaxWidth().height(250.dp),
            )

            IconButton(
                modifier =
                Modifier.padding(10.dp).align(Alignment.TopStart),
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
                modifier = Modifier.padding(10.dp).align(Alignment.TopEnd),
                onClick = {
                    action(
                        if (movie.isFavorite) {
                            DetailMovieAction.ConfirmRemoveMovieFavorite(movie)
                        } else {
                            DetailMovieAction.SetMovieFavorite(
                                movie,
                            )
                        },
                    )
                },
            ) {
                Icon(
                    modifier = Modifier.size(35.dp),
                    imageVector =
                    if (movie.isFavorite) {
                        Icons.Filled.Favorite
                    } else {
                        Icons.Filled.FavoriteBorder
                    },
                    tint = if (movie.isFavorite) Color.Red else Color.Black,
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
                        LoaderImage(movie.posterPath, modifier = Modifier.fillMaxSize())
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
                        text = movie.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Row {
                        Icon(
                            modifier = Modifier.size(20.dp),
                            imageVector = Icons.Filled.Star,
                            contentDescription = "vote",
                            tint = Color(0xFFFEB800),
                        )
                        Text(
                            text = movie.voteAverage.toVote(),
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
            text = movie.overview,
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
                            modifier = Modifier.fillMaxWidth(),
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
        style = MaterialTheme.typography.bodyLarge,
    )
}

@Composable
private fun itemGenre(genre: MovieDetail.Genre) {
    Box(
        modifier = Modifier.background(Color.Blue.copy(alpha = .1f), shape = CircleShape),
    ) {
        Text(
            text = genre.name,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(8.dp),
        )
    }
}
