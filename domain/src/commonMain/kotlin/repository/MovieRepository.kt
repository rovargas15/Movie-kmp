package repository

import kotlinx.coroutines.flow.Flow
import model.Movie
import model.MovieBase
import model.MovieDetail
import model.MovieImage

interface MovieRepository {
    suspend fun getMovies(category: String): Result<MovieBase>

    suspend fun getMovieById(id: Int): Flow<Movie>

    suspend fun getMovieByIdRemote(id: Int): Result<MovieDetail>

    suspend fun getMovieImageById(id: Int): Result<MovieImage>

    suspend fun getSearchMovie(query: String): Result<MovieBase>

    suspend fun getFavoriteMovie(): Flow<List<Movie>>

    suspend fun updateMovie(movie: Movie)

    suspend fun getPaginatedMovies(
        category: String,
        pageSize: Int,
        page: Int,
    ): Result<MovieBase>
}
