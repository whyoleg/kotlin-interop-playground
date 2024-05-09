#include <openssl/err.h>

const void* ffi_ERR_get_error_call __asm("ffi_ERR_get_error");
const void* ffi_ERR_get_error_call = (const void*)&ERR_get_error;

const void* ffi_ERR_error_string_call __asm("ffi_ERR_error_string");
const void* ffi_ERR_error_string_call = (const void*)&ERR_error_string;
