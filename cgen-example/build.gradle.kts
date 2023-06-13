plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
    application
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    google()
    maven { url = uri("https://jitpack.io") }
}

buildscript {
    repositories {
        maven {
            url = uri("https://jitpack.io")
        }
    }
    dependencies {
//        classpath("com.github.vshcryabets:codegen:238769ae06")
//        classpath("org.jetbrains.kotlin:kotlin-scripting-jsr223:${Versions.kotlin}")
//        classpath(project(":cgen-lib"))
    }
}

dependencies {
//    api(project(":cgen-lib"))
//    api(project(":cgen-console"))
    api("com.github.vshcryabets:codegen:238769ae06")
}

kotlin {
    jvmToolchain(Versions.jvmLevel)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(Versions.jvmLevel))
    }
}

task("hello2", JavaExec::class) {
//    workingDir(File("../"))
//    args("./test/project.json")
//    mainClass.set("ce.entrypoints.BuildProjectKt")
//    classpath = sourceSets["test"].runtimeClasspath
}