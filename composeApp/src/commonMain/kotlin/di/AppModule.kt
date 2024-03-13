package di

import di.featureDiscoverModule
import org.koin.dsl.module

val featureMovie =
    module {
        includes(dataModule, featureDiscoverModule)
    }

val featureDetailMovie =
    module {
        includes(dataModule, featureDetailModule)
    }

val koinModules = listOf(featureMovie, featureDetailMovie)
