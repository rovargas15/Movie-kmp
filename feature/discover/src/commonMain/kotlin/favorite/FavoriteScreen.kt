package favorite

import LoaderImage
import Loading
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import model.Movie
import movies.BottomNavRoute
import movies.bottomNavItems

@Composable
fun ScreenFavorite(
    viewmodel: FavoriteViewmodel,
    onSelectMenu: (BottomNavRoute) -> Unit,
    onViewDetail: (Movie) -> Unit,
) {
    ManagerState(
        viewmodel = viewmodel,
        onSelectMenu = onSelectMenu,
        onViewDetail = onViewDetail,
    )

    val action: (FavoriteAction) -> Unit = {
        viewmodel.managerAction(it)
    }

    TopBarMovie(
        bottomNavRoute = viewmodel.bottomNavRoute,
        content = { paddingValues ->
            MoviesList(
                modifier = Modifier.padding(paddingValues),
                movies = viewmodel.moviesFavorite,
                action = action,
            )
        },
        action = action,
    )
}

@Composable
private fun ManagerState(
    viewmodel: FavoriteViewmodel,
    onSelectMenu: (BottomNavRoute) -> Unit,
    onViewDetail: (Movie) -> Unit,
) {
    DisposableEffect(Unit) {
        onDispose {
            viewmodel.managerAction(FavoriteAction.CleanStatus)
        }
    }

    val state by viewmodel.uiState.collectAsState()

    when (state) {
        is FavoriteUiState.Init -> {
            viewmodel.managerAction(FavoriteAction.Init)
        }

        is FavoriteUiState.Loading -> {
            Loading()
        }

        is FavoriteUiState.Error -> {
        }

        is FavoriteUiState.OnShowDetail -> {
            val detail = (state as FavoriteUiState.OnShowDetail)
            LaunchedEffect(Unit) {
                onViewDetail(detail.movie)
            }
        }

        is FavoriteUiState.Success -> {
        }

        is FavoriteUiState.OnShowOptionMenu -> {
            val menu = (state as FavoriteUiState.OnShowOptionMenu)
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
    action: (FavoriteAction) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Movies") },
            )
        },
        bottomBar = {
            BottomNavigationBar(
                action = action,
                bottomNavRoute = bottomNavRoute,
            )
        },
        content = content,
    )
}

@Composable
private fun BottomNavigationBar(
    action: (FavoriteAction) -> Unit,
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
                    onClick = { action(FavoriteAction.OnSelectMenu(item)) },
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
    action: (FavoriteAction) -> Unit,
    modifier: Modifier,
) {
    LazyVerticalStaggeredGrid(
        modifier = modifier,
        columns = StaggeredGridCells.Adaptive(200.dp),
        verticalItemSpacing = 4.dp,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
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
    action: (FavoriteAction) -> Unit,
) {
    Card(
        modifier =
            Modifier.padding(start = 10.dp).clickable {
                action(FavoriteAction.OnSelectMovie(movie))
            },
    ) {
        Box {
            LoaderImage(movie.posterPath, Modifier.fillMaxSize())
        }
    }
}
