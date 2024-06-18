package dev.whyoleg.kip.c.native.raw

import kotlinx.cinterop.*
import kotlin.test.*

@OptIn(
    ExperimentalForeignApi::class,
    ExperimentalStdlibApi::class,
    ExperimentalUnsignedTypes::class,
    UnsafeNumber::class // because of size_t
)
class NativeRawTest {

    @Test
    fun test1_simpleFunctionCalls() {
        // returns primitive
        val majorVersion = OPENSSL_version_major()
        assertEquals(3, majorVersion)

        // accepts a primitive, returns pointer to a String
        val stringVersion = OpenSSL_version(OPENSSL_VERSION_STRING)
        assertEquals("3.2.0", stringVersion.toCPointer<ByteVar>()?.toKString())
    }

    @Test
    fun test2_pointers() = memScoped {
        // accepts pointers, returns pointer to an opaque struct
        val md = EVP_MD_fetch(0, "SHA256".cstr.ptr.toLong(), 0)

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
    fun test4_digest() = memScoped {
        val md = EVP_MD_fetch(0, "SHA256".cstr.ptr.toLong(), 0)
        val context = EVP_MD_CTX_new()
        try {
            val data = "Hello World".encodeToByteArray()
            val digest = ByteArray(EVP_MD_get_size(md))

            check(EVP_DigestInit(context, md) == 1)
            data.usePinned {
                check(EVP_DigestUpdate(context, it.addressOf(0).toLong(), data.size.convert()) == 1)
            }
            digest.asUByteArray().usePinned {
                check(EVP_DigestFinal(context, it.addressOf(0).toLong(), 0) == 1)
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

        // size=40, align=8 for macosArm64
        val struct = alloc(
            size = size_off_OSSL_PARAM().convert(),
            align = align_off_OSSL_PARAM().convert()
        )
        val structPointer = struct.rawPtr.toLong()
        val valueInput = alloc(value)
        OSSL_PARAM_construct_int("field".cstr.ptr.toLong(), valueInput.ptr.toLong(), structPointer)
        val valueOutput = alloc<IntVar>()

        assertEquals(0, valueOutput.value)
        // accepts pointers, returns result if operation is success
        check(OSSL_PARAM_get_int(structPointer, valueOutput.ptr.toLong()) > 0)
        assertEquals(value, valueOutput.value)

        // access struct fields
        val data = interpretPointed<CPointerVarOf<CPointer<IntVar>>>(
            struct.rawPtr + offset_off_data_OSSL_PARAM().convert()
        ).pointed?.value

        val data_type = interpretPointed<IntVar>(
            struct.rawPtr + offset_off_data_type_OSSL_PARAM().convert()
        ).value

        val key = interpretPointed<CPointerVarOf<CPointer<ByteVar>>>(
            struct.rawPtr + offset_off_key_OSSL_PARAM().convert()
        ).value
        assertEquals(value, data)
        assertEquals(OSSL_PARAM_INTEGER.convert(), data_type)
        assertEquals("field", key?.toKString())
    }

    @Test
    fun test6_hmac() = memScoped {
        val dataInput = "Hi There".encodeToByteArray()
        val key = "0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b".hexToByteArray()

        val mac = EVP_MAC_fetch(0, "HMAC".cstr.ptr.toLong(), 0)
        val context = EVP_MAC_CTX_new(mac)
        try {
            val paramsArray = alloc(
                size = size_off_OSSL_PARAM().convert<Int>() * 2, // 2 elements array
                align = align_off_OSSL_PARAM().convert()
            )
            OSSL_PARAM_construct_utf8_string(
                key = "digest".cstr.ptr.toLong(),
                buf = "SHA256".cstr.ptr.toLong(),
                bsize = 0.convert(),
                // put a first element
                returnPointer = paramsArray.rawPtr.toLong()
            )
            OSSL_PARAM_construct_end(
                // put a second element
                returnPointer = (paramsArray.rawPtr + size_off_OSSL_PARAM().convert()).toLong()
            )

            key.asUByteArray().usePinned {
                check(
                    EVP_MAC_init(
                        ctx = context,
                        key = it.addressOf(0).toLong(),
                        keylen = key.size.convert(),
                        params = paramsArray.rawPtr.toLong()
                    ) > 0
                )
            }

            val signature = ByteArray(EVP_MAC_CTX_get_mac_size(context).convert())

            dataInput.asUByteArray().usePinned {
                check(EVP_MAC_update(context, it.addressOf(0).toLong(), dataInput.size.convert()) > 0)
            }
            signature.asUByteArray().usePinned {
                check(EVP_MAC_final(context, it.addressOf(0).toLong(), 0, signature.size.convert()) > 0)
            }

            assertEquals("b0344c61d8db38535ca8afceaf0bf12b881dc200c9833da726e9376c2e32cff7", signature.toHexString())
        } finally {
            EVP_MAC_CTX_free(context)
            EVP_MAC_free(mac)
        }
    }
}
