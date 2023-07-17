import org.jetbrains.kotlin.gradle.tasks.KotlinNativeLink

plugins {
    kotlin("multiplatform") version "1.9.0"
}

val jniLibrariesPath = layout.buildDirectory.dir("jniLibraries")

// here `linkDebugSharedMacosArm64` should be replaced with the needed release type and target
//
// using `evaluationDependsOn` is for the sake of simplicity, better to use gradle configurations API for such tasks
val linkNativeLibrary =
    evaluationDependsOn(":library:native").tasks
        .named<KotlinNativeLink>("linkDebugSharedMacosArm64")

// copy K/N libraries to current project - same, better to use gradle configurations
val copyNativeLibrary by tasks.registering(Copy::class) {
    into(layout.buildDirectory.dir("nativeLibraries"))
    from(linkNativeLibrary.map { it.destinationDirectory }) {
        into("libs")
    }
    include("*.dylib")
}

// build a shared JNI library from src/jvmMain/c/jni.c linked to built K/N library
//  Note: uses plain `clang` - tested only on macOS.
//  For production use cases better to further configure compiler flags
val buildJniLibrary by tasks.registering(Exec::class) {
    dependsOn(linkNativeLibrary)

    executable("clang")
    args("-shared", "-fPIC")

    // use Gradle toolchain to get JDK headers
    val javaHome = javaToolchains.compilerFor {
        languageVersion.set(JavaLanguageVersion.of(8))
    }.get().metadata.installationPath.dir("include")

    val nativeLibDir = linkNativeLibrary.get().destinationDirectory.get()

    args(
        "-L${nativeLibDir}",
        "-lntj"
    )

    //include dirs
    args(
        listOf(
            javaHome,
            javaHome.dir("darwin"), // need to be changed for other OSs
            nativeLibDir
        ).map { "-I$it" }
    )

    val outputFile = jniLibrariesPath.get().file("libs/libntjjni.dylib").asFile
    outputFile.parentFile.mkdirs()
    //output
    args("-o", outputFile.absolutePath)

    //input
    args(layout.projectDirectory.file("src/jvmMain/c/jni.c").asFile.absolutePath)
}

kotlin {
    jvmToolchain(8)
    jvm()
    sourceSets {
        val jvmMain by getting {
            resources.srcDirs(
                buildJniLibrary.map { jniLibrariesPath },
                copyNativeLibrary.map { it.destinationDir }
            )
        }
    }
}
