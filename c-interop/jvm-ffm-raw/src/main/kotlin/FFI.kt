package dev.whyoleg.kip.c.jvm.ffm.raw

import java.lang.foreign.*
import java.lang.invoke.*

internal object FFI {
    private val linker = Linker.nativeLinker()
    private val lookup =
        SymbolLookup.libraryLookup(System.mapLibraryName("crypto"), Arena.ofAuto())
            .or(SymbolLookup.loaderLookup())
            .or(Linker.nativeLinker().defaultLookup())

    fun methodHandle(name: String, descriptor: FunctionDescriptor): MethodHandle =
        linker.downcallHandle(lookup.find(name).get(), descriptor)

    val UnboundedAddressLayout: AddressLayout = ValueLayout.ADDRESS.withTargetLayout(
        MemoryLayout.sequenceLayout(Long.MAX_VALUE, ValueLayout.JAVA_BYTE)
    )
}
