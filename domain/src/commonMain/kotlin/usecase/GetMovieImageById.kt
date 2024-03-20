package usecase

import model.MovieImage
import repository.MovieRepository

class GetMovieImageById(private val repository: MovieRepository) {
    suspend fun invoke(id: Int): Result<MovieImage> = repository.getMovieImageById(id)
}
