@file:Suppress("ClassName", "FunctionName")

package dev.whyoleg.kip.c.jvm.jni

import java.nio.*

// # define OSSL_PARAM_INTEGER 1
const val OSSL_PARAM_INTEGER = 1

// not to clash with `ossl_param`
object OSSL_PARAM_STRUCT {
    val size = 8 + 4 + 4 + 8 + 8 + 8

    val keyOffset = 0
    val data_typeOffset = 8
    val dataOffset = 16
    // ...

    fun key(buffer: ByteBuffer): Long = buffer.getLong(keyOffset)
    fun data_type(buffer: ByteBuffer): Int = buffer.getInt(data_typeOffset)
    fun data(buffer: ByteBuffer): Long = buffer.getLong(dataOffset)
}

object ossl_param {
    init {
        JNI
    }

    @JvmStatic
    external fun OSSL_PARAM_construct_utf8_string(key: Long, buf: Long, bsize: Long, returnPointer: Long)

    @JvmStatic
    external fun OSSL_PARAM_construct_int(key: Long, buf: Long, returnPointer: Long)

    @JvmStatic
    external fun OSSL_PARAM_construct_end(returnPointer: Long)

    @JvmStatic
    external fun OSSL_PARAM_get_int(p: Long, value: Long): Int
}
