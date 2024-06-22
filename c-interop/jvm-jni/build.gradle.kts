import kipbuild.*
import kipbuild.clang.*
import kipbuild.openssl.*
import org.jetbrains.kotlin.konan.target.*

plugins {
    id("kipbuild.kotlin.jvm")
    id("kipbuild.openssl")
}

val compileMacosArm64Jni = tasks.register<CompileJni>(
    "compileMacosArm64Jni",
    KonanTarget.MACOS_ARM64
)

compileMacosArm64Jni.configure {
    inputFiles.from("src/main/c")
    includeDirs.from(openssl.includeDirectory(HOST.toOpensslTarget()))
    linkPaths.add(openssl.libDirectory(HOST.toOpensslTarget()).map { it.asFile.absolutePath })
    linkLibraries.add("crypto")
    outputLibraryName.set("crypto-jni")
}

kotlin {
    jvmToolchain(8)
}

tasks.processResources {
    from(compileMacosArm64Jni) {
        into("libs/macos-arm64")
    }
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    dependsOn(openssl.setupTask)

    when (HOST) {
        KonanTarget.LINUX_X64                          -> environment(
            "LD_LIBRARY_PATH",
            openssl.libDirectory(HOST.toOpensslTarget()).get().asFile.absolutePath
        )

        KonanTarget.MACOS_X64, KonanTarget.MACOS_ARM64 ->
            environment(
                "DYLD_LIBRARY_PATH",
                openssl.libDirectory(HOST.toOpensslTarget()).get().asFile.absolutePath
            )

        KonanTarget.MINGW_X64                          -> TODO("add to `PATH`")
        else                                           -> error("Unknown host: $HOST")
    }
}
