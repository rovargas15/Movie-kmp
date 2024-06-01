package di

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.cinterop.ExperimentalForeignApi
import local.database.MovieDatabase
import local.database.instantiateImpl
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

fun createDatabase(): MovieDatabase {
    val dbFile = "${fileDirectory()}/${MovieDatabase.DB_FILE_NAME}"
    return Room.databaseBuilder<MovieDatabase>(
        name = dbFile,
        factory = { MovieDatabase::class.instantiateImpl() },
    ).setDriver(BundledSQLiteDriver())
        .build()
}

@OptIn(ExperimentalForeignApi::class)
private fun fileDirectory(): String {
    val documentDirectory: NSURL? =
        NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null,
        )
    return requireNotNull(documentDirectory).path!!
}
