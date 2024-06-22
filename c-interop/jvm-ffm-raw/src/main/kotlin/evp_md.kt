@file:Suppress("FunctionName")

package dev.whyoleg.kip.c.jvm.ffm.raw

import java.lang.foreign.*
import java.lang.invoke.*

private val EVP_MD_fetch: MethodHandle = FFI.methodHandle(
    name = "EVP_MD_fetch",
    FunctionDescriptor.of(
        /* resLayout = */ ValueLayout.ADDRESS,
        /* ...argLayouts = */ ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS
    )
)

fun EVP_MD_fetch(
    ctx: MemorySegment,
    algorithm: MemorySegment,
    properties: MemorySegment,
): MemorySegment {
    return EVP_MD_fetch.invokeExact(
        ctx,
        algorithm,
        properties,
    ) as MemorySegment
}

private val EVP_MD_CTX_new: MethodHandle = FFI.methodHandle(
    name = "EVP_MD_CTX_new",
    FunctionDescriptor.of(
        /* resLayout = */ ValueLayout.ADDRESS
    )
)

fun EVP_MD_CTX_new(): MemorySegment {
    return EVP_MD_CTX_new.invokeExact() as MemorySegment
}

private val EVP_MD_get_size: MethodHandle = FFI.methodHandle(
    name = "EVP_MD_get_size",
    FunctionDescriptor.of(
        /* resLayout = */ ValueLayout.JAVA_INT,
        /* ...argLayouts = */ ValueLayout.ADDRESS
    )
)

fun EVP_MD_get_size(md: MemorySegment): Int {
    return EVP_MD_get_size.invokeExact(md) as Int
}

private val EVP_DigestInit: MethodHandle = FFI.methodHandle(
    name = "EVP_DigestInit",
    FunctionDescriptor.of(
        /* resLayout = */ ValueLayout.JAVA_INT,
        /* ...argLayouts = */ ValueLayout.ADDRESS, ValueLayout.ADDRESS
    )
)

fun EVP_DigestInit(
    ctx: MemorySegment,
    type: MemorySegment,
): Int {
    return EVP_DigestInit.invokeExact(ctx, type) as Int
}

private val EVP_DigestUpdate: MethodHandle = FFI.methodHandle(
    name = "EVP_DigestUpdate",
    FunctionDescriptor.of(
        /* resLayout = */ ValueLayout.JAVA_INT,
        /* ...argLayouts = */ ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG
    )
)

fun EVP_DigestUpdate(
    ctx: MemorySegment,
    d: MemorySegment,
    cnt: Long,
): Int {
    return EVP_DigestUpdate.invokeExact(ctx, d, cnt) as Int
}

private val EVP_DigestFinal: MethodHandle = FFI.methodHandle(
    name = "EVP_DigestFinal",
    FunctionDescriptor.of(
        /* resLayout = */ ValueLayout.JAVA_INT,
        /* ...argLayouts = */ ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS
    )
)

fun EVP_DigestFinal(
    ctx: MemorySegment,
    md: MemorySegment,
    s: MemorySegment,
): Int {
    return EVP_DigestFinal.invokeExact(ctx, md, s) as Int
}

private val EVP_MD_CTX_free: MethodHandle = FFI.methodHandle(
    name = "EVP_MD_CTX_free",
    FunctionDescriptor.ofVoid(
        /* ...argLayouts = */ ValueLayout.ADDRESS
    )
)

fun EVP_MD_CTX_free(ctx: MemorySegment) {
    EVP_MD_CTX_free.invokeExact(ctx)
}

private val EVP_MD_free: MethodHandle = FFI.methodHandle(
    name = "EVP_MD_free",
    FunctionDescriptor.ofVoid(
        /* ...argLayouts = */ ValueLayout.ADDRESS
    )
)

fun EVP_MD_free(ctx: MemorySegment) {
    EVP_MD_free.invokeExact(ctx)
}
