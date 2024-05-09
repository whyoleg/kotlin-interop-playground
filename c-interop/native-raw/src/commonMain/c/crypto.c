#include <openssl/crypto.h>

const void* ffi_OpenSSL_version_call __asm("ffi_OpenSSL_version");
const void* ffi_OpenSSL_version_call = (const void*)&OpenSSL_version;

const void* ffi_OPENSSL_version_major_call __asm("ffi_OPENSSL_version_major");
const void* ffi_OPENSSL_version_major_call = (const void*)&OPENSSL_version_major;
