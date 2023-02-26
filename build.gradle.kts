//plugins {
//    kotlin("jvm") version "1.7.20"
//}

allprojects {
    version = "0.1.2-SNAPSHOT"
    group = "ce"

    repositories {
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
