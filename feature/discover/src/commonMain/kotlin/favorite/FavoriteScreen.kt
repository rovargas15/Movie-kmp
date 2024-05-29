package favorite

import ConfirmRemoveFavoriteDialog
import LoaderImage
import Loading
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import model.Movie
import movies.BottomNavRoute
import movies.bottomNavItems
import observeLifecycleEvents
import org.koin.compose.koinInject
import toVote

@Composable
fun ScreenFavorite(
    viewmodel: FavoriteViewmodel = koinInject(),
    onSelectMenu: (BottomNavRoute) -> Unit,
    onViewDetail: (Movie) -> Unit,
) {
    viewmodel.observeLifecycleEvents(lifecycle = LocalLifecycleOwner.current.lifecycle)
    HandleState(
        viewmodel = viewmodel,
        onSelectMenu = onSelectMenu,
        onViewDetail = onViewDetail,
    )

    val action: (FavoriteAction) -> Unit = {
        viewmodel.handleAction(it)
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
private fun HandleState(
    viewmodel: FavoriteViewmodel,
    onSelectMenu: (BottomNavRoute) -> Unit,
    onViewDetail: (Movie) -> Unit,
) {
    DisposableEffect(Unit) {
        onDispose {
            viewmodel.handleAction(FavoriteAction.CleanStatus)
        }
    }

    val state by viewmodel.uiState.collectAsState()

    when (state) {
        is FavoriteUiState.Init -> {
            viewmodel.handleAction(FavoriteAction.Init)
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

        is FavoriteUiState.OnConfirmRemoveFavorite -> {
            ConfirmRemoveFavoriteDialog(
                onDismissRequest = {
                    viewmodel.handleAction(FavoriteAction.CleanStatus)
                },
                onConfirm = {
                    viewmodel.handleAction(
                        FavoriteAction.RemoveMovieFavorite(
                            (state as FavoriteUiState.OnConfirmRemoveFavorite).movie,
                        ),
                    )
                },
                onDismiss = {
                    viewmodel.handleAction(FavoriteAction.CleanStatus)
                },
            )
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
    if (movies.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text(text = "No Movies")
        }
    } else {
        LazyVerticalStaggeredGrid(
            modifier = modifier.fillMaxSize(),
            columns = StaggeredGridCells.Fixed(1),
            verticalItemSpacing = 4.dp,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            content = {
                items(movies) {
                    MovieItem(it, action)
                }
            },
        )
    }
}

@Composable
private fun MovieItem(
    movie: Movie,
    action: (FavoriteAction) -> Unit,
) {
    ElevatedCard(
        modifier =
            Modifier.padding(start = 8.dp, end = 8.dp).clickable {
                action(FavoriteAction.OnSelectMovie(movie))
            },
    ) {
        Row {
            LoaderImage(
                url = movie.posterPath,
                modifier = Modifier.width(150.dp).height(220.dp),
            )
            Column(modifier = Modifier.fillMaxWidth().padding(start = 4.dp)) {
                Row {
                    Column(modifier = Modifier.weight(2f).padding(top = 8.dp)) {
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
                    }
                    IconButton(
                        modifier = Modifier.padding(3.dp),
                        onClick = {
                            action(FavoriteAction.ConfirmRemoveMovieFavorite(movie))
                        },
                    ) {
                        Icon(
                            modifier =
                                Modifier.size(35.dp),
                            imageVector = Icons.Filled.Favorite,
                            tint = Color.Red,
                            contentDescription = "favorite",
                        )
                    }
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
