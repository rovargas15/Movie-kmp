import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import app.cash.paging.compose.collectAsLazyPagingItems
import model.Movie
import org.koin.compose.koinInject

@Composable
fun ScreenPaging(
    viewmodel: PagingViewmodel = koinInject(),
    category: String,
    onViewDetail: (Movie) -> Unit,
    onBackPress: () -> Unit,
) {
    viewmodel.onLoad(category)
    HandleState(
        viewmodel = viewmodel,
        onViewDetail = onViewDetail,
        onBackPress = onBackPress,
    )

    val action: (PagingAction) -> Unit = {
        viewmodel.handleAction(it)
    }

    viewmodel.observeLifecycleEvents(lifecycle = LocalLifecycleOwner.current.lifecycle)

    TopBarMovie(
        action = action,
        content = { paddingValues ->
            Column(
                modifier = Modifier.padding(paddingValues),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                PagingGrid(
                    data = viewmodel.result.collectAsLazyPagingItems(),
                    content = {
                        MovieItem(
                            movie = it,
                            action = action,
                        )
                    },
                )
            }
        },
    )
}

@Composable
private fun HandleState(
    viewmodel: PagingViewmodel,
    onViewDetail: (Movie) -> Unit,
    onBackPress: () -> Unit,
) {

    DisposableEffect(Unit) {
        onDispose {
            viewmodel.handleAction(PagingAction.Init)
        }
    }

    val state by viewmodel.uiState.collectAsState()

    when (state) {
        is PagingUiState.Init -> {
            viewmodel.handleAction(PagingAction.Init)
        }

        is PagingUiState.Loading -> {
            Loading()
        }

        is PagingUiState.Error -> {
        }

        is PagingUiState.OnShowDetail -> {
            val detail = (state as PagingUiState.OnShowDetail)
            LaunchedEffect(Unit) {
                onViewDetail(detail.movie)
            }
        }

        is PagingUiState.Success -> {
        }

        PagingUiState.OnBackPress -> {
            LaunchedEffect(Unit) {
                onBackPress()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBarMovie(
    content: @Composable (PaddingValues) -> Unit,
    action: (PagingAction) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Movies") },
                navigationIcon = {
                    IconButton(onClick = { action(PagingAction.OnBackPress) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                },
            )
        },
        content = content,
    )
}

@Composable
private fun MovieItem(
    movie: Movie,
    action: (PagingAction) -> Unit,
) {
    Card(
        modifier =
        Modifier.padding(start = 10.dp).clickable {
            action(PagingAction.OnSelectMovie(movie))
        },
    ) {
        Box {
            LoaderImage(movie.posterPath, Modifier.fillMaxSize())
        }
    }
}
