package usecase

import repository.MovieRepository

class GetLocalMovieById(private val repository: MovieRepository) {
    suspend fun invoke(id: String) = repository.getMovieById(id)
}
