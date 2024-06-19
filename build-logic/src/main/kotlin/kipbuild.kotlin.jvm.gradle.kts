plugins {
    kotlin("jvm")
}

kotlin {
    compilerOptions {
        allWarningsAsErrors.set(true)
        progressiveMode.set(true)
        freeCompilerArgs.add("-Xrender-internal-diagnostic-names")
    }
}
