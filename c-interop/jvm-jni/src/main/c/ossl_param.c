#include <jni.h>
#include <openssl/params.h>

JNIEXPORT void JNICALL Java_dev_whyoleg_kip_c_jvm_jni_ossl_param_OSSL_1PARAM_1construct_1utf8_1string (JNIEnv* env, jclass jclss,
  jlong p_key,
  jlong p_buf,
  jlong p_bsize,
  jlong p_returnPointer
) {
    *(OSSL_PARAM*)p_returnPointer = OSSL_PARAM_construct_utf8_string((char*)p_key, (char*)p_buf, p_bsize);
}

JNIEXPORT void JNICALL Java_dev_whyoleg_kip_c_jvm_jni_ossl_param_OSSL_1PARAM_1construct_1end (JNIEnv* env, jclass jclss,
  jlong p_returnPointer
) {
    *(OSSL_PARAM*)p_returnPointer = OSSL_PARAM_construct_end();
}
