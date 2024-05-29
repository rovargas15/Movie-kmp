import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import detail.ScreenDetailMovie
import favorite.ScreenFavorite
import movies.ScreenMovies
import movies.ScreenSearch

@Composable
fun NavigatorApp(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = Router.DISCOVER,
    ) {
        home(navController)
        detail(navController)
        search(navController)
        paging(navController)
    }
}

fun NavGraphBuilder.home(navController: NavHostController) {
    composable(route = Router.DISCOVER) {
        ScreenMovies(
            onSelectMenu = {
                navController.navigate(it.route)
            },
            onViewDetail = {
                navController.navigate("${Router.DETAIL_MOVIE}${it.id}")
            },
            onSearchScreen = {
                navController.navigate(Router.SEARCH)
            },
            onPagingShow = {
                navController.navigate("${Router.MOVIE_ALL}$it")
            },
        )
    }

    composable(route = Router.FAVORITE) {
        ScreenFavorite(
            onSelectMenu = {
                navController.navigate(it.route)
            },
            onViewDetail = {
                navController.navigate("${Router.DETAIL_MOVIE}${it.id}")
            },
        )
    }
}

fun NavGraphBuilder.detail(navController: NavHostController) {
    composable(
        route = "${Router.DETAIL_MOVIE}{${Arg.ID}}",
        arguments =
            listOf(
                navArgument(Arg.ID) {
                    type = NavType.IntType
                    nullable = false
                },
            ),
    ) {
        ScreenDetailMovie(
            movieId = it.arguments?.getInt(Arg.ID) ?: 0,
            onBackPress = {
                navController.popBackStack()
            },
        )
    }
}

fun NavGraphBuilder.search(navController: NavHostController) {
    composable(route = Router.SEARCH) {
        ScreenSearch(
            onViewDetail = {
                navController.navigate("${Router.DETAIL_MOVIE}${it.id}")
            },
            onBackPress = {
                navController.popBackStack()
            },
        )
    }
}

fun NavGraphBuilder.paging(navController: NavHostController) {
    composable(
        route = "${Router.MOVIE_ALL}{${Arg.CATEGORY}}",
        arguments =
            listOf(
                navArgument(Arg.CATEGORY) {
                    type = NavType.StringType
                    nullable = false
                },
            ),
    ) {
        ScreenPaging(
            onViewDetail = {
                navController.navigate("${Router.DETAIL_MOVIE}${it.id}")
            },
            onBackPress = {
                navController.popBackStack()
            },
            category = it.arguments?.getString(Arg.CATEGORY) ?: "",
        )
    }
}
