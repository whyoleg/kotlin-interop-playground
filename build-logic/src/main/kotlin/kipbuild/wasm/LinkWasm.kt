package kipbuild.wasm

import org.gradle.api.file.*
import org.gradle.api.provider.*
import org.gradle.api.tasks.*
import org.gradle.kotlin.dsl.*
import javax.inject.*

abstract class LinkWasm @Inject constructor(

) : AbstractExecTask<LinkWasm>(LinkWasm::class.java) {
    @get:Input
    val emcc: Property<String> = objectFactory.property<String>().convention("emcc")

    @get:Input
    val linkPaths: ListProperty<String> = objectFactory.listProperty()

    @get:Input
    val linkLibraries: ListProperty<String> = objectFactory.listProperty()

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

    @get:Input
    val outputLibraryName: Property<String> = objectFactory.property()

    override fun exec() {
        // recreate output directory
        outputDirectory.get().asFile.apply {
            deleteRecursively()
            mkdirs()
        }

        // clang will produce output into the current working directory
        workingDir(outputDirectory.get())

        executable(emcc.get())
        args("--no-entry")
        args("-s", "EXPORTED_FUNCTIONS=_free,_malloc")
        args("-s", "EXPORTED_RUNTIME_METHODS=UTF8ToString")

        // link paths/libs
        args(linkPaths.get().map { "-L$it" })
        args(linkLibraries.get().map { "-l$it" })

        // include dirs
        args(includeDirs.map { "-I${it.absolutePath}" })

        // input
        args(inputFiles.asFileTree.map { it.absolutePath })

        // output
        args("-o", "${outputLibraryName.get()}.wasm")
        args("-o", "${outputLibraryName.get()}.js")

        super.exec()
    }
}
