package dev.whyoleg.kip.c.jvm.jni

import java.nio.*
import java.nio.file.*
import kotlin.io.path.*

object JNI {
    init {
        System.load(extractJniLibraryFromJar("crypto-jni").absolutePathString())
    }

    @JvmStatic
    external fun getByteBufferFromPointer(pointer: Long, size: Int): ByteBuffer?

    @JvmStatic
    external fun getPointerFromByteBuffer(buffer: ByteBuffer?): Long

    @JvmStatic
    external fun getStringFromPointer(pointer: Long): String?
}

private fun extractJniLibraryFromJar(name: String): Path {
    val libraryName = System.mapLibraryName(name)
    val osName = System.getProperty("os.name")
    val os = when {
        osName == "Mac OS X"         -> "macos"
        osName == "Linux"            -> "linux"
        osName.startsWith("Windows") -> "windows"
        else                         -> error("Unknown OS: $osName")
    }
    val arch = when (val arch = System.getProperty("os.arch")) {
        "x86_64", "amd64"  -> "x64"
        "arm64", "aarch64" -> "arm64"
        else               -> error("Unsupported architecture: $arch")
    }
    return JNI::class.java.classLoader.getResourceAsStream(
        "libs/$os-$arch/$libraryName"
    )!!.use { library ->
        createTempFile(suffix = libraryName).also {
            it.outputStream().use(library::copyTo)
        }
    }
}
