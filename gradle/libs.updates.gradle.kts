/**
 * run to check for dependencies:
 *  ./gradlew dependencyUpdates --init-script gradle/libs.updates.gradle.kts --no-configuration-cache
 */

initscript {
    repositories {
        gradlePluginPortal()
    }
    dependencies {
        classpath("com.github.ben-manes:gradle-versions-plugin:+")
    }
}

rootProject {
    apply<com.github.benmanes.gradle.versions.VersionsPlugin>()
    tasks.named("dependencyUpdates") {
        gradle.includedBuilds.forEach {
            dependsOn(it.task(":dependencyUpdates"))
        }
    }
}
