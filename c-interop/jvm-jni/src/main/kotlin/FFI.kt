package dev.whyoleg.kip.c.jvm.jni

import java.nio.file.*
import kotlin.io.path.*

object FFI {
    init {
        // first load actual library, then jni for it
        System.loadLibrary("crypto")
        System.load(extractJniLibraryFromJar("crypto-jni").absolutePathString())
    }
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
    return FFI::class.java.classLoader.getResourceAsStream(
        "libs/$os-$arch/$libraryName"
    )!!.use { library ->
        createTempFile(suffix = libraryName).also {
            it.outputStream().use(library::copyTo)
        }
    }
}
