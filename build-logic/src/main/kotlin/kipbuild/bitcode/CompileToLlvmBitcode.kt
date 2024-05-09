package kipbuild.bitcode

import org.gradle.api.file.*
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.*
import org.jetbrains.kotlin.gradle.targets.native.toolchain.*
import org.jetbrains.kotlin.konan.target.*
import javax.inject.*

@Suppress("INVISIBLE_REFERENCE", "INVISIBLE_MEMBER")
abstract class CompileToLlvmBitcode @Inject constructor(
    @get:Input val konanTarget: KonanTarget,
) : AbstractExecTask<CompileToLlvmBitcode>(CompileToLlvmBitcode::class.java), UsesKotlinNativeBundleBuildService {

    @get:Nested
    internal val kotlinNativeProvider: Provider<KotlinNativeProvider> = project.provider {
        KotlinNativeProvider(project, konanTarget, kotlinNativeBundleBuildService)
    }

    @get:InputFiles
    val includeDirs: ConfigurableFileCollection = objectFactory.fileCollection()

    @get:InputFiles
    val inputFiles: ConfigurableFileCollection = objectFactory.fileCollection()

    @get:OutputDirectory
    val outputDirectory: DirectoryProperty = objectFactory.directoryProperty().convention(
        project.layout.dir(
            project.provider {
                temporaryDirFactory.create()!!
            }
        )
    )

    override fun exec() {
        val provider = kotlinNativeProvider.get()

        // recreate output directory
        outputDirectory.get().asFile.apply {
            deleteRecursively()
            mkdirs()
        }

        // clang will produce output into the current working directory
        workingDir(outputDirectory.get())

        // run clang vis K/N toolchain
        executable(provider.bundleDirectory.file("bin/run_konan").get().asFile.absolutePath)
        args("clang", "clang", konanTarget)

        // emit llvm bitcode, which will be embedded afterwards
        args("-emit-llvm", "-c")

        // include dirs to search headers
        includeDirs.forEach {
            args("-I$it")
        }

        // input files
        inputFiles.asFileTree.forEach {
            args(it.absolutePath)
        }
        super.exec()
    }
}
