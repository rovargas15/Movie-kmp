import di.createDatabase
import local.database.MovieDatabase
import org.koin.dsl.module

val dataBase =
    module {
        single<MovieDatabase> {
            createDatabase()
        }
    }
