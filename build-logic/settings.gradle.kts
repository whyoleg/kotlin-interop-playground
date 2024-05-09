pluginManagement {
    includeBuild("../build-settings")
}

plugins {
    id("kipsettings.default")
}

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

rootProject.name = "build-logic"
