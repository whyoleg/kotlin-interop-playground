package kipbuild.kotlin

import org.gradle.jvm.toolchain.*
import org.gradle.kotlin.dsl.*
import org.jetbrains.kotlin.gradle.dsl.*
import org.jetbrains.kotlin.gradle.targets.js.dsl.*
import org.jetbrains.kotlin.gradle.targets.jvm.*

fun KotlinMultiplatformExtension.appleTargets() {
    macosX64()
    macosArm64()

    iosArm64()
    iosX64()
    iosSimulatorArm64()

    watchosX64()
    watchosArm32()
    watchosArm64()
    watchosSimulatorArm64()
    watchosDeviceArm64()

    tvosX64()
    tvosArm64()
    tvosSimulatorArm64()
}

fun KotlinMultiplatformExtension.desktopTargets() {
    linuxX64()
    linuxArm64()

    mingwX64()

    macosX64()
    macosArm64()
}

fun KotlinMultiplatformExtension.nativeTargets() {
    appleTargets()
    desktopTargets()

    androidNativeX64()
    androidNativeX86()
    androidNativeArm64()
    androidNativeArm32()
}

fun KotlinMultiplatformExtension.jsTarget() {
    js {
        nodejs()
        browser()
    }
}

@OptIn(ExperimentalWasmDsl::class)
fun KotlinMultiplatformExtension.wasmTargets() {
    wasmJs {
        nodejs()
        browser()
    }
    wasmWasi {
        nodejs()
    }
}

@OptIn(ExperimentalWasmDsl::class)
fun KotlinMultiplatformExtension.wasmJsTarget() {
    wasmJs {
        nodejs()
        browser()
    }
}

fun KotlinJvmTarget.jdkTestRuns(vararg versions: Int) {
    val javaToolchains = project.extensions.getByName<JavaToolchainService>("javaToolchains")

    versions.distinct().forEach { jdkTestVersion ->
        testRuns.create("${jdkTestVersion}Test") {
            executionTask.configure {
                javaLauncher.set(javaToolchains.launcherFor {
                    languageVersion.set(JavaLanguageVersion.of(jdkTestVersion))
                })
            }
        }
    }
}
