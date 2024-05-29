package di

import org.koin.dsl.module

val featureMovie =
    module {
        includes(dataModule, featureDiscoverModule)
    }

val featureDetailMovie =
    module {
        includes(dataModule, featureDetailModule)
    }

val featureSearch =
    module {
        includes(dataModule, featureSearchModule)
    }

val featurePaging =
    module {
        includes(dataModule, featurePagingModule)
    }

val koinModules = listOf(featureMovie, featureDetailMovie, featureSearch, featurePaging)
