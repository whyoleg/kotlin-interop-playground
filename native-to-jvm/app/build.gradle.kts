import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    kotlin("multiplatform") version "1.9.0"
}

kotlin {
    jvmToolchain(8)
    jvm {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        mainRun {
            mainClass.set("dev.whyoleg.interop.playground.ntj.app.MainKt")
        }
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(project(":library:jvm"))
            }
        }
    }
}
