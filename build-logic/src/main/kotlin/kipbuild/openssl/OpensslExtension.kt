package kipbuild.openssl

import kipbuild.dependencies.*
import org.gradle.api.file.*
import org.gradle.api.provider.*
import org.gradle.api.tasks.*

class OpensslExtension(val setupTask: TaskProvider<UnzipTask>) {
    private val directory: Provider<Directory> = setupTask.map { it.outputDirectory.get() }

    // `bin` exists only for windows_x64
    fun binDirectory(target: OpensslTarget) = directory.map { it.dir("$target/bin") }
    fun libDirectory(target: OpensslTarget) = directory.map { it.dir("$target/lib") }
    fun includeDirectory(target: OpensslTarget) = directory.map { it.dir("$target/include") }
}
