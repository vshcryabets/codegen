plugins {
    kotlin("jvm") version Versions.kotlin
    id("org.jetbrains.compose") version Versions.compose
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
        classpath("com.github.vshcryabets:codegen:20eac1c984")
    }
}

// Create a task using the task type
tasks.register<ce.gradle.CgenBuildTask>("hello") {
    outputFolder.set(File("./generated/"))
    files.from("../test/")
    cgenPath.set("TestPath")
}