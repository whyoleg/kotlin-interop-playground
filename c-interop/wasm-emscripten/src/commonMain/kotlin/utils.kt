package dev.whyoleg.kip.c.wasm.emscripten

fun String.toPointer(): Int {
    val bytes = encodeToByteArray()
    return ffi.malloc(bytes.size).also {
        bytes.copyToPointer(it)
    }
}

fun <T> String.useAsPointer(block: (pointer: Int) -> T): T {
    return encodeToByteArray().useAsPointer(copyBefore = true, copyAfter = false, block = block)
}

fun <T> ByteArray.useAsPointer(
    copyAfter: Boolean,
    copyBefore: Boolean,
    block: (pointer: Int) -> T
): T {
    val pointer = ffi.malloc(size)
    try {
        if (copyBefore) copyToPointer(pointer)
        val result = block(pointer)
        if (copyAfter) copyFromPointer(pointer)
        return result
    } finally {
        ffi.free(pointer)
    }
}

fun ByteArray.copyToPointer(pointer: Int) {
    repeat(size) { index ->
        ffi.HEAPU8[pointer + index] = this[index]
    }
}

fun ByteArray.copyFromPointer(pointer: Int) {
    repeat(size) { index ->
        this[index] = ffi.HEAPU8[pointer + index]
    }
}