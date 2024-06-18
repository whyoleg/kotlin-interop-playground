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
    fun test1_simpleFunctionCalls() {
        // returns primitive
        val majorVersion = OPENSSL_version_major()
        assertEquals(3u, majorVersion)

        // accepts a primitive, returns pointer to a String
        val stringVersion = OpenSSL_version(OPENSSL_VERSION_STRING)
        assertEquals("3.2.0", stringVersion?.toKString())
    }

    @Test
    fun test2_pointers() {
        // accepts pointers, returns pointer to an opaque struct
        val md = EVP_MD_fetch(null, "SHA256", null)
        try {
            // accepts pointer, returns primitive
            val digestSize = EVP_MD_get_size(md)
            assertEquals(32, digestSize)
        } finally {
            // needs cleanup
            EVP_MD_free(md)
        }
    }

    @Test
    fun test3_bytes() {
        val data = byteArrayOf(1, 2, 3)

        // pin value to not relocate it address
        data.usePinned { pinned ->
            // get address of an element at `index`=0
            val address = pinned.addressOf(0)
        }
    }

    @Test
    fun test4_digest() {
        val md = EVP_MD_fetch(null, "SHA256", null)
        val context = EVP_MD_CTX_new()
        try {
            val data = "Hello World".encodeToByteArray()
            val digest = ByteArray(EVP_MD_get_size(md))

            check(EVP_DigestInit(context, md) == 1)
            data.usePinned {
                check(EVP_DigestUpdate(context, it.addressOf(0), data.size.convert()) == 1)
            }
            digest.asUByteArray().usePinned {
                check(EVP_DigestFinal(context, it.addressOf(0), null) == 1)
            }

            assertEquals("a591a6d40bf420404a011733cfb7b190d62c65bf0bcda32b57b277d9ad9f146e", digest.toHexString())
        } finally {
            EVP_MD_CTX_free(context)
            EVP_MD_free(md)
        }
    }

    @Test
    fun test5_structs() = memScoped {
        val value = 123
        // returns struct, accepts pointers
        val param = OSSL_PARAM_construct_int("field".cstr.ptr, alloc(value).ptr)
        val valueOutput = alloc<IntVar>()

        assertEquals(valueOutput.value, 0)
        // accepts pointers, returns result if operation is success
        check(OSSL_PARAM_get_int(param.ptr, valueOutput.ptr) > 0)
        assertEquals(valueOutput.value, value)

        // access struct fields
        param.useContents {
            assertEquals(OSSL_PARAM_INTEGER.convert(), data_type)
            assertEquals("field", key?.toKString())
        }
    }

    @Test
    fun test6_hmac() = memScoped {
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

            key.asUByteArray().usePinned {
                check(
                    EVP_MAC_init(
                        ctx = context,
                        key = it.addressOf(0),
                        keylen = key.size.convert(),
                        params = paramsArray
                    ) > 0
                )
            }

            val signature = ByteArray(EVP_MAC_CTX_get_mac_size(context).convert())

            dataInput.asUByteArray().usePinned {
                check(EVP_MAC_update(context, it.addressOf(0), dataInput.size.convert()) > 0)
            }
            signature.asUByteArray().usePinned {
                check(EVP_MAC_final(context, it.addressOf(0), null, signature.size.convert()) > 0)
            }

            assertEquals("b0344c61d8db38535ca8afceaf0bf12b881dc200c9833da726e9376c2e32cff7", signature.toHexString())
        } finally {
            EVP_MAC_CTX_free(context)
            EVP_MAC_free(mac)
        }
    }
}
