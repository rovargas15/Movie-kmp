package usecase

import repository.MovieRepository

class GetPagingMovieByCategory(private val repository: MovieRepository) {
    suspend fun invoke(
        category: String,
        page: Int = 1,
        pageSize: Int = 20,
    ) = repository.getPaginatedMovies(
        category = category,
        pageSize = pageSize,
        page = page,
    )
}
