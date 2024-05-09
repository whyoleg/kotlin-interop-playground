import kipbuild.dependencies.*
import kipbuild.openssl.*

fun createOpensslExtension(classifier: String, version: String, tag: String): OpensslExtension {
    val configuration = configurations.create("openssl_$classifier")
    dependencies {
        configuration("kipbuild.dependencies.openssl:openssl-$version:$tag@zip")
    }

    val setupOpenssl = tasks.register<UnzipTask>("setupOpenssl_$classifier") {
        inputFile.set(project.layout.file(provider { configuration.singleFile }))
        outputDirectory.set(temporaryDir)
    }

    return OpensslExtension(setupOpenssl)
}

//v3_0 = createOpensslExtension("v3_0", "3.0.12", "3.0.12_1")
//v3_1 = createOpensslExtension("v3_1", "3.1.4", "3.1.4_1")
//v3_2 = createOpensslExtension("v3_2", "3.2.0", "3.2.0_1")

extensions.add(
    "openssl",
    createOpensslExtension("v3_2", "3.2.0", "3.2.0_1")
)
