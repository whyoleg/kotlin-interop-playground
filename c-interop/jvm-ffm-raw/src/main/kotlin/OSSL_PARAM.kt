@file:Suppress("FunctionName", "ClassName")

package dev.whyoleg.kip.c.jvm.ffm.raw

import java.lang.foreign.*
import java.lang.invoke.*

// # define OSSL_PARAM_INTEGER 1
const val OSSL_PARAM_INTEGER = 1

object OSSL_PARAM {
    val layout: StructLayout = MemoryLayout.structLayout(
        FFI.UnboundedAddressLayout.withName("key"), //8
        ValueLayout.JAVA_INT.withName("data_type"), //4
        MemoryLayout.paddingLayout(4), //TODO: how this works ? :) //4
        FFI.UnboundedAddressLayout.withName("data"), //8
        ValueLayout.JAVA_LONG.withName("data_size"), //8
        ValueLayout.JAVA_LONG.withName("return_size"), //8
    )

    private val key = layout.varHandle(MemoryLayout.PathElement.groupElement("key"))
    private val data_type = layout.varHandle(MemoryLayout.PathElement.groupElement("data_type"))
    private val data = layout.varHandle(MemoryLayout.PathElement.groupElement("data"))

    fun key(segment: MemorySegment): MemorySegment = key.get(segment, 0) as MemorySegment
    fun data_type(segment: MemorySegment): Int = data_type.get(segment, 0) as Int
    fun data(segment: MemorySegment): MemorySegment = data.get(segment, 0) as MemorySegment
}

private val OSSL_PARAM_construct_utf8_string: MethodHandle = FFI.methodHandle(
    name = "OSSL_PARAM_construct_utf8_string",
    FunctionDescriptor.of(
        /* resLayout = */ OSSL_PARAM.layout,
        /* ...argLayouts = */ ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG
    )
)

fun OSSL_PARAM_construct_utf8_string(
    allocator: SegmentAllocator,
    key: MemorySegment,
    buf: MemorySegment,
    bsize: Long,
): MemorySegment {
    return OSSL_PARAM_construct_utf8_string.invokeExact(
        allocator,
        key,
        buf,
        bsize
    ) as MemorySegment
}

private val OSSL_PARAM_construct_int: MethodHandle = FFI.methodHandle(
    name = "OSSL_PARAM_construct_int",
    FunctionDescriptor.of(
        /* resLayout = */ OSSL_PARAM.layout,
        /* ...argLayouts = */ ValueLayout.ADDRESS, ValueLayout.ADDRESS,
    )
)

fun OSSL_PARAM_construct_int(
    allocator: SegmentAllocator,
    key: MemorySegment,
    buf: MemorySegment,
): MemorySegment {
    return OSSL_PARAM_construct_int.invokeExact(
        allocator,
        key,
        buf,
    ) as MemorySegment
}

private val OSSL_PARAM_get_int: MethodHandle = FFI.methodHandle(
    name = "OSSL_PARAM_get_int",
    FunctionDescriptor.of(
        /* resLayout = */ ValueLayout.JAVA_INT,
        /* ...argLayouts = */ ValueLayout.ADDRESS, ValueLayout.ADDRESS,
    )
)

fun OSSL_PARAM_get_int(
    p: MemorySegment,
    value: MemorySegment,
): Int {
    return OSSL_PARAM_get_int.invokeExact(
        p,
        value,
    ) as Int
}

private val OSSL_PARAM_construct_end: MethodHandle = FFI.methodHandle(
    name = "OSSL_PARAM_construct_end",
    FunctionDescriptor.of(
        /* resLayout = */ OSSL_PARAM.layout
    )
)

fun OSSL_PARAM_construct_end(allocator: SegmentAllocator): MemorySegment {
    return OSSL_PARAM_construct_end.invokeExact(allocator) as MemorySegment
}
