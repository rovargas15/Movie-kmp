package movies

import LoaderImage
import Loading
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import model.Movie
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.lifecycle.Lifecycle
import moe.tlaster.precompose.lifecycle.LifecycleObserver
import moe.tlaster.precompose.lifecycle.LifecycleRegistry

@Composable
fun ScreenMovies(
    route: String,
    onSelectOption: (BottomNavRoute) -> Unit,
    onSelectMovie: (Movie) -> Unit,
) {
    val viewmodel = koinViewModel(MoviesViewmodel::class)
    LifecycleRegistry().addObserver(
        observer = object : LifecycleObserver {
            override fun onStateChanged(state: Lifecycle.State) {
                if (state == Lifecycle.State.Initialized) {
                    viewmodel.getMovies(route)
                }
            }
        },
    )
    val state by viewmodel.uiState.collectAsStateWithLifecycle()
    ContentMovie(
        state = state,
        onSelectOption = onSelectOption,
        route = route,
        onSelectMovie = onSelectMovie,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ContentMovie(
    state: MovieUiState,
    onSelectOption: (BottomNavRoute) -> Unit,
    route: String,
    onSelectMovie: (Movie) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Películas $route") })
        },
        bottomBar = {
            BottomNavigationBar(onSelectOption, route)
        },
        content = {
            when (state) {
                is MovieUiState.Success -> {
                    MoviesList(it, state.movies, onSelectMovie)
                }

                is MovieUiState.Error -> {
                }

                else -> {
                    Loading()
                }
            }
        },
    )
}

@Composable
private fun BottomNavigationBar(
    onSelectOption: (BottomNavRoute) -> Unit,
    route: String,
) {
    BottomAppBar(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            bottomNavItems.forEach { item ->
                NavigationBarItem(
                    selected = route == item.route,
                    onClick = { onSelectOption(item) },
                    label = {
                        Text(
                            text = item.label,
                            fontWeight = FontWeight.SemiBold,
                        )
                    },
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = "${item.label} Icon",
                        )
                    },
                )
            }
        }
    }
}

@Composable
private fun MoviesList(
    paddingValues: PaddingValues,
    movies: List<Movie>,
    onSelectMovie: (Movie) -> Unit,
) {
    LazyVerticalGrid(
        GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.padding(paddingValues).padding(start = 8.dp, end = 8.dp),
    ) {
        items(movies) {
            MovieItem(it, onSelectMovie)
        }
    }
}

@Composable
private fun MovieItem(
    movie: Movie,
    onSelectMovie: (Movie) -> Unit,
) {
    Card(
        modifier = Modifier.clickable {
            onSelectMovie(movie)
        },
    ) {
        Box {
            LoaderImage(movie.posterPath, Modifier.fillMaxSize())
        }
    }
}
