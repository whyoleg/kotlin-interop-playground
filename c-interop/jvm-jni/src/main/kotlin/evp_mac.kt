@file:Suppress("ClassName", "FunctionName")

package dev.whyoleg.kip.c.jvm.jni

object evp_mac {
    init {
        JNI
    }

    @JvmStatic
    external fun EVP_MAC_fetch(libctx: Long, algorithm: Long, properties: Long): Long

    @JvmStatic
    external fun EVP_MAC_free(ctx: Long)

    @JvmStatic
    external fun EVP_MAC_init(ctx: Long, key: Long, keylen: Long, params: Long): Int

    @JvmStatic
    external fun EVP_MAC_update(ctx: Long, data: Long, datalen: Long): Int

    @JvmStatic
    external fun EVP_MAC_final(ctx: Long, out: Long, outl: Long, outsize: Long): Int

    @JvmStatic
    external fun EVP_MAC_CTX_new(mac: Long): Long

    @JvmStatic
    external fun EVP_MAC_CTX_free(ctx: Long)

    @JvmStatic
    external fun EVP_MAC_CTX_get_mac_size(ctx: Long): Long
}
