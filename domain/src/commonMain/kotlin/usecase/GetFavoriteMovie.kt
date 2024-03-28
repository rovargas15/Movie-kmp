package usecase

import repository.MovieRepository

class GetFavoriteMovie(private val repository: MovieRepository) {
    suspend fun invoke() = repository.getFavoriteMovie()
}
