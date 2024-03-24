import androidx.compose.animation.slideIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.IntOffset
import detail.DetailViewmodel
import detail.ScreenDetailMovie
import favorite.FavoriteViewmodel
import favorite.ScreenFavorite
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.RouteBuilder
import moe.tlaster.precompose.navigation.transition.NavTransition
import movies.MoviesViewmodel
import movies.ScreenMovies
import movies.ScreenSearch
import movies.SearchViewmodel
import movies.bottomNavItems
import org.koin.core.parameter.parametersOf

@Composable
fun NavigatorApp(navigator: Navigator) {
    NavHost(
        navigator = navigator,
        initialRoute = Router.DISCOVER,
        persistNavState = true,
    ) {
        home(navigator)
        detail(navigator)
        search(navigator)
    }
}

fun RouteBuilder.home(navigator: Navigator) {
    scene(
        route = Router.DISCOVER,
        navTransition = NavTransition(
            createTransition = slideInHorizontally(),
            destroyTransition = slideOutHorizontally(),
        ),
    ) {
        val viewmodel = koinViewModel(MoviesViewmodel::class) {
            parametersOf(bottomNavItems[0])
        }

        ScreenMovies(viewmodel = viewmodel, onSelectMenu = {
            navigator.navigate(it.route)
        }, onViewDetail = {
            navigator.navigate("${Router.DETAIL_MOVIE}${it.id}")
        }, onSearchScreen = {
            navigator.navigate(Router.SEARCH)
        })
    }

    scene(
        route = Router.FAVORITE,
        navTransition = NavTransition(
            createTransition = slideIn(
                initialOffset = { IntOffset(it.width, 0) },
            ),
            destroyTransition = slideOutHorizontally(),
        ),
    ) {
        val viewmodel = koinViewModel(FavoriteViewmodel::class) {
            parametersOf(bottomNavItems[1])
        }
        ScreenFavorite(
            viewmodel = viewmodel,
            onSelectMenu = {
                navigator.navigate(it.route)
            },
            onViewDetail = {
                navigator.navigate("${Router.DETAIL_MOVIE}${it.id}")
            },
        )
    }
}

fun RouteBuilder.detail(navigator: Navigator) {
    scene(route = "${Router.DETAIL_MOVIE}{${Arg.ID}}") {
        val detailViewmodel = koinViewModel(DetailViewmodel::class) {
            parametersOf(it)
        }
        ScreenDetailMovie(
            detailViewmodel = detailViewmodel,
            onBackPress = {
                navigator.goBack()
            },
        )
    }
}

fun RouteBuilder.search(navigator: Navigator) {
    scene(route = Router.SEARCH) {
        val viewmodel: SearchViewmodel = koinViewModel(SearchViewmodel::class)
        ScreenSearch(
            viewmodel = viewmodel,
            onViewDetail = {
                navigator.navigate("${Router.DETAIL_MOVIE}${it.id}")
            },
            onBackPress = {
                navigator.goBack()
            },
        )
    }
}
