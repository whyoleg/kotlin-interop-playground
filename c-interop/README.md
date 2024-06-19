# C Interop

All examples uses OpenSSL 3.x `libcrypto` library

Examples:

1. simple function calls
    1. OPENSSL_version_major: no arguments, returns primitive
    2. OpenSSL_version: accepts primitive argument, returns pointer + String conversion
2. pointers
    1. EVP_MD_fetch: accepts pointer arguments and returns pointer
    2. EVP_MD_free: cleanup structures, accept pointer
    3. EVP_MD_get_size: accepts pointer, returns primitive
3. bytes: working with bytes, getting address of pinned values (TODO: better description)
4. SHA256 sample
5. structs
    1. TODO: add create plain struct
    2. return struct: OSSL_PARAM_construct_int
    3. accept struct: OSSL_PARAM_get_int
    4. struct access: OSSL_PARAM
6. HMAC sample

Targets:

* K/Native
    * `native-cinterop`: cinterop generated bindings and runtime
    * `native-raw`: uses kotlinx.cinterop runtime, but doesn't use cinterop tool, so manual bindings
* K/Jvm
    * `jvm-jni`
    * `jvm-jna`
    * `jvm-jnr`
    * `jvm-ffm`
    * `jvm-ffm-jextract`
* K/Wasm(Js/Wasi), K/Js
    * `wasm-emscripten`: uses emscripten to generate `wasm` binary with bindings