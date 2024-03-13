package remote.repository

import model.MovieBase
import remote.datasource.MovieDataSource
import repository.MovieRepository
import util.BaseRepository

class MovieRepositoryImpl(private val dataSource: MovieDataSource) :
    BaseRepository(),
    MovieRepository {
    override suspend fun getMovies(category: String): Result<MovieBase> =
        launchResultSafe {
            dataSource.getMovies("https://api.themoviedb.org/3/movie/$category").toDomain()
        }
}
