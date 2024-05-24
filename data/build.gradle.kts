import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.room)
    alias(libs.plugins.gradleBuildConfig)
    alias(libs.plugins.ksp)
    alias(libs.plugins.skie)
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "11"
            }
        }
    }

    jvm("desktop")

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Data"
            isStatic = true
        }
    }

    sourceSets.all {
        languageSettings.optIn("kotlin.experimental.ExperimentalObjCName")
    }
    sourceSets {
        val desktopMain by getting

        androidMain.dependencies {
            implementation(libs.ktor.client.okhttp)
            // Koin
            implementation(libs.koin.core)
            implementation(libs.koin.android)
            implementation(libs.androidx.room.paging)
        }

        commonMain.dependencies {
            implementation(projects.domain)
            implementation(compose.runtime)
            api(compose.foundation)
            api(compose.animation)
            // ktor
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.content.negotiation)
            implementation(libs.ktor.serialization)
            implementation(libs.ktor.logger)
            implementation(libs.ktor.client.auth)
            implementation(libs.skie.annotations)
            // Koin
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)
            // coroutines
            implementation(libs.kotlinx.coroutines.core)
            // Log
            implementation(libs.napier)
            // room
            implementation(libs.androidx.paging.common)
            implementation(libs.androidx.room.runtime)
            implementation(libs.sqlite.bundled)
        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
            implementation(libs.stately.common)
        }

        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
        }
    }
}

android {
    namespace = "com.movie.kmp.data"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.movie.kmp"
            packageVersion = "1.0.0"
        }
    }
}

buildConfig {
    packageName("com.movie.data")

    val properties = Properties()
    properties.load(project.rootProject.file("secrets.properties").reader())
    val piKey = properties.getProperty("API_KEY")

    buildConfigField("API_KEY", piKey)
    buildConfigField("URL_BASE", "https://api.themoviedb.org/3/")
}

dependencies {
    add("kspAndroid", libs.androidx.room.compiler)
    add("kspIosSimulatorArm64", libs.androidx.room.compiler)
    add("kspIosX64", libs.androidx.room.compiler)
    add("kspIosArm64", libs.androidx.room.compiler)
    debugImplementation(libs.compose.ui.tooling)
}

room {
    schemaDirectory("$projectDir/schemas")
}
