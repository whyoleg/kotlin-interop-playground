@file:Suppress("FunctionName")

package dev.whyoleg.kip.c.jvm.ffm.raw

import java.lang.foreign.*
import java.lang.invoke.*

private val EVP_MAC_fetch_MH: MethodHandle = FFI.methodHandle(
    name = "EVP_MAC_fetch",
    FunctionDescriptor.of(
        /* resLayout = */ ValueLayout.ADDRESS,
        /* ...argLayouts = */ ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS
    )
)

fun EVP_MAC_fetch(
    libctx: MemorySegment,
    algorithm: MemorySegment,
    properties: MemorySegment,
): MemorySegment {
    return EVP_MAC_fetch_MH.invokeExact(
        libctx,
        algorithm,
        properties,
    ) as MemorySegment
}

private val EVP_MAC_CTX_new_MH: MethodHandle = FFI.methodHandle(
    name = "EVP_MAC_CTX_new",
    FunctionDescriptor.of(
        /* resLayout = */ ValueLayout.ADDRESS,
        /* ...argLayouts = */ ValueLayout.ADDRESS
    )
)

fun EVP_MAC_CTX_new(
    mac: MemorySegment,
): MemorySegment {
    return EVP_MAC_CTX_new_MH.invokeExact(mac) as MemorySegment
}

private val EVP_MAC_init_MH: MethodHandle = FFI.methodHandle(
    name = "EVP_MAC_init",
    FunctionDescriptor.of(
        /* resLayout = */ ValueLayout.JAVA_INT,
        /* ...argLayouts = */ ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG, ValueLayout.ADDRESS
    )
)

fun EVP_MAC_init(
    ctx: MemorySegment,
    key: MemorySegment,
    keylen: Long,
    params: MemorySegment,
): Int {
    return EVP_MAC_init_MH.invokeExact(
        ctx,
        key,
        keylen,
        params
    ) as Int
}

private val EVP_MAC_CTX_get_mac_size_MH: MethodHandle = FFI.methodHandle(
    name = "EVP_MAC_CTX_get_mac_size",
    FunctionDescriptor.of(
        /* resLayout = */ ValueLayout.JAVA_LONG,
        /* ...argLayouts = */ ValueLayout.ADDRESS
    )
)

fun EVP_MAC_CTX_get_mac_size(
    ctx: MemorySegment,
): Long {
    return EVP_MAC_CTX_get_mac_size_MH.invokeExact(ctx) as Long
}

private val EVP_MAC_update_MH: MethodHandle = FFI.methodHandle(
    name = "EVP_MAC_update",
    FunctionDescriptor.of(
        /* resLayout = */ ValueLayout.JAVA_INT,
        /* ...argLayouts = */ ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG
    )
)

fun EVP_MAC_update(
    ctx: MemorySegment,
    data: MemorySegment,
    datalen: Long,
): Int {
    return EVP_MAC_update_MH.invokeExact(
        ctx,
        data,
        datalen,
    ) as Int
}

private val EVP_MAC_final_MH: MethodHandle = FFI.methodHandle(
    name = "EVP_MAC_final",
    FunctionDescriptor.of(
        /* resLayout = */ ValueLayout.JAVA_INT,
        /* ...argLayouts = */ ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG
    )
)

fun EVP_MAC_final(
    ctx: MemorySegment,
    out: MemorySegment,
    outl: MemorySegment,
    outsize: Long,
): Int {
    return EVP_MAC_final_MH.invokeExact(
        ctx,
        out,
        outl,
        outsize.toLong(),
    ) as Int
}

private val EVP_MAC_CTX_free_MH: MethodHandle = FFI.methodHandle(
    name = "EVP_MAC_CTX_free",
    FunctionDescriptor.ofVoid(
        /* ...argLayouts = */ ValueLayout.ADDRESS
    )
)

fun EVP_MAC_CTX_free(ctx: MemorySegment) {
    EVP_MAC_CTX_free_MH.invokeExact(ctx)
}

private val EVP_MAC_free_MH: MethodHandle = FFI.methodHandle(
    name = "EVP_MAC_free",
    FunctionDescriptor.ofVoid(
        /* ...argLayouts = */ ValueLayout.ADDRESS
    )
)

fun EVP_MAC_free(ctx: MemorySegment) {
    EVP_MAC_free_MH.invokeExact(ctx)
}
