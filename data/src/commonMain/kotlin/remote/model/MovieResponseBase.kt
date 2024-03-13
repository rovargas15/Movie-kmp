package remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import model.MovieBase

@Serializable
data class MovieResponseBase(
    @SerialName("page")
    val page: Int,
    @SerialName("results")
    val results: List<MovieResponse>,
    @SerialName("total_pages")
    val totalPages: Int,
    @SerialName("total_results")
    val totalResults: Int,
) {
    fun toDomain() =
        MovieBase(
            page = page,
            results = results.map { it.toDomain() },
            totalPages = totalPages,
            totalResults = totalResults,
        )
}
