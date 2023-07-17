# native-to-jvm (ntj)

Sample shows how to configure build to make it possible to call K/N function from K/JVM.
Only `macosArm64` is supported for the sake of simplicity.

To run sample app: `./gradlew app:jvmRun`

## Structure

There are 3 modules

* [library/native](library/native) - configures how to build K/N code into the shared library
  called `ntj` (`libntj.dylib`)
* [library/jvm](library/jvm) - configures how to build JNI library (`libntjjni.dylib`) based on K/N built shared
  library and package everything in jar
* [app](app) - runs code from [library/jvm](library/jvm) without any additional configuration
