@file:Suppress("ClassName", "FunctionName")

package dev.whyoleg.kip.c.jvm.jni

object evp_md {
    init {
        JNI
    }

    @JvmStatic
    external fun EVP_MD_fetch(ctx: Long, algorithm: Long, properties: Long): Long

    @JvmStatic
    external fun EVP_MD_free(md: Long)

    @JvmStatic
    external fun EVP_MD_get_size(md: Long): Int

    @JvmStatic
    external fun EVP_MD_CTX_new(): Long

    @JvmStatic
    external fun EVP_MD_CTX_free(ctx: Long)

    @JvmStatic
    external fun EVP_DigestInit(ctx: Long, type: Long): Int

    @JvmStatic
    external fun EVP_DigestUpdate(ctx: Long, d: Long, cnt: Long): Int

    @JvmStatic
    external fun EVP_DigestFinal(ctx: Long, md: Long, s: Long): Int
}
