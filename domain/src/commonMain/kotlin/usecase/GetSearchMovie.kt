package usecase

import repository.MovieRepository

class GetSearchMovie(private val repository: MovieRepository) {
    suspend fun invoke(getSearchMovie: String) = repository.getSearchMovie(getSearchMovie)
}
