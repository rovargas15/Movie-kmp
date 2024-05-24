package repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import local.database.MovieDatabase
import model.Movie
import model.MovieBase
import model.MovieDetail
import model.MovieImage
import remote.datasource.MovieDataSource
import util.BaseRepository

class MovieRepositoryImpl(
    private val dataSourceRemote: MovieDataSource,
    private val movieDatabase: MovieDatabase,
) : BaseRepository(), MovieRepository {
    override suspend fun getMovies(category: String): Result<MovieBase> =
        launchResultSafe {
            val count = movieDatabase.movieDao().countByCategory(category)

            if (count == 0) {
                val response = dataSourceRemote.getMovies(category)
                val moviesResponse = response.results.map { it.toEntity(category) }
                movieDatabase.movieDao().insertAll(moviesResponse)
            }

            val movies =
                movieDatabase.movieDao().getMovieByCategory(category)
                    .map { entityList -> entityList.map { it.toDomain() } }
            MovieBase(
                page = 1,
                results = movies.first(),
                totalPages = 1,
                totalResults = movies.first().size,
            )
        }

    override suspend fun getMovieById(id: Int): Flow<Movie> =
        movieDatabase.movieDao().getMovieById(id).map { resultsChange -> resultsChange.toDomain() }

    override suspend fun getMovieByIdRemote(id: Int): Result<MovieDetail> =
        launchResultSafe {
            dataSourceRemote.getMovieById(id).toDomain()
        }

    override suspend fun getMovieImageById(id: Int): Result<MovieImage> =
        launchResultSafe {
            dataSourceRemote.getImageById(id).toDomain()
        }

    override suspend fun getSearchMovie(query: String): Result<MovieBase> =
        launchResultSafe {
            val response = dataSourceRemote.getMoviesByQuery(query)
            val moviesResponse = response.results.map { it.toEntity("") }
            movieDatabase.movieDao().insertAll(moviesResponse)

            val movies =
                movieDatabase.movieDao().getMovieByCategory("")
                    .map { entityList -> entityList.map { it.toDomain() } }
            MovieBase(
                page = response.page,
                results = movies.first(),
                totalPages = response.totalResults,
                totalResults = response.totalResults,
            )
        }

    override suspend fun getFavoriteMovie(): Flow<List<Movie>> =
        movieDatabase.movieDao().getAllAsFlow().map { resultsChange ->
            resultsChange.map { it.toDomain() }
        }

    override suspend fun updateMovie(movie: Movie) {
        //      movieDao.updateMovie(movie.)
    }
}
