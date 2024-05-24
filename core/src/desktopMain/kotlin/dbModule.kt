import di.createDatabase
import di.getRoomDatabase
import local.database.MovieDatabase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val dataBase =
    module {
        singleOf(::createDatabase)
        singleOf(::getRoomDatabase)
    }
