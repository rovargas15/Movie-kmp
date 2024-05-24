import di.createDatabase
import local.database.MovieDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val dataBase =
    module {
        single<MovieDatabase> {
            createDatabase(androidApplication())
        }
    }
