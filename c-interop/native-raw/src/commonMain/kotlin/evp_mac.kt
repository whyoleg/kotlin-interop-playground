@file:Suppress("FunctionName")

package dev.whyoleg.kip.c.native.raw

import kotlinx.cinterop.*
import kotlinx.cinterop.internal.*
import platform.posix.*

typealias Pointer_EVP_MAC = Long
typealias Pointer_EVP_MAC_CTX = Long

@CCall("ffi_EVP_MAC_fetch")
external fun EVP_MAC_fetch(
    libctx: Pointer_OSSL_LIB_CTX,
    algorithm: Pointer_Byte,
    properties: Pointer_Byte,
): Pointer_EVP_MAC

@CCall("ffi_EVP_MAC_CTX_new")
external fun EVP_MAC_CTX_new(
    mac: Pointer_EVP_MAC
): Pointer_EVP_MAC_CTX

@OptIn(UnsafeNumber::class)
@CCall("ffi_EVP_MAC_init")
external fun EVP_MAC_init(
    ctx: Pointer_EVP_MAC_CTX,
    key: Pointer_UByte,
    keylen: size_t,
    params: Pointer_OSSL_PARAM,
): Int

@OptIn(UnsafeNumber::class)
@CCall("ffi_EVP_MAC_update")
external fun EVP_MAC_update(
    ctx: Pointer_EVP_MAC_CTX,
    data: Pointer_UByte,
    datalen: size_t,
): Int

@OptIn(UnsafeNumber::class)
@CCall("ffi_EVP_MAC_final")
external fun EVP_MAC_final(
    ctx: Pointer_EVP_MAC_CTX,
    out: Pointer_UByte,
    outl: Pointer_size_t,
    outsize: size_t,
): Int

@OptIn(UnsafeNumber::class)
@CCall("ffi_EVP_MAC_CTX_get_mac_size")
external fun EVP_MAC_CTX_get_mac_size(
    ctx: Pointer_EVP_MAC_CTX,
): size_t

@CCall("ffi_EVP_MAC_CTX_free")
external fun EVP_MAC_CTX_free(ctx: Pointer_EVP_MAC_CTX)

@CCall("ffi_EVP_MAC_free")
external fun EVP_MAC_free(ctx: Pointer_EVP_MAC)
