package kipbuild.openssl

import org.jetbrains.kotlin.konan.target.*

data class OpensslTarget(val value: String) {
    override fun toString(): String = value

    companion object {
        val ios_device_arm64 = OpensslTarget("ios-device-arm64")
        val ios_simulator_arm64 = OpensslTarget("ios-simulator-arm64")
        val ios_simulator_x64 = OpensslTarget("ios-simulator-x64")

        val tvos_device_arm64 = OpensslTarget("tvos-device-arm64")
        val tvos_simulator_x64 = OpensslTarget("tvos-simulator-x64")
        val tvos_simulator_arm64 = OpensslTarget("tvos-simulator-arm64")

        val watchos_device_arm32 = OpensslTarget("watchos-device-arm32")
        val watchos_device_arm64_32 = OpensslTarget("watchos-device-arm64_32")
        val watchos_device_arm64 = OpensslTarget("watchos-device-arm64")
        val watchos_simulator_x64 = OpensslTarget("watchos-simulator-x64")
        val watchos_simulator_arm64 = OpensslTarget("watchos-simulator-arm64")

        val macos_arm64 = OpensslTarget("macos-arm64")
        val macos_x64 = OpensslTarget("macos-x64")

        val linux_x64 = OpensslTarget("linux-x64")
        val linux_arm64 = OpensslTarget("linux-arm64")

        val mingw_x64 = OpensslTarget("mingw-x64")
        val windows_x64 = OpensslTarget("windows-x64")

        val android_x64 = OpensslTarget("android-x64")
        val android_x86 = OpensslTarget("android-x86")
        val android_arm32 = OpensslTarget("android-arm32")
        val android_arm64 = OpensslTarget("android-arm64")

        val wasm = OpensslTarget("wasm")
    }
}

fun KonanTarget.toOpensslTarget(): OpensslTarget = when (this) {
    KonanTarget.IOS_ARM64               -> OpensslTarget.ios_device_arm64
    KonanTarget.IOS_SIMULATOR_ARM64     -> OpensslTarget.ios_simulator_arm64
    KonanTarget.IOS_X64                 -> OpensslTarget.ios_simulator_x64
    KonanTarget.TVOS_ARM64              -> OpensslTarget.tvos_device_arm64
    KonanTarget.TVOS_X64                -> OpensslTarget.tvos_simulator_x64
    KonanTarget.TVOS_SIMULATOR_ARM64    -> OpensslTarget.tvos_simulator_arm64
    KonanTarget.WATCHOS_ARM32           -> OpensslTarget.watchos_device_arm32
    KonanTarget.WATCHOS_ARM64           -> OpensslTarget.watchos_device_arm64_32
    KonanTarget.WATCHOS_DEVICE_ARM64    -> OpensslTarget.watchos_device_arm64
    KonanTarget.WATCHOS_X64             -> OpensslTarget.watchos_simulator_x64
    KonanTarget.WATCHOS_SIMULATOR_ARM64 -> OpensslTarget.watchos_simulator_arm64
    KonanTarget.LINUX_X64               -> OpensslTarget.linux_x64
    KonanTarget.LINUX_ARM64             -> OpensslTarget.linux_arm64
    KonanTarget.MACOS_ARM64             -> OpensslTarget.macos_arm64
    KonanTarget.MACOS_X64               -> OpensslTarget.macos_x64
    KonanTarget.MINGW_X64               -> OpensslTarget.mingw_x64
    KonanTarget.ANDROID_X64             -> OpensslTarget.android_x64
    KonanTarget.ANDROID_X86             -> OpensslTarget.android_x86
    KonanTarget.ANDROID_ARM32           -> OpensslTarget.android_arm32
    KonanTarget.ANDROID_ARM64           -> OpensslTarget.android_arm64
    else                                -> error("$this is not supported by OpenSSL")
}
