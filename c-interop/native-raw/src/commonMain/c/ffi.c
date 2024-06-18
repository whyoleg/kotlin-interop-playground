#include <openssl/crypto.h>
#include <openssl/evp.h>
#include <openssl/params.h>

// splitting this file to separate files works bad for K/N compilation :)

const void* ffi_OpenSSL_version_call __asm("ffi_OpenSSL_version");
const void* ffi_OpenSSL_version_call = (const void*)&OpenSSL_version;

const void* ffi_OPENSSL_version_major_call __asm("ffi_OPENSSL_version_major");
const void* ffi_OPENSSL_version_major_call = (const void*)&OPENSSL_version_major;

///

const void* ffi_EVP_MAC_fetch_call __asm("ffi_EVP_MAC_fetch");
const void* ffi_EVP_MAC_fetch_call = (const void*)&EVP_MAC_fetch;

const void* ffi_EVP_MAC_free_call __asm("ffi_EVP_MAC_free");
const void* ffi_EVP_MAC_free_call = (const void*)&EVP_MAC_free;

const void* ffi_EVP_MAC_CTX_free_call __asm("ffi_EVP_MAC_CTX_free");
const void* ffi_EVP_MAC_CTX_free_call = (const void*)&EVP_MAC_CTX_free;

const void* ffi_EVP_MAC_final_call __asm("ffi_EVP_MAC_final");
const void* ffi_EVP_MAC_final_call = (const void*)&EVP_MAC_final;

const void* ffi_EVP_MAC_update_call __asm("ffi_EVP_MAC_update");
const void* ffi_EVP_MAC_update_call = (const void*)&EVP_MAC_update;

const void* ffi_EVP_MAC_init_call __asm("ffi_EVP_MAC_init");
const void* ffi_EVP_MAC_init_call = (const void*)&EVP_MAC_init;

const void* ffi_EVP_MAC_CTX_get_mac_size_call __asm("ffi_EVP_MAC_CTX_get_mac_size");
const void* ffi_EVP_MAC_CTX_get_mac_size_call = (const void*)&EVP_MAC_CTX_get_mac_size;

const void* ffi_EVP_MAC_CTX_new_call __asm("ffi_EVP_MAC_CTX_new");
const void* ffi_EVP_MAC_CTX_new_call = (const void*)&EVP_MAC_CTX_new;

///

const void* ffi_EVP_MD_get_size_call __asm("ffi_EVP_MD_get_size");
const void* ffi_EVP_MD_get_size_call = (const void*)&EVP_MD_get_size;

const void* ffi_EVP_MD_CTX_new_call __asm("ffi_EVP_MD_CTX_new");
const void* ffi_EVP_MD_CTX_new_call = (const void*)&EVP_MD_CTX_new;

const void* ffi_EVP_MD_fetch_call __asm("ffi_EVP_MD_fetch");
const void* ffi_EVP_MD_fetch_call = (const void*)&EVP_MD_fetch;

const void* ffi_EVP_MD_CTX_free_call __asm("ffi_EVP_MD_CTX_free");
const void* ffi_EVP_MD_CTX_free_call = (const void*)&EVP_MD_CTX_free;

const void* ffi_EVP_MD_free_call __asm("ffi_EVP_MD_free");
const void* ffi_EVP_MD_free_call = (const void*)&EVP_MD_free;

const void* ffi_EVP_DigestFinal_call __asm("ffi_EVP_DigestFinal");
const void* ffi_EVP_DigestFinal_call = (const void*)&EVP_DigestFinal;

const void* ffi_EVP_DigestUpdate_call __asm("ffi_EVP_DigestUpdate");
const void* ffi_EVP_DigestUpdate_call = (const void*)&EVP_DigestUpdate;

const void* ffi_EVP_DigestInit_call __asm("ffi_EVP_DigestInit");
const void* ffi_EVP_DigestInit_call = (const void*)&EVP_DigestInit;

///

__attribute__((always_inline))
void interop_OSSL_PARAM_construct_utf8_string (
  char* p_key,
  char* p_buf,
  size_t p_bsize,
  OSSL_PARAM* p_returnPointer
) {
    *p_returnPointer = OSSL_PARAM_construct_utf8_string(p_key, p_buf, p_bsize);
}

const void* ffi_OSSL_PARAM_construct_utf8_string_call __asm("ffi_OSSL_PARAM_construct_utf8_string");
const void* ffi_OSSL_PARAM_construct_utf8_string_call = (const void*)&interop_OSSL_PARAM_construct_utf8_string;

__attribute__((always_inline))
void interop_OSSL_PARAM_construct_int (
  char* p_key,
  int* p_buf,
  OSSL_PARAM* p_returnPointer
) {
    *p_returnPointer = OSSL_PARAM_construct_int(p_key, p_buf);
}

OSSL_PARAM OSSL_PARAM_construct_int(const char *key, int *buf);

const void* ffi_OSSL_PARAM_construct_int_call __asm("ffi_OSSL_PARAM_construct_int");
const void* ffi_OSSL_PARAM_construct_int_call = (const void*)&interop_OSSL_PARAM_construct_int;

const void* ffi_OSSL_PARAM_get_int_call __asm("ffi_OSSL_PARAM_get_int");
const void* ffi_OSSL_PARAM_get_int_call = (const void*)&OSSL_PARAM_get_int;

__attribute__((always_inline))
void interop_OSSL_PARAM_construct_end (
  OSSL_PARAM* p_returnPointer
) {
    *p_returnPointer = OSSL_PARAM_construct_end();
}

const void* ffi_OSSL_PARAM_construct_end_call __asm("ffi_OSSL_PARAM_construct_end");
const void* ffi_OSSL_PARAM_construct_end_call = (const void*)&interop_OSSL_PARAM_construct_end;

__attribute__((always_inline))
size_t interop_size_off_OSSL_PARAM () {
    return sizeof(OSSL_PARAM);
}

const void* ffi_size_off_OSSL_PARAM __asm("ffi_size_off_OSSL_PARAM");
const void* ffi_size_off_OSSL_PARAM = (const void*)&interop_size_off_OSSL_PARAM;

__attribute__((always_inline))
size_t interop_align_off_OSSL_PARAM () {
    return _Alignof(OSSL_PARAM);
}

const void* ffi_align_off_OSSL_PARAM __asm("ffi_align_off_OSSL_PARAM");
const void* ffi_align_off_OSSL_PARAM = (const void*)&interop_align_off_OSSL_PARAM;

__attribute__((always_inline))
size_t interop_offset_off_data_OSSL_PARAM () {
    return offsetof(OSSL_PARAM, data);
}

const void* ffi_offset_off_data_OSSL_PARAM __asm("ffi_offset_off_data_OSSL_PARAM");
const void* ffi_offset_off_data_OSSL_PARAM = (const void*)&interop_offset_off_data_OSSL_PARAM;

__attribute__((always_inline))
size_t interop_offset_off_data_type_OSSL_PARAM () {
    return offsetof(OSSL_PARAM, data_type);
}

const void* ffi_offset_off_data_type_OSSL_PARAM __asm("ffi_offset_off_data_type_OSSL_PARAM");
const void* ffi_offset_off_data_type_OSSL_PARAM = (const void*)&interop_offset_off_data_type_OSSL_PARAM;

__attribute__((always_inline))
size_t interop_offset_off_key_OSSL_PARAM () {
    return offsetof(OSSL_PARAM, key);
}

const void* ffi_offset_off_key_OSSL_PARAM __asm("ffi_offset_off_key_OSSL_PARAM");
const void* ffi_offset_off_key_OSSL_PARAM = (const void*)&interop_offset_off_key_OSSL_PARAM;
