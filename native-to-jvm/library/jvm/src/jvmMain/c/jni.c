#include <jni.h>
#include <libntj_api.h>

JNIEXPORT jint JNICALL Java_dev_whyoleg_interop_playground_ntj_jvm_JNI_nativeCall (JNIEnv* env, jclass jclss,
  jint argument
) {
  return libntj_symbols()->kotlin.root.dev.whyoleg.interop.playground.ntj.native.nativeCall(argument);
}
