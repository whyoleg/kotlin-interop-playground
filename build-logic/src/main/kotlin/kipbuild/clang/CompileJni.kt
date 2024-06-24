package kipbuild.clang

import org.gradle.api.file.*
import org.gradle.api.provider.*
import org.gradle.api.tasks.*
import org.gradle.jvm.toolchain.*
import org.jetbrains.kotlin.konan.target.*
import javax.inject.*

abstract class CompileJni @Inject constructor(
    konanTarget: KonanTarget,
    private val javaToolchainService: JavaToolchainService,
) : ClangKonanTask<CompileJni>(konanTarget, CompileJni::class) {

    init {
        // for cross compilation
        enabled = HostManager().isEnabled(konanTarget)
    }

    @get:InputFiles
    val includeDirs: ConfigurableFileCollection = objectFactory.fileCollection()

    @get:InputFiles
    val inputFiles: ConfigurableFileCollection = objectFactory.fileCollection()

    @get:Input
    val linkPaths: ListProperty<String> = objectFactory.listProperty(String::class.java)

    @get:Input
    val linkLibraries: ListProperty<String> = objectFactory.listProperty(String::class.java)

    @get:Input
    val outputLibraryName: Property<String> = objectFactory.property(String::class.java)

    override fun setup() {
        // build a shared library
        args("-shared", "-fPIC")

        // TODO: for cross compilation other JDK are needed
        // find jni headers
        val javaHome = javaToolchainService.compilerFor {
            // version can be taken from the toolchain
            languageVersion.set(JavaLanguageVersion.of(8))
        }.get().metadata.installationPath.dir("include")

        val javaHostFolder = when (konanTarget) {
            KonanTarget.LINUX_X64                          -> "linux"
            KonanTarget.MACOS_X64, KonanTarget.MACOS_ARM64 -> "darwin"
            KonanTarget.MINGW_X64                          -> "windows"
            else                                           -> error("Unknown target: $konanTarget")
        }

        // include paths to jni headers
        args("-I${javaHome.asFile.absolutePath}")
        args("-I${javaHome.dir(javaHostFolder).asFile.absolutePath}")

        // include dirs to search headers
        args(includeDirs.map { "-I${it.absolutePath}" })

        // link paths/libs
        args(linkPaths.get().map { "-L$it" })
        args(linkLibraries.get().map { "-l$it" })

        val shortLibraryName = outputLibraryName.get()
        val fullLibraryName = when (konanTarget) {
            KonanTarget.LINUX_X64                          -> "lib${shortLibraryName}.so"
            KonanTarget.MACOS_X64, KonanTarget.MACOS_ARM64 -> "lib${shortLibraryName}.dylib"
            KonanTarget.MINGW_X64                          -> "${shortLibraryName}.dll"
            else                                           -> error("Unknown target: $konanTarget")
        }

        // output path
        args("-o", fullLibraryName)

        // input files
        inputFiles.asFileTree.forEach {
            args(it.absolutePath)
        }
    }
}
