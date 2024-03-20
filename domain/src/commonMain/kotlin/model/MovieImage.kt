package model

data class MovieImage(
    val backdrops: List<Poster>,
    val id: Int,
    val logos: List<Poster>,
    val posters: List<Poster>,
) {
    data class Poster(
        val aspectRatio: Double?,
        val filePath: String,
        val height: Int?,
        val iso6391: String?,
        val voteAverage: Double?,
        val voteCount: Int?,
        val width: Int?,
    )
}
