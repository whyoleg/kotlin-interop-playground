package dev.whyoleg.kip.c.wasm.emscripten

import kotlin.test.*

@OptIn(
    ExperimentalStdlibApi::class
)
class WasmEmscriptenTest {

    @Test
    fun test1_simpleFunctionCalls() {
        // returns primitive
        val majorVersion = ffi.OPENSSL_version_major()
        assertEquals(3, majorVersion)

        // accepts a primitive, returns pointer to a String
        val stringVersion = ffi.OpenSSL_version(OPENSSL_VERSION_STRING)
        assertEquals("3.2.0", ffi.UTF8ToString(stringVersion))
    }

    @Test
    fun test2_pointers() {
        // accepts pointers, returns pointer to an opaque struct

        val md = "SHA256".useAsPointer {
            ffi.EVP_MD_fetch(0, it, 0)
        }

        try {
            // accepts pointer, returns primitive
            val digestSize = ffi.EVP_MD_get_size(md)
            assertEquals(32, digestSize)
        } finally {
            // needs cleanup
            ffi.EVP_MD_free(md)
        }
    }

    //
//    @Test
//    fun test3_bytes() {
//        val data = byteArrayOf(1, 2, 3)
//
//        // pin value to not relocate it address
//        data.usePinned { pinned ->
//            // get address of an element at `index`=0
//            val address = pinned.addressOf(0)
//        }
//    }
//
    @Test
    fun test4_digest() {
        val md = "SHA256".useAsPointer {
            ffi.EVP_MD_fetch(0, it, 0)
        }
        val context = ffi.EVP_MD_CTX_new()
        try {
            val data = "Hello World".encodeToByteArray()
            val digest = ByteArray(ffi.EVP_MD_get_size(md))

            check(ffi.EVP_DigestInit(context, md) == 1)
            data.useAsPointer(copyBefore = true, copyAfter = false) {
                check(ffi.EVP_DigestUpdate(context, it, data.size) == 1)
            }
            digest.useAsPointer(copyBefore = false, copyAfter = true) {
                check(ffi.EVP_DigestFinal(context, it, 0) == 1)
            }

            assertEquals("a591a6d40bf420404a011733cfb7b190d62c65bf0bcda32b57b277d9ad9f146e", digest.toHexString())
        } finally {
            ffi.EVP_MD_CTX_free(context)
            ffi.EVP_MD_free(md)
        }
    }

    @Test
    fun test5_structs() {
        val value = 123
        // returns struct, accepts pointers

        // size=40, align=8 for macosArm64
        val structPointer = ffi.malloc(OSSL_PARAM.size)
        val valueInput = ffi.malloc(Int.SIZE_BYTES)
        ffi.HEAPU32[valueInput / 4] = value

        val fieldPointer = "field".toPointer()
        ffi.OSSL_PARAM_construct_int(fieldPointer, valueInput, structPointer)

        val valueOutput = ffi.malloc(Int.SIZE_BYTES)
        ffi.HEAPU32[valueOutput / 4] = 0 // we need to manually free it :)

        assertEquals(0, ffi.HEAPU32[valueOutput / 4])
        // accepts pointers, returns result if operation is success
        check(ffi.OSSL_PARAM_get_int(structPointer, valueOutput) > 0)
        assertEquals(value, ffi.HEAPU32[valueOutput / 4])

        val key = ffi.UTF8ToString(ffi.HEAPU32[(structPointer + OSSL_PARAM.key_offset) / 4])
        val data_type = ffi.HEAPU32[(structPointer + OSSL_PARAM.data_type_offset) / 4]
        val data = ffi.HEAPU32[
            (ffi.HEAPU32[(structPointer + OSSL_PARAM.data_offset) / 4]) / 4
        ]

        assertEquals(value, data)
        assertEquals(OSSL_PARAM_INTEGER, data_type)
        assertEquals("field", key)

        ffi.free(fieldPointer)
        ffi.free(valueOutput)
        ffi.free(valueInput)
        ffi.free(structPointer)
    }
//
//    @Test
//    fun test6_hmac() = memScoped {
//        val dataInput = "Hi There".encodeToByteArray()
//        val key = "0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b".hexToByteArray()
//
//        val mac = EVP_MAC_fetch(0, "HMAC".cstr.ptr.toLong(), 0)
//        val context = EVP_MAC_CTX_new(mac)
//        try {
//            val paramsArray = alloc(
//                size = size_off_OSSL_PARAM().convert<Int>() * 2, // 2 elements array
//                align = align_off_OSSL_PARAM().convert()
//            )
//            OSSL_PARAM_construct_utf8_string(
//                key = "digest".cstr.ptr.toLong(),
//                buf = "SHA256".cstr.ptr.toLong(),
//                bsize = 0.convert(),
//                // put a first element
//                returnPointer = paramsArray.rawPtr.toLong()
//            )
//            OSSL_PARAM_construct_end(
//                // put a second element
//                returnPointer = (paramsArray.rawPtr + size_off_OSSL_PARAM().convert()).toLong()
//            )
//
//            key.asUByteArray().usePinned {
//                check(
//                    EVP_MAC_init(
//                        ctx = context,
//                        key = it.addressOf(0).toLong(),
//                        keylen = key.size.convert(),
//                        params = paramsArray.rawPtr.toLong()
//                    ) > 0
//                )
//            }
//
//            val signature = ByteArray(EVP_MAC_CTX_get_mac_size(context).convert())
//
//            dataInput.asUByteArray().usePinned {
//                check(EVP_MAC_update(context, it.addressOf(0).toLong(), dataInput.size.convert()) > 0)
//            }
//            signature.asUByteArray().usePinned {
//                check(EVP_MAC_final(context, it.addressOf(0).toLong(), 0, signature.size.convert()) > 0)
//            }
//
//            assertEquals("b0344c61d8db38535ca8afceaf0bf12b881dc200c9833da726e9376c2e32cff7", signature.toHexString())
//        } finally {
//            EVP_MAC_CTX_free(context)
//            EVP_MAC_free(mac)
//        }
//    }
}
