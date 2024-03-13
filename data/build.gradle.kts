import java.util.Properties
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.realm)
    kotlin("plugin.serialization") version "1.9.22"
    alias(libs.plugins.gradleBuildConfig)
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }

    jvm("desktop")

    listOf(
        iosX64(), iosArm64(), iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Data"
            isStatic = true
        }
    }

    sourceSets {
        val desktopMain by getting

        androidMain.dependencies {
            implementation(libs.ktor.client.okhttp)
            //Koin
            implementation(project.dependencies.platform("io.insert-koin:koin-bom:3.5.1"))
            implementation("io.insert-koin:koin-core")
            implementation("io.insert-koin:koin-android")
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
            implementation("io.ktor:ktor-client-auth:2.3.8")

            //Koin
            implementation(project.dependencies.platform("io.insert-koin:koin-bom:3.5.1"))
            implementation("io.insert-koin:koin-core")
            implementation("io.insert-koin:koin-compose")
            // realm
            implementation(libs.realm.base)
            // coroutines
            implementation(libs.kotlinx.coroutines.core)
            // Log
            implementation(libs.napier)
        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
            implementation("co.touchlab:stately-common:2.0.5")
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
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
    buildConfigField("URL_BASE", "api.themoviedb.org/3/movie")
}
