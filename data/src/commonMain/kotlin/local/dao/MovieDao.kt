package local.dao

import io.realm.kotlin.ext.query
import local.entity.MovieEntity
import model.Movie

interface MovieDao : RealmDao<MovieEntity> {
    override suspend fun insertAll(
        entities: List<MovieEntity>,
        category: String,
    ): List<MovieEntity> {
        var count = realm.query<MovieEntity>().find().size + 1
        realm.write {
            for (entity in entities) {
                entity.category = category
                entity.id = count
                copyToRealm(entity)
                count++
            }
        }
        return findAllByCategory(category)
    }

    suspend fun updateMovie(movie: Movie) {
        realm.query<MovieEntity>("id == $0", movie.id)
            .first()
            .find()
            ?.also { result ->
                // Add a dog in a transaction
                realm.writeBlocking {
                    findLatest(result)?.isFavorite = movie.isFavorite
                }
            }
    }
}
