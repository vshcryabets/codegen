plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.10.0"
}
include("cgen-lib")
include("cgen-console")
//include("cgen-example") // enable only while testing gradle api
include("cgen-nnparser")
include("cgen-parser")
