package dev.whyoleg.kip.c.wasm.emscripten

@Target(AnnotationTarget.FILE)
expect annotation class JsModule(
    val import: String
)

expect annotation class JsName(
    val name: String
)
