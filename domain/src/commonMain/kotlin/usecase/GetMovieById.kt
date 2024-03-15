package usecase

import repository.MovieRepository

class GetMovieById(private val repository: MovieRepository) {
    suspend fun invoke(id: String) = repository.getMovieById(id)
}
