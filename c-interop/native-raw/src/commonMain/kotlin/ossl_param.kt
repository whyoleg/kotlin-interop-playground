@file:Suppress("FunctionName")

package dev.whyoleg.kip.c.native.raw

import kotlinx.cinterop.*
import kotlinx.cinterop.internal.*
import platform.posix.*

typealias Pointer_OSSL_PARAM = Long

// # define OSSL_PARAM_INTEGER 1
const val OSSL_PARAM_INTEGER = 1

@OptIn(UnsafeNumber::class)
@CCall("ffi_OSSL_PARAM_construct_utf8_string")
external fun OSSL_PARAM_construct_utf8_string(
    key: Pointer_Byte,
    buf: Pointer_Byte,
    bsize: size_t,
    returnPointer: Pointer_OSSL_PARAM
)

@CCall("ffi_OSSL_PARAM_construct_int")
external fun OSSL_PARAM_construct_int(
    key: Pointer_Byte,
    buf: Pointer_Int,
    returnPointer: Pointer_OSSL_PARAM
)

@CCall("ffi_OSSL_PARAM_get_int")
external fun OSSL_PARAM_get_int(
    p: Pointer_OSSL_PARAM,
    v: Pointer_Int
): Int

@CCall("ffi_OSSL_PARAM_construct_end")
external fun OSSL_PARAM_construct_end(returnPointer: Pointer_OSSL_PARAM)

@OptIn(UnsafeNumber::class)
@CCall("ffi_size_off_OSSL_PARAM")
external fun size_off_OSSL_PARAM(): size_t

@OptIn(UnsafeNumber::class)
@CCall("ffi_align_off_OSSL_PARAM")
external fun align_off_OSSL_PARAM(): size_t

@OptIn(UnsafeNumber::class)
@CCall("ffi_offset_off_data_OSSL_PARAM")
external fun offset_off_data_OSSL_PARAM(): size_t

@OptIn(UnsafeNumber::class)
@CCall("ffi_offset_off_data_type_OSSL_PARAM")
external fun offset_off_data_type_OSSL_PARAM(): size_t

@OptIn(UnsafeNumber::class)
@CCall("ffi_offset_off_key_OSSL_PARAM")
external fun offset_off_key_OSSL_PARAM(): size_t
