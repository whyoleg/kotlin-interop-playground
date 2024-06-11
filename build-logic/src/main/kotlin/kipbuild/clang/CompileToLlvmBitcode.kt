package kipbuild.clang

import org.gradle.api.file.*
import org.gradle.api.tasks.*
import org.jetbrains.kotlin.konan.target.*
import javax.inject.*

abstract class CompileToLlvmBitcode @Inject constructor(
    konanTarget: KonanTarget,
) : ClangKonanTask<CompileToLlvmBitcode>(konanTarget, CompileToLlvmBitcode::class) {

    @get:InputFiles
    val includeDirs: ConfigurableFileCollection = objectFactory.fileCollection()

    @get:InputFiles
    val inputFiles: ConfigurableFileCollection = objectFactory.fileCollection()

    override fun exec() {
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
