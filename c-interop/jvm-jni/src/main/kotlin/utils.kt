package dev.whyoleg.kip.c.jvm.jni

import java.nio.*

fun String.toPointer(): Long {
    val bytes = encodeToByteArray()
    val buffer = ByteBuffer.allocateDirect(bytes.size)
    buffer.put(bytes)
    return JNI.getPointerFromByteBuffer(buffer)
}

fun ByteArray.wrapWithDirectBuffer(): ByteBuffer {
    return ByteBuffer.allocateDirect(size).also {
        it.put(this)
    }
}

fun ByteBuffer.readByteArray(): ByteArray {
    val array = ByteArray(capacity())
    get(array)
    return array
}

fun ByteBuffer.toPointer(): Long = JNI.getPointerFromByteBuffer(this)
