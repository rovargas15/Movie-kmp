package movies

import LoaderImage
import Loading
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import model.Movie
import observeLifecycleEvents
import org.koin.compose.koinInject
import toVote

@Composable
fun ScreenSearch(
    viewmodel: SearchViewmodel = koinInject(),
    onBackPress: () -> Unit,
    onViewDetail: (Movie) -> Unit,
) {
    viewmodel.observeLifecycleEvents(lifecycle = LocalLifecycleOwner.current.lifecycle)
    val query by rememberSaveable { viewmodel.query }
    val movies by rememberSaveable { viewmodel.movies }

    HandleState(
        viewmodel = viewmodel,
        onDetailMovie = onViewDetail,
        onBackPress = onBackPress,
    )

    val action: (SearchAction) -> Unit = {
        viewmodel.handlerAction(it)
    }

    Scaffold(
        content = { paddingValuest: PaddingValues ->
            Column(
                modifier = Modifier.padding(paddingValuest),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                SearchView(
                    query = query,
                    action = action,
                )

                MoviesList(
                    movies = movies,
                    action = action,
                )
            }
        },
    )
}

@Composable
private fun HandleState(
    viewmodel: SearchViewmodel,
    onBackPress: () -> Unit,
    onDetailMovie: (Movie) -> Unit,
) {

    DisposableEffect(Unit) {
        onDispose {
            viewmodel.handlerAction(SearchAction.CleanStatus)
        }
    }

    val state by viewmodel.uiState.collectAsState()

    when (state) {
        is SearchUiState.Init -> {
            viewmodel.handlerAction(SearchAction.Init)
        }

        is SearchUiState.Loading -> {
            Loading()
        }

        is SearchUiState.Error -> {
        }

        is SearchUiState.OnDetailMovie -> {
            LaunchedEffect(Unit) {
                onDetailMovie((state as SearchUiState.OnDetailMovie).movie)
            }
        }

        SearchUiState.OnBackPress -> {
            LaunchedEffect(Unit) {
                onBackPress()
            }
        }
    }
}

@Composable
private fun SearchView(
    query: String,
    action: (SearchAction) -> Unit,
) {
    val localSoftwareKeyboardController = LocalSoftwareKeyboardController.current

    Card(
        modifier =
        Modifier.padding(
            all = 12.dp,
        ),
        elevation = CardDefaults.cardElevation(8.dp),
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = {
                action(SearchAction.QueryMovie(it))
            },
            modifier = Modifier.fillMaxWidth().padding(5.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions =
            KeyboardActions(
                onSearch = {
                    localSoftwareKeyboardController?.hide()
                    action(SearchAction.Search)
                },
            ),
            singleLine = true,
            trailingIcon = {
                Icon(
                    Icons.Outlined.Clear,
                    "contentDescription",
                    modifier =
                    Modifier.padding(4.dp).clickable {
                        action(SearchAction.QueryMovie(""))
                    },
                )
            },
            leadingIcon = {
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    "contentDescription",
                    modifier =
                    Modifier.clickable {
                        action(SearchAction.OnBackPress)
                    },
                )
            },
        )
    }
}

@Composable
private fun MoviesList(
    movies: List<Movie>,
    action: (SearchAction) -> Unit,
) {
    LazyVerticalStaggeredGrid(
        modifier = Modifier.padding(top = 12.dp),
        columns = StaggeredGridCells.Fixed(1),
        verticalItemSpacing = 6.dp,
        content = {
            items(movies) {
                MovieItem(it, action)
            }
        },
    )
}

@Composable
private fun MovieItem(
    movie: Movie,
    action: (SearchAction) -> Unit,
) {
    ElevatedCard(
        modifier =
        Modifier.padding(start = 8.dp, end = 8.dp).clickable {
            action(SearchAction.OnSelectMovie(movie))
        },
    ) {
        Row {
            LoaderImage(
                url = movie.posterPath,
                modifier = Modifier.width(150.dp).height(220.dp),
            )
            Column(modifier = Modifier.fillMaxWidth().padding(start = 4.dp)) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.titleMedium,
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
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
                Text(
                    text = movie.overview,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 5,
                )
            }
        }
    }
}
