plugins {
    kotlin("jvm") version "1.7.20"
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
        classpath("com.github.vshcryabets:codegen:12661a00a8")
    }
}

// Create a task using the task type
tasks.register<ce.gradle.GreetingTask>("hello")