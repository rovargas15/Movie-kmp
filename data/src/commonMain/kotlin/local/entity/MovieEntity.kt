package local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import model.Movie

@Entity
data class MovieEntity(
    @PrimaryKey
    val movieId: Int,
    val category: String,
    val adult: Boolean,
    val backdropPath: String,
    @field:TypeConverters(GenreConvert::class) val genreIds: List<Int>,
    val originalLanguage: String,
    val originalTitle: String,
    val overview: String,
    val popularity: Double,
    val posterPath: String,
    val releaseDate: String,
    val title: String,
    val video: Boolean,
    val voteAverage: Double,
    val voteCount: Int,
    val isFavorite: Boolean,
) {
    fun toDomain() =
        Movie(
            id = movieId,
            movieId = movieId,
            adult = adult,
            backdropPath = backdropPath,
            genreIds = genreIds.toList(),
            originalLanguage = originalLanguage,
            originalTitle = originalTitle,
            overview = overview,
            popularity = popularity,
            posterPath = posterPath,
            releaseDate = releaseDate,
            title = title,
            video = video,
            voteAverage = voteAverage,
            voteCount = voteCount,
            isFavorite = isFavorite,
        )
}

fun Movie.toEntity() =
    MovieEntity(
        movieId = movieId,
        category = "",
        adult = adult,
        backdropPath = backdropPath,
        genreIds = genreIds,
        originalLanguage = originalLanguage,
        originalTitle = originalTitle,
        overview = overview,
        popularity = popularity,
        posterPath = posterPath,
        releaseDate = releaseDate,
        title = title,
        video = video,
        voteAverage = voteAverage,
        voteCount = voteCount,
        isFavorite = isFavorite,
    )

data class FavoriteMovie(
    val isFavorite: Boolean,
)
