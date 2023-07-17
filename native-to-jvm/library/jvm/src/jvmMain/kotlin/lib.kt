package dev.whyoleg.interop.playground.ntj.jvm

import java.nio.file.Files
import kotlin.io.path.outputStream
import kotlin.io.path.pathString

fun nativeCall(argument: Int): Int = JNI.nativeCall(argument)

private object JNI {
    init {
        loadFromResources("ntj")
        loadFromResources("ntjjni")
    }

    external fun nativeCall(argument: Int): Int
}

private fun loadFromResources(libraryName: String) {
    val tempFile = Files.createTempFile(libraryName, "dylib")
    JNI::class.java.getResourceAsStream("/libs/lib$libraryName.dylib")!!.use { libraryStream ->
        tempFile.outputStream().use { tempStream ->
            libraryStream.copyTo(tempStream)
        }
    }
    System.load(tempFile.toAbsolutePath().pathString)
}
