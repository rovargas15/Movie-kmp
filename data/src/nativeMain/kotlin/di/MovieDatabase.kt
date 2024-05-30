package di

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import local.database.MovieDatabase
import local.database.instantiateImpl
import platform.Foundation.NSHomeDirectory

fun createDatabase(): MovieDatabase {
    val dbFile = "${NSHomeDirectory()}/${MovieDatabase.DB_FILE_NAME}"
    return Room.databaseBuilder<MovieDatabase>(
        name = dbFile,
        factory = { MovieDatabase::class.instantiateImpl() },
    ).setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}
