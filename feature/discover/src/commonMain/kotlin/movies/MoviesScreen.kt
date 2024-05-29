package movies

import Category.POPULAR
import Category.TOP_RATED
import Category.UPCOMING
import LoaderImage
import Loading
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.movie.kmp.Res
import com.movie.kmp.title_popular
import com.movie.kmp.title_top_rated
import com.movie.kmp.title_upcoming
import model.Movie
import observeLifecycleEvents
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
fun ScreenMovies(
    viewmodel: MoviesViewmodel = koinInject(),
    onSelectMenu: (BottomNavRoute) -> Unit,
    onViewDetail: (Movie) -> Unit,
    onSearchScreen: () -> Unit,
    onPagingShow: (String) -> Unit,
) {
    HandleState(
        viewmodel = viewmodel,
        onSelectMenu = onSelectMenu,
        onViewDetail = onViewDetail,
        onSearchScreen = onSearchScreen,
        onPagingShow = onPagingShow,
    )

    val action: (MovieAction) -> Unit = {
        viewmodel.managerAction(it)
    }

    TopBarMovie(
        bottomNavRoute = viewmodel.bottomNavRoute,
        content = { paddingValues ->
            Column(
                modifier =
                    Modifier.padding(paddingValues).fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                TextCategory(
                    title = stringResource(Res.string.title_popular),
                    category = POPULAR,
                    action = action,
                )
                MoviesList(
                    movies = viewmodel.moviesPopular,
                    action = action,
                )
                TextCategory(
                    title = stringResource(resource = Res.string.title_top_rated),
                    category = TOP_RATED,
                    action = action,
                )
                MoviesList(
                    movies = viewmodel.moviesTop,
                    action = action,
                )

                TextCategory(
                    title = stringResource(Res.string.title_upcoming),
                    category = UPCOMING,
                    action = action,
                )
                MoviesList(
                    movies = viewmodel.moviesUpComing,
                    action = action,
                )
            }
        },
        movieAction = action,
    )
}

@Composable
private fun TextCategory(
    title: String,
    category: String,
    action: (MovieAction) -> Unit,
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier.weight(1f).padding(start = 10.dp),
            text = title,
            style = TextStyle().copy(fontSize = 18.sp, fontWeight = FontWeight.SemiBold),
        )

        Text(
            modifier =
                Modifier.weight(1f).padding(end = 10.dp).clickable {
                    action.invoke(MovieAction.OnPagingView(category))
                },
            text = "All",
            textAlign = TextAlign.End,
            style = TextStyle().copy(fontSize = 18.sp, fontWeight = FontWeight.Medium),
        )
    }
}

@Composable
private fun HandleState(
    viewmodel: MoviesViewmodel,
    onSelectMenu: (BottomNavRoute) -> Unit,
    onViewDetail: (Movie) -> Unit,
    onSearchScreen: () -> Unit,
    onPagingShow: (String) -> Unit,
) {
    DisposableEffect(Unit) {
        onDispose {
            viewmodel.managerAction(MovieAction.Init)
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

        is MovieUiState.Success -> {
        }

        MovieUiState.OnSearchView -> {
            LaunchedEffect(Unit) {
                onSearchScreen()
            }
        }

        is MovieUiState.OnPagingView -> {
            LaunchedEffect(Unit) {
                onPagingShow((state as MovieUiState.OnPagingView).category)
            }
        }
    }
    viewmodel.observeLifecycleEvents(lifecycle = LocalLifecycleOwner.current.lifecycle)
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
            TopAppBar(
                title = { Text("Movies") },
                actions = {
                    IconButton(
                        onClick = {
                            movieAction(MovieAction.OnSearchView)
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Search,
                            contentDescription = "Search",
                            tint = Color.Gray,
                        )
                    }
                },
            )
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
            modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
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
    movies: List<Movie>,
    action: (MovieAction) -> Unit,
) {
    LazyRow(
        modifier =
            Modifier.scrollable(
                state = rememberScrollState(),
                orientation = Orientation.Horizontal,
            ).height(250.dp),
    ) {
        items(movies) { movie ->
            MovieItem(movie, action)
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
            Modifier.padding(start = 10.dp).clickable {
                action(MovieAction.OnSelectMovie(movie))
            },
    ) {
        Box {
            LoaderImage(movie.posterPath, Modifier.fillMaxSize())
        }
    }
}
