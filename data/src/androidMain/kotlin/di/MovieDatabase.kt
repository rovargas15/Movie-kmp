package di

import android.content.Context
import androidx.room.Room
import local.database.MovieDatabase

fun createDatabase(context: Context): MovieDatabase {
    val dbFile = context.getDatabasePath(MovieDatabase.DB_FILE_NAME)
    return Room.databaseBuilder<MovieDatabase>(
        context = context,
        name = dbFile.absolutePath,
    ).build()
}
