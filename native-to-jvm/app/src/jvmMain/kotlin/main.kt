package dev.whyoleg.interop.playground.ntj.app

import dev.whyoleg.interop.playground.ntj.jvm.nativeCall
import kotlin.random.Random

fun main() {
    val value = Random.nextInt(100)
    val result = nativeCall(value)

    println("Native call result: $value * 42 = $result = ${value * 42}")
}
