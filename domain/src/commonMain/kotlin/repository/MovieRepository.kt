package repository

import model.MovieBase

interface MovieRepository {
    suspend fun getMovies(category: String): Result<MovieBase>
}
