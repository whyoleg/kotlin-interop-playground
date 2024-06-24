import kipbuild.openssl.*
import kipbuild.wasm.*
import org.jetbrains.kotlin.gradle.*
import org.jetbrains.kotlin.gradle.targets.js.dsl.*
import org.jetbrains.kotlin.gradle.targets.js.ir.*

plugins {
    id("kipbuild.kotlin.multiplatform")
    id("kipbuild.openssl")
}

@OptIn(
    ExperimentalWasmDsl::class,
    ExperimentalKotlinGradlePluginApi::class
)
kotlin {
    // to support browser, need to set `ENVIRONMENT=web` for emcc and configure proxy for `wasm` file
    wasmJs {
        nodejs()
        // browser()
    }
    js {
        useEsModules()
        nodejs()
        // browser()
    }

    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

    sourceSets.commonTest.dependencies {
        implementation(kotlin("test"))
    }
}

val linkWasmCrypto = tasks.register<LinkWasm>("linkWasmCrypto") {
    // TODO: should be changed
    emcc.set("/opt/homebrew/bin/emcc")
    inputFiles.from("src/commonMain/c")
    includeDirs.from(openssl.includeDirectory(OpensslTarget.wasm))
    linkPaths.add(openssl.libDirectory(OpensslTarget.wasm).map { it.asFile.absolutePath })
    linkLibraries.add("crypto")
    outputLibraryName.set("ffi-crypto")
}

tasks.withType<DefaultIncrementalSyncTask>().configureEach {
    from.from(linkWasmCrypto)
    // contains initialization code for wasm module
    from.from("src/commonMain/js")
}
