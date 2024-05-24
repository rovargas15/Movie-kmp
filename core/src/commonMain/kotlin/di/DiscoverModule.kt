package di

import favorite.FavoriteViewmodel
import movies.MoviesViewmodel
import movies.bottomNavItems
import org.koin.dsl.module

val featureDiscoverModule =
    module {
        factory {
            MoviesViewmodel(
                getMovieByCategory = get(),
                coroutineDispatcher = get(),
                bottomNavRoute = bottomNavItems[0],
            )
        }

        factory {
            FavoriteViewmodel(
                getFavoriteMovie = get(),
                bottomNavRoute = bottomNavItems[1],
                coroutineDispatcher = get(),
                updateMovie = get(),
            )
        }
    }
