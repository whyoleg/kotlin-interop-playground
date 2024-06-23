#include <jni.h>
#include <openssl/evp.h>

JNIEXPORT jlong JNICALL Java_dev_whyoleg_kip_c_jvm_jni_evp_1mac_EVP_1MAC_1fetch (JNIEnv* env, jclass jclss,
  jlong p_ctx,
  jlong p_algorithm,
  jlong p_properties
) {
    return (jlong)EVP_MAC_fetch((OSSL_LIB_CTX*)p_ctx, (char*)p_algorithm, (char*)p_properties);
}

JNIEXPORT jlong JNICALL Java_dev_whyoleg_kip_c_jvm_jni_evp_1mac_EVP_1MAC_1CTX_1new (JNIEnv* env, jclass jclss,
  jlong p_ctx
) {
    return (jlong)EVP_MAC_CTX_new((EVP_MAC*)p_ctx);
}

JNIEXPORT jlong JNICALL Java_dev_whyoleg_kip_c_jvm_jni_evp_1mac_EVP_1MAC_1CTX_1get_1mac_1size (JNIEnv* env, jclass jclss,
  jlong p_ctx
) {
    return (jlong)EVP_MAC_CTX_get_mac_size((EVP_MAC_CTX*)p_ctx);
}

JNIEXPORT jint JNICALL Java_dev_whyoleg_kip_c_jvm_jni_evp_1mac_EVP_1MAC_1init (JNIEnv* env, jclass jclss,
  jlong p_ctx,
  jlong p_key,
  jlong p_keylen,
  jlong p_params
) {
    return (jint)EVP_MAC_init((EVP_MAC_CTX*)p_ctx, (unsigned char*)p_key, p_keylen, (OSSL_PARAM*)p_params);
}

JNIEXPORT jint JNICALL Java_dev_whyoleg_kip_c_jvm_jni_evp_1mac_EVP_1MAC_1update (JNIEnv* env, jclass jclss,
  jlong p_ctx,
  jlong p_data,
  jlong p_datalen
) {
    return (jint)EVP_MAC_update((EVP_MAC_CTX*)p_ctx, (unsigned char*)p_data, p_datalen);
}

JNIEXPORT jint JNICALL Java_dev_whyoleg_kip_c_jvm_jni_evp_1mac_EVP_1MAC_1final (JNIEnv* env, jclass jclss,
  jlong p_ctx,
  jlong p_out,
  jlong p_outl,
  jlong p_outsize
) {
    return (jint)EVP_MAC_final((EVP_MAC_CTX*)p_ctx, (unsigned char*)p_out, (unsigned long*)p_outl, p_outsize);
}

JNIEXPORT void JNICALL Java_dev_whyoleg_kip_c_jvm_jni_evp_1mac_EVP_1MAC_1CTX_1free (JNIEnv* env, jclass jclss,
  jlong p_ctx
) {
    EVP_MAC_CTX_free((EVP_MAC_CTX*)p_ctx);
}

JNIEXPORT void JNICALL Java_dev_whyoleg_kip_c_jvm_jni_evp_1mac_EVP_1MAC_1free (JNIEnv* env, jclass jclss,
  jlong p_ctx
) {
    EVP_MAC_free((EVP_MAC*)p_ctx);
}
