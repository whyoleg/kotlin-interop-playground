import kipbuild.kotlin.*
import kipbuild.openssl.*
import org.jetbrains.kotlin.gradle.plugin.mpp.*
import org.jetbrains.kotlin.gradle.tasks.*

plugins {
    id("kipbuild.kotlin.multiplatform")
    id("kipbuild.openssl")
}

kotlin {
    nativeTargets()

    sourceSets.commonTest.dependencies {
        implementation(kotlin("test"))
    }

    targets.withType<KotlinNativeTarget>().configureEach {
        compilations.named("main") {
            cinterops.register("libcrypto") {
                definitionFile = layout.projectDirectory.file("src/commonMain/cinterop/libcrypto.def")
                includeDirs(openssl.includeDirectory(konanTarget.toOpensslTarget()))
                extraOpts("-libraryPath", openssl.libDirectory(konanTarget.toOpensslTarget()).get().asFile.absolutePath)
            }
        }
    }
}

tasks.withType<CInteropProcess>().configureEach { dependsOn(openssl.setupTask) }
