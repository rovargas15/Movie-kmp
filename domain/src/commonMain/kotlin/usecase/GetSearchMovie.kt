package usecase

import repository.MovieRepository

class GetSearchMovie(private val repository: MovieRepository) {
    suspend fun invoke(search: String) = repository.getSearchMovie(search)
}
