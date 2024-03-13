import androidx.compose.runtime.Composable
import detail.ScreenDetailMovie
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.RouteBuilder
import moe.tlaster.precompose.navigation.transition.NavTransition
import movies.ScreenMovies

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
    scene(route = Router.POPULAR, navTransition = NavTransition()) {
        ScreenMovies(
            route = Router.POPULAR,
            onSelectOption = {
                navigator.navigate(it.route)
            },
            onSelectMovie = {
                navigator.navigate(Router.DETAIL_MOVIE)
            },
        )
    }

    scene(route = Router.TOP_RATED, navTransition = NavTransition()) {
        ScreenMovies(
            Router.TOP_RATED,
            onSelectOption = {
                navigator.navigate(it.route)
            },
            onSelectMovie = {
                navigator.navigate(Router.DETAIL_MOVIE)
            },
        )
    }

    scene(route = Router.UPCOMING, navTransition = NavTransition()) {
        ScreenMovies(
            Router.UPCOMING,
            onSelectOption = {
                navigator.navigate(it.route)
            },
            onSelectMovie = {
                navigator.navigate(Router.DETAIL_MOVIE)
            },
        )
    }
}

fun RouteBuilder.detail(navigator: Navigator) {
    scene(route = Router.DETAIL_MOVIE, navTransition = NavTransition()) {
        ScreenDetailMovie {
            navigator.goBack()
        }
    }
}
