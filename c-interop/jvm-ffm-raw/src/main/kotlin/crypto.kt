@file:Suppress("FunctionName")

package dev.whyoleg.kip.c.jvm.ffm.raw

import java.lang.foreign.*
import java.lang.invoke.*

// magic :)
// defined as `# define OPENSSL_VERSION_STRING 6`
const val OPENSSL_VERSION_STRING = 6

private val OpenSSL_version: MethodHandle = FFI.methodHandle(
    name = "OpenSSL_version",
    FunctionDescriptor.of(
        /* resLayout = */ FFI.UnboundedAddressLayout,
        /* ...argLayouts = */ ValueLayout.JAVA_INT
    )
)

fun OpenSSL_version(type: Int): MemorySegment {
    return OpenSSL_version.invokeExact(type) as MemorySegment
}

private val OPENSSL_version_major: MethodHandle = FFI.methodHandle(
    name = "OPENSSL_version_major",
    FunctionDescriptor.of(
        /* resLayout = */ ValueLayout.JAVA_INT
    )
)

fun OPENSSL_version_major(): Int {
    return OPENSSL_version_major.invokeExact() as Int
}
