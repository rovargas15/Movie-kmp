package repository

import model.Movie
import model.MovieBase
import model.MovieDetail
import model.MovieImage

interface MovieRepository {
    suspend fun getMovies(category: String): Result<MovieBase>

    suspend fun getMovieById(id: String): Result<Movie?>

    suspend fun getMovieByIdRemote(id: Int): Result<MovieDetail>

    suspend fun getMovieImageById(id: Int): Result<MovieImage>
}
