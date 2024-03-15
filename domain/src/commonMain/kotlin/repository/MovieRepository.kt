package repository

import model.Movie
import model.MovieBase

interface MovieRepository {
    suspend fun getMovies(category: String): Result<MovieBase>

    suspend fun getMovieById(id: String): Result<Movie?>
}
