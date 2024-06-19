package dev.whyoleg.kip.c.jvm.ffm.jextract

import dev.whyoleg.kip.c.jvm.ffm.jextract.generated.*
import dev.whyoleg.kip.c.jvm.ffm.jextract.generated.evp_h.*
import java.lang.foreign.*
import kotlin.test.*

// IDEA starts lagging here
@OptIn(ExperimentalStdlibApi::class)
class JvmFfmJextractTest {

    @Test
    fun test1_simpleFunctionCalls() {
        // returns primitive
        val majorVersion = OPENSSL_version_major()
        assertEquals(3, majorVersion)

        // accepts a primitive, returns pointer to a String
        val stringVersion = OpenSSL_version(OPENSSL_VERSION_STRING())
        assertEquals("3.2.0", stringVersion.getString(0))
    }

    @Test
    fun test2_pointers() = Arena.ofConfined().use { arena ->
        // accepts pointers, returns pointer to an opaque struct
        val md = EVP_MD_fetch(
            MemorySegment.NULL,
            arena.allocateFrom("SHA256"),
            MemorySegment.NULL
        )

        try {
            // accepts pointer, returns primitive
            // providing `MemorySegment` = providing pointer
            val digestSize = EVP_MD_get_size(md)
            assertEquals(32, digestSize)
        } finally {
            // needs cleanup
            EVP_MD_free(md)
        }
    }

    // TODO: critical
    @Test
    fun test3_bytes() {
        val data = byteArrayOf(1, 2, 3)

        println(MemorySegment.ofArray(data).address())

//        // pin value to not relocate it address
//        data.usePinned { pinned ->
//            // get address of an element at `index`=0
//            val address = pinned.addressOf(0)
//        }
    }


    @Test
    fun test4_digest() = Arena.ofConfined().use { arena ->
        val md = EVP_MD_fetch(
            MemorySegment.NULL,
            arena.allocateFrom("SHA256"),
            MemorySegment.NULL
        )
        val context = EVP_MD_CTX_new()
        try {
            // note `*` - spread operator required
            val data = arena.allocateFrom(C_CHAR, *"Hello World".encodeToByteArray())
            val digest = arena.allocate(EVP_MD_get_size(md).toLong())

            check(EVP_DigestInit(context, md) == 1)
            check(EVP_DigestUpdate(context, data, data.byteSize()) == 1)
            check(EVP_DigestFinal(context, digest, MemorySegment.NULL) == 1)

            assertEquals(
                "a591a6d40bf420404a011733cfb7b190d62c65bf0bcda32b57b277d9ad9f146e",
                digest.toArray(C_CHAR).toHexString()
            )
        } finally {
            EVP_MD_CTX_free(context)
            EVP_MD_free(md)
        }
    }

    @Test
    fun test5_structs() = Arena.ofConfined().use { arena ->
        val value = 123
        // returns struct, accepts pointers

        // size=40, align=8 for macosArm64
        val struct = OSSL_PARAM_construct_int(
            // struct will be scoped to `arena`
            arena,
            // parameters are just MemorySegment - pointers
            arena.allocateFrom("field"),
            arena.allocateFrom(C_INT, value)
        )
        val valueOutput = arena.allocate(C_INT)

        assertEquals(0, valueOutput.get(C_INT, 0))
        // accepts pointers, returns result if operation is success
        check(OSSL_PARAM_get_int(struct, valueOutput) > 0)
        assertEquals(value, valueOutput.get(C_INT, 0))

        // access struct fields
        assertEquals(value, OSSL_PARAM.data(struct).get(C_INT, 0))
        assertEquals(OSSL_PARAM_INTEGER(), OSSL_PARAM.data_type(struct))
        assertEquals("field", OSSL_PARAM.key(struct).getString(0))
    }

    @Test
    fun test6_hmac() = Arena.ofConfined().use { arena ->
        val dataInput = arena.allocateFrom(C_CHAR, *"Hi There".encodeToByteArray())
        val key = arena.allocateFrom(C_CHAR, *"0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b".hexToByteArray())

        val mac = EVP_MAC_fetch(
            MemorySegment.NULL,
            arena.allocateFrom("HMAC"),
            MemorySegment.NULL
        )
        val context = EVP_MAC_CTX_new(mac)
        try {
            val paramsArray = arena.allocate(OSSL_PARAM.layout(), 2)
            // TODO: description
            val alloc = SegmentAllocator.slicingAllocator(paramsArray)
            OSSL_PARAM_construct_utf8_string(
                alloc,
                arena.allocateFrom("digest"),
                arena.allocateFrom("SHA256"),
                0
            )
            OSSL_PARAM_construct_end(alloc)

            // second variant
//            MemorySegment.copy(
//                OSSL_PARAM_construct_utf8_string(
//                    arena,
//                    arena.allocateFrom("digest"),
//                    arena.allocateFrom("SHA256"),
//                    0
//                ),
//                0,
//                paramsArray,
//                0,
//                OSSL_PARAM.sizeof()
//            )
//
//            MemorySegment.copy(
//                OSSL_PARAM_construct_end(arena),
//                0,
//                paramsArray,
//                OSSL_PARAM.sizeof(), // after the first param
//                OSSL_PARAM.sizeof()
//            )

            check(
                EVP_MAC_init(
                    context,
                    key,
                    key.byteSize(),
                    paramsArray
                ) > 0
            )

            val signature = arena.allocate(EVP_MAC_CTX_get_mac_size(context).toInt().toLong())

            check(EVP_MAC_update(context, dataInput, dataInput.byteSize()) > 0)
            check(EVP_MAC_final(context, signature, MemorySegment.NULL, signature.byteSize()) > 0)

            assertEquals(
                "b0344c61d8db38535ca8afceaf0bf12b881dc200c9833da726e9376c2e32cff7",
                signature.toArray(C_CHAR).toHexString()
            )
        } finally {
            EVP_MAC_CTX_free(context)
            EVP_MAC_free(mac)
        }
    }
}
