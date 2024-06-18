@file:Suppress("FunctionName")

package dev.whyoleg.kip.c.native.raw

import kotlinx.cinterop.internal.*

// magic :)
// defined as `# define OPENSSL_VERSION_STRING 6`
const val OPENSSL_VERSION_STRING = 6

@CCall("ffi_OpenSSL_version")
external fun OpenSSL_version(type: Int): Pointer_Byte

@CCall("ffi_OPENSSL_version_major")
external fun OPENSSL_version_major(): Int
