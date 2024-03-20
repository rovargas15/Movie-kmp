package di

import detail.DetailViewmodel
import moe.tlaster.precompose.navigation.BackStackEntry
import org.koin.dsl.module

val featureDetailModule =
    module {
        factory { (backStackEntry: BackStackEntry) ->
            DetailViewmodel(
                coroutineDispatcher = get(),
                getLocalMovieById = get(),
                getRemoteMovieById = get(),
                getMovieImageById = get(),
                backStackEntry = backStackEntry,
            )
        }
    }
