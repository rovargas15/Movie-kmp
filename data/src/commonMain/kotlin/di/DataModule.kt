package di

import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import local.dao.MovieDao
import local.datasource.MovieDatasource
import local.entity.MovieEntity
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import remote.api.MovieApi
import remote.datasource.MovieDataSource
import repository.MovieRepository
import repository.MovieRepositoryImpl
import usecase.GetLocalMovieById
import usecase.GetMovieByCategory
import usecase.GetMovieImageById
import usecase.GetRemoteMovieById

val dataModule =
    module {
        singleOf(::coroutineDispatcherProvider)

        singleOf(::client)

        singleOf(::MovieDatasource) { bind<MovieDao>() }

        singleOf(::MovieDataSource) { bind<MovieApi>() }

        singleOf(::MovieRepositoryImpl) { bind<MovieRepository>() }

        factoryOf(::GetMovieByCategory)

        factoryOf(::GetLocalMovieById)

        factoryOf(::GetRemoteMovieById)

        factoryOf(::GetMovieImageById)

        single {
            Realm.open(realmConfig)
        }
    }

fun coroutineDispatcherProvider() = Dispatchers.IO

private val realmConfig =
    RealmConfiguration.create(
        schema =
            setOf(
                MovieEntity::class,
            ),
    )
