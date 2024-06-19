import kipbuild.jextract.*
import kipbuild.openssl.*
import org.jetbrains.kotlin.konan.target.*

plugins {
    id("kipbuild.kotlin.jvm")
    id("kipbuild.openssl")
    id("kipbuild.jextract")
}

val runJextractTask = jextract.registerTask("runJextract") {
    mainHeader.set(openssl.includeDirectory(HOST.toOpensslTarget()).map {
        it.file("openssl/evp.h")
    })
    libraries.add("crypto")
    includeDirectory.set(openssl.includeDirectory(HOST.toOpensslTarget()))
    packageName.set("dev.whyoleg.kip.c.jvm.ffm.jextract.generated")
}

// needed to generate sources during IDEA import
(tasks.findByPath("prepareKotlinIdeaImport") ?: tasks.create("prepareKotlinIdeaImport")).dependsOn(runJextractTask)

kotlin {
    jvmToolchain(22)
}

dependencies {
    testImplementation(kotlin("test"))
}

sourceSets {
    main {
        java.srcDir(runJextractTask)
    }
}

tasks.test {
    dependsOn(openssl.setupTask)
    // to suppress warning
    jvmArgs("--enable-native-access=ALL-UNNAMED")

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
