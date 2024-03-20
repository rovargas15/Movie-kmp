package remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import model.MovieDetail

@Serializable
data class MovieDetailResponse(
    @SerialName("adult")
    val adult: Boolean,
    @SerialName("backdrop_path")
    val backdropPath: String,
    @SerialName("belongs_to_collection")
    val belongsToCollectionResponse: BelongsToCollectionResponse?,
    @SerialName("budget")
    val budget: Int,
    @SerialName("genres")
    val genreResponses: List<GenreResponse>,
    @SerialName("homepage")
    val homepage: String,
    @SerialName("id")
    val id: Int,
    @SerialName("imdb_id")
    val imdbId: String,
    @SerialName("original_language")
    val originalLanguage: String,
    @SerialName("original_title")
    val originalTitle: String,
    @SerialName("overview")
    val overview: String,
    @SerialName("popularity")
    val popularity: Double,
    @SerialName("poster_path")
    val posterPath: String,
    @SerialName("production_companies")
    val productionCompanies: List<ProductionCompanyResponse>,
    @SerialName("production_countries")
    val productionCountries: List<ProductionCountryResponse>,
    @SerialName("release_date")
    val releaseDate: String,
    @SerialName("revenue")
    val revenue: Int,
    @SerialName("runtime")
    val runtime: Int,
    @SerialName("spoken_languages")
    val spokenLanguageResponses: List<SpokenLanguageResponse>,
    @SerialName("status")
    val status: String,
    @SerialName("tagline")
    val tagline: String,
    @SerialName("title")
    val title: String,
    @SerialName("video")
    val video: Boolean,
    @SerialName("vote_average")
    val voteAverage: Double,
    @SerialName("vote_count")
    val voteCount: Int,
) {
    @Serializable
    data class BelongsToCollectionResponse(
        @SerialName("backdrop_path") val backdropPath: String,
        @SerialName("id") val id: Int,
        @SerialName("name") val name: String,
        @SerialName("poster_path") val posterPath: String,
    ) {
        fun toDomain() =
            MovieDetail.BelongsToCollection(
                backdropPath = backdropPath,
                id = id,
                name = name,
                posterPath = posterPath,
            )
    }

    @Serializable
    data class GenreResponse(
        @SerialName("id") val id: Int,
        @SerialName("name") val name: String,
    ) {
        fun toDomain() = MovieDetail.Genre(id = id, name = name)
    }

    @Serializable
    data class ProductionCompanyResponse(
        @SerialName("id") val id: Int,
        @SerialName("logo_path") val logoPath: String?,
        @SerialName("name") val name: String,
        @SerialName("origin_country") val originCountry: String,
    ) {
        fun toDomain() =
            MovieDetail.ProductionCompany(
                id = id,
                logoPath = logoPath,
                name = name,
                originCountry = originCountry,
            )
    }

    @Serializable
    data class ProductionCountryResponse(
        @SerialName("iso_3166_1") val iso31661: String,
        @SerialName("name") val name: String,
    ) {
        fun toDomain() =
            MovieDetail.ProductionCountry(
                iso31661 = iso31661,
                name = name,
            )
    }

    @Serializable
    data class SpokenLanguageResponse(
        @SerialName("english_name") val englishName: String,
        @SerialName("iso_639_1") val iso6391: String,
        @SerialName("name") val name: String,
    ) {
        fun toDomain() =
            MovieDetail.SpokenLanguage(
                englishName = englishName,
                iso6391 = iso6391,
                name = name,
            )
    }

    fun toDomain() =
        MovieDetail(
            adult = adult,
            backdropPath = backdropPath,
            belongsToCollection = belongsToCollectionResponse?.toDomain(),
            budget = budget,
            genres = genreResponses.map { it.toDomain() },
            homepage = homepage,
            id = id,
            imdbId = imdbId,
            originalLanguage = originalLanguage,
            originalTitle = originalTitle,
            overview = overview,
            popularity = popularity,
            posterPath = posterPath,
            productionCompanies = productionCompanies.map { it.toDomain() },
            productionCountries = productionCountries.map { it.toDomain() },
            releaseDate = releaseDate,
            revenue = revenue,
            runtime = runtime,
            spokenLanguages = spokenLanguageResponses.map { it.toDomain() },
            status = status,
            tagline = tagline,
            title = title,
            video = video,
            voteAverage = voteAverage,
            voteCount = voteCount,
        )
}
