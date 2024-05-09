pluginManagement {
    includeBuild("build-logic")
    includeBuild("build-settings")
}

plugins {
    id("kipsettings.default")
}

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        ivy("https://github.com/whyoleg/openssl-builds/releases/download") {
            name = "Prebuilt OpenSSL distributions"
            metadataSources { artifact() }
            content { includeGroup("kipbuild.dependencies.openssl") }
            patternLayout { artifact("[revision]/[artifact].[ext]") }
        }
    }
}

rootProject.name = "kotlin-interop-playground"

include("c-interop:jvm-jni")
include("c-interop:jvm-ffm")

include("c-interop:native-cinterop")
include("c-interop:native-raw")
