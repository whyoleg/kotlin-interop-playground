#include <jni.h>
#include <openssl/crypto.h>

JNIEXPORT jlong JNICALL Java_dev_whyoleg_kip_c_jvm_jni_crypto_OpenSSL_1version (JNIEnv* env, jclass jclss,
  jint p_type
) {
    return (jlong)OpenSSL_version(p_type);
}

JNIEXPORT jint JNICALL Java_dev_whyoleg_kip_c_jvm_jni_crypto_OPENSSL_1version_1major (JNIEnv* env, jclass jclss) {
    return (jint)OPENSSL_version_major();
}
