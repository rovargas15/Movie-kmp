package remote.api

import remote.model.MovieResponseBase

interface MovieApi {
    suspend fun getMovies(category: String = ""): MovieResponseBase
}
