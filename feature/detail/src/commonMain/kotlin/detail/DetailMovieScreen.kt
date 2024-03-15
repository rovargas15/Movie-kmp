package detail

import LoaderImage
import Loading
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
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
import androidx.compose.ui.unit.dp
import model.Movie
import moe.tlaster.precompose.lifecycle.Lifecycle
import moe.tlaster.precompose.lifecycle.LifecycleObserver
import moe.tlaster.precompose.lifecycle.LifecycleRegistry
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
                        viewmodel.getMovieById()
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
        DetailMovieUiState.Error -> {
        }
        DetailMovieUiState.Init -> {
        }
        DetailMovieUiState.Loading -> {
            Loading()
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
) {
    val scrollState = rememberScrollState()
    Column(modifier = Modifier.verticalScroll(scrollState)) {
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
                            .background(Color.LightGray.copy(0.4f), shape = CircleShape),
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Volver",
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
                    Text(text = movie?.title ?: "", style = MaterialTheme.typography.titleMedium)
                    Text(text = movie?.releaseDate ?: "")

                    Row {
                        Icon(
                            modifier =
                                Modifier.size(20.dp),
                            imageVector = Icons.Filled.Star,
                            contentDescription = "vote",
                            tint = Color.LightGray,
                        )
                        Text(
                            text = movie?.voteAverage.toString(),
                            modifier = Modifier.align(Alignment.CenterVertically),
                        )
                    }
                }
            }
        }

        Text(
            text = movie?.overview ?: "",
            modifier = Modifier.padding(horizontal = 12.dp).padding(top = 8.dp),
        )
    }
}
