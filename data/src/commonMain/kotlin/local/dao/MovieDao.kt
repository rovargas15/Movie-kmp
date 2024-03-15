package local.dao

import io.realm.kotlin.ext.query
import local.entity.MovieEntity

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
}
