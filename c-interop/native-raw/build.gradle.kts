import kipbuild.bitcode.*
import kipbuild.kotlin.*
import kipbuild.openssl.*
import org.jetbrains.kotlin.gradle.*
import org.jetbrains.kotlin.gradle.plugin.mpp.*
import org.jetbrains.kotlin.konan.target.*

plugins {
    id("kipbuild.kotlin.multiplatform")
    id("kipbuild.openssl")
}

kotlin {
    nativeTargets()

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    compilerOptions {
        optIn.add("kotlin.native.internal.InternalForKotlinNative")
    }

    sourceSets.commonTest.dependencies {
        implementation(kotlin("test"))
    }

    targets.withType<KotlinNativeTarget>().configureEach {
        compilations.named("main") {
            val konanTarget = this.konanTarget
            val compileToBitcodeTask = tasks.register<CompileToLlvmBitcode>(
                compileKotlinTaskName.replace("Kotlin", "C"),
                konanTarget
            )

            compileToBitcodeTask.configure {
                group = "build"
                inputFiles.from("src/commonMain/c")
                includeDirs.from(openssl.includeDirectory(konanTarget.toOpensslTarget()))
            }

            compileTaskProvider.configure {
                compilerOptions {
                    // include bitcode
                    freeCompilerArgs.addAll(
                        compileToBitcodeTask.map { task ->
                            task.outputDirectory.asFileTree.flatMap {
                                listOf("-native-library", it.absolutePath)
                            }
                        }
                    )
                    // include a static library
                    freeCompilerArgs.addAll(
                        openssl.libDirectory(konanTarget.toOpensslTarget()).map {
                            listOf("-include-binary", it.asFile.resolve("libcrypto.a").absolutePath)
                        }
                    )
                    // additional linker options
                    when (konanTarget.family) {
                        Family.MINGW -> {
                            freeCompilerArgs.addAll(
                                "-include-binary",
                                layout.projectDirectory.file("src/mingwMain/libs/libz.a").asFile.absolutePath
                            )
                            freeCompilerArgs.addAll("-linker-option", "-lws2_32")
                            freeCompilerArgs.addAll("-linker-option", "-lcrypt32")
                        }

                        Family.LINUX -> freeCompilerArgs.addAll("-linker-option", "-lz")

                        Family.ANDROID -> {
                            freeCompilerArgs.addAll("-linker-option", "-lz")
                            if (konanTarget == KonanTarget.ANDROID_X86) {
                                freeCompilerArgs.addAll("-linker-option", "-latomic")
                            }
                        }

                        else -> {}
                    }
                }
            }
        }
    }
}
