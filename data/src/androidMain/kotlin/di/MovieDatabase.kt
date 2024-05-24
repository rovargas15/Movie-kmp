package di

import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import local.MovieDatabase

fun createDatabase(context: Context): MovieDatabase {
    val dbFile = context.getDatabasePath(MovieDatabase.DB_FILE_NAME)
    return Room.databaseBuilder<MovieDatabase>(context, dbFile.absolutePath)
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}
