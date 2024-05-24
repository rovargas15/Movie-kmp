
import di.getMovieDao
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataBase =
    module {
        single {
            getMovieDao(androidContext())
        }
    }
