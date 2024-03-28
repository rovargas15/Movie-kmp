package usecase

import model.Movie
import repository.MovieRepository

class UpdateMovie(private val repository: MovieRepository) {
    suspend fun invoke(movie: Movie) = repository.updateMovie(movie)
}
