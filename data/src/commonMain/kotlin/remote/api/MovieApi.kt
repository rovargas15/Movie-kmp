package remote.api

import remote.model.MovieDetailResponse
import remote.model.MovieImageResponse
import remote.model.MovieResponseBase

interface MovieApi {
    suspend fun getMovies(
        category: String = "",
        page: Int = 1,
    ): MovieResponseBase

    suspend fun getMovieById(id: Int): MovieDetailResponse

    suspend fun getImageById(id: Int): MovieImageResponse

    suspend fun getMoviesByQuery(query: String = ""): MovieResponseBase
}
