@file:Suppress("FunctionName")

package dev.whyoleg.kip.c.native.raw

import kotlinx.cinterop.*
import kotlinx.cinterop.internal.*
import platform.posix.*

typealias Pointer_OSSL_LIB_CTX = Long
typealias Pointer_EVP_MD = Long
typealias Pointer_EVP_MD_CTX = Long

@CCall("ffi_EVP_MD_fetch")
external fun EVP_MD_fetch(ctx: Pointer_OSSL_LIB_CTX, algorithm: Pointer_Byte, properties: Pointer_Byte): Pointer_EVP_MD

@CCall("ffi_EVP_MD_free")
external fun EVP_MD_free(md: Pointer_EVP_MD)

@CCall("ffi_EVP_MD_get_size")
external fun EVP_MD_get_size(md: Pointer_EVP_MD): Int

@CCall("ffi_EVP_MD_CTX_new")
external fun EVP_MD_CTX_new(): Pointer_EVP_MD_CTX

@CCall("ffi_EVP_MD_CTX_free")
external fun EVP_MD_CTX_free(ctx: Pointer_EVP_MD_CTX)

@CCall("ffi_EVP_DigestInit")
external fun EVP_DigestInit(ctx: Pointer_EVP_MD_CTX, type: Pointer_EVP_MD): Int

@OptIn(UnsafeNumber::class)
@CCall("ffi_EVP_DigestUpdate")
external fun EVP_DigestUpdate(ctx: Pointer_EVP_MD_CTX, d: Pointer_Void, cnt: size_t): Int

@CCall("ffi_EVP_DigestFinal")
external fun EVP_DigestFinal(ctx: Pointer_EVP_MD_CTX, md: Pointer_UByte, s: Pointer_UInt): Int
