#include <emscripten.h>
#include <openssl/opensslv.h>
#include <openssl/crypto.h>
#include <openssl/err.h>
#include <openssl/evp.h>

EMSCRIPTEN_KEEPALIVE const char* ffi_OpenSSL_version(int p_type) {
    return OpenSSL_version(p_type);
}

EMSCRIPTEN_KEEPALIVE unsigned int ffi_OPENSSL_version_major() {
    return OPENSSL_version_major();
}

EMSCRIPTEN_KEEPALIVE unsigned int ffi_ERR_get_error () {
    return ERR_get_error();
}

EMSCRIPTEN_KEEPALIVE char* ffi_ERR_error_string (
  unsigned int p_e,
  char* p_buf
) {
    return ERR_error_string(p_e, p_buf);
}


//evpmd start

EMSCRIPTEN_KEEPALIVE EVP_MD* ffi_EVP_MD_fetch (
  OSSL_LIB_CTX* p_ctx,
  char* p_algorithm,
  char* p_properties
) {
    return EVP_MD_fetch(p_ctx, p_algorithm, p_properties);
}

EMSCRIPTEN_KEEPALIVE void ffi_EVP_MD_free (
  EVP_MD* p_ctx
) {
    EVP_MD_free(p_ctx);
}

EMSCRIPTEN_KEEPALIVE unsigned int ffi_EVP_MD_get_size (
  EVP_MD* p_ctx
) {
    return EVP_MD_get_size(p_ctx);
}

EMSCRIPTEN_KEEPALIVE EVP_MD_CTX* ffi_EVP_MD_CTX_new () {
    return EVP_MD_CTX_new();
}

EMSCRIPTEN_KEEPALIVE void ffi_EVP_MD_CTX_free (
  EVP_MD_CTX* p_ctx
) {
    EVP_MD_CTX_free(p_ctx);
}

EMSCRIPTEN_KEEPALIVE unsigned int ffi_EVP_DigestInit (
  EVP_MD_CTX* p_ctx,
  EVP_MD* p_type
) {
    return EVP_DigestInit(p_ctx, p_type);
}

EMSCRIPTEN_KEEPALIVE unsigned int ffi_EVP_DigestUpdate (
  EVP_MD_CTX* p_ctx,
  void* p_d,
  unsigned int p_cnt
) {
    return EVP_DigestUpdate(p_ctx, p_d, p_cnt);
}

EMSCRIPTEN_KEEPALIVE unsigned int ffi_EVP_DigestFinal (
  EVP_MD_CTX* p_ctx,
  unsigned char* p_md,
  unsigned int* p_cnt
) {
    return EVP_DigestFinal(p_ctx, p_md, p_cnt);
}

//evpmd end

//osslparam start

EMSCRIPTEN_KEEPALIVE void ffi_OSSL_PARAM_construct_utf8_string (
  char* p_key,
  char* p_buf,
  unsigned int p_bsize,
  OSSL_PARAM* p_returnPointer
) {
    *p_returnPointer = OSSL_PARAM_construct_utf8_string(p_key, p_buf, p_bsize);
}

EMSCRIPTEN_KEEPALIVE void ffi_OSSL_PARAM_construct_int (
  char* p_key,
  int* p_buf,
  OSSL_PARAM* p_returnPointer
) {
    *p_returnPointer = OSSL_PARAM_construct_int(p_key, p_buf);
}

EMSCRIPTEN_KEEPALIVE void ffi_OSSL_PARAM_construct_end (
  OSSL_PARAM* p_returnPointer
) {
    *p_returnPointer = OSSL_PARAM_construct_end();
}

EMSCRIPTEN_KEEPALIVE int ffi_OSSL_PARAM_get_int (
  OSSL_PARAM* p_p,
  int* p_val
) {
    return OSSL_PARAM_get_int(p_p, p_val);
}

//osslparam end

//evpmac start

EMSCRIPTEN_KEEPALIVE EVP_MAC* ffi_EVP_MAC_fetch (
  OSSL_LIB_CTX* p_ctx,
  char* p_algorithm,
  char* p_properties
) {
    return EVP_MAC_fetch(p_ctx, p_algorithm, p_properties);
}

EMSCRIPTEN_KEEPALIVE EVP_MAC_CTX* ffi_EVP_MAC_CTX_new (
  EVP_MAC* p_ctx
) {
    return EVP_MAC_CTX_new(p_ctx);
}

EMSCRIPTEN_KEEPALIVE unsigned int ffi_EVP_MAC_CTX_get_mac_size (
  EVP_MAC_CTX* p_ctx
) {
    return EVP_MAC_CTX_get_mac_size(p_ctx);
}

EMSCRIPTEN_KEEPALIVE int ffi_EVP_MAC_init (
  EVP_MAC_CTX* p_ctx,
  unsigned char* p_key,
  unsigned int p_keylen,
  OSSL_PARAM* p_params
) {
    return EVP_MAC_init(p_ctx, p_key, p_keylen, p_params);
}

EMSCRIPTEN_KEEPALIVE int ffi_EVP_MAC_update (
  EVP_MAC_CTX* p_ctx,
  unsigned char* p_data,
  unsigned int p_datalen
) {
    return EVP_MAC_update(p_ctx, p_data, p_datalen);
}

EMSCRIPTEN_KEEPALIVE int ffi_EVP_MAC_final (
  EVP_MAC_CTX* p_ctx,
  unsigned char* p_out,
  unsigned long* p_outl,
  unsigned int p_outsize
) {
    return EVP_MAC_final(p_ctx, p_out, p_outl, p_outsize);
}

EMSCRIPTEN_KEEPALIVE void ffi_EVP_MAC_CTX_free (
  EVP_MAC_CTX* p_ctx
) {
    EVP_MAC_CTX_free(p_ctx);
}

EMSCRIPTEN_KEEPALIVE void ffi_EVP_MAC_free (
  EVP_MAC* p_ctx
) {
    EVP_MAC_free(p_ctx);
}

//evpmac end
