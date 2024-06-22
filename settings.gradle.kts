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

        ivy("https://download.java.net/java/early_access/jextract/22/5") {
            name = "Jextract Early-Access Builds"
            metadataSources { artifact() }
            content { includeGroup("kipbuild.dependencies.jextract") }
            patternLayout { artifact("openjdk-22-jextract+5-33_[artifact]_bin.[ext]") }
        }
    }
}

rootProject.name = "kotlin-interop-playground"

include("c-interop:jvm-jni")
include("c-interop:jvm-jna")
include("c-interop:jvm-jnr")
include("c-interop:jvm-ffm-raw")
include("c-interop:jvm-ffm-jextract")

include("c-interop:native-cinterop")
include("c-interop:native-raw")
