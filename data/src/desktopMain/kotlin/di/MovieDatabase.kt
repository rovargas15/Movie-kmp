package di

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import local.database.MovieDatabase
import java.io.File

fun createDatabase(): RoomDatabase.Builder<MovieDatabase> {
    val dbFile = File(System.getProperty("java.io.tmpdir"), MovieDatabase.DB_FILE_NAME)
    return Room.databaseBuilder<MovieDatabase>(
        name = dbFile.absolutePath,
    )
}

fun getRoomDatabase(builder: RoomDatabase.Builder<MovieDatabase>): MovieDatabase {
    return builder
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}
