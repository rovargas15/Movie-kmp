package local.dao

import io.realm.kotlin.Realm
import io.realm.kotlin.notifications.ResultsChange
import io.realm.kotlin.query.RealmResults
import io.realm.kotlin.types.RealmObject
import kotlinx.coroutines.flow.Flow
import kotlin.reflect.KClass

interface RealmDao<T : RealmObject> {
    val realm: Realm
    val clazz: KClass<T>

    suspend fun insert(entity: T) {
        realm.write {
            copyToRealm(entity)
        }
    }

    suspend fun insertAll(
        entities: List<T>,
        category: String,
    ): List<T> {
        realm.write {
            for (entity in entities) {
                copyToRealm(entity)
            }
        }
        return findAllByCategory(category)
    }

    suspend fun update(entity: T) {
        realm.write {
            copyToRealm(entity)
        }
    }

    suspend fun findAll(): RealmResults<T> {
        return realm.query(clazz).find()
    }

    suspend fun findAllByCategory(category: String): RealmResults<T> {
        return realm.query(clazz, "category == $0", category).find()
    }

    suspend fun findById(id: String): Flow<ResultsChange<T>> {
        return realm.query(clazz, "id == $0", id.toInt()).asFlow()
    }

    suspend fun findBySearch(ids: String): RealmResults<T> {
        val query = ids.replace("[", "{").replace("]", "}")
        return realm.query(
            clazz, "movieId IN $query",
        ).find()
    }


    suspend fun delete(entity: T) {
        realm.write {
            delete(entity)
        }
    }

    suspend fun stream(): Flow<ResultsChange<T>> {
        return realm.query(clazz).asFlow()
    }

    suspend fun streamFavorite(): Flow<ResultsChange<T>> {
        return realm.query(clazz, "isFavorite == $0", true).asFlow()
    }

    suspend fun deleteAll() {
        realm.write {
            val all = this.query(clazz).find()
            delete(all)
        }
    }
}
