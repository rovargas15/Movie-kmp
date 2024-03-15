import androidx.compose.runtime.Composable
import detail.DetailViewmodel
import detail.ScreenDetailMovie
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.RouteBuilder
import movies.MoviesViewmodel
import movies.ScreenMovies
import movies.bottomNavItems
import org.koin.core.parameter.parametersOf

@Composable
fun NavigatorApp(navigator: Navigator) {
    NavHost(
        navigator = navigator,
        initialRoute = Router.POPULAR,
    ) {
        home(navigator)
        detail(navigator)
    }
}

fun RouteBuilder.home(navigator: Navigator) {
    scene(route = Router.POPULAR) {
        val viewmodel =
            koinViewModel(MoviesViewmodel::class) {
                parametersOf(bottomNavItems.first())
            }

        ScreenMovies(
            viewmodel = viewmodel,
            onSelectMenu = {
                navigator.navigate(it.route)
            },
            onViewDetail = {
                navigator.navigate("${Router.DETAIL_MOVIE}${it.id}")
            },
        )
    }

    scene(route = Router.TOP_RATED) {
        val viewmodel =
            koinViewModel(MoviesViewmodel::class) {
                parametersOf(bottomNavItems[1])
            }

        ScreenMovies(
            viewmodel = viewmodel,
            onSelectMenu = {
                navigator.navigate(it.route)
            },
            onViewDetail = {
                navigator.navigate("${Router.DETAIL_MOVIE}${it.id}")
            },
        )
    }

    scene(route = Router.UPCOMING) {
        val viewmodel =
            koinViewModel(MoviesViewmodel::class) {
                parametersOf(bottomNavItems[2])
            }

        ScreenMovies(
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
        val detailViewmodel =
            koinViewModel(DetailViewmodel::class) {
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
