#include <jni.h>
#include <openssl/params.h>

JNIEXPORT void JNICALL Java_dev_whyoleg_kip_c_jvm_jni_ossl_1param_OSSL_1PARAM_1construct_1utf8_1string (JNIEnv* env, jclass jclss,
  jlong p_key,
  jlong p_buf,
  jlong p_bsize,
  jlong p_returnPointer
) {
    *(OSSL_PARAM*)p_returnPointer = OSSL_PARAM_construct_utf8_string((char*)p_key, (char*)p_buf, p_bsize);
}

JNIEXPORT void JNICALL Java_dev_whyoleg_kip_c_jvm_jni_ossl_1param_OSSL_1PARAM_1construct_1int (JNIEnv* env, jclass jclss,
  jlong p_key,
  jlong p_buf,
  jlong p_returnPointer
) {
    *(OSSL_PARAM*)p_returnPointer = OSSL_PARAM_construct_int((char*)p_key, (int*)p_buf);
}

JNIEXPORT void JNICALL Java_dev_whyoleg_kip_c_jvm_jni_ossl_1param_OSSL_1PARAM_1construct_1end (JNIEnv* env, jclass jclss,
  jlong p_returnPointer
) {
    *(OSSL_PARAM*)p_returnPointer = OSSL_PARAM_construct_end();
}

JNIEXPORT jint JNICALL Java_dev_whyoleg_kip_c_jvm_jni_ossl_1param_OSSL_1PARAM_1get_1int (JNIEnv* env, jclass jclss,
  jlong p_p,
  jlong p_val
) {
    return (jint)OSSL_PARAM_get_int((OSSL_PARAM*)p_p, (int*)p_val);
}
