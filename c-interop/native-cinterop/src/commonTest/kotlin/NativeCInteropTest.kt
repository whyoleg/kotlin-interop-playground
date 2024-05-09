package dev.whyoleg.kip.c.native.cinterop

import dev.whyoleg.kip.c.native.cinterop.libcrypto.*
import kotlinx.cinterop.*
import kotlin.test.*

@OptIn(
    ExperimentalForeignApi::class,
    ExperimentalStdlibApi::class,
    ExperimentalUnsignedTypes::class,
    UnsafeNumber::class // because of size_t
)
class NativeCInteropTest {

    @Test
    fun testVersion() {
        assertEquals(3u, OPENSSL_version_major())
        assertEquals("3.2.0", OpenSSL_version(OPENSSL_VERSION_STRING)?.toKString())
    }

    @Test
    fun testSha256() {
        val md = EVP_MD_fetch(null, "SHA256", null)
        val context = EVP_MD_CTX_new()
        try {
            val data = "Hello World".encodeToByteArray()
            val digest = ByteArray(32)

            check(EVP_DigestInit(context, md) == 1)
            check(EVP_DigestUpdate(context, data.refTo(0), data.size.convert()) == 1)
            check(EVP_DigestFinal(context, digest.asUByteArray().refTo(0), null) == 1)

            assertEquals("a591a6d40bf420404a011733cfb7b190d62c65bf0bcda32b57b277d9ad9f146e", digest.toHexString())
        } finally {
            EVP_MD_CTX_free(context)
            EVP_MD_free(md)
        }
    }

    @Test
    fun testHmac() = memScoped {
        val dataInput = "Hi There".encodeToByteArray()
        val key = "0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b".hexToByteArray()

        val mac = EVP_MAC_fetch(null, "HMAC", null)
        val context = EVP_MAC_CTX_new(mac)
        try {
            val params = listOf(
                OSSL_PARAM_construct_utf8_string("digest".cstr.ptr, "SHA256".cstr.ptr, 0U),
                OSSL_PARAM_construct_end()
            )
            val paramsArray = allocArray<OSSL_PARAM>(params.size)
            params.forEachIndexed { index, value ->
                value.place(paramsArray[index].ptr)
            }

            check(
                EVP_MAC_init(
                    ctx = context,
                    key = key.asUByteArray().refTo(0),
                    keylen = key.size.convert(),
                    params = paramsArray
                ) > 0
            )
            val signature = ByteArray(EVP_MAC_CTX_get_mac_size(context).convert())

            check(
                EVP_MAC_update(context, dataInput.asUByteArray().refTo(0), dataInput.size.convert()) > 0
            )
            check(
                EVP_MAC_final(context, signature.asUByteArray().refTo(0), null, signature.size.convert()) > 0
            )

            assertEquals("b0344c61d8db38535ca8afceaf0bf12b881dc200c9833da726e9376c2e32cff7", signature.toHexString())
        } finally {
            EVP_MAC_CTX_free(context)
            EVP_MAC_free(mac)
        }
    }
}
