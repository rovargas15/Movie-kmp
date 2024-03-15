package repository

import local.datasource.MovieDatasource
import model.Movie
import model.MovieBase
import remote.datasource.MovieDataSource
import util.BaseRepository

class MovieRepositoryImpl(
    private val dataSourceRemote: MovieDataSource,
    private val datasourceLocal: MovieDatasource,
) : BaseRepository(), MovieRepository {
    override suspend fun getMovies(category: String): Result<MovieBase> =
        launchResultSafe {
            var movies = datasourceLocal.findAllByCategory(category).map { it.toDomain() }

            if (movies.isEmpty()) {
                val response = dataSourceRemote.getMovies(category)
                val moviesResponse = response.results.map { it.toEntity() }
                movies = datasourceLocal.insertAll(moviesResponse, category).map { it.toDomain() }
            }

            MovieBase(
                page = 1,
                results = movies,
                totalPages = 1,
                totalResults = datasourceLocal.findAll().size,
            )
        }

    override suspend fun getMovieById(id: String): Result<Movie?> =
        launchResultSafe {
            datasourceLocal.findById(id)?.toDomain()
        }
}
