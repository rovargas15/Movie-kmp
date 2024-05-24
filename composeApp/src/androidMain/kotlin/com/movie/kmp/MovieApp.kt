package com.movie.kmp

import android.app.Application
import dataBase
import di.koinModules
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MovieApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Napier.base(DebugAntilog())
        startKoin {
            androidContext(this@MovieApp)
            androidLogger()
            modules(dataBase + koinModules)
        }
    }
}
