import di.createDatabase
import di.getRoomDatabase
import local.database.MovieDatabase
import org.koin.dsl.module

val dataBase =
    module {
        single<MovieDatabase> {
            getRoomDatabase(createDatabase())
        }
    }
