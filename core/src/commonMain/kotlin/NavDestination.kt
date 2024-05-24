import kotlinx.serialization.Serializable

@Serializable
object Home

@Serializable
object Favorite

@Serializable
object Search

@Serializable
data class Detail(val movieId: Int)
