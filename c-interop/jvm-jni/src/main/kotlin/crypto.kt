@file:Suppress("ClassName", "FunctionName")

package dev.whyoleg.kip.c.jvm.jni

object crypto {
    init {
        JNI
    }

    const val OPENSSL_VERSION_STRING: Int = 6 //magic :)

    @JvmStatic
    external fun OpenSSL_version(type: Int): Long

    @JvmStatic
    external fun OPENSSL_version_major(): Int
}
