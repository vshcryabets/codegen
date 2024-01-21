allprojects {
    version = "0.1.3-SNAPSHOT"
    group = "com.github.vshcryabets.codegen"

    repositories {
        mavenCentral()
        google()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven { url = uri("https://jitpack.io") }
    }
}

plugins {
    kotlin("jvm") version Versions.kotlin apply false
//    id("org.jetbrains.compose") version Versions.compose apply false
}