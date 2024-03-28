package local.datasource

import io.realm.kotlin.Realm
import local.dao.MovieDao
import local.entity.MovieEntity
import kotlin.reflect.KClass

class MovieDatasource(
    r: Realm,
) : MovieDao {

    override val realm: Realm = r
    override val clazz: KClass<MovieEntity> = MovieEntity::class

}