package di

import detail.DetailViewmodel
import org.koin.dsl.module

val featureDetailModule =
    module {
        factory { DetailViewmodel() }
    }
