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
    val isFavorite: Int,
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
            isFavorite = isFavorite == 1,
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
        isFavorite = if (isFavorite) 1 else 0,
    )
