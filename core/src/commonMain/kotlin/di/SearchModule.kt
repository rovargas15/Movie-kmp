package di

import movies.SearchViewmodel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val featureSearchModule = module {
    factoryOf(::SearchViewmodel)
}
