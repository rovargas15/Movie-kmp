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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import model.Movie

@Composable
fun ScreenMovies(
    viewmodel: MoviesViewmodel,
    onSelectMenu: (BottomNavRoute) -> Unit,
    onViewDetail: (Movie) -> Unit,
) {
    val movies = remember { mutableStateOf(listOf<Movie>()) }

    ManagerState(
        viewmodel = viewmodel,
        movies = movies,
        onSelectMenu = onSelectMenu,
        onViewDetail = onViewDetail,
    )

    val action: (MovieAction) -> Unit = {
        viewmodel.managerAction(it)
    }

    TopBarMovie(
        bottomNavRoute = viewmodel.bottomNavRoute,
        content = { paddingValues ->
            MoviesList(
                paddingValues = paddingValues,
                movies = movies.value,
                action = action,
            )
        },
        movieAction = action,
    )
}

@Composable
private fun ManagerState(
    viewmodel: MoviesViewmodel,
    movies: MutableState<List<Movie>>,
    onSelectMenu: (BottomNavRoute) -> Unit,
    onViewDetail: (Movie) -> Unit,
) {
    DisposableEffect(Unit) {
        onDispose {
            viewmodel.managerAction(MovieAction.CleanStatus)
        }
    }

    val state by viewmodel.uiState.collectAsState()

    when (state) {
        is MovieUiState.Init -> {
            viewmodel.managerAction(MovieAction.Init)
        }

        is MovieUiState.Loading -> {
            Loading()
        }

        is MovieUiState.Success -> {
            val success = (state as MovieUiState.Success)
            movies.value = success.movies
        }

        is MovieUiState.Error -> {
        }

        is MovieUiState.OnShowDetail -> {
            val detail = (state as MovieUiState.OnShowDetail)
            LaunchedEffect(Unit) {
                onViewDetail(detail.movie)
            }
        }

        is MovieUiState.OnShowOptionMenu -> {
            val menu = (state as MovieUiState.OnShowOptionMenu)
            LaunchedEffect(Unit) {
                onSelectMenu(menu.bottomNavRoute)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBarMovie(
    bottomNavRoute: BottomNavRoute,
    content: @Composable (PaddingValues) -> Unit,
    movieAction: (MovieAction) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(bottomNavRoute.label) })
        },
        bottomBar = {
            BottomNavigationBar(
                movieAction = movieAction,
                bottomNavRoute = bottomNavRoute,
            )
        },
        content = content,
    )
}

@Composable
private fun BottomNavigationBar(
    movieAction: (MovieAction) -> Unit,
    bottomNavRoute: BottomNavRoute,
) {
    BottomAppBar(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            bottomNavItems.forEach { item ->
                NavigationBarItem(
                    selected = bottomNavRoute.route == item.route,
                    onClick = { movieAction(MovieAction.OnSelectMenu(item)) },
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
    action: (MovieAction) -> Unit,
) {
    LazyVerticalGrid(
        GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.padding(paddingValues).padding(start = 8.dp, end = 8.dp),
    ) {
        items(movies) {
            MovieItem(it, action)
        }
    }
}

@Composable
private fun MovieItem(
    movie: Movie,
    action: (MovieAction) -> Unit,
) {
    Card(
        modifier =
            Modifier.clickable {
                action(MovieAction.OnSelectMovie(movie))
            },
    ) {
        Box {
            LoaderImage(movie.posterPath, Modifier.fillMaxSize())
        }
    }
}
