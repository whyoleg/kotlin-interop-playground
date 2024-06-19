package kipbuild.jextract

import org.gradle.api.file.*
import org.gradle.api.tasks.*
import org.gradle.kotlin.dsl.*
import javax.inject.*

abstract class JextractTask @Inject constructor() : AbstractExecTask<JextractTask>(JextractTask::class.java) {

    @get:InputFile
    internal val executable = objectFactory.fileProperty()

    @get:Input
    val packageName = objectFactory.property<String>()

    @get:Input
    val libraries = objectFactory.listProperty<String>()

    @get:InputFile
    val mainHeader = objectFactory.fileProperty()

    @get:InputFiles
    val includeDirectory = objectFactory.directoryProperty()

    @get:OutputDirectory
    val outputDirectory: DirectoryProperty = objectFactory.directoryProperty().convention(
        project.layout.dir(
            project.provider {
                temporaryDirFactory.create()!!
            }
        )
    )

    final override fun exec() {
        // recreate output directory
        outputDirectory.get().asFile.apply {
            deleteRecursively()
            mkdirs()
        }

        executable(executable.get().asFile.absolutePath)

        args("--include-dir", includeDirectory.get().asFile.absolutePath)
        args("--output", outputDirectory.get().asFile.absolutePath)
        args("--target-package", packageName.get())
        libraries.get().forEach {
            args("--library", it)
        }
        args(mainHeader.get().asFile.absolutePath)

        super.exec()
    }
}
