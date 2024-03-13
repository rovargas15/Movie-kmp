import di.koinModules
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.core.context.startKoin

fun initKoin() {
    startKoin {
        modules(koinModules)
    }
}

fun debugBuild() {
    Napier.base(DebugAntilog())
}
