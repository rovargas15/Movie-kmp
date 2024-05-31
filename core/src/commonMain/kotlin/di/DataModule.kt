package di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import remote.api.MovieApi
import remote.datasource.MovieDataSource
import repository.MovieRepository
import repository.MovieRepositoryImpl
import usecase.GetFavoriteMovie
import usecase.GetLocalMovieById
import usecase.GetMovieByCategory
import usecase.GetMovieImageById
import usecase.GetPagingMovieByCategory
import usecase.GetRemoteMovieById
import usecase.GetSearchMovie
import usecase.UpdateMovie

val dataModule =
    module {

        singleOf(::coroutineDispatcherProvider)

        singleOf(::client)

        singleOf(::MovieDataSource) { bind<MovieApi>() }

        singleOf(::MovieRepositoryImpl) { bind<MovieRepository>() }

        factoryOf(::GetMovieByCategory)

        factoryOf(::GetLocalMovieById)

        factoryOf(::GetRemoteMovieById)

        factoryOf(::GetMovieImageById)

        factoryOf(::GetSearchMovie)

        factoryOf(::GetFavoriteMovie)

        factoryOf(::UpdateMovie)

        factoryOf(::GetPagingMovieByCategory)
    }

fun coroutineDispatcherProvider(): CoroutineDispatcher = Dispatchers.IO
