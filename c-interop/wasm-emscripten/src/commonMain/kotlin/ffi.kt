@file:Suppress("ClassName", "FunctionName")
@file:JsModule("./instantiate.mjs")

package dev.whyoleg.kip.c.wasm.emscripten

const val OPENSSL_VERSION_STRING: Int = 6 //magic :)

@JsName("default")
external object ffi {

    // from emscripten

    val HEAPU8: Uint8Array
    val HEAPU32: Uint32Array

    fun UTF8ToString(ptr: Int): String

    @JsName("_malloc")
    fun malloc(size: Int): Int

    @JsName("_free")
    fun free(ptr: Int)

    // crypto.h

    @JsName("_ffi_OpenSSL_version")
    fun OpenSSL_version(type: Int): Int

    @JsName("_ffi_OPENSSL_version_major")
    fun OPENSSL_version_major(): Int

    // evp_md.h

    @JsName("_ffi_EVP_MD_fetch")
    fun EVP_MD_fetch(ctx: Int, algorithm: Int, properties: Int): Int

    @JsName("_ffi_EVP_MD_free")
    fun EVP_MD_free(md: Int)

    @JsName("_ffi_EVP_MD_get_size")
    fun EVP_MD_get_size(md: Int): Int

    @JsName("_ffi_EVP_MD_CTX_new")
    fun EVP_MD_CTX_new(): Int

    @JsName("_ffi_EVP_MD_CTX_free")
    fun EVP_MD_CTX_free(ctx: Int)

    @JsName("_ffi_EVP_DigestInit")
    fun EVP_DigestInit(ctx: Int, type: Int): Int

    @JsName("_ffi_EVP_DigestUpdate")
    fun EVP_DigestUpdate(ctx: Int, d: Int, cnt: Int): Int

    @JsName("_ffi_EVP_DigestFinal")
    fun EVP_DigestFinal(ctx: Int, md: Int, s: Int): Int

    // evp_mac.h

    @JsName("_ffi_EVP_MAC_fetch")
    fun EVP_MAC_fetch(libctx: Int, algorithm: Int, properties: Int): Int

    @JsName("_ffi_EVP_MAC_free")
    fun EVP_MAC_free(ctx: Int)

    @JsName("_ffi_EVP_MAC_init")
    fun EVP_MAC_init(ctx: Int, key: Int, keylen: Int, params: Int): Int

    @JsName("_ffi_EVP_MAC_update")
    fun EVP_MAC_update(ctx: Int, data: Int, datalen: Int): Int

    @JsName("_ffi_EVP_MAC_final")
    fun EVP_MAC_final(ctx: Int, out: Int, outl: Int, outsize: Int): Int

    @JsName("_ffi_EVP_MAC_CTX_new")
    fun EVP_MAC_CTX_new(mac: Int): Int

    @JsName("_ffi_EVP_MAC_CTX_free")
    fun EVP_MAC_CTX_free(ctx: Int)

    @JsName("_ffi_EVP_MAC_CTX_get_mac_size")
    fun EVP_MAC_CTX_get_mac_size(ctx: Int): Int

    // param.h

    @JsName("_ffi_OSSL_PARAM_construct_utf8_string")
    fun OSSL_PARAM_construct_utf8_string(key: Int, buf: Int, bsize: Int, returnPointer: Int)

    @JsName("_ffi_OSSL_PARAM_construct_int")
    fun OSSL_PARAM_construct_int(key: Int, buf: Int, returnPointer: Int)

    @JsName("_ffi_OSSL_PARAM_construct_end")
    fun OSSL_PARAM_construct_end(returnPointer: Int)

    @JsName("_ffi_OSSL_PARAM_get_int")
    fun OSSL_PARAM_get_int(p: Int, value: Int): Int
}
