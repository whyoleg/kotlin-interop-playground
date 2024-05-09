@file:Suppress("FunctionName")

package dev.whyoleg.kip.c.native.raw

import kotlinx.cinterop.internal.*

// returns pointer
@CCall("ffi_OpenSSL_version")
internal external fun ffi_OpenSSL_version(type: Int): Long

@CCall("ffi_OPENSSL_version_major")
internal external fun ffi_OPENSSL_version_major(): Int

