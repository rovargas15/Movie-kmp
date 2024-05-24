package di

import detail.DetailViewmodel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val featureDetailModule =
    module {
        factoryOf(::DetailViewmodel)
    }
