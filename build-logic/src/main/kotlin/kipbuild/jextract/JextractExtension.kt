package kipbuild.jextract

import org.gradle.api.*
import org.gradle.api.file.*
import org.gradle.api.provider.*
import org.gradle.api.tasks.*
import org.jetbrains.kotlin.konan.target.*

val HOST = HostManager.host

class JextractExtension(
    private val project: Project,
    private val setupTask: TaskProvider<Copy>
) {
    private val directory: Provider<Directory> = project.layout.dir(setupTask.map { it.destinationDir })

    private val executable = directory.map {
        val binary = when (HOST) {
            KonanTarget.MINGW_X64 -> "jextract.bat"
            else                  -> "jextract"
        }
        it.file("jextract-22/bin/$binary")
    }

    fun registerTask(name: String, block: JextractTask.() -> Unit): TaskProvider<JextractTask> =
        project.tasks.register(name, JextractTask::class.java) {
            executable.set(this@JextractExtension.executable)
            block()
        }
}
