package dev.whyoleg.kip.c.jvm.jni

import dev.whyoleg.kip.c.jvm.jni.evp_mac.EVP_MAC_CTX_free
import dev.whyoleg.kip.c.jvm.jni.evp_mac.EVP_MAC_CTX_get_mac_size
import dev.whyoleg.kip.c.jvm.jni.evp_mac.EVP_MAC_CTX_new
import dev.whyoleg.kip.c.jvm.jni.evp_mac.EVP_MAC_fetch
import dev.whyoleg.kip.c.jvm.jni.evp_mac.EVP_MAC_final
import dev.whyoleg.kip.c.jvm.jni.evp_mac.EVP_MAC_free
import dev.whyoleg.kip.c.jvm.jni.evp_mac.EVP_MAC_init
import dev.whyoleg.kip.c.jvm.jni.evp_mac.EVP_MAC_update
import dev.whyoleg.kip.c.jvm.jni.evp_md.EVP_DigestFinal
import dev.whyoleg.kip.c.jvm.jni.evp_md.EVP_DigestInit
import dev.whyoleg.kip.c.jvm.jni.evp_md.EVP_DigestUpdate
import dev.whyoleg.kip.c.jvm.jni.evp_md.EVP_MD_CTX_free
import dev.whyoleg.kip.c.jvm.jni.evp_md.EVP_MD_CTX_new
import dev.whyoleg.kip.c.jvm.jni.evp_md.EVP_MD_fetch
import dev.whyoleg.kip.c.jvm.jni.evp_md.EVP_MD_free
import dev.whyoleg.kip.c.jvm.jni.evp_md.EVP_MD_get_size
import dev.whyoleg.kip.c.jvm.jni.ossl_param.OSSL_PARAM_construct_end
import dev.whyoleg.kip.c.jvm.jni.ossl_param.OSSL_PARAM_construct_int
import dev.whyoleg.kip.c.jvm.jni.ossl_param.OSSL_PARAM_construct_utf8_string
import dev.whyoleg.kip.c.jvm.jni.ossl_param.OSSL_PARAM_get_int
import java.nio.*
import kotlin.test.*

@OptIn(
    ExperimentalStdlibApi::class,
)
class JvmJniTest {

    @Test
    fun test1_simpleFunctionCalls() {
        // returns primitive
        val majorVersion = crypto.OPENSSL_version_major()
        assertEquals(3, majorVersion)

        // accepts a primitive, returns pointer to a String
        val stringVersion = crypto.OpenSSL_version(crypto.OPENSSL_VERSION_STRING)
        assertEquals("3.2.0", JNI.getStringFromPointer(stringVersion))
    }

    @Test
    fun test2_pointers() {
        // accepts pointers, returns pointer to an opaque struct
        val md = EVP_MD_fetch(0, "SHA256".toPointer(), 0)

        try {
            // accepts pointer, returns primitive
            val digestSize = EVP_MD_get_size(md)
            assertEquals(32, digestSize)
        } finally {
            // needs cleanup
            evp_md.EVP_MD_free(md)
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
        val md = EVP_MD_fetch(0, "SHA256".toPointer(), 0)
        val context = EVP_MD_CTX_new()
        try {
            val data = "Hello World".encodeToByteArray().wrapWithDirectBuffer()
            val digest = ByteBuffer.allocateDirect(EVP_MD_get_size(md))

            check(EVP_DigestInit(context, md) == 1)

            check(EVP_DigestUpdate(context, data.toPointer(), data.capacity().toLong()) == 1)

            check(EVP_DigestFinal(context, digest.toPointer(), 0) == 1)

            assertEquals(
                "a591a6d40bf420404a011733cfb7b190d62c65bf0bcda32b57b277d9ad9f146e",
                digest.readByteArray().toHexString()
            )
        } finally {
            EVP_MD_CTX_free(context)
            EVP_MD_free(md)
        }
    }

    @Test
    fun test5_structs() {
        val value = 123
        // returns struct, accepts pointers

        // size=40, align=8 for macosArm64
        val struct = ByteBuffer.allocateDirect(OSSL_PARAM_STRUCT.size).also {
            // accessing struct values should be in LITTLE_ENDIAN
            it.order(ByteOrder.LITTLE_ENDIAN)
        }
        val structPointer = struct.toPointer()
        val valueInput = ByteBuffer.allocateDirect(Int.SIZE_BYTES)
        valueInput.putInt(value)
        OSSL_PARAM_construct_int(
            "field".toPointer(),
            valueInput.toPointer(),
            structPointer
        )
        val valueOutput = ByteBuffer.allocateDirect(Int.SIZE_BYTES)

        assertEquals(0, valueOutput.getInt(0))
        // accepts pointers, returns result if operation is success
        check(OSSL_PARAM_get_int(structPointer, valueOutput.toPointer()) > 0)
        assertEquals(value, valueOutput.getInt(0))

        // access struct fields
        val data = JNI.getByteBufferFromPointer(
            OSSL_PARAM_STRUCT.data(struct),
            Int.SIZE_BYTES
        )
            ?.getInt(0)
        val data_type = OSSL_PARAM_STRUCT.data_type(struct)

        val key = JNI.getStringFromPointer(
            OSSL_PARAM_STRUCT.key(struct)
        )
        assertEquals(value, data)
        assertEquals(OSSL_PARAM_INTEGER, data_type)
        assertEquals("field", key)
    }

    @Test
    fun test6_hmac() {
        val dataInput = "Hi There".encodeToByteArray().wrapWithDirectBuffer()
        val key = "0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b".hexToByteArray().wrapWithDirectBuffer()

        val mac = EVP_MAC_fetch(0, "HMAC".toPointer(), 0)
        val context = EVP_MAC_CTX_new(mac)
        try {
            val paramsArray = ByteBuffer.allocateDirect(
                OSSL_PARAM_STRUCT.size * 2 // 2 elements array
            ).toPointer()
            OSSL_PARAM_construct_utf8_string(
                key = "digest".toPointer(),
                buf = "SHA256".toPointer(),
                bsize = 0,
                // put a first element
                returnPointer = paramsArray
            )
            OSSL_PARAM_construct_end(
                // put a second element
                returnPointer = paramsArray + OSSL_PARAM_STRUCT.size
            )

            check(
                EVP_MAC_init(
                    ctx = context,
                    key = key.toPointer(),
                    keylen = key.capacity().toLong(),
                    params = paramsArray
                ) > 0
            )

            val signature = ByteBuffer.allocateDirect(EVP_MAC_CTX_get_mac_size(context).toInt())

            check(EVP_MAC_update(context, dataInput.toPointer(), dataInput.capacity().toLong()) > 0)
            check(EVP_MAC_final(context, signature.toPointer(), 0, signature.capacity().toLong()) > 0)

            assertEquals(
                "b0344c61d8db38535ca8afceaf0bf12b881dc200c9833da726e9376c2e32cff7",
                signature.readByteArray().toHexString()
            )
        } finally {
            EVP_MAC_CTX_free(context)
            EVP_MAC_free(mac)
        }
    }
}
