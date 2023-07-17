plugins {
    kotlin("multiplatform") version "1.9.0"
}

kotlin {
    macosArm64 {
        binaries {
            sharedLib {
                baseName = "ntj"
            }
        }
    }
}
