@file:Suppress("ClassName", "FunctionName")
@file:JsModule("./instantiate.mjs")

package dev.whyoleg.kip.c.wasm.emscripten

const val OPENSSL_VERSION_STRING: Int = 6 //magic :)

@JsName("default")
external object crypto {

    @JsName("_ffi_OpenSSL_version")
    fun OpenSSL_version(type: Int): Int

    @JsName("_ffi_OPENSSL_version_major")
    fun OPENSSL_version_major(): Int
}
