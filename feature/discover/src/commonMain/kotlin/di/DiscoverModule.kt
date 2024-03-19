package di

import favorite.FavoriteViewmodel
import movies.BottomNavRoute
import movies.MoviesViewmodel
import org.koin.dsl.module

val featureDiscoverModule =
    module {
        factory { (bottomNavRoute: BottomNavRoute) ->
            MoviesViewmodel(
                getMovieByCategory = get(),
                bottomNavRoute = bottomNavRoute,
                coroutineDispatcher = get(),
            )
        }

        factory { (bottomNavRoute: BottomNavRoute) ->
            FavoriteViewmodel(
                getMovieByCategory = get(),
                bottomNavRoute = bottomNavRoute,
                coroutineDispatcher = get(),
            )
        }
    }
