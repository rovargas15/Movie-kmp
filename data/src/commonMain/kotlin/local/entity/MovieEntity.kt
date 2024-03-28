package local.entity

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import model.Movie

class MovieEntity : RealmObject {
    @PrimaryKey
    var id: Int = 1
    var movieId: Int = 1
    var category: String = ""
    var adult: Boolean = false
    var backdropPath: String = ""
    var genreIds: RealmList<Int> = realmListOf()
    var originalLanguage: String = ""
    var originalTitle: String = ""
    var overview: String = ""
    var popularity: Double = .0
    var posterPath: String = ""
    var releaseDate: String = ""
    var title: String = ""
    var video: Boolean = false
    var voteAverage: Double = .0
    var voteCount: Int = 0
    var isFavorite: Boolean = false

    fun toDomain() =
        Movie(
            id = id,
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

    constructor()

    constructor(
        id: Int,
        movieId: Int,
        category: String,
        adult: Boolean,
        backdropPath: String?,
        genreIds: RealmList<Int>,
        originalLanguage: String,
        originalTitle: String,
        overview: String,
        popularity: Double,
        posterPath: String,
        releaseDate: String,
        title: String,
        video: Boolean,
        voteAverage: Double,
        voteCount: Int,
        isFavorite: Boolean,
    ) {
        this.id = id
        this.movieId = movieId
        this.category = category
        this.adult = adult
        this.backdropPath = backdropPath ?: ""
        this.genreIds = genreIds
        this.originalLanguage = originalLanguage
        this.originalTitle = originalTitle
        this.overview = overview
        this.popularity = popularity
        this.posterPath = posterPath
        this.releaseDate = releaseDate
        this.title = title
        this.video = video
        this.voteAverage = voteAverage
        this.voteCount = voteCount
        this.isFavorite = isFavorite
    }
}
