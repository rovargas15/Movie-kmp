package usecase

import repository.MovieRepository

class GetMovieByCategory(private val repository: MovieRepository) {
    suspend fun invoke(category: String) = repository.getMovies(category)
}
