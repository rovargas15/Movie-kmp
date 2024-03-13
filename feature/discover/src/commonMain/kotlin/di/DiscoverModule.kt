package di

import movies.MoviesViewmodel
import org.koin.dsl.module

val featureDiscoverModule =
    module {
        factory { MoviesViewmodel(getMovieByCategory = get()) }
    }
