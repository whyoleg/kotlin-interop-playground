import kipbuild.dependencies.*
import kipbuild.openssl.*

val configuration = configurations.create("openssl")
dependencies {
    configuration("kipbuild.dependencies.openssl:openssl-3.2.0:3.2.0_1@zip")
}

val setupOpenssl = tasks.register<UnzipTask>("setupOpenssl") {
    inputFile.set(project.layout.file(provider { configuration.singleFile }))
    outputDirectory.set(temporaryDir)
}

extensions.add(
    "openssl",
    OpensslExtension(setupOpenssl)
)
