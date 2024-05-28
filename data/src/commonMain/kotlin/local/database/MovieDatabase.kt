package local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import local.dao.MovieDao
import local.entity.MovieEntity

@Database(entities = [MovieEntity::class], version = 1)
abstract class MovieDatabase : RoomDatabase(), DB {
    abstract fun movieDao(): MovieDao

    override fun clearAllTables() {}

    companion object {
        const val DB_FILE_NAME = "movies_database.db"
    }
}

// temporal hack
interface DB {
    fun clearAllTables() {}
}
