package kipbuild.clang

import org.gradle.api.file.*
import org.gradle.api.provider.*
import org.gradle.api.tasks.*
import org.jetbrains.kotlin.gradle.targets.native.toolchain.*
import org.jetbrains.kotlin.konan.target.*
import kotlin.reflect.*

// `UsesKotlinNativeBundleBuildService` and `KotlinNativeProvider` are `internal` KGP APIs
@Suppress("INVISIBLE_REFERENCE", "INVISIBLE_MEMBER")
abstract class ClangKonanTask<T : ClangKonanTask<T>>(
    @get:Input val konanTarget: KonanTarget,
    taskType: KClass<T>
) : AbstractExecTask<T>(taskType.java), UsesKotlinNativeBundleBuildService {

    @get:Nested
    internal val kotlinNativeProvider: Provider<KotlinNativeProvider> = project.provider {
        KotlinNativeProvider(project, konanTarget, kotlinNativeBundleBuildService)
    }

    @get:OutputDirectory
    val outputDirectory: DirectoryProperty = objectFactory.directoryProperty().convention(
        project.layout.dir(
            project.provider {
                temporaryDirFactory.create()!!
            }
        )
    )

    protected abstract fun setup()

    final override fun exec() {
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

        setup()

        super.exec()
    }
}
