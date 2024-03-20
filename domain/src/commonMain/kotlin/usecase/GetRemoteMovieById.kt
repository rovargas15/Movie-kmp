package usecase

import model.MovieDetail
import repository.MovieRepository

class GetRemoteMovieById(private val repository: MovieRepository) {
    suspend fun invoke(id: Int): Result<MovieDetail> = repository.getMovieByIdRemote(id)
}
