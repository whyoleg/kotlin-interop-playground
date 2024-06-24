package dev.whyoleg.kip.c.wasm.emscripten

import org.khronos.webgl.*

actual operator fun Uint8Array.get(index: Int): Byte {
    return (this as org.khronos.webgl.Uint8Array)[index]
}

actual operator fun Uint8Array.set(index: Int, value: Byte) {
    (this as org.khronos.webgl.Uint8Array)[index] = value
}

actual operator fun Uint32Array.get(index: Int): Int {
    return (this as org.khronos.webgl.Uint32Array)[index]
}

actual operator fun Uint32Array.set(index: Int, value: Int) {
    (this as org.khronos.webgl.Uint32Array)[index] = value
}
