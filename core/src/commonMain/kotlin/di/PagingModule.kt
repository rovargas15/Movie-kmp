package di

import PagingViewmodel
import org.koin.dsl.module

val featurePagingModule =
    module {
        factory {
            PagingViewmodel(
                coroutineDispatcher = get(),
                useCase = get(),
            )
        }
    }
