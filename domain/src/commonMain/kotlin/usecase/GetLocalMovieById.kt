package usecase

import repository.MovieRepository

class GetLocalMovieById(private val repository: MovieRepository) {
    suspend fun invoke(id: Int) = repository.getMovieById(id)
}
