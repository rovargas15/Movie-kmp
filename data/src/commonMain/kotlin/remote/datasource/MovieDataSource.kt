package remote.datasource

import com.movie.data.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import remote.api.MovieApi
import remote.model.MovieResponseBase

class MovieDataSource(private val client: HttpClient) : MovieApi {
    override suspend fun getMovies(category: String): MovieResponseBase {
        val response = client.get(BuildConfig.URL_BASE + category)
        if (response.status == HttpStatusCode.OK) {
            return response.body<MovieResponseBase>()
        } else {
            throw Throwable(response.status.description)
        }
    }
}
