package remote.datasource

import com.movie.data.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import remote.api.MovieApi
import remote.model.MovieDetailResponse
import remote.model.MovieImageResponse
import remote.model.MovieResponseBase

class MovieDataSource(private val client: HttpClient) : MovieApi {
    override suspend fun getMovies(category: String): MovieResponseBase {
        val response = client.get("${BuildConfig.URL_BASE}movie/$category?language=en")
        if (response.status == HttpStatusCode.OK) {
            return response.body<MovieResponseBase>()
        } else {
            throw Throwable(response.status.description)
        }
    }

    override suspend fun getMovieById(id: Int): MovieDetailResponse {
        val response = client.get("${BuildConfig.URL_BASE}movie/$id?language=en")
        if (response.status == HttpStatusCode.OK) {
            return response.body<MovieDetailResponse>()
        } else {
            throw Throwable(response.status.description)
        }
    }

    override suspend fun getImageById(id: Int): MovieImageResponse {
        val response = client.get("${BuildConfig.URL_BASE}movie/$id/images?language=en")
        if (response.status == HttpStatusCode.OK) {
            return response.body<MovieImageResponse>()
        } else {
            throw Throwable(response.status.description)
        }
    }

    override suspend fun getMoviesByQuery(query: String): MovieResponseBase {
        val response = client.get("${BuildConfig.URL_BASE}search/movie?query=$query?&language=en")
        if (response.status == HttpStatusCode.OK) {
            return response.body<MovieResponseBase>()
        } else {
            throw Throwable(response.status.description)
        }
    }
}
