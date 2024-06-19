import kipbuild.jextract.*
import org.jetbrains.kotlin.konan.target.*

val configuration = configurations.create("jextract")
dependencies {
    val artifact = when (HOST) {
        KonanTarget.LINUX_X64   -> "linux-x64"
        KonanTarget.MACOS_X64   -> "macos-x64"
        KonanTarget.MACOS_ARM64 -> "macos-aarch64"
        KonanTarget.MINGW_X64   -> "windows-x64"
        else                    -> error("Unknown host: $HOST")
    }
    configuration("kipbuild.dependencies.jextract:$artifact@tar.gz")
}

val setupJextract = tasks.register<Copy>("setupJextract") {
    from(tarTree(resources.gzip(provider { configuration.singleFile })))
    into(temporaryDir)
}

extensions.add(
    "jextract",
    JextractExtension(project, setupJextract)
)
