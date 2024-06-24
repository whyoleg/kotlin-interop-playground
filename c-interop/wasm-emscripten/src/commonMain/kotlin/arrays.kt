package dev.whyoleg.kip.c.wasm.emscripten

external interface Uint8Array

expect operator fun Uint8Array.get(index: Int): Byte
expect operator fun Uint8Array.set(index: Int, value: Byte)

external interface Uint32Array

expect operator fun Uint32Array.get(index: Int): Int
expect operator fun Uint32Array.set(index: Int, value: Int)
