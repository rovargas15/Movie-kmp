package di

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import remote.api.MovieApi
import remote.datasource.MovieDataSource
import remote.repository.MovieRepositoryImpl
import repository.MovieRepository
import usecase.GetMovieByCategory

val dataModule =
    module {
        singleOf(::client)

        singleOf(::MovieDataSource) { bind<MovieApi>() }

        singleOf(::MovieRepositoryImpl) { bind<MovieRepository>() }

        factoryOf(::GetMovieByCategory)
    }
