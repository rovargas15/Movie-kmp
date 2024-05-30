package di

import androidx.room.Room.databaseBuilder
import local.database.MovieDatabase
import java.io.File

fun createDatabase(): MovieDatabase {
    val dbFile = File(System.getProperty("java.io.tmpdir"), MovieDatabase.DB_FILE_NAME)
    return databaseBuilder<MovieDatabase>(
        name = dbFile.absolutePath,
    ).build()
}
