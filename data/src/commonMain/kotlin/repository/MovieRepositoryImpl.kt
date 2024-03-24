package repository

import local.datasource.MovieDatasource
import model.Movie
import model.MovieBase
import model.MovieDetail
import model.MovieImage
import remote.datasource.MovieDataSource
import util.BaseRepository

class MovieRepositoryImpl(
    private val dataSourceRemote: MovieDataSource,
    private val datasourceLocal: MovieDatasource,
) : BaseRepository(), MovieRepository {
    override suspend fun getMovies(category: String): Result<MovieBase> = launchResultSafe {
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

    override suspend fun getMovieById(id: String): Result<Movie?> = launchResultSafe {
        datasourceLocal.findById(id)?.toDomain()
    }

    override suspend fun getMovieByIdRemote(id: Int): Result<MovieDetail> = launchResultSafe {
        dataSourceRemote.getMovieById(id).toDomain()
    }

    override suspend fun getMovieImageById(id: Int): Result<MovieImage> = launchResultSafe {
        dataSourceRemote.getImageById(id).toDomain()
    }

    override suspend fun getSearchMovie(query: String): Result<MovieBase> = launchResultSafe {
        val response = dataSourceRemote.getMoviesByQuery(query)

        val moviesResponse = response.results.map { it.toEntity() }
        val movies = datasourceLocal.insertAll(moviesResponse, "").map { it.toDomain() }

        MovieBase(
            page = response.page,
            results = movies,
            totalPages = response.totalResults,
            totalResults = response.totalResults,
        )
    }

}
