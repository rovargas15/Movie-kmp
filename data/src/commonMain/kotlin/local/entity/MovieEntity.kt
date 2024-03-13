package local.entity

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class MovieEntity : RealmObject {
    @PrimaryKey
    var id: Int = 1
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
}
