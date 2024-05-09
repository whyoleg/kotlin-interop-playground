import org.gradle.kotlin.dsl.*
import org.jetbrains.kotlin.gradle.*
import org.jetbrains.kotlin.gradle.plugin.mpp.*
import org.jetbrains.kotlin.gradle.tasks.*

plugins {
    kotlin("multiplatform")
}

@OptIn(ExperimentalKotlinGradlePluginApi::class)
kotlin {
    compilerOptions {
        allWarningsAsErrors.set(true)
        progressiveMode.set(true)
        freeCompilerArgs.add("-Xrender-internal-diagnostic-names")
    }

    // setup tests running in RELEASE mode
    targets.withType<KotlinNativeTarget>().configureEach {
        binaries.test(listOf(NativeBuildType.RELEASE))
    }
    targets.withType<KotlinNativeTargetWithTests<*>>().configureEach {
        testRuns.create("releaseTest") {
            setExecutionSourceFrom(binaries.getTest(NativeBuildType.RELEASE))
        }
    }
}

// on build, link even those binaries, which it's not possible to run
tasks.build {
    dependsOn(tasks.withType<KotlinNativeLink>())
}
